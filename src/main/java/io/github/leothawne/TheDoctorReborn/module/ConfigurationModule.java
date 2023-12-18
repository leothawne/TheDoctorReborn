package io.github.leothawne.TheDoctorReborn.module;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bukkit.configuration.file.FileConfiguration;

import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;
import io.github.leothawne.TheDoctorReborn.type.VersionType;

public final class ConfigurationModule {
	private ConfigurationModule() {}
	public static final void preLoad() {
		final File configFile = new File(TheDoctorReborn.getInstance().getDataFolder(), "config.yml");
		if(!configFile.exists()) {
			TheDoctorReborn.getInstance().getConsole().info("Creating config.yml...");
			TheDoctorReborn.getInstance().saveDefaultConfig();
			TheDoctorReborn.getInstance().getConsole().info("Created config.yml.");
		}
	}
	public static final FileConfiguration load() {
		final File configFile = new File(TheDoctorReborn.getInstance().getDataFolder(), "config.yml");
		if(configFile.exists() && configFile.isFile()) {
			TheDoctorReborn.getInstance().getConsole().info("Loading config.yml...");
			final FileConfiguration configuration = TheDoctorReborn.getInstance().getConfig();
			TheDoctorReborn.getInstance().getConsole().info("Loaded config.yml.");
			return configuration;
		}
		TheDoctorReborn.getInstance().getConsole().severe("config.yml is missing.");
		return null;
	}
	public static final boolean isItOutdated() {
		final File configFile = new File(TheDoctorReborn.getInstance().getDataFolder(), "config.yml");
		if(configFile.exists() && configFile.isFile()) {
			final FileConfiguration configuration = TheDoctorReborn.getInstance().getConfig();
			if(configuration.getInt("config-version") != Integer.parseInt(DataModule.getVersion(VersionType.CONFIG_YML))) return true;
		}
		return false;
	}
	public static final boolean makeOldFile() {
		final File configFile = new File(TheDoctorReborn.getInstance().getDataFolder(), "config.yml");
		final SimpleDateFormat format = new SimpleDateFormat("HH_mm_ss-yyyy_MM_dd");
		if(configFile.exists() && configFile.isFile()) return configFile.renameTo(new File(TheDoctorReborn.getInstance().getDataFolder(), "config-" + format.format(Calendar.getInstance().getTime()) + ".yml"));
		return false;
	}
}