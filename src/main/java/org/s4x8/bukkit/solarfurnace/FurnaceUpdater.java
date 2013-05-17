
package org.s4x8.bukkit.solarfurnace;

import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.Material;
import org.bukkit.World;

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
	
	public static void update(Furnace furnace) throws Exception {
		update(furnace.getBlock(), furnace.getBurnTime() > 0);
	};
	
	public static void update(Block furnace, boolean burn) throws Exception {
		// Cache values for faster access
		int x = furnace.getX();
		int y = furnace.getY();
		int z = furnace.getZ();
		int block = burn ? Material.BURNING_FURNACE.getId() : Material.FURNACE.getId();
	
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
		nmsChunkSection.getClass().getMethod("setTypeId", int.class, int.class, int.class, int.class).invoke(nmsChunkSection, x & 0xF, y & 0xF, z & 0xF, block);
		
		// Set chunk "isModified" flag
		nmsChunkClass.getField("l").setBoolean(nmsChunk, true);
		
		// Update lightning
		nmsWorldClass.getMethod("A", int.class, int.class, int.class).invoke(nmsWorld, x, y, z);
		
		// Schedule block update for clients
		nmsWorldClass.getMethod("notify", int.class, int.class, int.class).invoke(nmsWorld, x, y, z);
	};
};
