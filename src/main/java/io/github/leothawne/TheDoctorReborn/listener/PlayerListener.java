package io.github.leothawne.TheDoctorReborn.listener;

import org.bukkit.ChatColor;
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
import io.github.leothawne.TheDoctorReborn.module.NBTModule;
import io.github.leothawne.TheDoctorReborn.module.RegenerationModule;
import io.github.leothawne.TheDoctorReborn.module.StorageModule;
import io.github.leothawne.TheDoctorReborn.type.DataType;

public final class PlayerListener implements Listener {
	public PlayerListener() {}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onPlayerJoin(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		StorageModule.checkPlayer(player);
		TheDoctorReborn.getInstance().getPlayersRegen().put(player.getUniqueId(), false);
		if(player.hasPermission("TheDoctorReborn.use")) if(TheDoctorReborn.getInstance().getConfiguration().getBoolean("status-after-login")) player.performCommand("reborn info");
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onPlayerQuit(final PlayerQuitEvent event) {
		final Player player = event.getPlayer();
		if(TheDoctorReborn.getInstance().getPlayersRegen().get(player.getUniqueId()).booleanValue()) {
			TheDoctorReborn.getInstance().getPlayersRegen().put(player.getUniqueId(), false);
			player.setHealth(0.0);
			player.setFoodLevel(0);
			if(TheDoctorReborn.getInstance().getServer().getScheduler().isCurrentlyRunning(TheDoctorReborn.getInstance().getRegenTasks().get(player.getUniqueId()))) TheDoctorReborn.getInstance().getServer().getScheduler().cancelTask(TheDoctorReborn.getInstance().getRegenTasks().get(player.getUniqueId()));
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onPlayerGetDamage(final EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			final Player player = (Player) event.getEntity();
			if(player.hasPermission("TheDoctorReborn.use")) if((int) StorageModule.getPlayer(player, DataType.REGENERATION_NUMBER) < 12) if(!(boolean) StorageModule.getPlayer(player, DataType.REGENERATION_LOCKED)) if(!TheDoctorReborn.getInstance().getPlayersRegen().get(player.getUniqueId()).booleanValue()) {
				if(event.getFinalDamage() > player.getHealth()) {
					event.setCancelled(true);
					player.setHealth(20.0);
					player.setFoodLevel(20);
					if(event.getCause().equals(DamageCause.VOID)) {
						player.teleport(player.getLocation().getWorld().getSpawnLocation());
					} else RegenerationModule.beginRegeneration( player, false);
				}
			}
			if(TheDoctorReborn.getInstance().getPlayersRegen().get(player.getUniqueId()).booleanValue()) event.setCancelled(true);
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onPlayerDeath(final PlayerDeathEvent event) {
		final Player player = event.getEntity();
		if(player.hasPermission("TheDoctorReborn.use")) if((boolean) StorageModule.getPlayer(player, DataType.REGENERATION_LOCKED)) {
			for(final Player players : TheDoctorReborn.getInstance().getServer().getOnlinePlayers()) players.sendMessage(player.getName() + " " + TheDoctorReborn.getInstance().getLanguage().getString("regeneration-refused"));
		} else if((int) StorageModule.getPlayer(player, DataType.REGENERATION_NUMBER) == 12) {
			for(final Player players : TheDoctorReborn.getInstance().getServer().getOnlinePlayers()) players.sendMessage(player.getName() + " " + TheDoctorReborn.getInstance().getLanguage().getString("regeneration-ended"));
		} else {
			TheDoctorReborn.getInstance().getPlayersRegen().put(player.getUniqueId(), false);
			if(TheDoctorReborn.getInstance().getServer().getScheduler().isCurrentlyRunning(TheDoctorReborn.getInstance().getRegenTasks().get(player.getUniqueId())) || TheDoctorReborn.getInstance().getServer().getScheduler().isQueued(TheDoctorReborn.getInstance().getRegenTasks().get(player.getUniqueId()))) TheDoctorReborn.getInstance().getServer().getScheduler().cancelTask(TheDoctorReborn.getInstance().getRegenTasks().get(player.getUniqueId()));
			for(final Player players : TheDoctorReborn.getInstance().getServer().getOnlinePlayers()) players.sendMessage(player.getName() + " " + TheDoctorReborn.getInstance().getLanguage().getString("died-regenerating"));
			TheDoctorReborn.getInstance().getConsole().warning("Timelord " + player.getName() + " died?");
		}
		//if(TheDoctorReborn.getInstance().getConfiguration().getBoolean("respawn-on-death")) if(SpigotAPI.isSpigot()) player.spigot().respawn();
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onPlayerMove(final PlayerMoveEvent event) {
		final Player player = event.getPlayer();
		if(player.hasPermission("TheDoctorReborn.use")) if(!(boolean) StorageModule.getPlayer(player, DataType.REGENERATION_LOCKED)) if(TheDoctorReborn.getInstance().getPlayersRegen().get(player.getUniqueId()).booleanValue()) event.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onPlayerTeleport(final PlayerTeleportEvent event) {
		final Player player = event.getPlayer();
		if(player.hasPermission("TheDoctorReborn.use")) if(!(boolean) StorageModule.getPlayer(player, DataType.REGENERATION_LOCKED)) if(TheDoctorReborn.getInstance().getPlayersRegen().get(player.getUniqueId()).booleanValue()) event.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onPlayerConsume(final PlayerItemConsumeEvent event) {
		final Player player = event.getPlayer();
		if(TheDoctorReborn.getInstance().getPlayersRegen().get(player.getUniqueId()).booleanValue()) {
			event.setCancelled(true);
		} else {
			final ItemStack item = event.getItem();
			if(item != null && item.getItemMeta() != null) if(NBTModule.isTheDoctorRebornItem(new SymbioticNucleiItem(), item)) {
				if(player.hasPermission("TheDoctorReborn.use")) {
					if(!(boolean) StorageModule.getPlayer(player, DataType.REGENERATION_LOCKED) && (int) StorageModule.getPlayer(player, DataType.REGENERATION_NUMBER) == 12) {
						if(!TheDoctorReborn.getInstance().getPlayersRegen().get(player.getUniqueId()).booleanValue()) {
							RegenerationModule.beginRegeneration(player, true);
						} else {
							event.setCancelled(true);
							player.sendMessage(ChatColor.AQUA + "[" + TheDoctorReborn.getInstance().getDescription().getName() + "] " + ChatColor.YELLOW + TheDoctorReborn.getInstance().getLanguage().getString("symbiotic-nuclei-error"));
						}
					} else player.sendMessage(ChatColor.AQUA + "[" + TheDoctorReborn.getInstance().getDescription().getName() + "] " + ChatColor.YELLOW + TheDoctorReborn.getInstance().getLanguage().getString("symbiotic-nuclei-no-effect"));
				} else player.sendMessage(ChatColor.AQUA + "[" + TheDoctorReborn.getInstance().getDescription().getName() + "] " + ChatColor.YELLOW + TheDoctorReborn.getInstance().getLanguage().getString("no-permission"));
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onBlockBreak(final BlockBreakEvent event) {
		final Player player = event.getPlayer();
		if(player.hasPermission("TheDoctorReborn.use")) if(!(boolean) StorageModule.getPlayer(player, DataType.REGENERATION_LOCKED)) if(TheDoctorReborn.getInstance().getPlayersRegen().get(player.getUniqueId()).booleanValue()) event.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onBlockPlace(final BlockPlaceEvent event) {
		final Player player = event.getPlayer();
		if(player.hasPermission("TheDoctorReborn.use")) if(!(boolean) StorageModule.getPlayer(player, DataType.REGENERATION_LOCKED)) if(TheDoctorReborn.getInstance().getPlayersRegen().get(player.getUniqueId()).booleanValue()) event.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onPlayerChat(final AsyncPlayerChatEvent event) {
		final Player player = event.getPlayer();
		if(player.hasPermission("TheDoctorReborn.use")) if(!(boolean) StorageModule.getPlayer(player, DataType.REGENERATION_LOCKED)) if(TheDoctorReborn.getInstance().getPlayersRegen().get(player.getUniqueId()).booleanValue()) event.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onPlayerCommand(final PlayerCommandPreprocessEvent event) {
		final Player player = event.getPlayer();
		if(player.hasPermission("TheDoctorReborn.use")) if(!(boolean) StorageModule.getPlayer(player, DataType.REGENERATION_LOCKED)) if(TheDoctorReborn.getInstance().getPlayersRegen().get(player.getUniqueId()).booleanValue()) event.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onPlayerInteract(final PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		if(player.hasPermission("TheDoctorReborn.use")) if(!(boolean) StorageModule.getPlayer(player, DataType.REGENERATION_LOCKED)) if(TheDoctorReborn.getInstance().getPlayersRegen().get(player.getUniqueId()).booleanValue()) event.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onInventoryOpen(final InventoryOpenEvent event) {
		final Player player = (Player) event.getPlayer();
		if(player.hasPermission("TheDoctorReborn.use")) if(!(boolean) StorageModule.getPlayer(player, DataType.REGENERATION_LOCKED)) if(TheDoctorReborn.getInstance().getPlayersRegen().get(player.getUniqueId()).booleanValue()) event.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onInventoryClick(final InventoryClickEvent event) {
		final Player player = (Player) event.getWhoClicked();
		if(player.hasPermission("TheDoctorReborn.use")) if(!(boolean) StorageModule.getPlayer(player, DataType.REGENERATION_LOCKED)) if(TheDoctorReborn.getInstance().getPlayersRegen().get(player.getUniqueId()).booleanValue()) event.setCancelled(true);
	}
	/*@EventHandler(priority = EventPriority.HIGHEST)
	public static final void onPlayerDamageEntity(EntityDamageEvent event) {
		if(!event.getEntityType().equals(EntityType.PLAYER)) {
			Entity entity = event.getEntity();
			entity.get
		}
	}*/
}