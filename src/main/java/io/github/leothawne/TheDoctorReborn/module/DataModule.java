package io.github.leothawne.TheDoctorReborn.module;

import java.util.HashMap;

import io.github.leothawne.TheDoctorReborn.type.ProjectPageType;
import io.github.leothawne.TheDoctorReborn.type.VersionType;

public final class DataModule {
	private DataModule() {}
	private static final String PROJECT_PAGES = "SPIGOT_MC:https://www.spigotmc.org/resources/60429";
	private static final String VERSIONS = "CONFIG_YML:5,ENGLISH_YML:2,PORTUGUESE_YML:2,MINECRAFT:1.20.4,JAVA:18";
	private static final String Plugin_Date = "16/12/2023 16:21 (BRT)";
	private static final String Bukkit_API = "spigot-api-1.20.4-R0.1-SNAPSHOT";
	private static final String Update_URL = "https://leothawne.github.io/TheDoctorReborn/api/$version.html";
	private static final String Plugin_URL = "https://leothawne.github.io/TheDoctorReborn/api/$version/plugin.html";
	public static final String getProjectPage(final ProjectPageType type) {
		final String[] pageString = DataModule.PROJECT_PAGES.split(",");
		final HashMap<ProjectPageType, String> pageMap = new HashMap<ProjectPageType, String>();
		for(final String page : pageString) {
			final String[] string = page.split(":");
			pageMap.put(ProjectPageType.valueOf(string[0]), string[1]);
		}
		return pageMap.get(type);
	}
	public static final String getVersion(final VersionType type) {
		final String[] versionString = DataModule.VERSIONS.split(",");
		final HashMap<VersionType, String> versionMap = new HashMap<VersionType, String>();
		for(final String version : versionString) {
			final String[] string = version.split(":");
			versionMap.put(VersionType.valueOf(string[0]), string[1]);
		}
		return versionMap.get(type);
	}
	public static final String getVersionDate() {
		return DataModule.Plugin_Date;
	}
	public static final String getBukkitAPI() {
		return DataModule.Bukkit_API;
	}
	public static final String getUpdateURL() {
		String Update_URL = DataModule.Update_URL;
		Update_URL = Update_URL.replace("$version", DataModule.getVersion(VersionType.MINECRAFT));
		return Update_URL;
	}
	public static final String getPluginURL(final String version) {
		String Plugin_URL = DataModule.Plugin_URL;
		Plugin_URL = Plugin_URL.replace("$version", version);
		return Plugin_URL;
	}
}
