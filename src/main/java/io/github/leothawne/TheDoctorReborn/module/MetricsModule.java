package io.github.leothawne.TheDoctorReborn.module;

import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;
import io.github.leothawne.TheDoctorReborn.api.MetricsAPI;

public final class MetricsModule {
	private MetricsModule() {}
	public static final MetricsAPI init() {
		final MetricsAPI metrics = new MetricsAPI(TheDoctorReborn.getInstance(), 4083);
		return metrics;
	}
}