package io.github.leothawne.TheDoctorReborn.api.utility;

public class SpigotAPI {
	@SuppressWarnings("unused")
	private static Class<?> spigot;
	public static final boolean isSpigot() {
		try {
			spigot = Class.forName("org.spigotmc.CustomTimingsHandler");
		} catch (ClassNotFoundException exception) {
			return false;
		}
		return true;
	}
}