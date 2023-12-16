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

import java.util.HashMap;
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

import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;
import io.github.leothawne.TheDoctorReborn.item.SymbioticNucleiItem;
import io.github.leothawne.TheDoctorReborn.module.ConsoleModule;
import io.github.leothawne.TheDoctorReborn.module.NBTModule;
import io.github.leothawne.TheDoctorReborn.module.RegenerationModule;
import io.github.leothawne.TheDoctorReborn.module.StorageModule;
import io.github.leothawne.TheDoctorReborn.type.DataType;

public final class PlayerListener implements Listener {
	private TheDoctorReborn plugin;
	private ConsoleModule console;
	private FileConfiguration configuration;
	private FileConfiguration language;
	private FileConfiguration regenerationData;
	private HashMap<UUID, Boolean> isRegenerating;
	private HashMap<UUID, Integer> regenerationTaskNumber;
	public PlayerListener(final TheDoctorReborn plugin, final ConsoleModule console, final FileConfiguration configuration, final FileConfiguration language, final FileConfiguration regenerationData, final HashMap<UUID, Boolean> isRegenerating, final HashMap<UUID, Integer> regenerationTaskNumber) {
		this.plugin = plugin;
		this.console = console;
		this.configuration = configuration;
		this.language= language;
		this.regenerationData = regenerationData;
		this.isRegenerating = isRegenerating;
		this.regenerationTaskNumber = regenerationTaskNumber;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onPlayerJoin(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		StorageModule.checkPlayer(this.regenerationData, player);
		this.isRegenerating.put(player.getUniqueId(), false);
		if(player.hasPermission("TheDoctorReborn.use")) if(this.configuration.getBoolean("status-after-login")) player.performCommand("reborn info");
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onPlayerQuit(final PlayerQuitEvent event) {
		final Player player = event.getPlayer();
		if(this.isRegenerating.get(player.getUniqueId()).booleanValue()) {
			this.isRegenerating.put(player.getUniqueId(), false);
			player.setHealth(0.0);
			player.setFoodLevel(0);
			if(this.plugin.getServer().getScheduler().isCurrentlyRunning(this.regenerationTaskNumber.get(player.getUniqueId()))) this.plugin.getServer().getScheduler().cancelTask(this.regenerationTaskNumber.get(player.getUniqueId()));
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onPlayerGetDamage(final EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			final Player player = (Player) event.getEntity();
			if(player.hasPermission("TheDoctorReborn.use")) if((int) StorageModule.getPlayer(this.regenerationData, player, DataType.REGENERATION_NUMBER) < 12) if(!(boolean) StorageModule.getPlayer(this.regenerationData, player, DataType.REGENERATION_LOCKED)) if(!this.isRegenerating.get(player.getUniqueId()).booleanValue()) {
				if(event.getFinalDamage() > player.getHealth()) {
					event.setCancelled(true);
					player.setHealth(20.0);
					player.setFoodLevel(20);
					if(event.getCause().equals(DamageCause.VOID)) {
						player.teleport(player.getLocation().getWorld().getSpawnLocation());
					} else RegenerationModule.beginRegeneration(this.plugin, this.language, this.regenerationData, player, this.isRegenerating, this.regenerationTaskNumber, false);
				}
			}
			if(this.isRegenerating.get(player.getUniqueId()).booleanValue()) event.setCancelled(true);
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onPlayerDeath(final PlayerDeathEvent event) {
		final Player player = event.getEntity();
		if(player.hasPermission("TheDoctorReborn.use")) if((boolean) StorageModule.getPlayer(this.regenerationData, player, DataType.REGENERATION_LOCKED)) {
			for(final Player players : this.plugin.getServer().getOnlinePlayers()) players.sendMessage(player.getName() + " " + this.language.getString("regeneration-refused"));
		} else if((int) StorageModule.getPlayer(this.regenerationData, player, DataType.REGENERATION_NUMBER) == 12) {
			for(final Player players : this.plugin.getServer().getOnlinePlayers()) players.sendMessage(player.getName() + " " + this.language.getString("regeneration-ended"));
		} else {
			this.isRegenerating.put(player.getUniqueId(), false);
			if(this.plugin.getServer().getScheduler().isCurrentlyRunning(this.regenerationTaskNumber.get(player.getUniqueId())) || this.plugin.getServer().getScheduler().isQueued(this.regenerationTaskNumber.get(player.getUniqueId()))) this.plugin.getServer().getScheduler().cancelTask(this.regenerationTaskNumber.get(player.getUniqueId()));
			for(final Player players : this.plugin.getServer().getOnlinePlayers()) players.sendMessage(player.getName() + " " + this.language.getString("died-regenerating"));
			this.console.warning("Timelord " + player.getName() + " died?");
		}
		//if(this.configuration.getBoolean("respawn-on-death")) if(SpigotAPI.isSpigot()) player.spigot().respawn();
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onPlayerMove(final PlayerMoveEvent event) {
		final Player player = event.getPlayer();
		if(player.hasPermission("TheDoctorReborn.use")) if(!(boolean) StorageModule.getPlayer(this.regenerationData, player, DataType.REGENERATION_LOCKED)) if(this.isRegenerating.get(player.getUniqueId()).booleanValue()) event.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onPlayerTeleport(final PlayerTeleportEvent event) {
		final Player player = event.getPlayer();
		if(player.hasPermission("TheDoctorReborn.use")) if(!(boolean) StorageModule.getPlayer(this.regenerationData, player, DataType.REGENERATION_LOCKED)) if(this.isRegenerating.get(player.getUniqueId()).booleanValue()) event.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onPlayerConsume(final PlayerItemConsumeEvent event) {
		final Player player = event.getPlayer();
		if(this.isRegenerating.get(player.getUniqueId()).booleanValue()) {
			event.setCancelled(true);
		} else {
			final ItemStack item = event.getItem();
			if(item != null && item.getItemMeta() != null) if(NBTModule.isTheDoctorRebornItem(new SymbioticNucleiItem(), item)) {
				if(player.hasPermission("TheDoctorReborn.use")) {
					if(!(boolean) StorageModule.getPlayer(this.regenerationData, player, DataType.REGENERATION_LOCKED) && (int) StorageModule.getPlayer(this.regenerationData, player, DataType.REGENERATION_NUMBER) == 12) {
						if(!this.isRegenerating.get(player.getUniqueId()).booleanValue()) {
							RegenerationModule.beginRegeneration(this.plugin, this.language, this.regenerationData, player, this.isRegenerating, this.regenerationTaskNumber, true);
						} else {
							event.setCancelled(true);
							player.sendMessage(ChatColor.AQUA + "[" + this.plugin.getDescription().getName() + "] " + ChatColor.YELLOW + this.language.getString("symbiotic-nuclei-error"));
						}
					} else player.sendMessage(ChatColor.AQUA + "[" + this.plugin.getDescription().getName() + "] " + ChatColor.YELLOW + this.language.getString("symbiotic-nuclei-no-effect"));
				} else player.sendMessage(ChatColor.AQUA + "[" + this.plugin.getDescription().getName() + "] " + ChatColor.YELLOW + this.language.getString("no-permission"));
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onBlockBreak(final BlockBreakEvent event) {
		final Player player = event.getPlayer();
		if(player.hasPermission("TheDoctorReborn.use")) if(!(boolean) StorageModule.getPlayer(this.regenerationData, player, DataType.REGENERATION_LOCKED)) if(this.isRegenerating.get(player.getUniqueId()).booleanValue()) event.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onBlockPlace(final BlockPlaceEvent event) {
		final Player player = event.getPlayer();
		if(player.hasPermission("TheDoctorReborn.use")) if(!(boolean) StorageModule.getPlayer(this.regenerationData, player, DataType.REGENERATION_LOCKED)) if(this.isRegenerating.get(player.getUniqueId()).booleanValue()) event.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onPlayerChat(final AsyncPlayerChatEvent event) {
		final Player player = event.getPlayer();
		if(player.hasPermission("TheDoctorReborn.use")) if(!(boolean) StorageModule.getPlayer(this.regenerationData, player, DataType.REGENERATION_LOCKED)) if(this.isRegenerating.get(player.getUniqueId()).booleanValue()) event.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onPlayerCommand(final PlayerCommandPreprocessEvent event) {
		final Player player = event.getPlayer();
		if(player.hasPermission("TheDoctorReborn.use")) if(!(boolean) StorageModule.getPlayer(this.regenerationData, player, DataType.REGENERATION_LOCKED)) if(this.isRegenerating.get(player.getUniqueId()).booleanValue()) event.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onPlayerInteract(final PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		if(player.hasPermission("TheDoctorReborn.use")) if(!(boolean) StorageModule.getPlayer(this.regenerationData, player, DataType.REGENERATION_LOCKED)) if(this.isRegenerating.get(player.getUniqueId()).booleanValue()) event.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onInventoryOpen(final InventoryOpenEvent event) {
		final Player player = (Player) event.getPlayer();
		if(player.hasPermission("TheDoctorReborn.use")) if(!(boolean) StorageModule.getPlayer(this.regenerationData, player, DataType.REGENERATION_LOCKED)) if(this.isRegenerating.get(player.getUniqueId()).booleanValue()) event.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onInventoryClick(final InventoryClickEvent event) {
		final Player player = (Player) event.getWhoClicked();
		if(player.hasPermission("TheDoctorReborn.use")) if(!(boolean) StorageModule.getPlayer(this.regenerationData, player, DataType.REGENERATION_LOCKED)) if(this.isRegenerating.get(player.getUniqueId()).booleanValue()) event.setCancelled(true);
	}
	/*@EventHandler(priority = EventPriority.HIGHEST)
	public static final void onPlayerDamageEntity(EntityDamageEvent event) {
		if(!event.getEntityType().equals(EntityType.PLAYER)) {
			Entity entity = event.getEntity();
			entity.get
		}
	}*/
}