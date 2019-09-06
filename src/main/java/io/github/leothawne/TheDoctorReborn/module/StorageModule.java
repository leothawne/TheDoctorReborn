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
import org.bukkit.entity.Player;

import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;
import io.github.leothawne.TheDoctorReborn.type.DataType;

public final class StorageModule {
	private StorageModule() {}
	public static final void preLoad(final TheDoctorReborn plugin, final ConsoleModule console) {
		final File storageFile = new File(plugin.getDataFolder(), "players.yml");
		if(!storageFile.exists()) {
			console.info("Creating players.yml...");
			plugin.saveResource("players.yml", false);
			console.info("Created players.yml.");
		}
	}
	public static final FileConfiguration load(final TheDoctorReborn plugin, final ConsoleModule console) {
		final File storageFile = new File(plugin.getDataFolder(), "players.yml");
		if(storageFile.exists()) {
			final FileConfiguration storage = new YamlConfiguration();
			try {
				console.info("Loading players.yml...");
				storage.load(storageFile);
				console.info("Loaded players.yml.");
				return storage;
			} catch (final IOException | InvalidConfigurationException exception) {
				exception.printStackTrace();
			}
			return null;
		}
		console.severe("players.yml is missing.");
		return null;
	}
	public static final void saveData(final TheDoctorReborn plugin, final ConsoleModule console, final FileConfiguration regenerationData) {
		final File storageFile = new File(plugin.getDataFolder(), "players.yml");
		if(storageFile.exists()) {
			try {
				console.info("Saving players.yml...");
				regenerationData.save(storageFile);
				console.info("Saved players.yml.");
			} catch (final IOException exception) {
				exception.printStackTrace();
				console.severe("Could not save the players.yml file.");
			}
			return;
		}
		console.severe("players.yml is missing.");
	}
	public static final Object getPlayer(final FileConfiguration regenerationData, final Player player, final DataType section) {
		if(section.equals(DataType.REGENERATION_NUMBER)) return regenerationData.getInt("players." + player.getUniqueId() + ".regeneration");
		if(section.equals(DataType.REGENERATION_CYCLE)) return regenerationData.getInt("players." + player.getUniqueId() + ".cycle");
		if(section.equals(DataType.REGENERATION_LOCKED)) return regenerationData.getBoolean("players." + player.getUniqueId() + ".locked");
		return null;
	}
	public static final void setPlayer(final FileConfiguration regenerationData, final Player player, final DataType section, final Object value) {
		if(section.equals(DataType.REGENERATION_NUMBER)) regenerationData.set("players." + player.getUniqueId() + ".regeneration", (int) value);
		if(section.equals(DataType.REGENERATION_CYCLE)) regenerationData.set("players." + player.getUniqueId() + ".cycle", (int) value);
		if(section.equals(DataType.REGENERATION_LOCKED)) regenerationData.set("players." + player.getUniqueId() + ".locked", (boolean) value);
	}
	public static final boolean checkPlayer(final FileConfiguration regenerationData, final Player player) {
		if(regenerationData.isSet("players." + player.getUniqueId() + ".regeneration") && regenerationData.isSet("players." + player.getUniqueId() + ".cycle") && regenerationData.isSet("players." + player.getUniqueId() + ".locked")) {
			return true;
		} else {
			StorageModule.setPlayer(regenerationData, player, DataType.REGENERATION_NUMBER, 0);
			StorageModule.setPlayer(regenerationData, player, DataType.REGENERATION_CYCLE, 1);
			StorageModule.setPlayer(regenerationData, player, DataType.REGENERATION_LOCKED, false);
			return false;
		}
	}
}