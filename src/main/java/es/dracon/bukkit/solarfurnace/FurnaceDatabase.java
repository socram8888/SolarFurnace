
package es.dracon.bukkit.solarfurnace;

import org.bukkit.block.Block;
import org.bukkit.World;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class FurnaceDatabase {
	private Map<World, Map<Block, SolarFurnace>> furnaces;
	private SFPlugin plugin;

	public FurnaceDatabase(SFPlugin plugin) {
		furnaces = new HashMap<>();
		this.plugin = plugin;
	};

	public void loadFurnaces() {
		Iterator<World> it = plugin.getServer().getWorlds().iterator();
		while (it.hasNext()) {
			loadWorldFurnaces(it.next());
		};
	};

	public void unloadFurnaces() {
		Iterator<World> it = furnaces.keySet().iterator();
		while (it.hasNext()) {
			saveWorldFurnaces(it.next());
			it.remove();
		};
	};

	public void loadWorldFurnaces(World world) {
		if (world.getEnvironment() != World.Environment.NORMAL) return;

		Map<Block, SolarFurnace> worldFurnaces = new HashMap<>();
		furnaces.put(world, worldFurnaces);

		File file = new File(world.getWorldFolder(), "solarfurnace.bin");
		FileInputStream fileStream = null;
		try {
			fileStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			plugin.getLogger().info("No solar furnace data found for world " + world.getName());
			return;
		};

		DataInputStream dataStream = new DataInputStream(fileStream);
		while (true) {
			try {
				int x = dataStream.readInt();
				int y = dataStream.readUnsignedByte();
				int z = dataStream.readInt();
				Block furnaceBlock = world.getBlockAt(x, y, z);

				SolarFurnace solarFurnace = new SolarFurnace(plugin, furnaceBlock);
				try {
					solarFurnace.check();
				} catch (ChunkNotLoadedException e) {
					// Do nothing
				} catch (InvalidSolarFurnaceException e) {
					continue;
				};
				worldFurnaces.put(furnaceBlock, solarFurnace);
			} catch (IOException e) {
				break;
			};
		};
		try {
			fileStream.close();
		} catch (IOException e) { };
		plugin.getLogger().info("Found " + worldFurnaces.size() + " solar furnace(s) in world " + world.getName());
	};

	private void saveWorldFurnaces(World world) {
		Map<Block, SolarFurnace> worldFurnaces = furnaces.get(world);

		File file = new File(world.getWorldFolder(), "solarfurnace.bin");
		FileOutputStream fileStream = null;
		try {
			fileStream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			plugin.getLogger().warning("Unable to save furnace data for world " + world.getName());
			return;
		};

		DataOutputStream dataStream = new DataOutputStream(fileStream);
		Iterator<SolarFurnace> it = worldFurnaces.values().iterator();
		while (it.hasNext()) {
			Block furnaceBlock = it.next().getFurnaceBlock();
			try {
				dataStream.writeInt(furnaceBlock.getX());
				dataStream.writeByte(furnaceBlock.getY());
				dataStream.writeInt(furnaceBlock.getZ());
			} catch (IOException e) {
				plugin.getLogger().warning("Unexpected I/O error while saving furnace data for world " + world.getName());
				break;
			};
		};
		try {
			fileStream.close();
		} catch (IOException e) { };
		plugin.getLogger().info("Saved " + worldFurnaces.size() + " solar furnace(s) in world " + world.getName());
	};

	public void unloadWorldFurnaces(World world) {
		saveWorldFurnaces(world);
		furnaces.remove(world);
	};

	public void add(SolarFurnace furnace) {
		furnaces.get(furnace.getFurnaceBlock().getWorld()).put(furnace.getFurnaceBlock(), furnace);
	};

	public void remove(SolarFurnace furnace) {
		furnaces.get(furnace.getFurnaceBlock().getWorld()).remove(furnace.getFurnaceBlock());
	};

	public boolean isSolarFurnace(Block block) {
		Map<Block, SolarFurnace> worldFurnaces = furnaces.get(block.getWorld());
		if (worldFurnaces == null) {
			return false;
		}

		return worldFurnaces.containsKey(block);
	}

	public Set<World> getWorlds() {
		return furnaces.keySet();
	};

	public Collection<SolarFurnace> getFurnacesInWorld(World world) {
		return furnaces.get(world).values();
	};
};
