package io.github.leothawne.thedoctorreborn.command;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.leothawne.thedoctorreborn.ConsoleLoader;
import io.github.leothawne.thedoctorreborn.TheDoctorRebornLoader;
import io.github.leothawne.thedoctorreborn.Version;
import io.github.leothawne.thedoctorreborn.event.RegenerationEvent;
@SuppressWarnings("deprecation")
public class RebornAdminCommand implements CommandExecutor {
	private TheDoctorRebornLoader plugin;
	private Connection connection;
	private ConsoleLoader myLogger;
	public RebornAdminCommand(TheDoctorRebornLoader plugin, Connection connection, ConsoleLoader myLogger) {
		this.plugin = plugin;
		this.connection = connection;
		this.myLogger = myLogger;
	}
	private final String TDRVersion = Version.getVersionNumber();
	private final String TDRVersion_Date = Version.getVersionDate();
	@Override
	public final boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(cmd.getName().equalsIgnoreCase("rebornadmin")) {
			if(sender.hasPermission("TheDoctorReborn.admin")) {
				if(args.length == 0) {
					sender.sendMessage(ChatColor.AQUA + "=+=+=+= [The Doctor Reborn :: Admin] =+=+=+=");
					sender.sendMessage(ChatColor.GREEN + "/rebornadmin " + ChatColor.AQUA + "- Administration commands for The Doctor Reborn.");
					sender.sendMessage(ChatColor.GREEN + "/rebornadmin version " + ChatColor.AQUA + "- Check for new updates.");
					sender.sendMessage(ChatColor.GREEN + "/rebornadmin info <timelord> " + ChatColor.AQUA + "- Show the regeneration status of a Time Lord.");
					sender.sendMessage(ChatColor.GREEN + "/rebornadmin force <timelord> " + ChatColor.AQUA + "- Force a Time Lord to regenerate. (Please, use this with caution!)");
					sender.sendMessage(ChatColor.GREEN + "/rebornadmin toggle <timelord> [on/off] " + ChatColor.AQUA + "- Toggle regeneration cycle of a Time Lord.");
					sender.sendMessage(ChatColor.GREEN + "/rebornadmin give <item> <amount> " + ChatColor.AQUA + "- An easy way of getting TDR items.");
					sender.sendMessage(ChatColor.GREEN + "/rebornadmin reload " + ChatColor.AQUA + "- Reload the configuration file.");
					sender.sendMessage(ChatColor.GREEN + "/rebornadmin purge [confirm] " + ChatColor.AQUA + "- Wipe every data from the database.");
					sender.sendMessage(ChatColor.GREEN + "/rebornadmin update <timelord> " + ChatColor.AQUA + "- Kick the specified Time Lord to update its account.");
					sender.sendMessage(ChatColor.YELLOW + "You can also use " + ChatColor.GREEN + "/rebornadmin " + ChatColor.YELLOW + "as " + ChatColor.GREEN + "/rba" + ChatColor.YELLOW + ".");
				} else if(args[0].equalsIgnoreCase("version")) {
					if(args.length < 2) {
						try {
							URLConnection newUpdateURL = new URL("https://leothawne.github.io/TheDoctorReborn/api/version.html").openConnection();
							newUpdateURL.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
							newUpdateURL.connect();
							BufferedReader newUpdateReader = new BufferedReader(new InputStreamReader(newUpdateURL.getInputStream(), Charset.forName("UTF-8")));
							StringBuilder sb = new StringBuilder();
							String line;
							while((line = newUpdateReader.readLine()) != null) {
								sb.append(line);
							}
							if(sb.toString() != null) {
								String[] LocalVersion = TDRVersion.split("\\.");
								int Local_VersionNumber1 = Integer.parseInt(LocalVersion[0]);
								int Local_VersionNumber2 = Integer.parseInt(LocalVersion[1]);
								int Local_VersionNumber3 = Integer.parseInt(LocalVersion[2]);
								String[] Server1 = sb.toString().split("-");
								String[] Server2 = Server1[0].split("\\.");
								int Server2_VersionNumber1 = Integer.parseInt(Server2[0]);
								int Server2_VersionNumber2 = Integer.parseInt(Server2[1]);
								int Server2_VersionNumber3 = Integer.parseInt(Server2[2]);
								sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "Running: " + ChatColor.GREEN + "" + TDRVersion + "" + ChatColor.YELLOW + " (Released on " + ChatColor.GREEN + "" + TDRVersion_Date + "" + ChatColor.YELLOW + ")");
								String updateMessage = ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "A newer version is available: " + ChatColor.GREEN + "" + Server1[0] + "" + ChatColor.YELLOW + " (released on " + ChatColor.GREEN + "" + Server1[1] + "" + ChatColor.YELLOW + ")";
								if(Server2_VersionNumber1 > Local_VersionNumber1) {
									sender.sendMessage(updateMessage);
								} else if(Server2_VersionNumber1 == Local_VersionNumber1 && Server2_VersionNumber2 > Local_VersionNumber2) {
									sender.sendMessage(updateMessage);
								} else if(Server2_VersionNumber1 == Local_VersionNumber1 && Server2_VersionNumber2 == Local_VersionNumber2 && Server2_VersionNumber3 > Local_VersionNumber3) {
									sender.sendMessage(updateMessage);
								} else {
									sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "The plugin is up to date!");
								}
							} else {
								sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "Error while checking for new updates: Server did not respond correctly.");
							}
						} catch(Exception e) {
							myLogger.severe("Error while checking for new updates: " + e.getMessage());
							sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "Error while checking for new updates.");
						}
					} else {
						sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "Too many arguments!");
					}
				} else if(args[0].equalsIgnoreCase("info")) {
					if(args.length < 2) {
						sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "You must specify a Time Lord!");
					} else {
						/*if(args[1].equalsIgnoreCase(sender.getName())) {
							sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "If you want to see your own regeneration status, please type " + ChatColor.GREEN + "/reborn info" + ChatColor.YELLOW + ".");
						} else {*/
							if(args.length < 3) {
								if(connection != null) {
									Player testPlayer = Bukkit.getPlayer(args[1]);
									if(testPlayer != null) {
										String playerName = testPlayer.getName();
										try {
											Statement stmtGetUs = connection.createStatement();
											ResultSet rsGetUs = stmtGetUs.executeQuery("SELECT * FROM timelords WHERE name = '" + playerName + "';");
											Statement stmtGetInfo = connection.createStatement();
											ResultSet rsGetInfo = stmtGetInfo.executeQuery("SELECT * FROM timelords WHERE name = '" + playerName + "';");
											int playerCount = 0;
											while(rsGetUs.next()) {
												++playerCount;
											}
											if(playerCount > 0) {
												if(playerCount > 1) {
													sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "More than one Time Lord was found with that username. This might cause several errors!");
												}
												Statement stmtGetPrefs = connection.createStatement();
												ResultSet rsGetPrefs = stmtGetPrefs.executeQuery("SELECT * FROM preferences WHERE id = '" + rsGetInfo.getInt("id") + "'");
												sender.sendMessage(ChatColor.AQUA + "=+=+=+= [The Doctor Reborn :: Admin] =+=+=+=");
												sender.sendMessage(ChatColor.GREEN + "Time Lord: " + ChatColor.AQUA + "" + rsGetInfo.getString("name"));
												sender.sendMessage(ChatColor.GREEN + "Minecraft UUID: " + ChatColor.AQUA + "" + rsGetInfo.getString("mid"));
												String playerOnline = null;
												if(rsGetInfo.getInt("online") == 1) {
													playerOnline = "Yes";
												} else {
													playerOnline = "No";
												}
												sender.sendMessage(ChatColor.GREEN + "Online: " + ChatColor.AQUA + "" + playerOnline);
												sender.sendMessage(ChatColor.GREEN + "Current regeneration number: " + ChatColor.AQUA + "" + rsGetInfo.getInt("regeneration") + " (" + (12 - rsGetInfo.getInt("regeneration")) + " left)");
												String regenEnabled = null;
												if(rsGetPrefs.getInt("enabled") == 1) {
													regenEnabled = "Yes";
												} else {
													regenEnabled = "No";
												}
												sender.sendMessage(ChatColor.GREEN + "Regeneration enabled: " + ChatColor.AQUA + "" + regenEnabled);
												String regenLocked = null;
												if(rsGetPrefs.getInt("locked") == 1) {
													regenLocked = "Yes";
												} else {
													regenLocked = "No";
												}
												sender.sendMessage(ChatColor.GREEN + "Regeneration locked: " + ChatColor.AQUA + "" + regenLocked);
												rsGetPrefs.close();
												stmtGetPrefs.close();
											} else {
												sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "The specified Time Lord wasn't found on the database!");
											}
											rsGetInfo.close();
											stmtGetInfo.close();
											rsGetUs.close();
											stmtGetUs.close();
										} catch(SQLException e) {
											myLogger.severe("Error while getting account details for the Time Lord " + sender.getName() + "!");
											myLogger.severe(e.getMessage());
										}
									} else {
										sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "The specified Time Lord doesn't exist!");
									}
								} else {
									myLogger.severe("Error while connecting to the database: The connection must be opened!");
									sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "Error while connecting to the database!");
								}
							} else {
								sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "Too many arguments!");
							}
						//}
					}
				} else if(args[0].equalsIgnoreCase("force")) {
					if(args.length < 2) {
						sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "You must specify a Time Lord!");
					} else {
						if(args[1].equalsIgnoreCase(sender.getName())) {
							sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "If you want to force yourself to regenerate, please type " + ChatColor.GREEN + "/reborn force" + ChatColor.YELLOW + ".");
						} else {
							if(args.length < 3) {
								if(connection != null) {
									Player testPlayer = Bukkit.getPlayer(args[1]);
									if(testPlayer != null) {
										String playerName = testPlayer.getName();
										try {
											Statement stmtGetPlayerCount = connection.createStatement();
											ResultSet rsGetPlayerCount = stmtGetPlayerCount.executeQuery("SELECT * FROM timelords WHERE name = '" + playerName + "';");
											int playerCount = 0;
											while(rsGetPlayerCount.next()) {
												++playerCount;
											}
											rsGetPlayerCount.close();
											stmtGetPlayerCount.close();
											if(playerCount > 0) {
												Statement stmtGetPlayer = connection.createStatement();
												ResultSet rsGetPlayer = stmtGetPlayer.executeQuery("SELECT * FROM timelords WHERE name = '" + playerName + "';");
												if(rsGetPlayer.getInt("online") == 1) {
													sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "You forced " + ChatColor.GREEN + "" + rsGetPlayer.getString("name") + "" + ChatColor.YELLOW + " to regenerate.");
													testPlayer.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.GREEN + "" + sender.getName() + "" + ChatColor.YELLOW + " is forcing you to regenerate.");
													new RegenerationEvent(plugin, connection, myLogger);
													RegenerationEvent.regenerate(testPlayer, false);
												} else {
													sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "The Time Lord " + ChatColor.GREEN + "" + rsGetPlayer.getString("name") + "" + ChatColor.YELLOW + " isn't online!");
												}
												rsGetPlayer.close();
												stmtGetPlayer.close();
											} else {
												sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "The specified Time Lord wasn't found on the database!");
											}
										} catch(SQLException e) {
											myLogger.severe("Error while forcing a Time Lord to regenerate!");
											myLogger.severe(e.getMessage());
											sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "Error while forcing a Time Lord to regenerate!");
										}
									} else {
										sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "The specified Time Lord doesn't exist!");
									}
								} else {
									myLogger.severe("Error while connecting to the database: The connection must be opened!");
									sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "Error while connecting to the database!");
								}
							} else {
								sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "Too many arguments!");
							}
						}
					}
				} else if(args[0].equalsIgnoreCase("toggle")) {
					if(args.length < 2) {
						sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "You must specify a Time Lord!");
					} else {
						if(args.length == 3) {
							if(connection != null) {
								Player testPlayer = Bukkit.getPlayer(args[1]);
								if(testPlayer != null) {
									String playerName = testPlayer.getName();
									try {
										Statement stmtGetUs = connection.createStatement();
										ResultSet rsGetUs = stmtGetUs.executeQuery("SELECT * FROM timelords WHERE name = '" + playerName + "';");
										Statement stmtGetInfo = connection.createStatement();
										ResultSet rsGetInfo = stmtGetInfo.executeQuery("SELECT * FROM timelords WHERE name = '" + playerName + "';");
										int playerCount = 0;
										while(rsGetUs.next()) {
											++playerCount;
										}
										if(playerCount > 0) {
											if(playerCount > 1) {
												sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "More than one Time Lord was found with that username. This might cause several errors!");
											}
											Statement stmtGetPrefs = connection.createStatement();
											ResultSet rsGetPrefs = stmtGetPrefs.executeQuery("SELECT * FROM preferences WHERE id = '" + rsGetInfo.getInt("id") + "'");
											try {
												Statement stmtUpdatePrefs = connection.createStatement();
												if(rsGetPrefs.getInt("enabled") == 1) {
													if(args[2].equalsIgnoreCase("on")) {
														sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "Regeneration cycle is already enabled!");
													} else if(args[2].equalsIgnoreCase("off")) {
														stmtUpdatePrefs.executeUpdate("UPDATE preferences SET enabled = '0' WHERE id = '" + rsGetInfo.getInt("id") + "';");
														sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "Regeneration cycle disabled!");
													} else {
														sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "Invalid arguments! Try " + ChatColor.GREEN + "on " + ChatColor.YELLOW + "or " + ChatColor.GREEN + "off" + ChatColor.YELLOW + ".");
													}
												} else if(rsGetPrefs.getInt("enabled") == 0) {
													stmtUpdatePrefs.executeUpdate("");
													if(args[2].equalsIgnoreCase("on")) {
														stmtUpdatePrefs.executeUpdate("UPDATE preferences SET enabled = '1' WHERE id = '" + rsGetInfo.getInt("id") + "';");
														sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "Regeneration cycle enabled!");
													} else if(args[2].equalsIgnoreCase("off")) {
														sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "Regeneration cycle is already disabled!");
													} else {
														sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "Invalid arguments! Try " + ChatColor.GREEN + "on " + ChatColor.YELLOW + "or " + ChatColor.GREEN + "off" + ChatColor.YELLOW + ".");
													}
												} else {
													sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "I think there's something wrong!");
												}
												stmtUpdatePrefs.close();
											} catch(SQLException e2) {
												myLogger.severe("Error while getting account details for the Time Lord " + playerName + "!");
												myLogger.severe(e2.getMessage());
											}
											rsGetPrefs.close();
											stmtGetPrefs.close();
										} else {
											sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "The specified Time Lord wasn't found on the database!");
										}
										rsGetInfo.close();
										stmtGetInfo.close();
										rsGetUs.close();
										stmtGetUs.close();
									} catch(SQLException e) {
										myLogger.severe("Error while getting account details for the Time Lord " + playerName + "!");
										myLogger.severe(e.getMessage());
									}
								} else {
									sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "The specified Time Lord doesn't exist!");
								}
							} else {
								myLogger.severe("Error while connecting to the database: The connection must be opened!");
								sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "Error while connecting to the database!");
							}
						} else if(args.length > 3) {
							sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "Too many arguments!");
						} else {
							sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "Please inform what you want to do!");
						}
					}
				} else {
					sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "Invalid command! Type " + ChatColor.GREEN + "/rebornadmin " + ChatColor.YELLOW + "to see all available commands.");
				}
			} else {
				sender.sendMessage(ChatColor.AQUA + "[TDR :: Admin] " + ChatColor.YELLOW + "You can't do that! You don't have permission.");
				myLogger.severe(sender.getName() + " don't have permission [TheDoctorReborn.admin].");
			}
		}
		return true;
	}
}
