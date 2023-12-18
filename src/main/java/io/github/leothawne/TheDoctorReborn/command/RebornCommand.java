package io.github.leothawne.TheDoctorReborn.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;
import io.github.leothawne.TheDoctorReborn.module.StorageModule;
import io.github.leothawne.TheDoctorReborn.type.DataType;

public final class RebornCommand implements CommandExecutor {
	public RebornCommand() {}
	@Override
	public final boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
		if(sender.hasPermission("TheDoctorReborn.use")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.AQUA + "=+=+=+= [" + TheDoctorReborn.getInstance().getDescription().getName() + "] =+=+=+=");
				sender.sendMessage(ChatColor.GOLD + TheDoctorReborn.getInstance().getDescription().getDescription());
				sender.sendMessage("");
				sender.sendMessage(ChatColor.GREEN + "/reborn " + ChatColor.AQUA + "- Shows the description of the plugin.");
				if(sender instanceof Player) {
					final Player player = (Player) sender;
					player.sendMessage(ChatColor.GREEN + "/reborn info " + ChatColor.AQUA + "- Shows your regeneration status.");
					player.sendMessage(ChatColor.GREEN + "/reborn lock <on/off> " + ChatColor.AQUA + "- Locks/unlocks your regeneration ability.");
				}
				if(sender.hasPermission("TheDoctorReborn.admin") || sender.isOp()) sender.sendMessage(ChatColor.GREEN + "/rebornadmin " + ChatColor.AQUA + "- Shows the list of administrator subcommands.");
			} else if(args[0].equalsIgnoreCase("info")) {
				if(args.length < 2) {
					if(sender instanceof Player) {
						final Player player = (Player) sender;
						if(!TheDoctorReborn.getInstance().getPlayersRegen().get(player.getUniqueId()).booleanValue()) {
							player.sendMessage("");
							player.sendMessage("");
							player.sendMessage(ChatColor.YELLOW + TheDoctorReborn.getInstance().getLanguage().getString("player-current-regeneration") + " " + ChatColor.GREEN + (int) StorageModule.getPlayer(player, DataType.REGENERATION_NUMBER) + ChatColor.YELLOW + "/12.");
							player.sendMessage(ChatColor.YELLOW + TheDoctorReborn.getInstance().getLanguage().getString("player-cycle") + " " + ChatColor.GREEN + (int) StorageModule.getPlayer(player, DataType.REGENERATION_CYCLE) + ChatColor.YELLOW + ".");
							String yesno = null;
							if((boolean) StorageModule.getPlayer(player, DataType.REGENERATION_LOCKED)) {
								yesno = TheDoctorReborn.getInstance().getLanguage().getString("yes-message");
							} else yesno = TheDoctorReborn.getInstance().getLanguage().getString("no-message");
							player.sendMessage(ChatColor.YELLOW + TheDoctorReborn.getInstance().getLanguage().getString("player-locked") + " " + ChatColor.GREEN + yesno + ChatColor.YELLOW + ".");
							player.sendMessage("");
							player.sendMessage("");
						} else {
							player.sendMessage("");
							player.sendMessage("");
							player.sendMessage(ChatColor.YELLOW + TheDoctorReborn.getInstance().getLanguage().getString("player-regenerating"));
							player.sendMessage("");
							player.sendMessage("");
						}
					} else {
						sender.sendMessage(ChatColor.AQUA + "[" + TheDoctorReborn.getInstance().getDescription().getName() + "] " + ChatColor.YELLOW + TheDoctorReborn.getInstance().getLanguage().getString("player-error"));
					}
				} else {
					sender.sendMessage(ChatColor.AQUA + "[" + TheDoctorReborn.getInstance().getDescription().getName() + "] " + ChatColor.YELLOW + TheDoctorReborn.getInstance().getLanguage().getString("player-tma"));
				}
			} else if(args[0].equalsIgnoreCase("lock")) {
				if(args.length < 2) {
					sender.sendMessage(ChatColor.AQUA + "[" + TheDoctorReborn.getInstance().getDescription().getName() + "] " + ChatColor.YELLOW + TheDoctorReborn.getInstance().getLanguage().getString("player-nea"));
				} else if(args.length < 3) {
					if(sender instanceof Player) {
						final Player player = (Player) sender;
						if(!TheDoctorReborn.getInstance().getPlayersRegen().get(player.getUniqueId()).booleanValue()) {
							if(args[1].equalsIgnoreCase("on")) {
								if(!(boolean) StorageModule.getPlayer(player, DataType.REGENERATION_LOCKED)) {
									StorageModule.setPlayer(player, DataType.REGENERATION_LOCKED, true);
									player.sendMessage("");
									player.sendMessage("");
									player.sendMessage(ChatColor.AQUA + "[" + TheDoctorReborn.getInstance().getDescription().getName() + "] " + ChatColor.YELLOW + TheDoctorReborn.getInstance().getLanguage().getString("locked"));
									player.sendMessage("");
									player.sendMessage("");
								} else {
									player.sendMessage("");
									player.sendMessage("");
									player.sendMessage(ChatColor.AQUA + "[" + TheDoctorReborn.getInstance().getDescription().getName() + "] " + ChatColor.YELLOW + TheDoctorReborn.getInstance().getLanguage().getString("already-locked"));
									player.sendMessage("");
									player.sendMessage("");
								}
							} else if(args[1].equalsIgnoreCase("off")) {
								if((boolean) StorageModule.getPlayer(player, DataType.REGENERATION_LOCKED)) {
									StorageModule.setPlayer(player, DataType.REGENERATION_LOCKED, false);
									player.sendMessage("");
									player.sendMessage("");
									player.sendMessage(ChatColor.AQUA + "[" + TheDoctorReborn.getInstance().getDescription().getName() + "] " + ChatColor.YELLOW + TheDoctorReborn.getInstance().getLanguage().getString("unlocked"));
									player.sendMessage("");
									player.sendMessage("");
								} else {
									player.sendMessage("");
									player.sendMessage("");
									player.sendMessage(ChatColor.AQUA + "[" + TheDoctorReborn.getInstance().getDescription().getName() + "] " + ChatColor.YELLOW + TheDoctorReborn.getInstance().getLanguage().getString("already-unlocked"));
									player.sendMessage("");
									player.sendMessage("");
								}
							}
						} else {
							player.sendMessage("");
							player.sendMessage("");
							player.sendMessage(ChatColor.YELLOW + TheDoctorReborn.getInstance().getLanguage().getString("player-regenerating"));
							player.sendMessage("");
							player.sendMessage("");
						}
					} else sender.sendMessage(ChatColor.AQUA + "[" + TheDoctorReborn.getInstance().getDescription().getName() + "] " + ChatColor.YELLOW + TheDoctorReborn.getInstance().getLanguage().getString("player-error"));
				} else sender.sendMessage(ChatColor.AQUA + "[" + TheDoctorReborn.getInstance().getDescription().getName() + "] " + ChatColor.YELLOW + TheDoctorReborn.getInstance().getLanguage().getString("player-tma"));
			} else sender.sendMessage(ChatColor.AQUA + "[" + TheDoctorReborn.getInstance().getDescription().getName() + "] " + ChatColor.YELLOW + "Invalid subcommand! Type " + ChatColor.GREEN + "/reborn " + ChatColor.YELLOW + "for help.");
		} else sender.sendMessage(ChatColor.AQUA + "[" + TheDoctorReborn.getInstance().getDescription().getName() + "] " + ChatColor.YELLOW + TheDoctorReborn.getInstance().getLanguage().getString("no-permission"));
		return true;
	}
}
