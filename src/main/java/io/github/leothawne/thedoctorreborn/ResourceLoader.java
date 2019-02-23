/*
 * Copyright (C) 2019 Murilo Amaral Nappi (murilonappi@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
