package io.github.leothawne.TheDoctorReborn.task;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;
import io.github.leothawne.TheDoctorReborn.module.StorageModule;
import io.github.leothawne.TheDoctorReborn.type.DataType;

public final class RegenerationTask implements Runnable {
	public RegenerationTask(){}
	@Override
	public final void run() {
		for(final Player player : Bukkit.getOnlinePlayers()) if(player.hasPermission("TheDoctorReborn.use")) if(!(boolean) StorageModule.getPlayer(player, DataType.REGENERATION_LOCKED)) if(TheDoctorReborn.getInstance().getPlayersRegen().get(player.getUniqueId()).booleanValue()) {
			Bukkit.getWorld(player.getLocation().getWorld().getUID()).playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
			Bukkit.getWorld(player.getLocation().getWorld().getUID()).playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 1);
			Bukkit.getWorld(player.getLocation().getWorld().getUID()).playSound(player.getLocation(), Sound.ENTITY_GENERIC_BURN, 2, 2);
		}
	}
}