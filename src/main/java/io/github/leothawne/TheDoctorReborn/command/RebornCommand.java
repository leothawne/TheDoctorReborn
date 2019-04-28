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
import io.github.leothawne.TheDoctorReborn.PlayerDataLoader;
import io.github.leothawne.TheDoctorReborn.Version;
import io.github.leothawne.TheDoctorReborn.type.DataSectionType;
public class RebornCommand implements CommandExecutor {
	private static ConsoleLoader myLogger;
	private static FileConfiguration language;
	private static FileConfiguration regenerationData;
	private static HashMap<UUID, Boolean> isRegenerating;
	public RebornCommand(ConsoleLoader myLogger, FileConfiguration language, FileConfiguration regenerationData, HashMap<UUID, Boolean> isRegenerating) {
		RebornCommand.myLogger = myLogger;
		RebornCommand.language = language;
		RebornCommand.regenerationData = regenerationData;
		RebornCommand.isRegenerating = isRegenerating;
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
							player.sendMessage(ChatColor.YELLOW + language.getString("player-current-regeneration") + " " + ChatColor.GREEN + (int) PlayerDataLoader.getPlayer(regenerationData, player, DataSectionType.REGENERATION_NUMBER) + ChatColor.YELLOW + "/12.");
							player.sendMessage(ChatColor.YELLOW + language.getString("player-cycle") + " " + ChatColor.GREEN + (int) PlayerDataLoader.getPlayer(regenerationData, player, DataSectionType.REGENERATION_CYCLE) + ChatColor.YELLOW + ".");
							String yesno;
							if((boolean) PlayerDataLoader.getPlayer(regenerationData, player, DataSectionType.REGENERATION_LOCKED) == true) {
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
									if((boolean) PlayerDataLoader.getPlayer(regenerationData, player, DataSectionType.REGENERATION_LOCKED) == false) {
										PlayerDataLoader.setPlayer(regenerationData, player, DataSectionType.REGENERATION_LOCKED, true);
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
									if((boolean) PlayerDataLoader.getPlayer(regenerationData, player, DataSectionType.REGENERATION_LOCKED) == true) {
										PlayerDataLoader.setPlayer(regenerationData, player, DataSectionType.REGENERATION_LOCKED, false);
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
