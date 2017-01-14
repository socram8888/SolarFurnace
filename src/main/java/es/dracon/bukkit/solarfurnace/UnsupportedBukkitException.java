
package es.dracon.bukkit.solarfurnace;

import lombok.Getter;

public class UnsupportedBukkitException extends Exception {
	@Getter private CraftVersion craftVersion;

	public UnsupportedBukkitException(CraftVersion craftVersion, String desc, Exception cause) {
		super(desc, cause);
		this.craftVersion = craftVersion;
	};

	public UnsupportedBukkitException(CraftVersion craftVersion, Exception cause) {
		this(craftVersion, "Unsupported CraftBukkit version", cause);
	};

	public UnsupportedBukkitException(CraftVersion craftVersion, String desc) {
		super(desc);
		this.craftVersion = craftVersion;
	};

	public UnsupportedBukkitException(CraftVersion craftVersion) {
		this(craftVersion, "Unsupported CraftBukkit version");
	};
};
