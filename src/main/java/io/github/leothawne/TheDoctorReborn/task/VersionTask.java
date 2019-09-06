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
package io.github.leothawne.TheDoctorReborn.task;

import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;
import io.github.leothawne.TheDoctorReborn.api.HTTPAPI;
import io.github.leothawne.TheDoctorReborn.module.ConsoleModule;
import io.github.leothawne.TheDoctorReborn.module.DataModule;
import io.github.leothawne.TheDoctorReborn.type.ProjectPageType;

public final class VersionTask implements Runnable {
	private TheDoctorReborn plugin;
	private ConsoleModule console;
	public VersionTask(final TheDoctorReborn plugin, final ConsoleModule console) {
		this.plugin = plugin;
		this.console = console;
	}
	@Override
	public final void run() {
		final String version = this.plugin.getDescription().getVersion();
		final String url = DataModule.getPluginURL(version);
		final String response = HTTPAPI.getData(url);
		if(response != null) {
			if(response.equalsIgnoreCase("disabled")) {
				this.console.warning("The version " + version + " is no longer allowed to be played.");
				this.console.warning("Download a newer version at " + DataModule.getProjectPage(ProjectPageType.SPIGOT_MC));
				this.plugin.getServer().getPluginManager().disablePlugin(this.plugin);
			}
		} else {
			this.console.severe("Unable to locate: " + url);
		}
	}
}