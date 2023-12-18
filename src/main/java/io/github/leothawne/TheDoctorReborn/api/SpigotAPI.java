package io.github.leothawne.TheDoctorReborn.api;

public final class SpigotAPI {
	private SpigotAPI() {}
	public static final boolean isSpigot() {
		try {
			Class.forName("org.spigotmc.CustomTimingsHandler");
			return true;
		} catch (final ClassNotFoundException exception) {
			return false;
		}
	}
}