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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import io.github.leothawne.TheDoctorReborn.ConsoleLoader;
import io.github.leothawne.TheDoctorReborn.PlayerDataLoader;
import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;
import io.github.leothawne.TheDoctorReborn.api.utility.RegenerationAPI;
import io.github.leothawne.TheDoctorReborn.api.utility.SpigotAPI;
import io.github.leothawne.TheDoctorReborn.item.SymbioticNucleiItem;
import io.github.leothawne.TheDoctorReborn.type.DataSectionType;
public class PlayerEvent implements Listener {
	private static TheDoctorReborn plugin;
	private static ConsoleLoader myLogger;
	private static FileConfiguration configuration;
	private static FileConfiguration language;
	private static FileConfiguration regenerationData;
	private static HashMap<UUID, Boolean> isRegenerating;
	private static HashMap<UUID, Integer> regenerationTaskNumber;
	public PlayerEvent(TheDoctorReborn plugin, ConsoleLoader myLogger, FileConfiguration configuration, FileConfiguration language, FileConfiguration regenerationData, HashMap<UUID, Boolean> isRegenerating, HashMap<UUID, Integer> regenerationTaskNumber) {
		PlayerEvent.plugin = plugin;
		PlayerEvent.myLogger = myLogger;
		PlayerEvent.configuration = configuration;
		PlayerEvent.language= language;
		PlayerEvent.regenerationData = regenerationData;
		PlayerEvent.isRegenerating = isRegenerating;
		PlayerEvent.regenerationTaskNumber = regenerationTaskNumber;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public static final void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		PlayerDataLoader.checkPlayer(regenerationData, player);
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
		if(isRegenerating.get(player.getUniqueId()).booleanValue() == true) {
			isRegenerating.put(player.getUniqueId(), false);
			player.setHealth(0.0);
			player.setFoodLevel(0);
			if(plugin.getServer().getScheduler().isCurrentlyRunning(regenerationTaskNumber.get(player.getUniqueId()))){
				plugin.getServer().getScheduler().cancelTask(regenerationTaskNumber.get(player.getUniqueId()));
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public static final void onPlayerGetDamage(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if(player.hasPermission("TheDoctorReborn.use")) {
				if((int) PlayerDataLoader.getPlayer(regenerationData, player, DataSectionType.REGENERATION_NUMBER) < 12) {
					if((boolean) PlayerDataLoader.getPlayer(regenerationData, player, DataSectionType.REGENERATION_LOCKED) == false) {
						if(isRegenerating.get(player.getUniqueId()).booleanValue() == false) {
							if(event.getFinalDamage() > player.getHealth() && !event.getCause().equals(DamageCause.SUICIDE)) {
								event.setCancelled(true);
								player.setHealth(20.0);
								player.setFoodLevel(20);
								if(event.getCause().equals(DamageCause.VOID)) {
									player.teleport(player.getLocation().getWorld().getSpawnLocation());
								} else {
									RegenerationAPI.playerRegenerate(plugin, language, regenerationData, player, isRegenerating, regenerationTaskNumber, false);
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
	public static final void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if(player.hasPermission("TheDoctorReborn.use")) {
			if((boolean) PlayerDataLoader.getPlayer(regenerationData, player, DataSectionType.REGENERATION_LOCKED) == true) {
				for(Player players : plugin.getServer().getOnlinePlayers()) {
					players.sendMessage(player.getName() + " " + language.getString("regeneration-refused"));
				}
			} else {
				if((int) PlayerDataLoader.getPlayer(regenerationData, player, DataSectionType.REGENERATION_NUMBER) == 12) {
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
			if(SpigotAPI.isSpigot() == true) {
				player.spigot().respawn();
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public static final void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if(player.hasPermission("TheDoctorReborn.use")) {
			if((boolean) PlayerDataLoader.getPlayer(regenerationData, player, DataSectionType.REGENERATION_LOCKED) == false) {
				if(isRegenerating.get(player.getUniqueId()).booleanValue() == true) {
					event.setCancelled(true);
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public static final void onPlayerTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		if(player.hasPermission("TheDoctorReborn.use")) {
			if((boolean) PlayerDataLoader.getPlayer(regenerationData, player, DataSectionType.REGENERATION_LOCKED) == false) {
				if(isRegenerating.get(player.getUniqueId()).booleanValue() == true) {
					event.setCancelled(true);
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public static final void onPlayerConsume(PlayerItemConsumeEvent event) {
		Player player = event.getPlayer();
		if(isRegenerating.get(player.getUniqueId()).booleanValue() == true) {
			event.setCancelled(true);
		} else {
			ItemStack item = event.getItem();
			if(item.getType().equals(SymbioticNucleiItem.getMaterial()) && item.hasItemMeta() && item.getItemMeta().getDisplayName().equals(SymbioticNucleiItem.getName())) {
				List<String> lore1 = item.getItemMeta().getLore();
				List<String> lore2 = SymbioticNucleiItem.getLore(language);
				if(lore1.size() == 2 && lore1.get(1).equals(lore2.get(1))) {
					if(player.hasPermission("TheDoctorReborn.use")) {
						if((boolean) PlayerDataLoader.getPlayer(regenerationData, player, DataSectionType.REGENERATION_LOCKED) == false) {
							if((int) PlayerDataLoader.getPlayer(regenerationData, player, DataSectionType.REGENERATION_NUMBER) == 12) {
								if(isRegenerating.get(player.getUniqueId()).booleanValue() == false) {
									RegenerationAPI.playerRegenerate(plugin, language, regenerationData, player, isRegenerating, regenerationTaskNumber, true);
								} else {
									event.setCancelled(true);
									player.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + language.getString("symbiotic-nuclei-error"));
								}
							} else {
								event.setCancelled(true);
								player.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + language.getString("symbiotic-nuclei-error"));
							}
						} else {
							event.setCancelled(true);
							player.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + language.getString("symbiotic-nuclei-error"));
						}
					} else {
						player.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + language.getString("no-permission"));
						myLogger.severe(player.getName() + " does not have permission [TheDoctorReborn.use]: Symbiotic Nuclei.");
					}
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public static final void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if(player.hasPermission("TheDoctorReborn.use")) {
			if((boolean) PlayerDataLoader.getPlayer(regenerationData, player, DataSectionType.REGENERATION_LOCKED) == false) {
				if(isRegenerating.get(player.getUniqueId()).booleanValue() == true) {
					event.setCancelled(true);
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public static final void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if(player.hasPermission("TheDoctorReborn.use")) {
			if((boolean) PlayerDataLoader.getPlayer(regenerationData, player, DataSectionType.REGENERATION_LOCKED) == false) {
				if(isRegenerating.get(player.getUniqueId()).booleanValue() == true) {
					event.setCancelled(true);
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public static final void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		if(player.hasPermission("TheDoctorReborn.use")) {
			if((boolean) PlayerDataLoader.getPlayer(regenerationData, player, DataSectionType.REGENERATION_LOCKED) == false) {
				if(isRegenerating.get(player.getUniqueId()).booleanValue() == true) {
					event.setCancelled(true);
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public static final void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		if(player.hasPermission("TheDoctorReborn.use")) {
			if((boolean) PlayerDataLoader.getPlayer(regenerationData, player, DataSectionType.REGENERATION_LOCKED) == false) {
				if(isRegenerating.get(player.getUniqueId()).booleanValue() == true) {
					event.setCancelled(true);
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public static final void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(player.hasPermission("TheDoctorReborn.use")) {
			if((boolean) PlayerDataLoader.getPlayer(regenerationData, player, DataSectionType.REGENERATION_LOCKED) == false) {
				if(isRegenerating.get(player.getUniqueId()).booleanValue() == true) {
					event.setCancelled(true);
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public static final void onInventoryOpen(InventoryOpenEvent event) {
		Player player = (Player) event.getPlayer();
		if(player.hasPermission("TheDoctorReborn.use")) {
			if((boolean) PlayerDataLoader.getPlayer(regenerationData, player, DataSectionType.REGENERATION_LOCKED) == false) {
				if(isRegenerating.get(player.getUniqueId()).booleanValue() == true) {
					event.setCancelled(true);
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public static final void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if(player.hasPermission("TheDoctorReborn.use")) {
			if((boolean) PlayerDataLoader.getPlayer(regenerationData, player, DataSectionType.REGENERATION_LOCKED) == false) {
				if(isRegenerating.get(player.getUniqueId()).booleanValue() == true) {
					event.setCancelled(true);
				}
			}
		}
	}
	/*@EventHandler(priority = EventPriority.HIGHEST)
	public static final void onPlayerDamageEntity(EntityDamageEvent event) {
		if(!event.getEntityType().equals(EntityType.PLAYER)) {
			Entity entity = event.getEntity();
			entity.get
		}
	}*/
}