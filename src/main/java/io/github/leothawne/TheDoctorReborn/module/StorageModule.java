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
	public static final void preLoad() {
		final File storageFile = new File(TheDoctorReborn.getInstance().getDataFolder(), "players.yml");
		if(!storageFile.exists()) {
			TheDoctorReborn.getInstance().getConsole().info("Creating players.yml...");
			TheDoctorReborn.getInstance().saveResource("players.yml", false);
			TheDoctorReborn.getInstance().getConsole().info("Created players.yml.");
		}
	}
	public static final FileConfiguration load() {
		final File storageFile = new File(TheDoctorReborn.getInstance().getDataFolder(), "players.yml");
		if(storageFile.exists() && storageFile.isFile()) {
			final FileConfiguration storage = new YamlConfiguration();
			try {
				TheDoctorReborn.getInstance().getConsole().info("Loading players.yml...");
				storage.load(storageFile);
				TheDoctorReborn.getInstance().getConsole().info("Loaded players.yml.");
				return storage;
			} catch (final IOException | InvalidConfigurationException exception) {
				exception.printStackTrace();
			}
			return null;
		}
		TheDoctorReborn.getInstance().getConsole().severe("players.yml is missing.");
		return null;
	}
	public static final void saveData(final boolean announce) {
		final File storageFile = new File(TheDoctorReborn.getInstance().getDataFolder(), "players.yml");
		if(storageFile.exists() && storageFile.isFile()) {
			try {
				if(announce) TheDoctorReborn.getInstance().getConsole().info("Saving players.yml...");
				TheDoctorReborn.getInstance().getRegenData().save(storageFile);
				if(announce) TheDoctorReborn.getInstance().getConsole().info("Saved players.yml.");
			} catch (final IOException exception) {
				exception.printStackTrace();
				if(announce) TheDoctorReborn.getInstance().getConsole().severe("Could not save the players.yml file.");
			}
			return;
		}
		if(announce) TheDoctorReborn.getInstance().getConsole().severe("players.yml is missing.");
	}
	public static final Object getPlayer(final Player player, final DataType section) {
		if(section.equals(DataType.REGENERATION_NUMBER)) return TheDoctorReborn.getInstance().getRegenData().getInt("players." + player.getUniqueId() + ".regeneration");
		if(section.equals(DataType.REGENERATION_CYCLE)) return TheDoctorReborn.getInstance().getRegenData().getInt("players." + player.getUniqueId() + ".cycle");
		if(section.equals(DataType.REGENERATION_LOCKED)) return TheDoctorReborn.getInstance().getRegenData().getBoolean("players." + player.getUniqueId() + ".locked");
		return null;
	}
	public static final void setPlayer(final Player player, final DataType section, final Object value) {
		if(section.equals(DataType.REGENERATION_NUMBER)) TheDoctorReborn.getInstance().getRegenData().set("players." + player.getUniqueId() + ".regeneration", (int) value);
		if(section.equals(DataType.REGENERATION_CYCLE)) TheDoctorReborn.getInstance().getRegenData().set("players." + player.getUniqueId() + ".cycle", (int) value);
		if(section.equals(DataType.REGENERATION_LOCKED)) TheDoctorReborn.getInstance().getRegenData().set("players." + player.getUniqueId() + ".locked", (boolean) value);
	}
	public static final boolean checkPlayer(final Player player) {
		if(TheDoctorReborn.getInstance().getRegenData().isSet("players." + player.getUniqueId() + ".regeneration") && TheDoctorReborn.getInstance().getRegenData().isSet("players." + player.getUniqueId() + ".cycle") && TheDoctorReborn.getInstance().getRegenData().isSet("players." + player.getUniqueId() + ".locked")) {
			return true;
		} else {
			StorageModule.setPlayer(player, DataType.REGENERATION_NUMBER, 0);
			StorageModule.setPlayer(player, DataType.REGENERATION_CYCLE, 1);
			StorageModule.setPlayer(player, DataType.REGENERATION_LOCKED, false);
			return false;
		}
	}
}