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
package io.github.leothawne.thedoctorreborn.command;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.leothawne.thedoctorreborn.ConsoleLoader;
import io.github.leothawne.thedoctorreborn.TheDoctorRebornLoader;
import io.github.leothawne.thedoctorreborn.Version;
import io.github.leothawne.thedoctorreborn.event.RegenerationEvent;
public class RebornCommand implements CommandExecutor {
	private TheDoctorRebornLoader plugin;
	private Connection connection;
	private ConsoleLoader myLogger;
	public RebornCommand(TheDoctorRebornLoader plugin, Connection connection, ConsoleLoader myLogger) {
		this.plugin = plugin;
		this.connection = connection;
		this.myLogger = myLogger;
	}
	@Override
	public final boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(cmd.getName().equalsIgnoreCase("reborn")) {
			if(sender.hasPermission("TheDoctorReborn.use")) {
				if(args.length == 0) {
					sender.sendMessage(ChatColor.AQUA + "=+=+=+= [The Doctor Reborn] =+=+=+=");
					sender.sendMessage(ChatColor.GREEN + "/reborn " + ChatColor.AQUA + "- Shows all commands for The Doctor Reborn.");
					sender.sendMessage(ChatColor.GREEN + "/reborn version " + ChatColor.AQUA + "- Shows the plugin version.");
					sender.sendMessage(ChatColor.GREEN + "/reborn info " + ChatColor.AQUA + "- Shows your regeneration status.");
					sender.sendMessage(ChatColor.GREEN + "/reborn force " + ChatColor.AQUA + "- Forces you to regenerate.");
					sender.sendMessage(ChatColor.GREEN + "/reborn lock <on/off> " + ChatColor.AQUA + "- Locks/unlocks your regeneration ability.");
					sender.sendMessage(ChatColor.GREEN + "/reborn ch " + ChatColor.AQUA + "- Clears your main hand.");
					sender.sendMessage(ChatColor.GREEN + "/rebornadmin " + ChatColor.AQUA + "- Shows administration commands for The Doctor Reborn.");
					sender.sendMessage(ChatColor.YELLOW + "You can also use "+ ChatColor.GREEN + "/reborn "+ ChatColor.YELLOW + "as "+ ChatColor.GREEN + "/rb"+ ChatColor.YELLOW + ".");
				} else if(args[0].equalsIgnoreCase("version")) {
					if(args.length < 2) {
						new Version(plugin, myLogger);
						Version.version(sender);
					} else {
						sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "Too many arguments!");
					}
				} else if(args[0].equalsIgnoreCase("info")) {
					if(args.length < 2) {
						if(sender instanceof Player) {
							if(connection != null) {
								try {
									Statement stmtGetInfo = connection.createStatement();
									ResultSet rsGetInfo = stmtGetInfo.executeQuery("SELECT * FROM timelords WHERE name = '" + sender.getName() + "';");
									Statement stmtGetPrefs = connection.createStatement();
									ResultSet rsGetPrefs = stmtGetPrefs.executeQuery("SELECT * FROM preferences WHERE id = '" + rsGetInfo.getInt("id") + "'");
									sender.sendMessage(ChatColor.AQUA + "=+=+=+= [The Doctor Reborn] =+=+=+=");
									if(rsGetPrefs.getInt("enabled") == 1) {
										sender.sendMessage(ChatColor.GREEN + "Time Lord: " + ChatColor.AQUA + "" + rsGetInfo.getString("name"));
										sender.sendMessage(ChatColor.GREEN + "Current regeneration number: " + ChatColor.AQUA + "" + rsGetInfo.getInt("regeneration") + " (" + (12 - rsGetInfo.getInt("regeneration")) + " left)");
										String regenLocked = null;
										if(rsGetPrefs.getInt("locked") == 1) {
											regenLocked = "Yes";
										} else {
											regenLocked = "No";
										}
										sender.sendMessage(ChatColor.GREEN + "Regeneration locked: " + ChatColor.AQUA + "" + regenLocked);
									} else {
										sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "Your regeneration ability was disabled by an admin.");
									}
									rsGetPrefs.close();
									stmtGetPrefs.close();
									rsGetInfo.close();
									stmtGetInfo.close();
								} catch(SQLException e) {
									myLogger.severe("Error while getting account details for the Time Lord " + sender.getName() + "!");
									myLogger.severe(e.getMessage());
								}
							} else {
								myLogger.severe("Error while connecting to the database: The connection must be opened!");
								sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "Error while connecting to the database!");
							}
						} else {
							sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "You must be a player to do that!");
						}
					} else {
						sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "Too many arguments!");
					}
				} else if(args[0].equalsIgnoreCase("force")) {
					if(args.length < 2) {
						if(sender instanceof Player) {
							Player player = (Player) sender;
							if(player.hasPermission("TheDoctorReborn.regenerate")) {
								sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "You forced your own regeneration!");
								new RegenerationEvent(plugin, connection, myLogger);
								RegenerationEvent.regenerate(player, false);
							} else {
								player.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "You can't do that! You don't have permission.");
								myLogger.severe(player.getName() + " don't have permission [TheDoctorReborn.regenerate].");
							}
						} else {
							sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "You must be a player to do that!");
						}
					} else {
						sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "Too many arguments!");
					}
				} else if(args[0].equalsIgnoreCase("lock")) {
					if(args.length < 2) {
						sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "Not enough arguments!");
					} else {
						if(args.length < 3) {
							if(sender instanceof Player) {
								if(connection != null) {
									try {
										Statement stmtGettimelord = connection.createStatement();
										ResultSet rsGettimelord = stmtGettimelord.executeQuery("SELECT * FROM timelords WHERE name = '" + sender.getName() + "';");
										Statement stmtGetPreferences = connection.createStatement();
										ResultSet rsGetPreferences = stmtGetPreferences.executeQuery("SELECT * FROM preferences WHERE id = '" + rsGettimelord.getInt("id") + "';");
										if(rsGetPreferences.getInt("enabled") == 1) {
											if(args[1].equalsIgnoreCase("on")) {
												if(rsGetPreferences.getInt("locked") == 0) {
													Statement stmtUpdatetimelord = connection.createStatement();
													stmtUpdatetimelord.executeUpdate("UPDATE preferences SET locked = '1' WHERE id = '" + rsGettimelord.getInt("id") + "';");
													stmtUpdatetimelord.close();
													sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "Regeneration ability was locked.");
												} else {
													sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "Regeneration ability is already locked.");
												}
											} else if(args[1].equalsIgnoreCase("off")){
												if(rsGetPreferences.getInt("locked") == 1) {
													Statement stmtUpdatetimelord = connection.createStatement();
													stmtUpdatetimelord.executeUpdate("UPDATE preferences SET locked = '0' WHERE id = '" + rsGettimelord.getInt("id") + "';");
													stmtUpdatetimelord.close();
													sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "Regeneration ability was unlocked.");
												} else {
													sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "Regeneration ability is already unlocked.");
												}
											} else {
												sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "Invalid arguments! Try " + ChatColor.GREEN + "on " + ChatColor.YELLOW + "or " + ChatColor.GREEN + "off" + ChatColor.YELLOW + ".");
											}
										} else {
											sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "Your regeneration ability was disabled by an admin.");
										}
										rsGettimelord.close();
										stmtGettimelord.close();
									} catch(SQLException e) {
										myLogger.severe("Error while getting account details for the Time Lord " + sender.getName() + "!");
										myLogger.severe(e.getMessage());
									}
								} else {
									myLogger.severe("Error while connecting to the database: The connection must be opened!");
									sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "Error while connecting to the database!");
								}
							} else {
								sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "You must be a player to do that!");
							}
						} else {
							sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "Too many arguments!");
						}
					}
				} else if(args[0].equalsIgnoreCase("ch")) {
					if(sender instanceof Player) {
						Player player = (Player) sender;
						if(args.length < 2) {
							if(player.getInventory().getItemInMainHand().getAmount() > 0) {
								int deletedNumber = player.getInventory().getItemInMainHand().getAmount();
								player.getInventory().getItemInMainHand().setAmount(deletedNumber - deletedNumber);
								player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1F, 1F);
								sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "Cleared " + ChatColor.GREEN + "" + deletedNumber + "" + ChatColor.YELLOW + " items!");
							} else {
								sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "Your hand is already empty!");
							}
						} else if(args.length < 3) {
							if(player.getInventory().getItemInMainHand().getAmount() > 0) {
								try {
									int numberToDelete = Integer.parseInt(args[1]);
									int currentNumber = player.getInventory().getItemInMainHand().getAmount();
									if(numberToDelete < 0) {
										sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "The number cannot be negative.");
									} else if(numberToDelete == 0) {
										sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "The number cannot be zero.");
									} else if((currentNumber - numberToDelete) >= 0) {
										player.getInventory().getItemInMainHand().setAmount(currentNumber - numberToDelete);
										player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1F, 1F);
										sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "Cleared " + ChatColor.GREEN + "" + numberToDelete + "" + ChatColor.YELLOW + " items!");
									} else {
										sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "You don't have " + ChatColor.GREEN + "" + numberToDelete + "" + ChatColor.YELLOW + " items do delete.");
									}
								} catch(Exception e) {
									sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "You must specify an integer number.");
								}
							} else {
								sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "Your hand is already empty!");
							}
						} else {
							sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "Too many arguments!");
						}
					} else {
						sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "You must be a player to do that!");
					}
				} else {
					sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "Invalid command! Type " + ChatColor.GREEN + "/reborn " + ChatColor.YELLOW + "to see all available commands.");
				}
			} else {
				sender.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "You can't do that! You don't have permission.");
				myLogger.severe(sender.getName() + " don't have permission [TheDoctorReborn.use].");
			}
		}
		return true;
	}
}
