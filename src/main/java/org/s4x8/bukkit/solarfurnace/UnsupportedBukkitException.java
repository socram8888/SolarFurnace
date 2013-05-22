
package org.s4x8.bukkit.solarfurnace;

public class UnsupportedBukkitException extends Exception {
	public UnsupportedBukkitException() {
		super();
	};
	
	public UnsupportedBukkitException(String desc) {
		super(desc);
	};
	
	public UnsupportedBukkitException(String desc, Throwable throwable) {
		super(desc, throwable);
	};
	
	public UnsupportedBukkitException(Throwable throwable) {
		super(throwable);
	};
};
