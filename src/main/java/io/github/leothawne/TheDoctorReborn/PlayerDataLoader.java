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
package io.github.leothawne.TheDoctorReborn;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import io.github.leothawne.TheDoctorReborn.type.DataSectionType;

public class PlayerDataLoader {
	public static final boolean check(TheDoctorReborn plugin, ConsoleLoader myLogger) {
		myLogger.info("Looking for the players.yml file...");
		File configFile = new File(plugin.getDataFolder(), "players.yml");
		if(configFile.exists() == false) {
			myLogger.warning("The players.yml file not found. Creating a new one...");
			plugin.saveResource("players.yml", true);
			myLogger.info("New players.yml file created.");
			return false;
		} else {
			myLogger.info("The players.yml file found.");
			return true;
		}
	}
	public static final FileConfiguration load(TheDoctorReborn plugin, ConsoleLoader myLogger) {
		File playersFile = new File(plugin.getDataFolder(), "players.yml");
		if(playersFile.exists()) {
			FileConfiguration regenerationData = new YamlConfiguration();
			try {
				regenerationData.load(playersFile);
				return regenerationData;
			} catch(IOException | InvalidConfigurationException exception) {
				myLogger.severe("The players.yml could not be loaded.");
				exception.printStackTrace();
				return null;
			}
		} else {
			myLogger.severe("The players.yml file was not found to be loaded.");
			myLogger.severe("Running without the players.yml file. You will face several errors from this point.");
			return null;
		}
	}
	public static final boolean save(TheDoctorReborn plugin, ConsoleLoader myLogger, FileConfiguration regenerationData) {
		File playersFile = new File(plugin.getDataFolder(), "players.yml");
		if(playersFile.exists()) {
			try {
				regenerationData.save(playersFile);
				return true;
			} catch(IOException exception) {
				myLogger.severe("The players.yml could not be saved.");
				exception.printStackTrace();
				return false;
			}
		} else {
			myLogger.severe("The players.yml file was not found! Skipping...");
			return false;
		}
	}
	public static final Object getPlayer(FileConfiguration regenerationData, Player player, DataSectionType section) {
		if(section.equals(DataSectionType.REGENERATION_NUMBER)) {
			return regenerationData.getInt("players." + player.getUniqueId() + ".regeneration");
		}
		if(section.equals(DataSectionType.REGENERATION_CYCLE)) {
			return regenerationData.getInt("players." + player.getUniqueId() + ".cycle");
		}
		if(section.equals(DataSectionType.REGENERATION_LOCKED)) {
			return regenerationData.getBoolean("players." + player.getUniqueId() + ".locked");
		}
		return null;
	}
	public static final void setPlayer(FileConfiguration regenerationData, Player player, DataSectionType section, Object value) {
		if(section.equals(DataSectionType.REGENERATION_NUMBER)) {
			regenerationData.set("players." + player.getUniqueId() + ".regeneration", (int) value);
		}
		if(section.equals(DataSectionType.REGENERATION_CYCLE)) {
			regenerationData.set("players." + player.getUniqueId() + ".cycle", (int) value);
		}
		if(section.equals(DataSectionType.REGENERATION_LOCKED)) {
			regenerationData.set("players." + player.getUniqueId() + ".locked", (boolean) value);
		}
	}
	public static final boolean checkPlayer(FileConfiguration regenerationData, Player player) {
		if(regenerationData.isSet("players." + player.getUniqueId() + ".regeneration") && regenerationData.isSet("players." + player.getUniqueId() + ".cycle") && regenerationData.isSet("players." + player.getUniqueId() + ".locked")) {
			return true;
		} else {
			PlayerDataLoader.setPlayer(regenerationData, player, DataSectionType.REGENERATION_NUMBER, 0);
			PlayerDataLoader.setPlayer(regenerationData, player, DataSectionType.REGENERATION_CYCLE, 1);
			PlayerDataLoader.setPlayer(regenerationData, player, DataSectionType.REGENERATION_LOCKED, false);
			return false;
		}
	}
}