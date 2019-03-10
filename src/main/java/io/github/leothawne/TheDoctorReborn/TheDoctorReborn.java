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
package io.github.leothawne.TheDoctorReborn;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.leothawne.TheDoctorReborn.api.bStats.MetricsAPI;
import io.github.leothawne.TheDoctorReborn.command.RebornAdminCommand;
import io.github.leothawne.TheDoctorReborn.command.RebornCommand;
import io.github.leothawne.TheDoctorReborn.command.tabCompleter.RebornAdminCommandTabCompleter;
import io.github.leothawne.TheDoctorReborn.command.tabCompleter.RebornCommandTabCompleter;
import io.github.leothawne.TheDoctorReborn.event.AdminEvent;
import io.github.leothawne.TheDoctorReborn.event.PlayerEvent;

/**
 * Main class.
 * 
 * @author leothawne
 *
 */
public class TheDoctorReborn extends JavaPlugin {
	private final ConsoleLoader myLogger = new ConsoleLoader(this);
	private final void registerEvents(Listener...listeners) {
		for(Listener listener : listeners) {
			Bukkit.getServer().getPluginManager().registerEvents(listener, this);
		}
	}
	private static FileConfiguration configuration;
	private static FileConfiguration language;
	private static MetricsAPI metrics;
	private static HashMap<UUID, Integer> regenerationNumber = new HashMap<UUID, Integer>();
	private static HashMap<UUID, Boolean> isRegenerating = new HashMap<UUID, Boolean>();
	private static HashMap<UUID, Boolean> isLocked = new HashMap<UUID, Boolean>();
	/**
	 * 
	 * @deprecated Not for public use.
	 * 
	 */
	@Override
	public final void onEnable() {
		myLogger.Hello();
		myLogger.info("Loading...");
		ConfigurationLoader.check(this, myLogger);
		configuration = ConfigurationLoader.load(this, myLogger);
		if(configuration.getBoolean("enable-plugin") == true) {
			Version.check(this, myLogger);
			MetricsLoader.init(this, myLogger, metrics);
			LanguageLoader.check(this, myLogger, configuration);
			language = LanguageLoader.load(this, myLogger, configuration);
			PlayersFileLoader.check(this, myLogger);
			for(Player player : getServer().getOnlinePlayers()) {
				PlayersFileLoader.load(this, myLogger, player, regenerationNumber, isLocked, false);
				isRegenerating.put(player.getUniqueId(), false);
			}
			getCommand("reborn").setExecutor(new RebornCommand(myLogger, language));
			getCommand("reborn").setTabCompleter(new RebornCommandTabCompleter());
			getCommand("rebornadmin").setExecutor(new RebornAdminCommand(this, myLogger, language));
			getCommand("rebornadmin").setTabCompleter(new RebornAdminCommandTabCompleter());
			registerEvents(new AdminEvent(configuration), new PlayerEvent(regenerationNumber, isRegenerating, isLocked));
		} else {
			myLogger.severe("You choose to disable this plugin.");
			getServer().getPluginManager().disablePlugin(this);
		}
	}
	/**
	 * 
	 * @deprecated Not for public use.
	 * 
	 */
	@Override
	public final void onDisable() {
		myLogger.info("Unloading...");
	}
}