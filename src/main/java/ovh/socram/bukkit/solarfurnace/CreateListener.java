
package ovh.socram.bukkit.solarfurnace;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.Material;

public class CreateListener implements Listener {
	private static final BlockFace[] FURNACE_SIDES = new BlockFace[] { BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST };

	private SFPlugin plugin;

	public CreateListener(SFPlugin plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	};

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (!event.getPlayer().hasPermission(plugin.getCreatePermission())) return;
		Block block = event.getBlock();
		Material material = block.getType();

		if (material == Material.FURNACE || material == Material.BURNING_FURNACE) {
			SolarFurnace solarFurnace = new SolarFurnace(plugin, block);
			try {
				solarFurnace.checkWorld();
				solarFurnace.checkPanels();
			} catch (ChunkNotLoadedException e) {
				// Do nothing
			} catch (InvalidSolarFurnaceException e) {
				return;
			};
			plugin.getFurnaces().add(solarFurnace);

		} else if (material == Material.DAYLIGHT_DETECTOR) {
			FurnaceDatabase database = plugin.getFurnaces();

			for (BlockFace face : FURNACE_SIDES) {
				SolarFurnace solarFurnace = new SolarFurnace(plugin, block.getRelative(face));
				try {
					solarFurnace.checkWorld();
					solarFurnace.checkFurnace();
				} catch (ChunkNotLoadedException e) {
					// Do nothing
				} catch (InvalidSolarFurnaceException e) {
					continue;
				};
				database.add(solarFurnace);
			};
		};
	};
};
