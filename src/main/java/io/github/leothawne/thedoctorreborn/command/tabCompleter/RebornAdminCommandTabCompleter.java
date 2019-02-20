package io.github.leothawne.thedoctorreborn.command.tabCompleter;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.google.common.collect.ImmutableList;

import io.github.leothawne.thedoctorreborn.api.utility.TabCompleterAPI;

public class RebornAdminCommandTabCompleter extends TabCompleterAPI implements TabCompleter {
	@Override
	public final List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args){
		List<String> ReturnNothing = new ArrayList<>();
		if(sender.hasPermission("TheDoctorReborn.use") && sender.hasPermission("TheDoctorReborn.admin") && cmd.getName().equalsIgnoreCase("rebornadmin")) {
			if(args.length == 1) {
				ImmutableList<String> Reborn = ImmutableList.of("version", "info", "force", "toggle");
				return partial(args[0], Reborn);
			} else {
				if(args[0].equalsIgnoreCase("info") && args.length > 1 && args.length < 3) {
					return null;
				}
				if(args[0].equalsIgnoreCase("force") && args.length > 1 && args.length < 3) {
					return null;
				}
				if(args[0].equalsIgnoreCase("toggle") && args.length > 1 && args.length < 3) {
					return null;
				}
				if(args[0].equalsIgnoreCase("toggle") && args.length > 2 && args.length < 4) {
					ImmutableList<String> Reborn = ImmutableList.of("on", "off");
					return partial(args[1], Reborn);
				}
			}
		}
		return ReturnNothing;
	}
}