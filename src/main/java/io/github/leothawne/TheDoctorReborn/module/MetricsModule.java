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

import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;
import io.github.leothawne.TheDoctorReborn.api.MetricsAPI;

public final class MetricsModule {
	private MetricsModule() {}
	public static final MetricsAPI init(final TheDoctorReborn plugin, final ConsoleModule console) {
		final MetricsAPI metrics = new MetricsAPI(plugin);
		if(!metrics.isEnabled()) {
			console.warning(plugin.getDescription().getName() + " has detected that bStats is disabled. Enable it so we can collect data that will help us improve future versions of the plugin. Read more at: https://bstats.org/getting-started");
		}
		return metrics;
	}
}