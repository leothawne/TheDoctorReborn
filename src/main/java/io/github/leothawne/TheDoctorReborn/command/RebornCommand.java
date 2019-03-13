/*
 * Copyright (C) 2019 Murilo Amaral Nappi (murilonappi@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.leothawne.TheDoctorReborn.command;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import io.github.leothawne.TheDoctorReborn.ConsoleLoader;
import io.github.leothawne.TheDoctorReborn.Version;
public class RebornCommand implements CommandExecutor {
	private static ConsoleLoader myLogger;
	private static FileConfiguration language;
	private static HashMap<UUID, Integer> regenerationNumber;
	private static HashMap<UUID, Integer> regenerationCycle;
	private static HashMap<UUID, Boolean> isRegenerating;
	private static HashMap<UUID, Boolean> isLocked;
	public RebornCommand(ConsoleLoader myLogger, FileConfiguration language, HashMap<UUID, Integer> regenerationNumber, HashMap<UUID, Integer> regenerationCycle, HashMap<UUID, Boolean> isRegenerating, HashMap<UUID, Boolean> isLocked) {
		RebornCommand.myLogger = myLogger;
		RebornCommand.language = language;
		RebornCommand.regenerationNumber = regenerationNumber;
		RebornCommand.regenerationCycle = regenerationCycle;
		RebornCommand.isRegenerating = isRegenerating;
		RebornCommand.isLocked = isLocked;
	}
	@Override
	public final boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(sender.hasPermission("TheDoctorReborn.use")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.AQUA + "=+=+=+= [The Doctor Reborn] =+=+=+=");
				sender.sendMessage(ChatColor.GREEN + "/reborn " + ChatColor.AQUA + "- Shows all commands for The Doctor Reborn.");
				sender.sendMessage(ChatColor.GREEN + "/reborn version " + ChatColor.AQUA + "- Shows the plugin version.");
				if(sender instanceof Player) {
					sender.sendMessage(ChatColor.GREEN + "/reborn info " + ChatColor.AQUA + "- Shows your regeneration status.");
					sender.sendMessage(ChatColor.GREEN + "/reborn lock <on/off> " + ChatColor.AQUA + "- Locks/unlocks your regeneration ability.");
				}
				sender.sendMessage(ChatColor.GREEN + "/rebornadmin " + ChatColor.AQUA + "- Shows administration commands for The Doctor Reborn.");
				sender.sendMessage(ChatColor.YELLOW + "You can also use "+ ChatColor.GREEN + "/reborn "+ ChatColor.YELLOW + "as "+ ChatColor.GREEN + "/rb"+ ChatColor.YELLOW + ".");
			} else if(args[0].equalsIgnoreCase("version")) {
				if(args.length < 2) {
					Version.version(sender);
				} else {
					sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + language.getString("player-tma"));
				}
			} else if(args[0].equalsIgnoreCase("info")) {
				if(args.length < 2) {
					if(sender instanceof Player) {
						Player player = (Player) sender;
						if(isRegenerating.get(player.getUniqueId()).booleanValue() == false) {
							player.sendMessage("");
							player.sendMessage("");
							player.sendMessage(ChatColor.YELLOW + language.getString("player-current-regeneration") + " " + ChatColor.GREEN + regenerationNumber.get(player.getUniqueId()).intValue() + ChatColor.YELLOW + "/12.");
							player.sendMessage(ChatColor.YELLOW + language.getString("player-cycle") + " " + ChatColor.GREEN + regenerationCycle.get(player.getUniqueId()).intValue() + ChatColor.YELLOW + ".");
							String yesno;
							if(isLocked.get(player.getUniqueId()).booleanValue() == true) {
								yesno = language.getString("yes-message");
							} else {
								yesno = language.getString("no-message");
							}
							player.sendMessage(ChatColor.YELLOW + language.getString("player-locked") + " " + ChatColor.GREEN + yesno + ChatColor.YELLOW + ".");
							player.sendMessage("");
							player.sendMessage("");
						} else {
							player.sendMessage("");
							player.sendMessage("");
							player.sendMessage(ChatColor.YELLOW + language.getString("player-regenerating"));
							player.sendMessage("");
							player.sendMessage("");
						}
					} else {
						sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + language.getString("player-error"));
					}
				} else {
					sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + language.getString("player-tma"));
				}
			} else if(args[0].equalsIgnoreCase("lock")) {
				if(args.length < 2) {
					sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + language.getString("player-nea"));
				} else {
					if(args.length < 3) {
						if(sender instanceof Player) {
							Player player = (Player) sender;
							if(isRegenerating.get(player.getUniqueId()).booleanValue() == false) {
								if(args[1].equalsIgnoreCase("on")) {
									if(isLocked.get(player.getUniqueId()).booleanValue() == false) {
										isLocked.put(player.getUniqueId(), true);
										player.sendMessage("");
										player.sendMessage("");
										player.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + language.getString("locked"));
										player.sendMessage("");
										player.sendMessage("");
									} else {
										player.sendMessage("");
										player.sendMessage("");
										player.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + language.getString("already-locked"));
										player.sendMessage("");
										player.sendMessage("");
									}
								}
								if(args[1].equalsIgnoreCase("off")) {
									if(isLocked.get(player.getUniqueId()).booleanValue() == true) {
										isLocked.put(player.getUniqueId(), false);
										player.sendMessage("");
										player.sendMessage("");
										player.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + language.getString("unlocked"));
										player.sendMessage("");
										player.sendMessage("");
									} else {
										player.sendMessage("");
										player.sendMessage("");
										player.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + language.getString("already-unlocked"));
										player.sendMessage("");
										player.sendMessage("");
									}
								}
							} else {
								player.sendMessage("");
								player.sendMessage("");
								player.sendMessage(ChatColor.YELLOW + language.getString("player-regenerating"));
								player.sendMessage("");
								player.sendMessage("");
							}
						} else {
							sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + language.getString("player-error"));
						}
					} else {
						sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + language.getString("player-tma"));
					}
				}
			} else {
				sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "Invalid command! Type " + ChatColor.GREEN + "/reborn " + ChatColor.YELLOW + "to see all available commands.");
			}
		} else {
			sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + language.getString("no-permission"));
			myLogger.severe(sender.getName() + " does not have permission [TheDoctorReborn.use]: '/reborn' command.");
		}
		return true;
	}
}
