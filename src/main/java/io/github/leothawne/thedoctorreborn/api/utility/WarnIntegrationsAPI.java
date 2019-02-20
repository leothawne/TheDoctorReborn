package io.github.leothawne.thedoctorreborn.api.utility;

import java.util.List;

import org.bukkit.plugin.Plugin;

import io.github.leothawne.thedoctorreborn.TheDoctorRebornLoader;

public class WarnIntegrationsAPI {
	public WarnIntegrationsAPI(TheDoctorRebornLoader mainPlugin, List<String> plugins) {
		for(String plugin : plugins) {
			Plugin getPlugin = mainPlugin.getServer().getPluginManager().getPlugin(plugin);
			if(getPlugin != null) {
				getPlugin.getLogger().warning(getPlugin.getName() + " was successfully integrated with " + mainPlugin.getName() + "!");
			}
		}
	}
}