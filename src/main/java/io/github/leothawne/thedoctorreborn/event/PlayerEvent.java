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
package io.github.leothawne.thedoctorreborn.event;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import io.github.leothawne.thedoctorreborn.ConsoleLoader;
public class PlayerEvent implements Listener {
	private static Connection connection;
	private static  ConsoleLoader myLogger;
	private static FileConfiguration configuration;
	public PlayerEvent(Connection connection, ConsoleLoader myLogger, FileConfiguration configuration) {
		PlayerEvent.connection = connection;
		PlayerEvent.myLogger = myLogger;
		PlayerEvent.configuration = configuration;
	}
	private static final boolean welcome(Player timelord) {
		Player player = (Player) timelord;
		try {
			if(connection != null) {
				Statement stmtPlayer = connection.createStatement();
				ResultSet rsPlayer = stmtPlayer.executeQuery("SELECT * FROM timelords WHERE mid = '" + player.getUniqueId() + "';");
				int playerCount = 0;
				while(rsPlayer.next()) {
					++playerCount;
				}
				if(playerCount > 0) {
					return true;
				} else {
					return false;
				}
			} else {
				myLogger.severe("Error while connecting to the database: The connection must be opened!");
				return false;
			}
		} catch(SQLException e) {
			myLogger.severe("Error while welcoming Time Lord " + player.getName() + ": " + e.getMessage());
			return false;
		}
	}
	private static final void check(Player timelord, boolean shouldSetOffline) {
		Player playerMid = (Player) timelord;
		try {
			if(connection != null) {
				if(playerMid != null) {
					Statement stmtCheck = connection.createStatement();
					ResultSet rsCheck = stmtCheck.executeQuery("SELECT * FROM timelords WHERE mid = '" + playerMid.getUniqueId() + "';");
					Statement stmtCheckRows = connection.createStatement();
					ResultSet rsCheckRows = stmtCheckRows.executeQuery("SELECT * FROM timelords;");
					Statement stmtPrefsCheck = connection.createStatement();
					ResultSet rsPrefsCheck = stmtPrefsCheck.executeQuery("SELECT * FROM preferences;");
					int count = 0;
					int countPrefs = 0;
					int countRows = 0;
					while(rsCheck.next()) {
						++count;
					}
					while(rsPrefsCheck.next()) {
						++countPrefs;
					}
					while(rsCheckRows.next()) {
						++countRows;
					}
					int shouldBeOnline;
					if(shouldSetOffline == true) {
						shouldBeOnline = 0;
					} else {
						shouldBeOnline = 1;
					}
					if(count == 0) {
						Statement stmtInsert = connection.createStatement();
						stmtInsert.executeUpdate("INSERT INTO timelords (id, mid, name, online) VALUES ('" + (countRows + 1) + "', '" + playerMid.getUniqueId() + "', '" + playerMid.getName() + "', '" + shouldBeOnline + "')");
						Statement stmt = connection.createStatement();
						ResultSet rs = stmt.executeQuery("SELECT * FROM timelords WHERE mid = '" + playerMid.getUniqueId() + "';");
						Statement stmtPrefsInsert = connection.createStatement();
						stmtPrefsInsert.executeUpdate("INSERT INTO preferences (pid, id) VALUES('" + (countPrefs + 1) + "', '" + rs.getInt("id") + "');");
						stmtPrefsInsert.close();
						rs.close();
						stmt.close();
						stmtInsert.close();
					} else if(count > 1) {
						myLogger.severe("There's more than one Time Lord registered with UUID " + playerMid.getUniqueId() + ". This might cause several errors!");
					} else {
						Statement stmt = connection.createStatement();
						ResultSet rs = stmt.executeQuery("SELECT * FROM timelords WHERE mid = '" + playerMid.getUniqueId() + "';");
						Statement stmtUpdate = connection.createStatement();
						stmtUpdate.executeUpdate("UPDATE timelords SET name = '" + playerMid.getName() + "', online = '" + shouldBeOnline + "' WHERE id = '" + rs.getInt("id") + "';");
						Statement stmtPrefsCheck2 = connection.createStatement();
						ResultSet rsPrefsCheck2 = stmtPrefsCheck2.executeQuery("SELECT * FROM preferences WHERE id = '" + rs.getInt("id") + "';");
						int countPrefs2 = 0;
						while(rsPrefsCheck2.next()) {
							++countPrefs2;
						}
						if(countPrefs2 == 0) {
							Statement stmt2 = connection.createStatement();
							ResultSet rs2 = stmt2.executeQuery("SELECT * FROM timelords WHERE mid = '" + playerMid.getUniqueId() + "';");
							Statement stmtPrefsInsert = connection.createStatement();
							stmtPrefsInsert.executeUpdate("INSERT INTO preferences (pid, id) VALUES('" + (countPrefs + 1) + "', '" + rs2.getInt("id") + "');");
							stmtPrefsInsert.close();
							rs2.close();
							stmt2.close();
						} else if(countPrefs2 > 1){
							myLogger.severe("There's more than one Time Lord preference file registered and attached to  " + playerMid.getName() + " (" + playerMid.getUniqueId() + "). This might cause several errors!");
						}
						rsPrefsCheck2.close();
						stmtPrefsCheck2.close();
						stmtUpdate.close();
						rs.close();
						stmt.close();
					}
					rsPrefsCheck.close();
					stmtPrefsCheck.close();
					rsCheckRows.close();
					stmtCheckRows.close();
					rsCheck.close();
					stmtCheck.close();
				}
			} else {
				myLogger.severe("Error while connecting to the database: The connection must be opened!");
			}
		} catch(SQLException e) {
			myLogger.severe("Error while checking if Time Lord exists!");
			myLogger.severe(e.getMessage());
		}
	}
	@EventHandler
	public static final void onPlayerJoin(PlayerJoinEvent e) {
		Player player = (Player) e.getPlayer();
		if(player.isInvulnerable() == true) {
			player.setInvulnerable(false);
		}
		if(welcome(player) == false) {
			player.sendMessage(ChatColor.DARK_PURPLE + "You were touched by the hands of Rassilon! Now you possess the regeneration ability! To learn more, type " + ChatColor.GREEN + "/reborn" + ChatColor.DARK_PURPLE + ".");
			player.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "In case you need help: " + ChatColor.GREEN + "/reborn support");
			if(player.hasPermission("TheDoctorReborn.use")) {
				player.performCommand("reborn version");
			}
		}
		check(player, false);
		if(player.hasPermission("TheDoctorReborn.admin")) {
			if(configuration.getBoolean("update.check") == true) {
				player.performCommand("rebornadmin version");
			}
		}
	}
	@EventHandler
	public static final void onPlayerQuit(PlayerQuitEvent e) {
		Player player = (Player) e.getPlayer();
		check(player, true);
	}
}