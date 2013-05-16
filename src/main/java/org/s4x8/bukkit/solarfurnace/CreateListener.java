
package org.s4x8.bukkit.solarfurnace;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CreateListener implements Listener {
	private SFPlugin plugin;
	
	public CreateListener(SFPlugin plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	};
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		SolarFurnace solarFurnace = new SolarFurnace(plugin);
		try {
			solarFurnace.scan(event.getBlock());
		} catch (InvalidSolarFurnaceException e) {
			return;
		};
		plugin.getFurnaces().add(solarFurnace);
	};
};
