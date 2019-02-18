package io.github.leothawne.thedoctorreborn;

import java.io.File;
import java.io.IOException;

import com.google.common.io.Files;

public class ResourceLoader {
	private static TheDoctorRebornLoader plugin;
	private static ConsoleLoader myLogger;
	public ResourceLoader(TheDoctorRebornLoader plugin, ConsoleLoader myLogger) {
		ResourceLoader.plugin = plugin;
		ResourceLoader.myLogger = myLogger;
	}
	public static final void run() {
		myLogger.info("Updating resources...");
		File database = getFile("database");
		if(!database.exists()) {
			myLogger.warning("Installing resource: tdr.db");
			database.getParentFile().mkdirs();
			plugin.saveResource("resources\\tdr.db", false);
			try {
				Files.move(new File(plugin.getDataFolder(), "resources\\tdr.db"), new File(plugin.getDataFolder(), "tdr.db"));
			} catch(IOException e) {
				myLogger.severe("Error while installing resources: " + e.getMessage());
			}
			myLogger.warning("Resource installed!.");
		}
		myLogger.info("Resources updated.");
	}
	public static final File getFile(String fileName) {
		if(fileName.equals("database")) {
			return new File(plugin.getDataFolder(), "tdr.db");
		}
		return null;
	}
}
