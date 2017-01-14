
package es.dracon.bukkit.solarfurnace;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.Server;

import java.io.IOException;

import org.mcstats.MetricsLite;

import lombok.Getter;

public class SFPlugin extends JavaPlugin {
	@Getter private FurnaceDatabase furnaces;
	@Getter private Permission createPermission = new Permission("solarfurnace.create", "Allows creation of solar furnaces", PermissionDefault.TRUE);

	public void onEnable() {
		Server server = getServer();
		PluginManager pm = server.getPluginManager();
		pm.addPermission(createPermission);

		furnaces = new FurnaceDatabase(this);
		furnaces.loadFurnaces();

		new CreateListener(this);
		new WorldListener(this);
		new FuelTask(this);

		try {
			(new MetricsLite(this)).start();
		} catch (IOException e) { };
	};

	public void onDisable() {
		getServer().getPluginManager().removePermission(createPermission);
		if (furnaces != null) {
			furnaces.unloadFurnaces();
		};
	};
};
