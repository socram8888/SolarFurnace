
package es.dracon.bukkit.solarfurnace;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.World;

import java.util.Iterator;
import java.util.logging.Level;

public class FuelTask extends BukkitRunnable {
	private SFPlugin plugin;

	public FuelTask(SFPlugin plugin) {
		this.plugin = plugin;
		runTaskTimer(plugin, 0, 1);
	};

	public void run() {
		FurnaceDatabase furnaces = plugin.getFurnaces();
		Iterator<World> worldIterator = furnaces.getWorlds().iterator();
		while (worldIterator.hasNext()) {
			World world = worldIterator.next();
			if (world.getTime() > 12000) continue;
			Iterator<SolarFurnace> furnaceIterator = furnaces.getFurnacesInWorld(world).iterator();
			while (furnaceIterator.hasNext()) {
				SolarFurnace furnace = furnaceIterator.next();
				try {
					furnace.doTick();
				} catch (ChunkNotLoadedException e) {
					// Do nothing
				} catch (InvalidSolarFurnaceException e) {
					furnaceIterator.remove();
				} catch (UnsupportedBukkitException e) {
					plugin.getLogger().severe("Unexpected exception in FurnaceUpdater");
					plugin.getLogger().severe("Detected version: " + e.getCraftVersion());
					plugin.getLogger().log(Level.SEVERE, "Exception cause:", e.getCause());
					plugin.getServer().getPluginManager().disablePlugin(plugin);
					return;
				} catch (Exception e) {
					plugin.getLogger().log(Level.SEVERE, "Unexpected exception ticking furnace at " + furnace.getFurnaceBlock(), e.getCause());
					furnaceIterator.remove();
				};
			};
		};
	};
};
