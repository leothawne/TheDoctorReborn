package io.github.leothawne.thedoctorreborn.command.tabCompleter;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.google.common.collect.ImmutableList;

import io.github.leothawne.thedoctorreborn.api.utility.TabCompleterAPI;

public class RebornCommandTabCompleter extends TabCompleterAPI implements TabCompleter {
	@Override
	public final List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args){
		List<String> ReturnNothing = new ArrayList<>();
		if(sender.hasPermission("TheDoctorReborn.use") && cmd.getName().equalsIgnoreCase("reborn")) {
			if(args.length == 1) {
				ImmutableList<String> Reborn = ImmutableList.of("version", "info", "force", "lock", "ch");
				return partial(args[0], Reborn);
			} else {
				if(args[0].equalsIgnoreCase("lock") && args.length > 1 && args.length < 3) {
					ImmutableList<String> Reborn = ImmutableList.of("on", "off");
					return partial(args[1], Reborn);
				}
				if(args[0].equalsIgnoreCase("ch") && args.length > 1 && args.length < 3) {
					ImmutableList<String> Reborn = ImmutableList.of("on", "off");
					return partial(args[1], Reborn);
				}
			}
		}
		return ReturnNothing;
	}
}