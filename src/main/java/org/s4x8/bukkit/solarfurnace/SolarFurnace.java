
package org.s4x8.bukkit.solarfurnace;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class SolarFurnace {
	private Block furnaceBlock;
	private Block panelBlock;
	
	public SolarFurnace() {
		furnaceBlock = null;
		panelBlock = null;
	};
	
	public SolarFurnace(Block furnaceBlock) throws InvalidSolarFurnaceException {
		this.furnaceBlock = furnaceBlock;
		this.panelBlock = furnaceBlock.getRelative(BlockFace.UP);
		checkBlocks();
	};
	
	public void scan(Block scanBlock) throws InvalidSolarFurnaceException {
		if (isSolarPanel(scanBlock)) {
			furnaceBlock = scanBlock.getRelative(BlockFace.DOWN);
			panelBlock = scanBlock;
		} else {
			furnaceBlock = scanBlock;
			panelBlock = scanBlock.getRelative(BlockFace.UP);
		};
		checkBlocks();
	};
	
	public boolean isValid() {
		return isFurnace(furnaceBlock) && isSolarPanel(panelBlock);
	};
	
	private boolean isFurnace(Block block) {
		if (block == null) return false;
		Material blockMaterial = block.getType();
		return Material.BURNING_FURNACE.equals(blockMaterial) || Material.FURNACE.equals(blockMaterial);
	};

	private boolean isSolarPanel(Block block) {
		if (block == null) return false;
		return Material.DAYLIGHT_DETECTOR.equals(block.getType());
	};
	
	private void checkBlocks() throws InvalidSolarFurnaceException {
		if (!isValid()) throw new InvalidSolarFurnaceException();
	};
	
	private boolean isAirItem(ItemStack item) {
		return item == null || Material.AIR.equals(item.getType());
	};
	
	public void doTick() throws InvalidSolarFurnaceException {
		checkBlocks();
		
		Furnace furnace = (Furnace) furnaceBlock.getState();
		if (isAirItem(furnace.getInventory().getSmelting())) return;
		
		if (isLightLevelLow()) return;
		
		short remainingTicks = (short) (furnace.getBurnTime() + 1);
		if (remainingTicks == 1) {
			remainingTicks++;
		};
		furnace.setBurnTime(remainingTicks);
	};
	
	private boolean isLightLevelLow() {
		World world = panelBlock.getWorld();
		if (world.getEnvironment() != World.Environment.NORMAL) return true;
		int worldTime = (int) world.getTime();
		if (worldTime < 3000 || worldTime > 9000) return true;
		if (panelBlock.getLightFromSky() < 15) return true;
		return false;
	};
	
	public Block getFurnaceBlock() {
		return furnaceBlock;
	};
};
