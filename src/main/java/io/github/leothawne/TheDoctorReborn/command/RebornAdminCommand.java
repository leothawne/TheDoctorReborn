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
import org.bukkit.scheduler.BukkitRunnable;

import io.github.leothawne.TheDoctorReborn.ConsoleLoader;
import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;
import io.github.leothawne.TheDoctorReborn.Version;
import io.github.leothawne.TheDoctorReborn.api.utility.HTTP;
public class RebornAdminCommand implements CommandExecutor {
	private static TheDoctorReborn plugin;
	private static ConsoleLoader myLogger;
	private static FileConfiguration language;
	public RebornAdminCommand(TheDoctorReborn plugin, ConsoleLoader myLogger, FileConfiguration language) {
		RebornAdminCommand.plugin = plugin;
		RebornAdminCommand.myLogger = myLogger;
		RebornAdminCommand.language = language;
	}
	@Override
	public final boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(sender.hasPermission("TheDoctorReborn.use")) {
			if(sender.hasPermission("TheDoctorReborn.admin")) {
				if(args.length == 0) {
					sender.sendMessage(ChatColor.AQUA + "=+=+=+= [The Doctor Reborn :: Admin] =+=+=+=");
					sender.sendMessage(ChatColor.GREEN + "/rebornadmin " + ChatColor.AQUA + "- Shows administration commands for The Doctor Reborn.");
					sender.sendMessage(ChatColor.GREEN + "/rebornadmin version " + ChatColor.AQUA + "- Checks for new updates.");
					sender.sendMessage(ChatColor.GREEN + "/rebornadmin info <timelord> " + ChatColor.AQUA + "- Shows the regeneration status of a Time Lord.");
					sender.sendMessage(ChatColor.YELLOW + "You can also use " + ChatColor.GREEN + "/rebornadmin " + ChatColor.YELLOW + "as " + ChatColor.GREEN + "/rba" + ChatColor.YELLOW + ".");
				} else if(args[0].equalsIgnoreCase("version")) {
					if(args.length < 2) {
						new BukkitRunnable() {
							@Override
							public final void run() {
								String[] LocalVersion = Version.getVersionNumber().split("\\.");
								int Local_VersionNumber1 = Integer.parseInt(LocalVersion[0]);
								int Local_VersionNumber2 = Integer.parseInt(LocalVersion[1]);
								int Local_VersionNumber3 = Integer.parseInt(LocalVersion[2]);
								String upToDate = ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "The plugin is up to date!";
								String[] Server1 = HTTP.getData(Version.getUpdateURL()).split("-");
								if(Server1[2].equals(Version.getMinecraftVersion())) {
									String[] Server2 = Server1[0].split("\\.");
									int Server2_VersionNumber1 = Integer.parseInt(Server2[0]);
									int Server2_VersionNumber2 = Integer.parseInt(Server2[1]);
									int Server2_VersionNumber3 = Integer.parseInt(Server2[2]);
									String updateMessage = ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "A newer version is available: " + ChatColor.GREEN + "" + Server1[0] + "" + ChatColor.YELLOW + " (released on " + ChatColor.GREEN + "" + Server1[1] + "" + ChatColor.YELLOW + ").";
									if(Server2_VersionNumber1 > Local_VersionNumber1) {
										sender.sendMessage(updateMessage);
									} else if(Server2_VersionNumber1 == Local_VersionNumber1 && Server2_VersionNumber2 > Local_VersionNumber2) {
										sender.sendMessage(updateMessage);
									} else if(Server2_VersionNumber1 == Local_VersionNumber1 && Server2_VersionNumber2 == Local_VersionNumber2 && Server2_VersionNumber3 > Local_VersionNumber3) {
										sender.sendMessage(updateMessage);
									} else {
										sender.sendMessage(upToDate);
									}
								} else {
									sender.sendMessage(upToDate);
								}
							}
						}.runTask(plugin);
					} else {
						sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "" + language.getString("player-tma"));
					}
				} else if(args[0].equalsIgnoreCase("info")) {
					if(args.length < 2) {
						sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + language.getString("missing-timelord"));
					} else {
						if(args.length < 3) {
							//aqui
							
						} else {
							sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + language.getString("player-tma"));
						}
					}
				} else {
					sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "Invalid command! Type " + ChatColor.GREEN + "/rebornadmin " + ChatColor.YELLOW + "to see all available commands.");
				}
			} else {
				sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + language.getString("no-permission"));
				myLogger.severe(sender.getName() + " don't have permission [TheDoctorReborn.admin].");
			}
		} else {
			sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + language.getString("no-permission"));
			myLogger.severe(sender.getName() + " don't have permission [TheDoctorReborn.use].");
		}
		return true;
	}
}
