
package org.s4x8.bukkit.solarfurnace;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.logging.Level;

public class SolarFurnace {
	private SFPlugin plugin;
	private Block furnaceBlock;
	
	public SolarFurnace(SFPlugin plugin) {
		this.plugin = plugin;
		furnaceBlock = null;
	};
	
	public SolarFurnace(SFPlugin plugin, Block furnaceBlock) throws InvalidSolarFurnaceException {
		this.plugin = plugin;
		this.furnaceBlock = furnaceBlock;
		check();
	};
	
	public void scan(Block scanBlock) throws InvalidSolarFurnaceException {
		furnaceBlock = Material.DAYLIGHT_DETECTOR.equals(scanBlock.getType()) ? scanBlock.getRelative(BlockFace.DOWN) : scanBlock;
		check();
	};
	
	public boolean isValid() {
		if (furnaceBlock == null) return false;
		if (furnaceBlock.getWorld().getEnvironment() != World.Environment.NORMAL) return false;
		Material furnaceMaterial = furnaceBlock.getType();
		if (!Material.BURNING_FURNACE.equals(furnaceMaterial) && !Material.FURNACE.equals(furnaceMaterial)) return false;
		if (!Material.DAYLIGHT_DETECTOR.equals(furnaceBlock.getRelative(BlockFace.UP).getType())) return false;
		return true;
	};
	
	private void check() throws InvalidSolarFurnaceException {
		if (!isValid()) throw new InvalidSolarFurnaceException();
	};
	
	public void doTick() throws InvalidSolarFurnaceException {
		check();
		
		Furnace furnace = (Furnace) furnaceBlock.getState();
		if (furnace.getInventory().getSmelting() == null) return;
		
		if (furnaceBlock.getRelative(BlockFace.UP).getLightFromSky() < 15) return;
		
		short remainingTicks = furnace.getBurnTime();
		if (remainingTicks == 0) {
			try {
				furnace.setBurnTime((short) 2);
				FurnaceUpdater.update(furnaceBlock, true);
			} catch (Exception e) {
				plugin.getLogger().log(Level.SEVERE, "Unable to update furnace block", e);
				plugin.getServer().getPluginManager().disablePlugin(plugin);
			};
		} else {
			furnace.setBurnTime((short) (remainingTicks + 1));
		};
	};
	
	public Block getFurnaceBlock() {
		return furnaceBlock;
	};
};
