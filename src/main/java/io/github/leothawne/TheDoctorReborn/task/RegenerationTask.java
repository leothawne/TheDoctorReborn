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
package io.github.leothawne.TheDoctorReborn.task;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;

public class RegenerationTask implements Runnable {
	private static TheDoctorReborn plugin;
	private static HashMap<UUID, Boolean> isRegenerating;
	private static HashMap<UUID, Boolean> isLocked;
	public RegenerationTask(TheDoctorReborn plugin, HashMap<UUID, Boolean> isRegenerating, HashMap<UUID, Boolean> isLocked) {
		RegenerationTask.plugin = plugin;
		RegenerationTask.isRegenerating = isRegenerating;
		RegenerationTask.isLocked = isLocked;
	}
	@Override
	public final void run() {
		for(Player player : plugin.getServer().getOnlinePlayers()) {
			if(player.hasPermission("TheDoctorReborn.use")) {
				if(isLocked.get(player.getUniqueId()).booleanValue() == false) {
					if(isRegenerating.get(player.getUniqueId()).booleanValue() == true) {
						plugin.getServer().getWorld(player.getLocation().getWorld().getUID()).playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 100);
						plugin.getServer().getWorld(player.getLocation().getWorld().getUID()).playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
						plugin.getServer().getWorld(player.getLocation().getWorld().getUID()).playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 1F, 1F);
						plugin.getServer().getWorld(player.getLocation().getWorld().getUID()).playSound(player.getLocation(), Sound.ENTITY_GENERIC_BURN, 1F, 1F);
					}
				}
			}
		}
	}
}