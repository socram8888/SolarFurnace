
package org.s4x8.bukkit.solarfurnace;

import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.Material;
import org.bukkit.World;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/*
import org.bukkit.craftbukkit.v1_5_R3.CraftWorld;

import net.minecraft.server.v1_5_R3.Block;
import net.minecraft.server.v1_5_R3.Chunk;
import net.minecraft.server.v1_5_R3.ChunkSection;
import net.minecraft.server.v1_5_R3.World;
*/

public class FurnaceUpdater {
	/* Original implementation, below translated to reflection calls for version-independency
	
	public static void update(Furnace furnace) throws Exception {
		int id = furnace.getBurnTime() > 0 ? Block.BURNING_FURNACE.id : Block.FURNACE.id;
		int x = furnace.getX();
		int y = furnace.getY();
		int z = furnace.getZ();
		
		World world = ((CraftWorld) furnace.getWorld()).getHandle();
		Chunk chunk = world.getChunkAtWorldCoords(x, z);
		
		ChunkSection section = chunk.i()[y >> 4];
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
		Method craftWorldGetNMS = craftWorld.getClass().getMethod("getHandle");
		Object nmsWorld = craftWorldGetNMS.invoke(craftWorld);
		
		// Retrieve the NMS chunk from the NMS world that contains the furnace block
		Class nmsWorldClass = nmsWorld.getClass();
		Method nmsWorldGetChunk = nmsWorldClass.getMethod("getChunkAtWorldCoords", int.class, int.class);
		Object nmsChunk = nmsWorldGetChunk.invoke(nmsWorld, x, z);
		
		// Retrieves the NMS ChunkSection from the NMS Chunk that contains the furnace block
		Class nmsChunkClass = nmsChunk.getClass();
		Method nmsChunkGetSections = nmsChunkClass.getMethod("i");
		Object nmsChunkSection = ((Object[]) nmsChunkGetSections.invoke(nmsChunk))[y >> 4];
		
		// Update block
		Method nmsChunkSectionSetBlock = nmsChunkSection.getClass().getMethod("setTypeId", int.class, int.class, int.class, int.class);
		nmsChunkSectionSetBlock.invoke(nmsChunkSection, x & 0xF, y & 0xF, z & 0xF, block);
		
		// Set chunk "isModified" flag
		Field nmsChunkModified = nmsChunkClass.getField("l");
		nmsChunkModified.setBoolean(nmsChunk, true);
		
		// Update lightning
		Method nmsWorldUpdateLightning = nmsWorldClass.getMethod("A", int.class, int.class, int.class);
		nmsWorldUpdateLightning.invoke(nmsWorld, x, y, z);
		
		// Schedule block update for clients
		Method nmsWorldNotifyClients = nmsWorldClass.getMethod("notify", int.class, int.class, int.class);
		nmsWorldNotifyClients.invoke(nmsWorld, x, y, z);
	};
};
