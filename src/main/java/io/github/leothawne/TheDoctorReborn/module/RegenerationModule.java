package io.github.leothawne.TheDoctorReborn.module;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;
import io.github.leothawne.TheDoctorReborn.type.DataType;

public final class RegenerationModule {
	private RegenerationModule() {}
	public static final void beginRegeneration(final Player player, final boolean symbioticNuclei) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 14, 1));
		player.sendTitle(ChatColor.BLUE + TheDoctorReborn.getInstance().getLanguage().getString("player-regenerating"), "", 20 * 1, 20 * 11, 20 * 1);
		TheDoctorReborn.getInstance().getPlayersRegen().put(player.getUniqueId(), true);
		int number = 0;
		if(symbioticNuclei) {
			number = (int) StorageModule.getPlayer( player, DataType.REGENERATION_CYCLE) + 1;
		} else number = (int) StorageModule.getPlayer( player, DataType.REGENERATION_NUMBER) + 1;
		final int newNumber = number;
		final BukkitTask task = new BukkitRunnable() {
			@Override
			public final void run() {
				if(symbioticNuclei) {
					StorageModule.setPlayer( player, DataType.REGENERATION_CYCLE, newNumber);
					StorageModule.setPlayer( player, DataType.REGENERATION_NUMBER, 0);
				} else StorageModule.setPlayer( player, DataType.REGENERATION_NUMBER, newNumber);
				TheDoctorReborn.getInstance().getPlayersRegen().put(player.getUniqueId(), false);
				player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 20 * 120, 14));
				player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 120, 14));
				player.setHealth(78.0);
				player.setFoodLevel(20);
			}
		}.runTaskLater(TheDoctorReborn.getInstance(), 20 * 13);
		TheDoctorReborn.getInstance().getRegenTasks().put(player.getUniqueId(), task.getTaskId());
	}
}