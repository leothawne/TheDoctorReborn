package io.github.leothawne.thedoctorreborn.command.tabCompleter;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class RebornCommandTabCompleter implements TabCompleter {
	@Override
	public final List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args){
		List<String> ReturnNothing = new ArrayList<>();
		if(sender.hasPermission("TheDoctorReborn.use") && cmd.getName().equalsIgnoreCase("reborn")) {
			if(args.length == 1) {
				List<String> Reborn = new ArrayList<>();
				Reborn.add("version");
				Reborn.add("info");
				if(sender.hasPermission("TheDoctorReborn.regenerate")) {
					Reborn.add("force");
				}
				Reborn.add("lock");
				Reborn.add("ch");
				return Reborn;
			} else {
				if(args[0].equalsIgnoreCase("lock") && args.length > 1 && args.length < 3) {
					List<String> RebornLock = new ArrayList<>();
					RebornLock.add("on");
					RebornLock.add("off");
					return RebornLock;
				}
				if(args[0].equalsIgnoreCase("ch") && args.length > 1 && args.length < 3) {
					List<String> RebornCH = new ArrayList<>();
					RebornCH.add("<amount>");
					return RebornCH;
				}
			}
		}
		return ReturnNothing;
	}
}