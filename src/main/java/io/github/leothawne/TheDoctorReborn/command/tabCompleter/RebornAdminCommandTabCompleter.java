package io.github.leothawne.TheDoctorReborn.command.tabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import io.github.leothawne.TheDoctorReborn.api.TabCompleterAPI;

public final class RebornAdminCommandTabCompleter implements TabCompleter {
	public RebornAdminCommandTabCompleter() {}
	@Override
	public final List<String> onTabComplete(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args){
		if(sender.hasPermission("TheDoctorReborn.admin") || sender.isOp()) if(args.length == 1) {
			return TabCompleterAPI.partial(args[0], new LinkedList<String>(Arrays.asList("update", "info")));
		} else if(args.length == 2 && args[0].equalsIgnoreCase("info")) {
			final LinkedList<String> players = new LinkedList<String>();
			for(final Player player : Bukkit.getOnlinePlayers()) players.add(player.getName());
			return TabCompleterAPI.partial(args[1], players);
		}
		return new ArrayList<>();
	}
}