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
import io.github.leothawne.TheDoctorReborn.item.SymbioticNucleiItem;

public final class RecipeTask implements Runnable {
	private TheDoctorReborn plugin;
	private FileConfiguration language;
	public RecipeTask(final TheDoctorReborn plugin, final FileConfiguration language) {
		this.plugin = plugin;
		this.language = language;
	}
	@Override
	public final void run() {
		try {
			this.plugin.getServer().addRecipe(new SymbioticNucleiItem().getRecipe(this.plugin, this.language));
		} catch(IllegalStateException exception) {}
	}
}