package io.github.leothawne.TheDoctorReborn.command.tabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import io.github.leothawne.TheDoctorReborn.api.TabCompleterAPI;

public final class RebornCommandTabCompleter implements TabCompleter {
	@Override
	public final List<String> onTabComplete(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args){
		if(sender.hasPermission("TheDoctorReborn.use")) if(args.length == 1) {
			return TabCompleterAPI.partial(args[0], new LinkedList<String>(Arrays.asList("info", "lock")));
		} else if(args.length == 2 && args[0].equalsIgnoreCase("lock")) return TabCompleterAPI.partial(args[1], new LinkedList<String>(Arrays.asList("on", "off")));
		return new ArrayList<>();
	}
}