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
	public RebornCommand(ConsoleLoader myLogger, FileConfiguration language) {
		RebornCommand.myLogger = myLogger;
		RebornCommand.language = language;
	}
	@Override
	public final boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(sender.hasPermission("TheDoctorReborn.use")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.AQUA + "=+=+=+= [The Doctor Reborn] =+=+=+=");
				sender.sendMessage(ChatColor.GREEN + "/reborn " + ChatColor.AQUA + "- Shows all commands for The Doctor Reborn.");
				sender.sendMessage(ChatColor.GREEN + "/reborn version " + ChatColor.AQUA + "- Shows the plugin version.");
				sender.sendMessage(ChatColor.GREEN + "/reborn info " + ChatColor.AQUA + "- Shows your regeneration status.");
				sender.sendMessage(ChatColor.GREEN + "/reborn lock <on/off> " + ChatColor.AQUA + "- Locks/unlocks your regeneration ability.");
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
						//aqui
						
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
							//aqui
							
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
			myLogger.severe(sender.getName() + " don't have permission [TheDoctorReborn.use].");
		}
		return true;
	}
}
