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
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;


public class PlayersFileLoader {
	public static final void check(TheDoctorReborn plugin, ConsoleLoader myLogger) {
		myLogger.info("Looking for players file...");
		File configFile = new File(plugin.getDataFolder(), "players.yml");
		if(configFile.exists() == false) {
			myLogger.warning("Players file not found. Creating a new one...");
			plugin.saveResource("players.yml", true);
			myLogger.info("New players file created.");
		} else {
			myLogger.info("Players file found.");
		}
	}
	public static final void load(TheDoctorReborn plugin, ConsoleLoader myLogger, Player player, HashMap<UUID, Integer> regenerationNumber, HashMap<UUID, Boolean> isLocked, boolean init) {
		if(init == false) myLogger.info("Loading player [" + player.getName() + "]...");
		File configFile = new File(plugin.getDataFolder(), "players.yml");
		if(configFile.exists()) {
			FileConfiguration playersConfig = new YamlConfiguration();
			try {
				playersConfig.load(configFile);
				if(playersConfig.isSet("players." + player.getUniqueId() + ".regeneration") == true && playersConfig.isInt("players." + player.getUniqueId() + ".regeneration") == true) {
					regenerationNumber.put(player.getUniqueId(), playersConfig.getInt("players." + player.getUniqueId() + ".regeneration"));
				} else {
					regenerationNumber.put(player.getUniqueId(), 0);
				}
				if(playersConfig.isSet("players." + player.getUniqueId() + ".locked") == true && playersConfig.isBoolean("players." + player.getUniqueId() + ".locked") == true) {
					isLocked.put(player.getUniqueId(), playersConfig.getBoolean("players." + player.getUniqueId() + ".locked"));
				} else {
					isLocked.put(player.getUniqueId(), false);
				}
				if(init == false) myLogger.info("Player [" + player.getName() + "] loaded.");
			} catch(IOException | InvalidConfigurationException exception) {
				exception.printStackTrace();
			}
		} else {
			myLogger.severe("A players file was not found to be loaded.");
			myLogger.severe("Running without players file. You will face several errors from this point.");
		}
	}
	public static final void save(TheDoctorReborn plugin, ConsoleLoader myLogger, Player player, HashMap<UUID, Integer> regenerationNumber, HashMap<UUID, Boolean> isLocked, boolean init) {
		if(init == false) myLogger.info("Saving player [" + player.getName() + "]...");
		File configFile = new File(plugin.getDataFolder(), "players.yml");
		if(configFile.exists()) {
			FileConfiguration playersConfig = new YamlConfiguration();
			try {
				playersConfig.load(configFile);
				for(UUID uuid : regenerationNumber.keySet()) {
					playersConfig.set("players." + player.getUniqueId() + ".regeneration", regenerationNumber.get(uuid));
				}
				for(UUID uuid : isLocked.keySet()) {
					playersConfig.set("players." + player.getUniqueId() + ".locked", isLocked.get(uuid));
				}
				playersConfig.save(configFile);
				if(init == false) myLogger.info("Player [" + player.getName() + "] saved.");
			} catch(IOException | InvalidConfigurationException exception) {
				exception.printStackTrace();
			}
		} else {
			myLogger.severe("Players file not found! Skipping...");
		}
	}
}