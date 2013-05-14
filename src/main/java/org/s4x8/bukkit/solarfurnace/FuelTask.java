
package org.s4x8.bukkit.solarfurnace;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;

public class FuelTask extends BukkitRunnable {
	private SFPlugin plugin;
	
	public FuelTask(SFPlugin plugin) {
		this.plugin = plugin;
		runTaskTimer(plugin, 0, 1);
	};
	
	public void run() {
		Iterator<SolarFurnace> it = plugin.getFurnaces().iterator();
		while (it.hasNext()) {
			try {
				it.next().doTick();
			} catch (InvalidSolarFurnaceException e) {
				it.remove();
			};
		};
	};
};
