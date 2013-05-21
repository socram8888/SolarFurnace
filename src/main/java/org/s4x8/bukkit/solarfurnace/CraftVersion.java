
package org.s4x8.bukkit.solarfurnace;

import org.bukkit.Server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;

public class CraftVersion {
	private static final Pattern CRAFT_PATTERN = Pattern.compile("^org\\.bukkit\\.craftbukkit\\.v([0-9]+)_([0-9]+)_R([0-9]+)\\.CraftServer$");

	@Getter private int major = -1;
	@Getter private int minor = -1;
	@Getter private int revision = -1;
	@Getter private Flavour flavour = null;

	public CraftVersion(Server server) {
		String serverClassPath = server.getClass().getCanonicalName();
		
		Matcher craftMatcher = CRAFT_PATTERN.matcher(serverClassPath);
		if (craftMatcher.matches()) {
			major = Integer.valueOf(craftMatcher.group(1));
			minor = Integer.valueOf(craftMatcher.group(2));
			revision = Integer.valueOf(craftMatcher.group(3));
			flavour = Flavour.CRAFTBUKKIT;
		};
	};

	public String toString() {
		return flavour + " " + major + "." + minor + "R" + revision;
	};

	public enum Flavour {
		UNKNOWN("Unknown"), CRAFTBUKKIT("CraftBukkit");

		private String niceName;
		private Flavour(String niceName) {
			this.niceName = niceName;
		};

		public String toString() {
			return niceName;
		};
	};
};

