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
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import io.github.leothawne.TheDoctorReborn.api.bStats.MetricsAPI;
import io.github.leothawne.TheDoctorReborn.command.RebornAdminCommand;
import io.github.leothawne.TheDoctorReborn.command.RebornCommand;
import io.github.leothawne.TheDoctorReborn.command.tabCompleter.RebornAdminCommandTabCompleter;
import io.github.leothawne.TheDoctorReborn.command.tabCompleter.RebornCommandTabCompleter;
import io.github.leothawne.TheDoctorReborn.event.AdminEvent;
import io.github.leothawne.TheDoctorReborn.event.PlayerEvent;
import io.github.leothawne.TheDoctorReborn.task.RecipeTask;
import io.github.leothawne.TheDoctorReborn.task.RegenerationTask;

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
	private static HashMap<UUID, Integer> regenerationCycle = new HashMap<UUID, Integer>();
	private static HashMap<UUID, Boolean> isRegenerating = new HashMap<UUID, Boolean>();
	private static HashMap<UUID, Boolean> isLocked = new HashMap<UUID, Boolean>();
	private static HashMap<UUID, Integer> regenerationTaskNumber = new HashMap<UUID, Integer>();
	private static HashMap<CommandSender, Boolean> purgePlayers = new HashMap<CommandSender, Boolean>();
	private static BukkitScheduler scheduler;
	private static int regenerationTask;
	private static int recipeTask;
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
				PlayersFileLoader.loadPlayer(this, myLogger, player, regenerationNumber, regenerationCycle, isLocked, true);
				isRegenerating.put(player.getUniqueId(), false);
			}
			getCommand("reborn").setExecutor(new RebornCommand(myLogger, language, regenerationNumber, regenerationCycle, isRegenerating, isLocked));
			getCommand("reborn").setTabCompleter(new RebornCommandTabCompleter());
			getCommand("rebornadmin").setExecutor(new RebornAdminCommand(this, myLogger, language, regenerationNumber, regenerationCycle, isLocked, purgePlayers));
			getCommand("rebornadmin").setTabCompleter(new RebornAdminCommandTabCompleter());
			registerEvents(new AdminEvent(configuration), new PlayerEvent(this, myLogger, configuration, language, regenerationNumber, regenerationCycle, isRegenerating, isLocked, regenerationTaskNumber));
			scheduler = getServer().getScheduler();
			regenerationTask = scheduler.scheduleSyncRepeatingTask(this, new RegenerationTask(this, isRegenerating, isLocked), 0, 2);
			recipeTask = scheduler.scheduleSyncRepeatingTask(this, new RecipeTask(this, language), 0, 20 * 1);
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
		if(scheduler.isCurrentlyRunning(regenerationTask)) {
			scheduler.cancelTask(regenerationTask);
		}
		if(scheduler.isCurrentlyRunning(recipeTask)) {
			scheduler.cancelTask(recipeTask);
		}
		for(Player player : getServer().getOnlinePlayers()) {
			PlayersFileLoader.savePlayer(this, myLogger, player, regenerationNumber, regenerationCycle, isLocked, true);
		}
		RecipeTask.resetRecipes(this);
	}
}