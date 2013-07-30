
package ti.s4x8.bukkit.solarfurnace;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import lombok.Getter;

public class SFPlugin extends JavaPlugin {
	@Getter private FurnaceDatabase furnaces;
	@Getter private Permission createPermission = new Permission("solarfurnace.create", "Allows creation of solar furnaces", PermissionDefault.TRUE);
	
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.addPermission(createPermission);
	
		furnaces = new FurnaceDatabase(this);
		new CreateListener(this);
		new WorldListener(this);
		new FuelTask(this);
		
		furnaces.loadFurnaces();
	};
	
	public void onDisable() {
		getServer().getPluginManager().removePermission(createPermission);
		if (furnaces != null) {
			furnaces.unloadFurnaces();
		};
	};
};
