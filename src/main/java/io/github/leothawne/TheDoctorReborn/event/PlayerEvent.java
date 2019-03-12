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
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import io.github.leothawne.TheDoctorReborn.ConsoleLoader;
import io.github.leothawne.TheDoctorReborn.PlayersFileLoader;
import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;
import io.github.leothawne.TheDoctorReborn.api.utility.RegenerationAPI;
import io.github.leothawne.TheDoctorReborn.item.SymbioticNucleiItem;
public class PlayerEvent implements Listener {
	private static TheDoctorReborn plugin;
	private static ConsoleLoader myLogger;
	private static FileConfiguration configuration;
	private static FileConfiguration language;
	private static HashMap<UUID, Integer> regenerationNumber;
	private static HashMap<UUID, Integer> regenerationCycle;
	private static HashMap<UUID, Boolean> isRegenerating;
	private static HashMap<UUID, Boolean> isLocked;
	private static HashMap<UUID, Integer> regenerationTaskNumber;
	public PlayerEvent(TheDoctorReborn plugin, ConsoleLoader myLogger, FileConfiguration configuration, FileConfiguration language, HashMap<UUID, Integer> regenerationNumber, HashMap<UUID, Integer> regenerationCycle, HashMap<UUID, Boolean> isRegenerating, HashMap<UUID, Boolean> isLocked, HashMap<UUID, Integer> regenerationTaskNumber) {
		PlayerEvent.plugin = plugin;
		PlayerEvent.myLogger = myLogger;
		PlayerEvent.configuration = configuration;
		PlayerEvent.language= language;
		PlayerEvent.regenerationNumber = regenerationNumber;
		PlayerEvent.regenerationCycle = regenerationCycle;
		PlayerEvent.isRegenerating = isRegenerating;
		PlayerEvent.isLocked = isLocked;
		PlayerEvent.regenerationTaskNumber = regenerationTaskNumber;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public static final void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		PlayersFileLoader.loadPlayer(plugin, myLogger, player, regenerationNumber, regenerationCycle, isLocked, false);
		isRegenerating.put(player.getUniqueId(), false);
		if(player.hasPermission("TheDoctorReborn.use")) {
			if(configuration.getBoolean("status-after-login") == true) {
				player.performCommand("reborn info");
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public static final void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		PlayersFileLoader.savePlayer(plugin, myLogger, player, regenerationNumber, regenerationCycle, isLocked, false);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public static final void onPlayerDamage(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if(player.hasPermission("TheDoctorReborn.use")) {
				if(regenerationNumber.get(player.getUniqueId()).intValue() < 12) {
					if(isLocked.get(player.getUniqueId()).booleanValue() == false) {
						if(isRegenerating.get(player.getUniqueId()).booleanValue() == false) {
							if(event.getFinalDamage() > player.getHealth()) {
								event.setCancelled(true);
								player.setHealth(20.0);
								player.setFoodLevel(20);
								if(event.getCause().equals(DamageCause.VOID)) {
									player.teleport(player.getLocation().getWorld().getSpawnLocation());
								} else {
									RegenerationAPI.playerRegenerate(plugin, language, player, regenerationNumber, regenerationCycle, isRegenerating, regenerationTaskNumber, false);
								}
							}
						} else {
							event.setCancelled(true);
							player.setHealth(20.0);
							player.setFoodLevel(20);
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
				for(Player players : plugin.getServer().getOnlinePlayers()) {
					players.sendMessage(player.getName() + " " + language.getString("regeneration-refused"));
				}
			} else {
				if(regenerationNumber.get(player.getUniqueId()).intValue() == 12) {
					for(Player players : plugin.getServer().getOnlinePlayers()) {
						players.sendMessage(player.getName() + " " + language.getString("regeneration-ended"));
					}
				} else {
					isRegenerating.put(player.getUniqueId(), false);
					if(plugin.getServer().getScheduler().isCurrentlyRunning(regenerationTaskNumber.get(player.getUniqueId()))){
						plugin.getServer().getScheduler().cancelTask(regenerationTaskNumber.get(player.getUniqueId()));
					}
					for(Player players : plugin.getServer().getOnlinePlayers()) {
						players.sendMessage(player.getName() + " " + language.getString("died-regenerating"));
					}
					myLogger.severe(player.getName() + " died for no reason!");
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public static final void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if(player.hasPermission("TheDoctorReborn.use")) {
			if(isLocked.get(player.getUniqueId()).booleanValue() == false) {
				if(isRegenerating.get(player.getUniqueId()).booleanValue() == true) {
					event.setCancelled(true);
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public static final void onPlayerConsume(PlayerItemConsumeEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItem();
		if(item.getType().equals(SymbioticNucleiItem.getMaterial()) && item.hasItemMeta() && item.getItemMeta().getDisplayName().equals(SymbioticNucleiItem.getName())) {
			List<String> lore1 = item.getItemMeta().getLore();
			List<String> lore2 = SymbioticNucleiItem.getLore(language);
			if(lore1.size() == 2 && lore1.get(1).equals(lore2.get(1))) {
				if(player.hasPermission("TheDoctorReborn.use")) {
					if(isLocked.get(player.getUniqueId()).booleanValue() == false) {
						if(regenerationNumber.get(player.getUniqueId()).intValue() == 12) {
							if(isRegenerating.get(player.getUniqueId()).booleanValue() == false) {
								RegenerationAPI.playerRegenerate(plugin, language, player, regenerationNumber, regenerationCycle, isRegenerating, regenerationTaskNumber, true);
							} else {
								event.setCancelled(true);
								player.performCommand("reborn info");
							}
						} else {
							event.setCancelled(true);
							player.performCommand("reborn info");
						}
					} else {
						event.setCancelled(true);
						player.performCommand("reborn info");
					}
				} else {
					player.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + language.getString("no-permission"));
					myLogger.severe(player.getName() + " doesn't have permission [TheDoctorReborn.use].");
				}
			}
		}
	}
}