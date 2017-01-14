
package es.dracon.bukkit.solarfurnace;

import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.World;

public class WorldListener implements Listener {
	private SFPlugin plugin;

	public WorldListener(SFPlugin plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	};

	@EventHandler
	public void onWorldLoad(WorldLoadEvent event) {
		plugin.getFurnaces().loadWorldFurnaces(event.getWorld());
	};

	@EventHandler
	public void onWorldUnload(WorldUnloadEvent event) {
		plugin.getFurnaces().unloadWorldFurnaces(event.getWorld());
	};
};
