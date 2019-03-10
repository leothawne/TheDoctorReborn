package io.github.leothawne.TheDoctorReborn.task;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Effect;
import org.bukkit.entity.Player;

import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;

public class RegenerationTask implements Runnable {
	private static TheDoctorReborn plugin;
	private static HashMap<UUID, Integer> regenerationNumber;
	private static HashMap<UUID, Boolean> isRegenerating;
	private static HashMap<UUID, Boolean> isLocked;
	public RegenerationTask(TheDoctorReborn plugin, HashMap<UUID, Integer> regenerationNumber, HashMap<UUID, Boolean> isRegenerating, HashMap<UUID, Boolean> isLocked) {
		RegenerationTask.plugin = plugin;
		RegenerationTask.regenerationNumber = regenerationNumber;
		RegenerationTask.isRegenerating = isRegenerating;
		RegenerationTask.isLocked = isLocked;
	}
	@Override
	public final void run() {
		for(Player player : plugin.getServer().getOnlinePlayers()) {
			if(player.hasPermission("TheDoctorReborn.use")) {
				if(regenerationNumber.get(player.getUniqueId()).intValue() < 12) {
					if(isLocked.get(player.getUniqueId()).booleanValue() == false) {
						if(isRegenerating.get(player.getUniqueId()).booleanValue() == true) {
							plugin.getServer().getWorld(player.getLocation().getWorld().getUID()).playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 0, 1);
						}
					}
				}
			}
		}
	}
}