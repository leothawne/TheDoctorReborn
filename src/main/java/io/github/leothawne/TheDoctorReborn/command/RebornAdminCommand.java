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

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;
import io.github.leothawne.TheDoctorReborn.api.HTTPAPI;
import io.github.leothawne.TheDoctorReborn.module.DataModule;
import io.github.leothawne.TheDoctorReborn.module.StorageModule;
import io.github.leothawne.TheDoctorReborn.type.DataType;

public final class RebornAdminCommand implements CommandExecutor {
	private TheDoctorReborn plugin;
	private FileConfiguration language;
	private FileConfiguration regenerationData;
	public RebornAdminCommand(final TheDoctorReborn plugin, final FileConfiguration language, final FileConfiguration regenerationData) {
		this.plugin = plugin;
		this.language = language;
		this.regenerationData = regenerationData;
	}
	@Override
	public final boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
		if(sender.hasPermission("TheDoctorReborn.admin") || sender.isOp()) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.AQUA + "=+=+=+= [" + this.plugin.getDescription().getName() + "] =+=+=+=");
				sender.sendMessage(ChatColor.GOLD + this.plugin.getDescription().getDescription());
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
							final String[] LocalVersion = plugin.getDescription().getVersion().split("\\.");
							final int Local_VersionNumber1 = Integer.parseInt(LocalVersion[0]);
							final int Local_VersionNumber2 = Integer.parseInt(LocalVersion[1]);
							final int Local_VersionNumber3 = Integer.parseInt(LocalVersion[2]);
							final String[] Server1 = HTTPAPI.getData(DataModule.getUpdateURL()).split("-");
							final String[] Server2 = Server1[0].split("\\.");
							final int Server2_VersionNumber1 = Integer.parseInt(Server2[0]);
							final int Server2_VersionNumber2 = Integer.parseInt(Server2[1]);
							final int Server2_VersionNumber3 = Integer.parseInt(Server2[2]);
							final String updateMessage = ChatColor.AQUA + "[" + RebornAdminCommand.this.plugin.getDescription().getName() + "] " + ChatColor.YELLOW + "A newer version is available: " + ChatColor.GREEN + Server1[0] + ChatColor.YELLOW + " (released on " + ChatColor.GREEN + Server1[1] + ChatColor.YELLOW + ").";
							if(Server2_VersionNumber1 > Local_VersionNumber1) {
								finalSender.sendMessage(updateMessage);
							} else if(Server2_VersionNumber1 == Local_VersionNumber1 && Server2_VersionNumber2 > Local_VersionNumber2) {
								finalSender.sendMessage(updateMessage);
							} else if(Server2_VersionNumber1 == Local_VersionNumber1 && Server2_VersionNumber2 == Local_VersionNumber2 && Server2_VersionNumber3 > Local_VersionNumber3) {
								finalSender.sendMessage(updateMessage);
							} else finalSender.sendMessage(ChatColor.AQUA + "[" + RebornAdminCommand.this.plugin.getDescription().getName() + "] " + ChatColor.YELLOW + "The plugin is up to date!");
						}
					}.runTaskAsynchronously(this.plugin);
				} else sender.sendMessage(ChatColor.AQUA + "[" + this.plugin.getDescription().getName() + "] " + ChatColor.YELLOW + "" + this.language.getString("player-tma"));
			} else if(args[0].equalsIgnoreCase("info")) {
				if(args.length < 2) {
					sender.sendMessage(ChatColor.AQUA + "[" + this.plugin.getDescription().getName() + "] " + ChatColor.YELLOW + this.language.getString("player-missing"));
				} else if(args.length < 3) {
					final Player onlinePlayer = this.plugin.getServer().getPlayer(args[1]);
					if(onlinePlayer != null) {
						sender.sendMessage("");
						sender.sendMessage("");
						sender.sendMessage(ChatColor.DARK_GREEN + onlinePlayer.getName() + ChatColor.YELLOW + " (" + ChatColor.GREEN + "Online" + ChatColor.YELLOW + ")");
						sender.sendMessage("");
						sender.sendMessage(ChatColor.YELLOW + "UUID: " + ChatColor.GREEN + onlinePlayer.getUniqueId() + ChatColor.YELLOW + ".");
						sender.sendMessage(ChatColor.YELLOW + this.language.getString("player-health-level") + " " + ChatColor.GREEN + onlinePlayer.getHealth() + ChatColor.YELLOW + "/20.0.");
						sender.sendMessage(ChatColor.YELLOW + this.language.getString("player-food-level") + " " + ChatColor.GREEN + onlinePlayer.getFoodLevel() + ChatColor.YELLOW + "/20.");
						sender.sendMessage(ChatColor.YELLOW + this.language.getString("player-current-regeneration") + " " + ChatColor.GREEN + (int) StorageModule.getPlayer(this.regenerationData, onlinePlayer, DataType.REGENERATION_NUMBER) + ChatColor.YELLOW + "/12.");
						sender.sendMessage(ChatColor.YELLOW + this.language.getString("player-cycle") + " " + ChatColor.GREEN + (int) StorageModule.getPlayer(this.regenerationData, onlinePlayer, DataType.REGENERATION_CYCLE) + ChatColor.YELLOW + ".");
						String yesno = null;
						if((boolean) StorageModule.getPlayer(regenerationData, onlinePlayer, DataType.REGENERATION_LOCKED)) {
							yesno = language.getString("yes-message");
						} else yesno = language.getString("no-message");
						sender.sendMessage(ChatColor.YELLOW + language.getString("player-locked") + " " + ChatColor.GREEN + yesno + ChatColor.YELLOW + ".");
						sender.sendMessage("");
						sender.sendMessage("");
					} else sender.sendMessage(ChatColor.AQUA + "[" + this.plugin.getDescription().getName() + "] " + ChatColor.YELLOW + language.getString("player-not-found"));
				} else sender.sendMessage(ChatColor.AQUA + "[" + this.plugin.getDescription().getName() + "] " + ChatColor.YELLOW + language.getString("player-tma"));
			} else sender.sendMessage(ChatColor.AQUA + "[" + this.plugin.getDescription().getName() + "] " + ChatColor.YELLOW + "Invalid subcommand! Type " + ChatColor.GREEN + "/rebornadmin " + ChatColor.YELLOW + "for help.");
		} else sender.sendMessage(ChatColor.AQUA + "[" + this.plugin.getDescription().getName() + "] " + ChatColor.YELLOW + language.getString("no-permission"));
		return true;
	}
}
