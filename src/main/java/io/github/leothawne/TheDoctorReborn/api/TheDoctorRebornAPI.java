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
package io.github.leothawne.TheDoctorReborn.api;

import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;

/**
 * 
 * The API class.
 * 
 * @author leothawne
 * 
 */
public final class TheDoctorRebornAPI {
	private MetricsAPI metrics;
	/**
	 * 
	 * @deprecated There is no need to manually create
	 * an object with this constructor when
	 * you can easily use {@link TheDoctorReborn#getAPI()}.
	 * 
	 */
	public TheDoctorRebornAPI(final MetricsAPI metrics) {
		this.metrics = metrics;
	}
	/**
	 * 
	 * Returns a boolean type value that can be used to determine
	 * if the plugin is currently using bStats.
	 * 
	 * @return A boolean type value.
	 * 
	 */
	public final boolean isMetricsEnabled() {
		return this.metrics.isEnabled();
	}
}