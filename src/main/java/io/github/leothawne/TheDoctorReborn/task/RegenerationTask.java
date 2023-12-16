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

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;
import io.github.leothawne.TheDoctorReborn.module.StorageModule;
import io.github.leothawne.TheDoctorReborn.type.DataType;

public final class RegenerationTask implements Runnable {
	private TheDoctorReborn plugin;
	private FileConfiguration regenerationData;
	private HashMap<UUID, Boolean> isRegenerating;
	public RegenerationTask(final TheDoctorReborn plugin, final FileConfiguration regenerationData, final HashMap<UUID, Boolean> isRegenerating) {
		this.plugin = plugin;
		this.regenerationData = regenerationData;
		this.isRegenerating = isRegenerating;
	}
	@Override
	public final void run() {
		for(final Player player : this.plugin.getServer().getOnlinePlayers()) if(player.hasPermission("TheDoctorReborn.use")) if((boolean) StorageModule.getPlayer(this.regenerationData, player, DataType.REGENERATION_LOCKED) == false) if(this.isRegenerating.get(player.getUniqueId()).booleanValue() == true) {
			this.plugin.getServer().getWorld(player.getLocation().getWorld().getUID()).playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
			this.plugin.getServer().getWorld(player.getLocation().getWorld().getUID()).playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 1);
			this.plugin.getServer().getWorld(player.getLocation().getWorld().getUID()).playSound(player.getLocation(), Sound.ENTITY_GENERIC_BURN, 2, 2);
		}
	}
}