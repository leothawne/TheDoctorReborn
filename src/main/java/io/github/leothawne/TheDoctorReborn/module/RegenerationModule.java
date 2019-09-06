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

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;
import io.github.leothawne.TheDoctorReborn.type.DataType;

public final class RegenerationModule {
	private RegenerationModule() {}
	public static final void beginRegeneration(final TheDoctorReborn plugin, final FileConfiguration language, final FileConfiguration regenerationData, final Player player, final HashMap<UUID, Boolean> isRegenerating, final HashMap<UUID, Integer> regenerationTaskNumber, final boolean symbioticNuclei) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 14, 1));
		player.sendTitle(ChatColor.BLUE + language.getString("player-regenerating"), "", 20 * 1, 20 * 11, 20 * 1);
		isRegenerating.put(player.getUniqueId(), true);
		int number = 0;
		if(symbioticNuclei) {
			number = (int) StorageModule.getPlayer(regenerationData, player, DataType.REGENERATION_CYCLE) + 1;
		} else number = (int) StorageModule.getPlayer(regenerationData, player, DataType.REGENERATION_NUMBER) + 1;
		final int newNumber = number;
		final BukkitTask task = new BukkitRunnable() {
			@Override
			public final void run() {
				if(symbioticNuclei) {
					StorageModule.setPlayer(regenerationData, player, DataType.REGENERATION_CYCLE, newNumber);
					StorageModule.setPlayer(regenerationData, player, DataType.REGENERATION_NUMBER, 0);
				} else StorageModule.setPlayer(regenerationData, player, DataType.REGENERATION_NUMBER, newNumber);
				isRegenerating.put(player.getUniqueId(), false);
				player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 20 * 120, 14));
				player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 120, 14));
				player.setHealth(78.0);
				player.setFoodLevel(20);
			}
		}.runTaskLater(plugin, 20 * 13);
		regenerationTaskNumber.put(player.getUniqueId(), task.getTaskId());
	}
}