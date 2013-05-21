
package org.s4x8.bukkit.solarfurnace;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class SFPlugin extends JavaPlugin {
	private FurnaceDatabase furnaces;
	private FurnaceUpdater furnaceUpdater;
	
	public void onEnable() {
		try {
			furnaceUpdater = new FurnaceUpdater(getServer());
		} catch (UnsupportedBukkitException e) {
			getLogger().log(Level.SEVERE, "Unsupported Bukkit version", e);
			getServer().getPluginManager().disablePlugin(this);
			return;
		};
	
		furnaces = new FurnaceDatabase(this);
		new CreateListener(this);
		new WorldListener(this);
		new FuelTask(this);
		
		furnaces.loadFurnaces();
	};
	
	public void onDisable() {
		furnaces.unloadFurnaces();
	};
	
	public FurnaceDatabase getFurnaces() {
		return furnaces;
	};
	
	public FurnaceUpdater getUpdater() {
		return furnaceUpdater;
	};
};
