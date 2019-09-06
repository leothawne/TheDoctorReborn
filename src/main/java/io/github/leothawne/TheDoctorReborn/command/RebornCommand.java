/*
 * Copyright (C) 2019 Murilo Amaral Nappi
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

import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;
import io.github.leothawne.TheDoctorReborn.module.StorageModule;
import io.github.leothawne.TheDoctorReborn.type.DataType;

public final class RebornCommand implements CommandExecutor {
	private TheDoctorReborn plugin;
	private FileConfiguration language;
	private FileConfiguration regenerationData;
	private HashMap<UUID, Boolean> isRegenerating;
	public RebornCommand(final TheDoctorReborn plugin, final FileConfiguration language, final FileConfiguration regenerationData, final HashMap<UUID, Boolean> isRegenerating) {
		this.plugin = plugin;
		this.language = language;
		this.regenerationData = regenerationData;
		this.isRegenerating = isRegenerating;
	}
	@Override
	public final boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
		if(sender.hasPermission("TheDoctorReborn.use")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.AQUA + "=+=+=+= [" + this.plugin.getDescription().getName() + "] =+=+=+=");
				sender.sendMessage(ChatColor.GOLD + this.plugin.getDescription().getDescription());
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
						if(!this.isRegenerating.get(player.getUniqueId()).booleanValue()) {
							player.sendMessage("");
							player.sendMessage("");
							player.sendMessage(ChatColor.YELLOW + this.language.getString("player-current-regeneration") + " " + ChatColor.GREEN + (int) StorageModule.getPlayer(this.regenerationData, player, DataType.REGENERATION_NUMBER) + ChatColor.YELLOW + "/12.");
							player.sendMessage(ChatColor.YELLOW + this.language.getString("player-cycle") + " " + ChatColor.GREEN + (int) StorageModule.getPlayer(this.regenerationData, player, DataType.REGENERATION_CYCLE) + ChatColor.YELLOW + ".");
							String yesno = null;
							if((boolean) StorageModule.getPlayer(this.regenerationData, player, DataType.REGENERATION_LOCKED)) {
								yesno = language.getString("yes-message");
							} else yesno = language.getString("no-message");
							player.sendMessage(ChatColor.YELLOW + this.language.getString("player-locked") + " " + ChatColor.GREEN + yesno + ChatColor.YELLOW + ".");
							player.sendMessage("");
							player.sendMessage("");
						} else {
							player.sendMessage("");
							player.sendMessage("");
							player.sendMessage(ChatColor.YELLOW + this.language.getString("player-regenerating"));
							player.sendMessage("");
							player.sendMessage("");
						}
					} else {
						sender.sendMessage(ChatColor.AQUA + "[" + this.plugin.getDescription().getName() + "] " + ChatColor.YELLOW + this.language.getString("player-error"));
					}
				} else {
					sender.sendMessage(ChatColor.AQUA + "[" + this.plugin.getDescription().getName() + "] " + ChatColor.YELLOW + this.language.getString("player-tma"));
				}
			} else if(args[0].equalsIgnoreCase("lock")) {
				if(args.length < 2) {
					sender.sendMessage(ChatColor.AQUA + "[" + this.plugin.getDescription().getName() + "] " + ChatColor.YELLOW + this.language.getString("player-nea"));
				} else if(args.length < 3) {
					if(sender instanceof Player) {
						final Player player = (Player) sender;
						if(!this.isRegenerating.get(player.getUniqueId()).booleanValue()) {
							if(args[1].equalsIgnoreCase("on")) {
								if(!(boolean) StorageModule.getPlayer(this.regenerationData, player, DataType.REGENERATION_LOCKED)) {
									StorageModule.setPlayer(this.regenerationData, player, DataType.REGENERATION_LOCKED, true);
									player.sendMessage("");
									player.sendMessage("");
									player.sendMessage(ChatColor.AQUA + "[" + this.plugin.getDescription().getName() + "] " + ChatColor.YELLOW + this.language.getString("locked"));
									player.sendMessage("");
									player.sendMessage("");
								} else {
									player.sendMessage("");
									player.sendMessage("");
									player.sendMessage(ChatColor.AQUA + "[" + this.plugin.getDescription().getName() + "] " + ChatColor.YELLOW + this.language.getString("already-locked"));
									player.sendMessage("");
									player.sendMessage("");
								}
							} else if(args[1].equalsIgnoreCase("off")) {
								if((boolean) StorageModule.getPlayer(this.regenerationData, player, DataType.REGENERATION_LOCKED)) {
									StorageModule.setPlayer(this.regenerationData, player, DataType.REGENERATION_LOCKED, false);
									player.sendMessage("");
									player.sendMessage("");
									player.sendMessage(ChatColor.AQUA + "[" + this.plugin.getDescription().getName() + "] " + ChatColor.YELLOW + this.language.getString("unlocked"));
									player.sendMessage("");
									player.sendMessage("");
								} else {
									player.sendMessage("");
									player.sendMessage("");
									player.sendMessage(ChatColor.AQUA + "[" + this.plugin.getDescription().getName() + "] " + ChatColor.YELLOW + this.language.getString("already-unlocked"));
									player.sendMessage("");
									player.sendMessage("");
								}
							}
						} else {
							player.sendMessage("");
							player.sendMessage("");
							player.sendMessage(ChatColor.YELLOW + this.language.getString("player-regenerating"));
							player.sendMessage("");
							player.sendMessage("");
						}
					} else sender.sendMessage(ChatColor.AQUA + "[" + this.plugin.getDescription().getName() + "] " + ChatColor.YELLOW + this.language.getString("player-error"));
				} else sender.sendMessage(ChatColor.AQUA + "[" + this.plugin.getDescription().getName() + "] " + ChatColor.YELLOW + this.language.getString("player-tma"));
			} else sender.sendMessage(ChatColor.AQUA + "[" + this.plugin.getDescription().getName() + "] " + ChatColor.YELLOW + "Invalid subcommand! Type " + ChatColor.GREEN + "/reborn " + ChatColor.YELLOW + "for help.");
		} else sender.sendMessage(ChatColor.AQUA + "[" + this.plugin.getDescription().getName() + "] " + ChatColor.YELLOW + this.language.getString("no-permission"));
		return true;
	}
}
