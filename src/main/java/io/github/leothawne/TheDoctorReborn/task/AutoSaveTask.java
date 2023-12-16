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

import org.bukkit.configuration.file.FileConfiguration;

import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;
import io.github.leothawne.TheDoctorReborn.module.ConsoleModule;
import io.github.leothawne.TheDoctorReborn.module.StorageModule;

public final class AutoSaveTask implements Runnable {
	private TheDoctorReborn plugin;
	private ConsoleModule console;
	private FileConfiguration regenerationData;
	public AutoSaveTask(final TheDoctorReborn plugin, final ConsoleModule console, final FileConfiguration regenerationData) {
		this.plugin = plugin;
		this.console = console;
		this.regenerationData = regenerationData;
	}
	@Override
	public final void run() {
		StorageModule.saveData(this.plugin, this.console, this.regenerationData, false);
	}
}