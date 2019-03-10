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
package io.github.leothawne.TheDoctorReborn.event;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.leothawne.TheDoctorReborn.ConsoleLoader;
import io.github.leothawne.TheDoctorReborn.PlayersFileLoader;
import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;
public class PlayerEvent implements Listener {
	private static TheDoctorReborn plugin;
	private static ConsoleLoader myLogger;
	private static HashMap<UUID, Integer> regenerationNumber;
	private static HashMap<UUID, Boolean> isRegenerating;
	private static HashMap<UUID, Boolean> isLocked;
	public PlayerEvent(HashMap<UUID, Integer> regenerationNumber, HashMap<UUID, Boolean> isRegenerating, HashMap<UUID, Boolean> isLocked) {
		PlayerEvent.isRegenerating = isRegenerating;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public static final void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		PlayersFileLoader.load(plugin, myLogger, player, regenerationNumber, isLocked, false);
		isRegenerating.put(player.getUniqueId(), false);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public static final void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		PlayersFileLoader.save(plugin, myLogger, player, regenerationNumber, isLocked, false);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public static final void onPlayerDamage(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if(player.hasPermission("TheDoctorReborn.use")) {
				if(regenerationNumber.get(player.getUniqueId()).intValue() < 12) {
					if(isLocked.get(player.getUniqueId()).booleanValue() == false) {
						if(event.getFinalDamage() > player.getHealth()) {
							event.setCancelled(true);
							player.setInvulnerable(true);
							isRegenerating.put(player.getUniqueId(), true);
							new BukkitRunnable() {
								@Override
								public final void run() {
									player.setInvulnerable(false);
									regenerationNumber.put(player.getUniqueId(), regenerationNumber.get(player.getUniqueId()) + 1);
									isRegenerating.put(player.getUniqueId(), false);
									player.setHealth(20.0);
									player.setFoodLevel(20);
								}
							}.runTaskLaterAsynchronously(plugin, 20 * 10);
						}
					}
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public static final void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if(player.hasPermission("TheDoctorReborn.use")) {
			if(isLocked.get(player.getUniqueId()).booleanValue() == true) {
				event.setDeathMessage(player.getName() + "  refused to regenerate.");
			} else {
				if(regenerationNumber.get(player.getUniqueId()).intValue() == 12) {
					regenerationNumber.put(player.getUniqueId(), 0);
					event.setDeathMessage(player.getName() + " has used all of its 13 lives and had its regeneration cycle restarted.");
				} else {
					myLogger.severe(player.getName() + " died for no reason!");
				}
			}
		}
	}
}