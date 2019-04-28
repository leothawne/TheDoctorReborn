/*
 * Copyright (C) 2019 Murilo Amaral Nappi (murilonappi@gmail.com)
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
package io.github.leothawne.TheDoctorReborn.api.utility;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import io.github.leothawne.TheDoctorReborn.PlayerDataLoader;
import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;
import io.github.leothawne.TheDoctorReborn.type.DataSectionType;

public class RegenerationAPI {
	public static final void playerRegenerate(TheDoctorReborn plugin, FileConfiguration language, FileConfiguration regenerationData, Player player, HashMap<UUID, Boolean> isRegenerating, HashMap<UUID, Integer> regenerationTaskNumber, boolean symbioticNuclei) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 14, 1));
		player.sendTitle(ChatColor.BLUE + language.getString("player-regenerating"), "", 20 * 1, 20 * 11, 20 * 1);
		isRegenerating.put(player.getUniqueId(), true);
		int newNumber;
		if(symbioticNuclei) {
			newNumber = (int) PlayerDataLoader.getPlayer(regenerationData, player, DataSectionType.REGENERATION_CYCLE) + 1;
		} else {
			newNumber = (int) PlayerDataLoader.getPlayer(regenerationData, player, DataSectionType.REGENERATION_NUMBER) + 1;
		}
		BukkitTask task = new BukkitRunnable() {
			@Override
			public final void run() {
				if(symbioticNuclei) {
					PlayerDataLoader.setPlayer(regenerationData, player, DataSectionType.REGENERATION_CYCLE, newNumber);
					PlayerDataLoader.setPlayer(regenerationData, player, DataSectionType.REGENERATION_NUMBER, 0);
				} else {
					PlayerDataLoader.setPlayer(regenerationData, player, DataSectionType.REGENERATION_NUMBER, newNumber);
				}
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