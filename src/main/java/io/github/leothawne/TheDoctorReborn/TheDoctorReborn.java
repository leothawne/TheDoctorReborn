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
package io.github.leothawne.TheDoctorReborn;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import io.github.leothawne.TheDoctorReborn.api.MetricsAPI;
import io.github.leothawne.TheDoctorReborn.api.TheDoctorRebornAPI;
import io.github.leothawne.TheDoctorReborn.command.RebornAdminCommand;
import io.github.leothawne.TheDoctorReborn.command.RebornCommand;
import io.github.leothawne.TheDoctorReborn.command.tabCompleter.RebornAdminCommandTabCompleter;
import io.github.leothawne.TheDoctorReborn.command.tabCompleter.RebornCommandTabCompleter;
import io.github.leothawne.TheDoctorReborn.listener.AdminListener;
import io.github.leothawne.TheDoctorReborn.listener.PlayerListener;
import io.github.leothawne.TheDoctorReborn.module.ConfigurationModule;
import io.github.leothawne.TheDoctorReborn.module.ConsoleModule;
import io.github.leothawne.TheDoctorReborn.module.LanguageModule;
import io.github.leothawne.TheDoctorReborn.module.MetricsModule;
import io.github.leothawne.TheDoctorReborn.module.StorageModule;
import io.github.leothawne.TheDoctorReborn.task.AutoSaveTask;
import io.github.leothawne.TheDoctorReborn.task.RecipeTask;
import io.github.leothawne.TheDoctorReborn.task.RegenerationTask;
import io.github.leothawne.TheDoctorReborn.task.VersionTask;

/**
 * Main class.
 * 
 * @author leothawne
 *
 */
public final class TheDoctorReborn extends JavaPlugin {
	private static TheDoctorReborn instance;
	private final ConsoleModule console = new ConsoleModule(this);
	private final void registerEvents(final Listener...listeners) {
		for(final Listener listener : listeners) {
			Bukkit.getServer().getPluginManager().registerEvents(listener, this);
		}
	}
	private FileConfiguration configuration = null;
	private FileConfiguration language = null;
	private MetricsAPI metrics = null;
	private BukkitScheduler scheduler = null;
	private FileConfiguration regenerationData = null;
	private HashMap<UUID, Boolean> isRegenerating = new HashMap<UUID, Boolean>();
	private HashMap<UUID, Integer> regenerationTaskNumber = new HashMap<UUID, Integer>();
	private int versionTask = 0;
	private int regenerationTask = 0;
	private int recipeTask = 0;
	private int autoSaveTask = 0;
	/**
	 * 
	 * @deprecated Not for public use.
	 * 
	 */
	@Override
	public final void onLoad() {
		instance = this;
		this.console.info(this.getServer().getName() + " version " + this.getServer().getVersion() + " is loading " + this.getDescription().getName() + " v" + this.getDescription().getVersion() + "...");
	}
	/**
	 * 
	 * @deprecated Not for public use.
	 * 
	 */
	@Override
	public final void onEnable() {
		this.console.Hello();
		this.console.info("Loading...");
		ConfigurationModule.preLoad(this, this.console);
		final File devKey = new File(this.getDataFolder(), "devMode.key");
		if(!(devKey.exists() && devKey.isFile())) if(ConfigurationModule.isItOutdated(this)) {
			this.console.warning("Updating config.yml with a newer version...");
			if(ConfigurationModule.makeOldFile(this)) {
				ConfigurationModule.preLoad(this, this.console);
			} else {
				this.console.severe("Could not rename the old config.yml file. Do this manually and restart the server.");
				this.getServer().getPluginManager().disablePlugin(this);
				return;
			}
		}
		this.configuration = ConfigurationModule.load(this, this.console);
		if(this.configuration.getBoolean("enable-plugin") == true) {
			this.metrics = MetricsModule.init(this, this.console);
			LanguageModule.preLoad(this, this.console, this.configuration);
			if(!(devKey.exists() && devKey.isFile())) if(LanguageModule.isItOutdated(this, this.configuration)) {
				this.console.warning("Updating " + this.configuration.getString("language") + ".yml with a newer version...");
				if(LanguageModule.deleteFile(this, this.configuration)) {
					LanguageModule.preLoad(this, this.console, this.configuration);
				} else {
					this.console.severe("Could not delete the " + this.configuration.getString("language") + ".yml file. Do this manually and restart the server.");
					this.getServer().getPluginManager().disablePlugin(this);
					return;
				}
			}
			this.language = LanguageModule.load(this, this.console, this.configuration);
			StorageModule.preLoad(this, this.console);
			this.regenerationData = StorageModule.load(this, this.console);
			for(final Player player : this.getServer().getOnlinePlayers()) {
				StorageModule.checkPlayer(this.regenerationData, player);
				this.isRegenerating.put(player.getUniqueId(), false);
			}
			this.getCommand("reborn").setExecutor(new RebornCommand(this, this.language, this.regenerationData, this.isRegenerating));
			this.getCommand("reborn").setTabCompleter(new RebornCommandTabCompleter());
			this.getCommand("rebornadmin").setExecutor(new RebornAdminCommand(this, this.language, this.regenerationData));
			this.getCommand("rebornadmin").setTabCompleter(new RebornAdminCommandTabCompleter(this));
			this.scheduler = this.getServer().getScheduler();
			this.versionTask = this.scheduler.scheduleAsyncRepeatingTask(this, new VersionTask(this, this.console), 0, 20 * 60 * 60);
			this.regenerationTask = this.scheduler.scheduleSyncRepeatingTask(this, new RegenerationTask(this, this.regenerationData, this.isRegenerating), 0, 2);
			this.recipeTask = this.scheduler.scheduleSyncRepeatingTask(this, new RecipeTask(this, this.language), 0, 20);
			this.autoSaveTask = this.scheduler.scheduleSyncRepeatingTask(this, new AutoSaveTask(this, this.console, this.regenerationData), 20 * 60 * 5, 20 * 60 * 5);
			this.registerEvents(new AdminListener(this.configuration), new PlayerListener(this, this.console, this.configuration, this.language, this.regenerationData, this.isRegenerating, this.regenerationTaskNumber));
			this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "rebornadmin update");
			if(devKey.exists() && devKey.isFile()) {
				this.console.development("Development key found!");
				try {
					final BufferedReader reader = Files.newBufferedReader(Paths.get(devKey.toURI()));
					final LinkedList<String> commandList = new LinkedList<String>();
					String command;
					while((command = reader.readLine()) != null) commandList.add(command);
					this.scheduler.runTask(this, new Runnable() {
						@Override
						public final void run() {
							for(final String cmd : commandList) TheDoctorReborn.this.getServer().dispatchCommand(TheDoctorReborn.this.getServer().getConsoleSender(), cmd);
						}
					});
				} catch (final IOException exception) {
					exception.printStackTrace();
				}
			}
		} else this.getServer().getPluginManager().disablePlugin(this);
	}
	/**
	 * 
	 * @deprecated Not for public use.
	 * 
	 */
	@Override
	public final void onDisable() {
		this.console.info("Unloading...");
		if(this.scheduler.isCurrentlyRunning(this.versionTask) || this.scheduler.isQueued(this.versionTask)) {
			this.scheduler.cancelTask(this.versionTask);
			this.console.info("Task #" + this.versionTask + " cancelled.");
		}
		if(this.scheduler.isCurrentlyRunning(this.regenerationTask) || this.scheduler.isQueued(this.regenerationTask)) {
			this.scheduler.cancelTask(this.regenerationTask);
			this.console.info("Task #" + this.regenerationTask + " cancelled.");
		}
		if(this.scheduler.isCurrentlyRunning(this.recipeTask) || this.scheduler.isQueued(this.recipeTask)) {
			this.scheduler.cancelTask(this.recipeTask);
			this.console.info("Task #" + this.recipeTask + " cancelled.");
		}
		if(this.scheduler.isCurrentlyRunning(this.autoSaveTask) || this.scheduler.isQueued(this.autoSaveTask)) {
			this.scheduler.cancelTask(this.autoSaveTask);
			this.console.info("Task #" + this.autoSaveTask + " cancelled.");
		}
		if(this.regenerationData != null) StorageModule.saveData(this, this.console, this.regenerationData, true);
	}
	/**
	 * 
	 * Method used to cast the API class.
	 * 
	 * @return The API class.
	 * 
	 */
	@SuppressWarnings("deprecation")
	public final TheDoctorRebornAPI getAPI() {
		return new TheDoctorRebornAPI(this.metrics);
	}
	/**
	 * 
	 * Method used to cast the main class.
	 * 
	 * @return The main class.
	 * 
	 */
	public static final TheDoctorReborn getInstance() {
		return instance;
	}
}