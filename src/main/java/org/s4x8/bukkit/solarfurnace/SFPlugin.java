
package org.s4x8.bukkit.solarfurnace;

import org.bukkit.plugin.java.JavaPlugin;

public class SFPlugin extends JavaPlugin {
	private FurnaceDatabase furnaces;
	
	public void onEnable() {
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
};
