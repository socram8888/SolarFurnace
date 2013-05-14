
package org.s4x8.bukkit.solarfurnace;

import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.World;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class SFPlugin extends JavaPlugin {
	private ArrayList<SolarFurnace> furnaces;
	
	public void onEnable() {
		furnaces = new ArrayList<SolarFurnace>();
		new FuelTask(this);
		new CreateListener(this);
		loadFurnaces();
	};
	
	public void onDisable() {
		saveFurnaces();
	};
	
	private void loadFurnaces() {
		Iterator<World> it = getServer().getWorlds().iterator();
		while (it.hasNext()) {
			loadWorldFurnaces(it.next());
		}
	};
	
	private void loadWorldFurnaces(World world) {
		File file = new File(world.getWorldFolder(), "solarfurnace.bin");
		int loaded = 0;
		
		FileInputStream fileStream = null;
		try {
			fileStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			getLogger().info("No solar furnace data found for world " + world.getName());
			return;
		};
		
		DataInputStream dataStream = new DataInputStream(fileStream);
		while (true) {
			try {
				int x = dataStream.readInt();
				int y = dataStream.readUnsignedByte();
				int z = dataStream.readInt();
				Block furnaceBlock = world.getBlockAt(x, y, z);
				try {
					SolarFurnace solarFurnace = new SolarFurnace(furnaceBlock);
					furnaces.add(solarFurnace);
					loaded++;
				} catch (InvalidSolarFurnaceException e) { };
			} catch (IOException e) {
				break;
			};
		};
		try {
			fileStream.close();
		} catch (IOException e) { };
		getLogger().info("Found " + loaded + " solar furnace(s) in world " + world.getName());
	};
	
	private void saveFurnaces() {
		Iterator<World> it = getServer().getWorlds().iterator();
		while (it.hasNext()) {
			saveWorldFurnaces(it.next());
		}
	};
	
	private void saveWorldFurnaces(World world) {
		File file = new File(world.getWorldFolder(), "solarfurnace.bin");
		int saved = 0;
		
		FileOutputStream fileStream = null;
		try {
			fileStream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			getLogger().warning("Unable to save furnace data for world " + world.getName());
			return;
		};
		
		DataOutputStream dataStream = new DataOutputStream(fileStream);
		Iterator<SolarFurnace> it = furnaces.iterator();
		while (it.hasNext()) {
			Block furnaceBlock = it.next().getFurnaceBlock();
			if (world.equals(furnaceBlock.getWorld())) {
				try {
					dataStream.writeInt(furnaceBlock.getX());
					dataStream.writeByte(furnaceBlock.getY());
					dataStream.writeInt(furnaceBlock.getZ());
					saved++;
				} catch (IOException e) {
					getLogger().warning("Unexpected I/O error while saving furnace data for world " + world.getName());
					break;
				};
			};
		};
		try {
			fileStream.close();
		} catch (IOException e) { };
		getLogger().info("Saved " + saved + " solar furnace(s) in world " + world.getName());
	};
	
	public ArrayList<SolarFurnace> getFurnaces() {
		return furnaces;
	};
};
