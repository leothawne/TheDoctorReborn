package io.github.leothawne.TheDoctorReborn.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;
import io.github.leothawne.TheDoctorReborn.api.HTTPAPI;
import io.github.leothawne.TheDoctorReborn.module.DataModule;
import io.github.leothawne.TheDoctorReborn.module.StorageModule;
import io.github.leothawne.TheDoctorReborn.type.DataType;

public final class RebornAdminCommand implements CommandExecutor {
	public RebornAdminCommand() {}
	@Override
	public final boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
		if(sender.hasPermission("TheDoctorReborn.admin") || sender.isOp()) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.AQUA + "=+=+=+= [" + TheDoctorReborn.getInstance().getDescription().getName() + "] =+=+=+=");
				sender.sendMessage(ChatColor.GOLD + TheDoctorReborn.getInstance().getDescription().getDescription());
				sender.sendMessage("");
				sender.sendMessage(ChatColor.GREEN + "/rebornadmin " + ChatColor.AQUA + "- Shows the list of administrator subcommands.");
				sender.sendMessage(ChatColor.GREEN + "/rebornadmin update " + ChatColor.AQUA + "- Checks for new updates.");
				sender.sendMessage(ChatColor.GREEN + "/rebornadmin info " + ChatColor.AQUA + "- Shows the regeneration status of a timelord.");
			} else if(args[0].equalsIgnoreCase("update")) {
				if(args.length < 2) {
					final CommandSender finalSender = sender;
					new BukkitRunnable() {
						@Override
						public final void run() {
							final String[] LocalVersion = TheDoctorReborn.getInstance().getDescription().getVersion().split("\\.");
							final int Local_VersionNumber1 = Integer.parseInt(LocalVersion[0]);
							final int Local_VersionNumber2 = Integer.parseInt(LocalVersion[1]);
							final int Local_VersionNumber3 = Integer.parseInt(LocalVersion[2]);
							final String[] Server1 = HTTPAPI.getData(DataModule.getUpdateURL()).split("-");
							final String[] Server2 = Server1[0].split("\\.");
							final int Server2_VersionNumber1 = Integer.parseInt(Server2[0]);
							final int Server2_VersionNumber2 = Integer.parseInt(Server2[1]);
							final int Server2_VersionNumber3 = Integer.parseInt(Server2[2]);
							final String updateMessage = ChatColor.AQUA + "[" + TheDoctorReborn.getInstance().getDescription().getName() + "] " + ChatColor.YELLOW + "A newer version is available: " + ChatColor.GREEN + Server1[0] + ChatColor.YELLOW + " (released on " + ChatColor.GREEN + Server1[1] + ChatColor.YELLOW + ").";
							if(Server2_VersionNumber1 > Local_VersionNumber1) {
								finalSender.sendMessage(updateMessage);
							} else if(Server2_VersionNumber1 == Local_VersionNumber1 && Server2_VersionNumber2 > Local_VersionNumber2) {
								finalSender.sendMessage(updateMessage);
							} else if(Server2_VersionNumber1 == Local_VersionNumber1 && Server2_VersionNumber2 == Local_VersionNumber2 && Server2_VersionNumber3 > Local_VersionNumber3) {
								finalSender.sendMessage(updateMessage);
							} else finalSender.sendMessage(ChatColor.AQUA + "[" + TheDoctorReborn.getInstance().getDescription().getName() + "] " + ChatColor.YELLOW + "The plugin is up to date!");
						}
					}.runTaskAsynchronously(TheDoctorReborn.getInstance());
				} else sender.sendMessage(ChatColor.AQUA + "[" + TheDoctorReborn.getInstance().getDescription().getName() + "] " + ChatColor.YELLOW + "" + TheDoctorReborn.getInstance().getLanguage().getString("player-tma"));
			} else if(args[0].equalsIgnoreCase("info")) {
				if(args.length < 2) {
					sender.sendMessage(ChatColor.AQUA + "[" + TheDoctorReborn.getInstance().getDescription().getName() + "] " + ChatColor.YELLOW + TheDoctorReborn.getInstance().getLanguage().getString("player-missing"));
				} else if(args.length < 3) {
					final Player onlinePlayer = TheDoctorReborn.getInstance().getServer().getPlayer(args[1]);
					if(onlinePlayer != null) {
						sender.sendMessage("");
						sender.sendMessage("");
						sender.sendMessage(ChatColor.DARK_GREEN + onlinePlayer.getName() + ChatColor.YELLOW + " (" + ChatColor.GREEN + "Online" + ChatColor.YELLOW + ")");
						sender.sendMessage("");
						sender.sendMessage(ChatColor.YELLOW + "UUID: " + ChatColor.GREEN + onlinePlayer.getUniqueId() + ChatColor.YELLOW + ".");
						sender.sendMessage(ChatColor.YELLOW + TheDoctorReborn.getInstance().getLanguage().getString("player-health-level") + " " + ChatColor.GREEN + onlinePlayer.getHealth() + ChatColor.YELLOW + "/20.0.");
						sender.sendMessage(ChatColor.YELLOW + TheDoctorReborn.getInstance().getLanguage().getString("player-food-level") + " " + ChatColor.GREEN + onlinePlayer.getFoodLevel() + ChatColor.YELLOW + "/20.");
						sender.sendMessage(ChatColor.YELLOW + TheDoctorReborn.getInstance().getLanguage().getString("player-current-regeneration") + " " + ChatColor.GREEN + (int) StorageModule.getPlayer(onlinePlayer, DataType.REGENERATION_NUMBER) + ChatColor.YELLOW + "/12.");
						sender.sendMessage(ChatColor.YELLOW + TheDoctorReborn.getInstance().getLanguage().getString("player-cycle") + " " + ChatColor.GREEN + (int) StorageModule.getPlayer(onlinePlayer, DataType.REGENERATION_CYCLE) + ChatColor.YELLOW + ".");
						String yesno = null;
						if((boolean) StorageModule.getPlayer(onlinePlayer, DataType.REGENERATION_LOCKED)) {
							yesno = TheDoctorReborn.getInstance().getLanguage().getString("yes-message");
						} else yesno = TheDoctorReborn.getInstance().getLanguage().getString("no-message");
						sender.sendMessage(ChatColor.YELLOW + TheDoctorReborn.getInstance().getLanguage().getString("player-locked") + " " + ChatColor.GREEN + yesno + ChatColor.YELLOW + ".");
						sender.sendMessage("");
						sender.sendMessage("");
					} else sender.sendMessage(ChatColor.AQUA + "[" + TheDoctorReborn.getInstance().getDescription().getName() + "] " + ChatColor.YELLOW + TheDoctorReborn.getInstance().getLanguage().getString("player-not-found"));
				} else sender.sendMessage(ChatColor.AQUA + "[" + TheDoctorReborn.getInstance().getDescription().getName() + "] " + ChatColor.YELLOW + TheDoctorReborn.getInstance().getLanguage().getString("player-tma"));
			} else if(args[0].equals("devResetServerRecipeList")) {
				TheDoctorReborn.getInstance().getServer().resetRecipes();
			} else sender.sendMessage(ChatColor.AQUA + "[" + TheDoctorReborn.getInstance().getDescription().getName() + "] " + ChatColor.YELLOW + "Invalid subcommand! Type " + ChatColor.GREEN + "/rebornadmin " + ChatColor.YELLOW + "for help.");
		} else sender.sendMessage(ChatColor.AQUA + "[" + TheDoctorReborn.getInstance().getDescription().getName() + "] " + ChatColor.YELLOW + TheDoctorReborn.getInstance().getLanguage().getString("no-permission"));
		return true;
	}
}
