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
package io.github.leothawne.TheDoctorReborn.listener;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class AdminListener implements Listener {
	private FileConfiguration configuration;
	public AdminListener(final FileConfiguration configuration) {
		this.configuration = configuration;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onPlayerJoin(final PlayerJoinEvent event) {
		final Player player = (Player) event.getPlayer();
		if(player.hasPermission("TheDoctorReborn.admin") || player.isOp()) if(this.configuration.getBoolean("update.check")) player.performCommand("rebornadmin update");
	}
}