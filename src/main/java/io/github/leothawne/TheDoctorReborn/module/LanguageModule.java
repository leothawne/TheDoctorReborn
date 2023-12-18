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
	public static final void preLoad() {
		final File languageFile = new File(TheDoctorReborn.getInstance().getDataFolder(), TheDoctorReborn.getInstance().getConfiguration().getString("language") + ".yml");
		if(!languageFile.exists()) {
			TheDoctorReborn.getInstance().getConsole().info("Creating " + TheDoctorReborn.getInstance().getConfiguration().getString("language") + ".yml...");
			if(TheDoctorReborn.getInstance().getConfiguration().getString("language").equalsIgnoreCase("english") || TheDoctorReborn.getInstance().getConfiguration().getString("language").equalsIgnoreCase("portuguese")) {
				TheDoctorReborn.getInstance().saveResource(TheDoctorReborn.getInstance().getConfiguration().getString("language") + ".yml", false);
				TheDoctorReborn.getInstance().getConsole().info("Created " + TheDoctorReborn.getInstance().getConfiguration().getString("language") + ".yml.");
			} else TheDoctorReborn.getInstance().getConsole().severe(TheDoctorReborn.getInstance().getConfiguration().getString("language") + " is not supported yet. I suggest you to manually create a new file named " + TheDoctorReborn.getInstance().getConfiguration().getString("language") + ".yml and manually create the desired translation.");
		}
	}
	public static final FileConfiguration load() {
		final File languageFile = new File(TheDoctorReborn.getInstance().getDataFolder(), TheDoctorReborn.getInstance().getConfiguration().getString("language") + ".yml");
		if(languageFile.exists() && languageFile.isFile()) {
			final FileConfiguration languageConfig = new YamlConfiguration();
			try {
				TheDoctorReborn.getInstance().getConsole().info("Loading " + TheDoctorReborn.getInstance().getConfiguration().getString("language") + ".yml...");
				languageConfig.load(languageFile);
				TheDoctorReborn.getInstance().getConsole().info("Loaded " + TheDoctorReborn.getInstance().getConfiguration().getString("language") + ".yml.");
				return languageConfig;
			} catch(final IOException | InvalidConfigurationException exception) {
				exception.printStackTrace();
			}
			return null;
		}
		TheDoctorReborn.getInstance().getConsole().severe(TheDoctorReborn.getInstance().getConfiguration().getString("language") + ".yml is missing.");
		return null;
	}
	public static final boolean isItOutdated() {
		final File languageFile = new File(TheDoctorReborn.getInstance().getDataFolder(), TheDoctorReborn.getInstance().getConfiguration().getString("language") + ".yml");
		if(languageFile.exists() && languageFile.isFile()) {
			final FileConfiguration languageConfig = new YamlConfiguration();
			try {
				languageConfig.load(languageFile);
				int languageVersion = 0;
				if(TheDoctorReborn.getInstance().getConfiguration().getString("language").equalsIgnoreCase("english")) languageVersion = Integer.parseInt(DataModule.getVersion(VersionType.ENGLISH_YML));
				if(TheDoctorReborn.getInstance().getConfiguration().getString("language").equalsIgnoreCase("portuguese")) languageVersion = Integer.parseInt(DataModule.getVersion(VersionType.PORTUGUESE_YML));
				if(languageVersion != 0) if(languageConfig.getInt("language-version") != languageVersion) return true;
			} catch (final IOException | InvalidConfigurationException exception) {
				exception.printStackTrace();
			}
		}
		return false;
	}
	public static final boolean deleteFile() {
		final File languageFile = new File(TheDoctorReborn.getInstance().getDataFolder(), TheDoctorReborn.getInstance().getConfiguration().getString("language") + ".yml");
		if(languageFile.exists() && languageFile.isFile()) return languageFile.delete();
		return false;
	}
}