package io.github.leothawne.thedoctorreborn.command.tabCompleter;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class RebornAdminCommandTabCompleter implements TabCompleter {
	@Override
	public final List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args){
		List<String> ReturnNothing = new ArrayList<>();
		if(sender.hasPermission("TheDoctorReborn.use") && sender.hasPermission("TheDoctorReborn.admin") && cmd.getName().equalsIgnoreCase("rebornadmin")) {
			if(args.length == 1) {
				List<String> RebornAdmin = new ArrayList<>();
				RebornAdmin.add("version");
				RebornAdmin.add("info");
				RebornAdmin.add("force");
				RebornAdmin.add("toggle");
				RebornAdmin.add("reload");
				return RebornAdmin;
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
					List<String> RebornAdminToggle = new ArrayList<>();
					RebornAdminToggle.add("on");
					RebornAdminToggle.add("off");
					return RebornAdminToggle;
				}
			}
		}
		return ReturnNothing;
	}
}