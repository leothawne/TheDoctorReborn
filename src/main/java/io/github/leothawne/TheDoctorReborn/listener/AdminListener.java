package io.github.leothawne.TheDoctorReborn.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;

public final class AdminListener implements Listener {
	public AdminListener() {}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onPlayerJoin(final PlayerJoinEvent event) {
		final Player player = (Player) event.getPlayer();
		if(player.hasPermission("TheDoctorReborn.admin") || player.isOp()) if(TheDoctorReborn.getInstance().getConfiguration().getBoolean("update.check")) player.performCommand("rebornadmin update");
	}
}