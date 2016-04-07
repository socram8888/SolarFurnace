
package ti.s4x8.bukkit.solarfurnace;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.Server;

import lombok.Getter;

public class SFPlugin extends JavaPlugin {
	@Getter private FurnaceDatabase furnaces;
	@Getter private FurnaceUpdater updater;
	@Getter private Permission createPermission = new Permission("solarfurnace.create", "Allows creation of solar furnaces", PermissionDefault.TRUE);

	public void onEnable() {
		Server server = getServer();
		PluginManager pm = server.getPluginManager();
		pm.addPermission(createPermission);

		CraftVersion craftVersion = new CraftVersion(server);
		getLogger().info("Detected Bukkit version: " + craftVersion);
		try {
			updater = new FurnaceUpdater(craftVersion);
		} catch (UnsupportedBukkitException exception) {
			getLogger().severe("Bukkit version not supported");
			pm.disablePlugin(this);
			return;
		};

		furnaces = new FurnaceDatabase(this);
		furnaces.loadFurnaces();

		new CreateListener(this);
		new WorldListener(this);
		new FuelTask(this);

		
	};

	public void onDisable() {
		getServer().getPluginManager().removePermission(createPermission);
		if (furnaces != null) {
			furnaces.unloadFurnaces();
		};
	};
};
