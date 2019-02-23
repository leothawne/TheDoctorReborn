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
package io.github.leothawne.thedoctorreborn;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.leothawne.thedoctorreborn.command.RebornAdminCommand;
import io.github.leothawne.thedoctorreborn.command.RebornCommand;
import io.github.leothawne.thedoctorreborn.command.tabCompleter.RebornAdminCommandTabCompleter;
import io.github.leothawne.thedoctorreborn.command.tabCompleter.RebornCommandTabCompleter;
import io.github.leothawne.thedoctorreborn.event.PlayerEvent;
import io.github.leothawne.thedoctorreborn.event.RegenerationEvent;
public class TheDoctorRebornLoader extends JavaPlugin {
	private final ConsoleLoader myLogger = new ConsoleLoader(this);
	public final void registerEvents(Listener...listeners) {
		for(Listener listener : listeners) {
			Bukkit.getServer().getPluginManager().registerEvents(listener, this);
		}
	}
	private static Connection connection = null;
	private static FileConfiguration configuration = null;
	@Override
	public final void onEnable() {
		myLogger.Hello();
		myLogger.info("Loading...");
		new ConfigurationLoader(this, myLogger);
		ConfigurationLoader.check();
		new ConfigurationLoader(this, myLogger);
		configuration = ConfigurationLoader.load();
		if(configuration.getBoolean("enable-plugin") == true) {
			new MetricsLoader(this, myLogger);
			MetricsLoader.init();
			new ResourceLoader(this, myLogger);
			ResourceLoader.run();
			new ResourceLoader(this, myLogger);
			File databaseFile = ResourceLoader.getFile("database");
			myLogger.info("Opening database...");
			String url = "jdbc:sqlite:" + databaseFile;
			try {
				connection = DriverManager.getConnection(url);
				myLogger.info("Database opened.");
			} catch(SQLException sqlex) {
				myLogger.severe("Error while opening the database: " + sqlex.getMessage());
			}
			getCommand("reborn").setExecutor(new RebornCommand(this, connection, myLogger));
			getCommand("reborn").setTabCompleter(new RebornCommandTabCompleter());
			getCommand("rebornadmin").setExecutor(new RebornAdminCommand(this, connection, myLogger));
			getCommand("rebornadmin").setTabCompleter(new RebornAdminCommandTabCompleter());
			new Version(this, myLogger);
			Version.check();
			registerEvents(new PlayerEvent(connection, myLogger, configuration), new RegenerationEvent(this, connection, myLogger));
		} else {
			myLogger.severe("You manually choose to disable this plugin.");
			getServer().getPluginManager().disablePlugin(this);
		}
	}
	@Override
	public final void onDisable() {
		myLogger.info("Unloading...");
		myLogger.info("Closing database...");
		try {
			connection.close();
			myLogger.info("Database closed.");
		} catch(SQLException sqlex) {
			myLogger.severe("Error while closing the database: " + sqlex.getMessage());
		}
	}
}