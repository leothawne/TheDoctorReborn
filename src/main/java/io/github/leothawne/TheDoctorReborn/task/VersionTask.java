package io.github.leothawne.TheDoctorReborn.task;

import org.bukkit.Bukkit;

import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;
import io.github.leothawne.TheDoctorReborn.api.HTTPAPI;
import io.github.leothawne.TheDoctorReborn.module.DataModule;
import io.github.leothawne.TheDoctorReborn.type.ProjectPageType;

public final class VersionTask implements Runnable {
	public VersionTask() {}
	@Override
	public final void run() {
		final String version = TheDoctorReborn.getInstance().getDescription().getVersion();
		final String url = DataModule.getPluginURL(version);
		final String response = HTTPAPI.getData(url);
		if(response != null) {
			if(response.equalsIgnoreCase("disabled")) {
				TheDoctorReborn.getInstance().getConsole().warning("The version " + version + " is no longer allowed to be played.");
				TheDoctorReborn.getInstance().getConsole().warning("Download a newer version at " + DataModule.getProjectPage(ProjectPageType.SPIGOT_MC));
				Bukkit.getPluginManager().disablePlugin(TheDoctorReborn.getInstance());
			}
		} else TheDoctorReborn.getInstance().getConsole().severe("Unable to locate: " + url);
	}
}