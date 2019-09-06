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

import java.util.HashMap;

import io.github.leothawne.TheDoctorReborn.type.ProjectPageType;
import io.github.leothawne.TheDoctorReborn.type.VersionType;

public final class DataModule {
	private DataModule() {}
	private static final String PROJECT_PAGES = "SPIGOT_MC:https://www.spigotmc.org/resources/60429";
	private static final String VERSIONS = "CONFIG_YML:5,ENGLISH_YML:1,PORTUGUESE_YML:1,MINECRAFT:1.14,JAVA:7";
	private static final String Plugin_Date = "12/06/2019 15:34 (BRT)";
	private static final String Bukkit_API = "spigot-api-1.14-R0.1-SNAPSHOT";
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
