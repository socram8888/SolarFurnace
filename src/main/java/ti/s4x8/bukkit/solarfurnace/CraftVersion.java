
package ti.s4x8.bukkit.solarfurnace;

import org.bukkit.Server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Data;

@Data
public class CraftVersion implements Comparable<CraftVersion> {
	public static final CraftVersion UNKNOWN_VERSION = new CraftVersion(Flavour.UNKNOWN, 0, 0, 0);
	private static final Pattern CRAFT_PATTERN = Pattern.compile("^org\\.bukkit\\.craftbukkit\\.v([0-9]+)_([0-9]+)_R([0-9]+)\\.CraftServer$");

	private final int major;
	private final int minor;
	private final int revision;
	private final Flavour flavour;

	public CraftVersion(Server server) {
		String serverClassPath = server.getClass().getCanonicalName();

		Matcher craftMatcher = CRAFT_PATTERN.matcher(serverClassPath);
		if (craftMatcher.matches()) {
			flavour = Flavour.byName(server.getName());
			major = Integer.valueOf(craftMatcher.group(1));
			minor = Integer.valueOf(craftMatcher.group(2));
			revision = Integer.valueOf(craftMatcher.group(3));
		} else {
			throw new RuntimeException("Could not parse server class: " + serverClassPath);
		}
	}

	public CraftVersion(Flavour flavour, int major, int minor, int revision) {
		this.flavour = flavour;
		this.major = major;
		this.minor = minor;
		this.revision = revision;
	}

	@Override
	public String toString() {
		return flavour + " " + major + "." + minor + "R" + revision;
	}

	@Override
	public int compareTo(CraftVersion other) {
		int otherMajor = other.getMajor();
		if (major < otherMajor) {
			return -1;
		} else if (major > otherMajor) {
			return 1;
		}

		int otherMinor = other.getMinor();
		if (minor < otherMinor) {
			return -1;
		} else if (minor > otherMinor) {
			return 1;
		}

		int otherRevision = other.getRevision();
		if (revision < otherRevision) {
			return -1;
		} else if (revision > otherRevision) {
			return 1;
		}

		return 0;
	}

	public enum Flavour {
		UNKNOWN("Unknown"),
		CRAFTBUKKIT("CraftBukkit"),
		SPORTBUKKIT("SportBukkit");

		private final String niceName;
		private Flavour(String niceName) {
			this.niceName = niceName;
		}

		@Override
		public String toString() {
			return niceName;
		}

		public static Flavour byName(String name) {
			for (Flavour flavour : values()) {
				if (flavour.niceName.equals(name)) {
					return flavour;
				}
			}
			return UNKNOWN;
		}
	}
}
