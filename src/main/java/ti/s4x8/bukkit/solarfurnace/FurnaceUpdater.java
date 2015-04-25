
package ti.s4x8.bukkit.solarfurnace;

import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.Server;

import lombok.Getter;

public class FurnaceUpdater {
	/* Original implementation, below translated to reflection calls for version-independency

	public static void update(Furnace furnace) throws Exception {
		int id = furnace.getBurnTime() > 0 ? Material.BURNING_FURNACE.getId() : Material.FURNACE.getId();
		int x = furnace.getX();
		int y = furnace.getY();
		int z = furnace.getZ();

		net.minecraft.server.v1_5_R3.World world = ((CraftWorld) furnace.getWorld()).getHandle();
		net.minecraft.server.v1_5_R3.Chunk chunk = world.getChunkAtWorldCoords(x, z);

		net.minecraft.server.v1_5_R3.ChunkSection section = chunk.i()[y >> 4];
		if (section.getTypeId(x & 0xF, y & 0xF, z & 0xF) == id) return;

		section.setTypeId(x & 0xF, y & 0xF, z & 0xF, id);

		chunk.l = true;
		world.A(x, y, z);
		world.notify(x, y, z);
	};
	*/

	@Getter private final CraftVersion craftVersion;
	private final boolean fixed;

	public FurnaceUpdater(CraftVersion craftVersion) throws UnsupportedBukkitException {
		this.craftVersion = craftVersion;
		fixed = isVersionFixed(craftVersion);
		if (fixed) return;

		CraftVersion.Flavour flavour = craftVersion.getFlavour();
		if (flavour == CraftVersion.Flavour.CRAFTBUKKIT) {
			int major = craftVersion.getMajor();
			int minor = craftVersion.getMinor();
			int revision = craftVersion.getRevision();
			if (major == 1 && minor == 5 && revision >= 1 && revision <= 3) return;
			if (major == 1 && minor == 6 && revision <= 3) return;
		};

		throw new UnsupportedBukkitException(craftVersion);
	};

	public void update(Furnace furnace) throws UnsupportedBukkitException {
		update(furnace.getBlock(), furnace.getBurnTime() > 0);
	};

	public void setIdle(Block furnace) throws UnsupportedBukkitException {
		update(furnace, false);
	};

	public void setBurning(Block furnace) throws UnsupportedBukkitException {
		update(furnace, true);
	};

	public void update(Block furnace, boolean burn) throws UnsupportedBukkitException {
		if (fixed) return;

		// Cache values for faster access
		int x = furnace.getX();
		int y = furnace.getY();
		int z = furnace.getZ();
		int block = burn ? Material.BURNING_FURNACE.getId() : Material.FURNACE.getId();

		try {
			// Retrieve NMS world using the CraftWorld getHandle() method
			World craftWorld = furnace.getWorld();
			Object nmsWorld = craftWorld.getClass().getMethod("getHandle").invoke(craftWorld);

			// Retrieve the NMS chunk from the NMS world that contains the furnace block
			Class nmsWorldClass = nmsWorld.getClass();
			Object nmsChunk = nmsWorldClass.getMethod("getChunkAtWorldCoords", int.class, int.class).invoke(nmsWorld, x, z);

			// Retrieves the NMS ChunkSection from the NMS Chunk that contains the furnace block
			Class nmsChunkClass = nmsChunk.getClass();
			Object nmsChunkSection = ((Object[]) nmsChunkClass.getMethod("i").invoke(nmsChunk))[y >> 4];

			// Update block
			String setTypeIdMethodName;
			if (craftVersion.getMajor() == 1 && craftVersion.getMinor() == 5 && craftVersion.getRevision() < 3) {
				setTypeIdMethodName = "a";
			} else {
				setTypeIdMethodName = "setTypeId";
			};
			nmsChunkSection.getClass().getMethod(setTypeIdMethodName, int.class, int.class, int.class, int.class).invoke(nmsChunkSection, x & 0xF, y & 0xF, z & 0xF, block);

			// Set chunk "isModified" flag
			nmsChunkClass.getField("l").setBoolean(nmsChunk, true);

			// Update lighting
			nmsWorldClass.getMethod("A", int.class, int.class, int.class).invoke(nmsWorld, x, y, z);

			// Schedule block update for clients
			nmsWorldClass.getMethod("notify", int.class, int.class, int.class).invoke(nmsWorld, x, y, z);
		} catch (Exception ex) {
			throw new UnsupportedBukkitException(craftVersion, "Unexpected exception. Please notify developers about this", ex);
		};
	};

	private static final CraftVersion CRAFT_FIXED = new CraftVersion(CraftVersion.Flavour.CRAFTBUKKIT, 1, 8, 2); // Spigot's CraftBukkit

	public static boolean isVersionFixed(CraftVersion craftVersion) {
		CraftVersion.Flavour flavour = craftVersion.getFlavour();

		// SportBukkit is patched, so it doesn't need this
		if (flavour == CraftVersion.Flavour.SPORTBUKKIT) return true;

		if (flavour == CraftVersion.Flavour.CRAFTBUKKIT) {
			return craftVersion.compareTo(CRAFT_FIXED) >= 0;
		}

		return false;
	}
};
