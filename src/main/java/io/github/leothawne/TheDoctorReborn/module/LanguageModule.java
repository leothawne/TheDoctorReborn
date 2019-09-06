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
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;
import io.github.leothawne.TheDoctorReborn.type.VersionType;

public final class LanguageModule {
	private LanguageModule() {}
	public static final void preLoad(final TheDoctorReborn plugin, final ConsoleModule console, final FileConfiguration configuration) {
		final File languageFile = new File(plugin.getDataFolder(), configuration.getString("language") + ".yml");
		if(!languageFile.exists()) {
			console.info("Creating " + configuration.getString("language") + ".yml...");
			if(configuration.getString("language").equalsIgnoreCase("english") || configuration.getString("language").equalsIgnoreCase("portuguese")) {
				plugin.saveResource(configuration.getString("language") + ".yml", false);
				console.info("Created " + configuration.getString("language") + ".yml.");
			} else {
				console.severe(configuration.getString("language") + " is not supported yet. I suggest you to manually create a new file named " + configuration.getString("language") + ".yml and manually create the desired translation.");
			}
		}
	}
	public static final FileConfiguration load(final TheDoctorReborn plugin, final ConsoleModule console, final FileConfiguration configuration) {
		final File languageFile = new File(plugin.getDataFolder(), configuration.getString("language") + ".yml");
		if(languageFile.exists()) {
			final FileConfiguration languageConfig = new YamlConfiguration();
			try {
				console.info("Loading " + configuration.getString("language") + ".yml...");
				languageConfig.load(languageFile);
				console.info("Loaded " + configuration.getString("language") + ".yml.");
				return languageConfig;
			} catch(final IOException | InvalidConfigurationException exception) {
				exception.printStackTrace();
			}
			return null;
		}
		console.severe(configuration.getString("language") + ".yml is missing.");
		return null;
	}
	public static final boolean isItOutdated(final TheDoctorReborn plugin, final FileConfiguration configuration) {
		final File languageFile = new File(plugin.getDataFolder(), configuration.getString("language") + ".yml");
		if(languageFile.exists()) {
			final FileConfiguration languageConfig = new YamlConfiguration();
			try {
				languageConfig.load(languageFile);
				int languageVersion = 0;
				if(configuration.getString("language").equalsIgnoreCase("english")) {
					languageVersion = Integer.parseInt(DataModule.getVersion(VersionType.ENGLISH_YML));
				}
				if(configuration.getString("language").equalsIgnoreCase("portuguese")) {
					languageVersion = Integer.parseInt(DataModule.getVersion(VersionType.PORTUGUESE_YML));
				}
				if(languageVersion != 0) {
					if(languageConfig.getInt("language-version") != languageVersion) {
						return true;
					} else return false;
				}
			} catch (final IOException | InvalidConfigurationException exception) {
				exception.printStackTrace();
			}
		}
		return true;
	}
	public static final boolean deleteFile(final TheDoctorReborn plugin, final FileConfiguration configuration) {
		final File languageFile = new File(plugin.getDataFolder(), configuration.getString("language") + ".yml");
		if(languageFile.exists()) return languageFile.delete();
		return false;
	}
}