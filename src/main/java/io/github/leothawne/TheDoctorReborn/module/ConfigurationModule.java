/*
 * Copyright (C) 2019 Murilo Amaral Nappi
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
package io.github.leothawne.TheDoctorReborn.module;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;
import io.github.leothawne.TheDoctorReborn.type.VersionType;

public final class ConfigurationModule {
	private ConfigurationModule() {}
	public static final void preLoad(final TheDoctorReborn plugin, final ConsoleModule console) {
		final File configFile = new File(plugin.getDataFolder(), "config.yml");
		if(!configFile.exists()) {
			console.info("Creating config.yml...");
			plugin.saveDefaultConfig();
			console.info("Created config.yml.");
		}
	}
	public static final FileConfiguration load(final TheDoctorReborn plugin, final ConsoleModule console) {
		final File configFile = new File(plugin.getDataFolder(), "config.yml");
		if(configFile.exists()) {
			console.info("Loading config.yml...");
			final FileConfiguration configuration = plugin.getConfig();
			console.info("Loaded config.yml.");
			return configuration;
		}
		console.severe("config.yml is missing.");
		return null;
	}
	public static final boolean isItOutdated(final TheDoctorReborn plugin) {
		final File configFile = new File(plugin.getDataFolder(), "config.yml");
		if(configFile.exists()) {
			final FileConfiguration configuration = plugin.getConfig();
			if(configuration.getInt("config-version") != Integer.parseInt(DataModule.getVersion(VersionType.CONFIG_YML))) {
				return true;
			} else return false;
		}
		return true;
	}
	public static final boolean deleteFile(final TheDoctorReborn plugin) {
		final File configFile = new File(plugin.getDataFolder(), "config.yml");
		if(configFile.exists()) return configFile.delete();
		return false;
	}
}