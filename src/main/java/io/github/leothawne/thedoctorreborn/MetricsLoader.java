package io.github.leothawne.thedoctorreborn;

import io.github.leothawne.thedoctorreborn.api.bStats.MetricsAPI;

public class MetricsLoader {
	private static TheDoctorRebornLoader plugin;
	private static ConsoleLoader myLogger;
	protected MetricsLoader(TheDoctorRebornLoader plugin, ConsoleLoader myLogger) {
		MetricsLoader.plugin = plugin;
		MetricsLoader.myLogger = myLogger;
	}
	protected static final void init() {
		MetricsAPI metrics = new MetricsAPI(plugin);
		if(metrics.isEnabled() == true) {
			myLogger.info("The Doctor Reborn is using bStats to collect data to improve the next versions. In case you want to know what data will be collected: [https://bstats.org/getting-started]");
		} else {
			myLogger.warning("bStats is disabled and Where Are You cannot use it. Please enable bStats! Thank you.");
		}
	}
}