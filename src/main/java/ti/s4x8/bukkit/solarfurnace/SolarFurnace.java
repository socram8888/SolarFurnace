
package ti.s4x8.bukkit.solarfurnace;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Furnace;
import org.bukkit.Effect;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import lombok.Getter;

public class SolarFurnace {
	private SFPlugin plugin;
	@Getter Block furnaceBlock = null;
	
	public SolarFurnace(SFPlugin plugin) {
		this.plugin = plugin;
	};
	
	public SolarFurnace(SFPlugin plugin, Block furnaceBlock) throws InvalidSolarFurnaceException {
		this.plugin = plugin;
		this.furnaceBlock = furnaceBlock;
		check();
	};
	
	public void scan(Block scanBlock) throws InvalidSolarFurnaceException {
		furnaceBlock = scanBlock;
		checkNullAndWorld();
		if (Material.DAYLIGHT_DETECTOR.equals(furnaceBlock.getType())) {
			furnaceBlock = furnaceBlock.getRelative(BlockFace.DOWN);
		} else if (!Material.DAYLIGHT_DETECTOR.equals(furnaceBlock.getRelative(BlockFace.UP).getType())) {
			throw new InvalidSolarFurnaceException("No solar panel found");
		};
		Material furnaceMaterial = furnaceBlock.getType();
		if (!Material.FURNACE.equals(furnaceMaterial) && !Material.BURNING_FURNACE.equals(furnaceMaterial)) {
			throw new InvalidSolarFurnaceException("No furnace found");
		};
	};
	
	public void check() throws InvalidSolarFurnaceException {
		checkNullAndWorld();
		if (!Material.DAYLIGHT_DETECTOR.equals(furnaceBlock.getRelative(BlockFace.UP).getType())) {
			throw new InvalidSolarFurnaceException("No solar panel found");
		};
		Material furnaceMaterial = furnaceBlock.getType();
		if (!Material.FURNACE.equals(furnaceMaterial) && !Material.BURNING_FURNACE.equals(furnaceMaterial)) {
			throw new InvalidSolarFurnaceException("No furnace found");
		};
	};
	
	private void checkNullAndWorld() throws InvalidSolarFurnaceException {
		if (furnaceBlock == null) {
			throw new InvalidSolarFurnaceException("Null block");
		};
		if (furnaceBlock.getWorld().getEnvironment() != World.Environment.NORMAL) {
			throw new InvalidSolarFurnaceException("Invalid enviroment");
		};
	};
	
	public void doTick() throws InvalidSolarFurnaceException, UnsupportedBukkitException {
		if (!furnaceBlock.getChunk().isLoaded()) return;
		check();

		Furnace furnace = (Furnace) furnaceBlock.getState();
		if (furnace.getInventory().getSmelting() == null) return;
		if (furnaceBlock.getRelative(BlockFace.UP).getLightFromSky() < 15) return;

		short remainingTicks = furnace.getBurnTime();
		if (remainingTicks == 0) {
			furnace.setBurnTime((short) 2);
			plugin.getUpdater().setBurning(furnaceBlock);
		} else {
			furnace.setBurnTime((short) (remainingTicks + 1));
		};
	};
};
