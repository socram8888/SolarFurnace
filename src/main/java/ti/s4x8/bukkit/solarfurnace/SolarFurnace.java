
package ti.s4x8.bukkit.solarfurnace;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.World;

import lombok.Getter;

public class SolarFurnace {
	private static final BlockFace[] PANEL_SIDES = new BlockFace[] { BlockFace.UP, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST };

	private SFPlugin plugin;
	@Getter private Block furnaceBlock;

	public SolarFurnace(SFPlugin plugin, Block furnaceBlock) {
		this.plugin = plugin;
		this.furnaceBlock = furnaceBlock;
	};

	public void check() throws InvalidSolarFurnaceException {
		checkWorld();
		checkFurnace();
		checkPanels();
	};

	public void checkWorld() throws InvalidSolarFurnaceException {
		if (furnaceBlock.getWorld().getEnvironment() != World.Environment.NORMAL) {
			throw new InvalidSolarFurnaceException("Invalid enviroment");
		};
	};

	public void checkFurnace() throws InvalidSolarFurnaceException {
		if (!furnaceBlock.getChunk().isLoaded()) {
			return;
		};
		Material furnaceMaterial = furnaceBlock.getType();
		if (furnaceMaterial != Material.FURNACE && furnaceMaterial != Material.BURNING_FURNACE) {
			throw new InvalidSolarFurnaceException("No furnace found");
		};
	};

	public void checkPanels() throws InvalidSolarFurnaceException {
		for (int i = 0; i < PANEL_SIDES.length; i++) {
			Block panelBlock = furnaceBlock.getRelative(PANEL_SIDES[i]);
			if (
				!panelBlock.getChunk().isLoaded() ||
				panelBlock.getType() == Material.DAYLIGHT_DETECTOR
			) {
				return;
			};
		};
		throw new InvalidSolarFurnaceException("No solar panel found");
	};

	public void doTick() throws InvalidSolarFurnaceException, UnsupportedBukkitException {
		if (!furnaceBlock.getChunk().isLoaded()) return;
		checkFurnace();

		Furnace furnace = (Furnace) furnaceBlock.getState();
		if (furnace.getInventory().getSmelting() == null) return;
		if (getPanelsLight() < 15) return;

		short remainingTicks = furnace.getBurnTime();
		if (remainingTicks == 0) {
			furnace.setBurnTime((short) 2);
			plugin.getUpdater().setBurning(furnaceBlock);
		} else {
			furnace.setBurnTime((short) (remainingTicks + 1));
		};
	};

	private int getPanelsLight() throws InvalidSolarFurnaceException {
		int intensity = -1;
		boolean die = true;
		for (int i = 0; i < PANEL_SIDES.length; i++) {
			Block panelBlock = furnaceBlock.getRelative(PANEL_SIDES[i]);
			if (!panelBlock.getChunk().isLoaded()) {
				die = false;
				continue;
			};
			if (panelBlock.getType() == Material.DAYLIGHT_DETECTOR) {
				intensity = Math.max(intensity, panelBlock.getLightFromSky());
				die = false;
			};
		};
		if (die) {
			throw new InvalidSolarFurnaceException("No solar panel found");
		};
		return intensity;
	};
};
