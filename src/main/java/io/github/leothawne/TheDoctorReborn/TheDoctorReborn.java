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
	private final void registerEvents(final Listener...listeners) {
		for(final Listener listener : listeners) Bukkit.getPluginManager().registerEvents(listener, this);
	}
	private final ConsoleModule console = new ConsoleModule();
	private FileConfiguration configuration = null;
	private FileConfiguration language = null;
	private MetricsAPI metrics = null;
	private BukkitScheduler scheduler = null;
	private FileConfiguration regenerationData = null;
	private HashMap<UUID, Boolean> isRegenerating = new HashMap<>();
	private HashMap<UUID, Integer> regenerationTaskNumber = new HashMap<>();
	private int versionTask = 0;
	private int regenerationTask = 0;
	private int recipeTask = 0;
	private int autoSaveTask = 0;
	@Override
	public final void onLoad() {
		instance = this;
		console.info(Bukkit.getName() + " version " + Bukkit.getVersion() + " is loading " + getDescription().getName() + " v" + getDescription().getVersion() + "...");
	}

	@Override
	public final void onEnable() {
		console.Hello();
		console.info("Loading...");
		ConfigurationModule.preLoad();
		final File devKey = new File(getDataFolder(), "devMode.key");
		if(!(devKey.exists() && devKey.isFile())) if(ConfigurationModule.isItOutdated()) {
			console.warning("Updating config.yml with a newer version...");
			if(ConfigurationModule.makeOldFile()) {
				ConfigurationModule.preLoad();
			} else {
				console.severe("Could not rename the old config.yml file. Do this manually and restart the server.");
				Bukkit.getPluginManager().disablePlugin(this);
				return;
			}
		}
		configuration = ConfigurationModule.load();
		if(configuration.getBoolean("enable-plugin")) {
			metrics = MetricsModule.init();
			LanguageModule.preLoad();
			if(!(devKey.exists() && devKey.isFile())) if(LanguageModule.isItOutdated()) {
				console.warning("Updating " + configuration.getString("language") + ".yml with a newer version...");
				if(LanguageModule.deleteFile()) {
					LanguageModule.preLoad();
				} else {
					console.severe("Could not delete the " + configuration.getString("language") + ".yml file. Do this manually and restart the server.");
					Bukkit.getPluginManager().disablePlugin(this);
					return;
				}
			}
			language = LanguageModule.load();
			StorageModule.preLoad();
			regenerationData = StorageModule.load();
			for(final Player player : Bukkit.getOnlinePlayers()) {
				StorageModule.checkPlayer(player);
				isRegenerating.put(player.getUniqueId(), false);
			}
			getCommand("reborn").setExecutor(new RebornCommand());
			getCommand("reborn").setTabCompleter(new RebornCommandTabCompleter());
			getCommand("rebornadmin").setExecutor(new RebornAdminCommand());
			getCommand("rebornadmin").setTabCompleter(new RebornAdminCommandTabCompleter());
			scheduler = Bukkit.getScheduler();
			versionTask = scheduler.scheduleSyncRepeatingTask(this, new VersionTask(), 0, 20 * 60 * 60);
			regenerationTask = scheduler.scheduleSyncRepeatingTask(this, new RegenerationTask(), 0, 2);
			recipeTask = scheduler.scheduleSyncRepeatingTask(this, new RecipeTask(), 0, 20);
			autoSaveTask = scheduler.scheduleSyncRepeatingTask(this, new AutoSaveTask(), 20 * 60 * 5, 20 * 60 * 5);
			registerEvents(new AdminListener(), new PlayerListener());
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rebornadmin update");
			if(devKey.exists() && devKey.isFile()) {
				console.development("Development key found!");
				try {
					final BufferedReader reader = Files.newBufferedReader(Paths.get(devKey.toURI()));
					final LinkedList<String> commandList = new LinkedList<>();
					String command = null;
					while((command = reader.readLine()) != null) commandList.add(command);
					scheduler.runTask(this, new Runnable() {
						@Override
						public final void run() {
							for(final String cmd : commandList) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
						}
					});
				} catch (final IOException exception) {
					exception.printStackTrace();
				}
			}
		} else Bukkit.getPluginManager().disablePlugin(this);
	}
	@Override
	public final void onDisable() {
		console.info("Unloading...");
		if(scheduler.isCurrentlyRunning(versionTask) || scheduler.isQueued(versionTask)) {
			scheduler.cancelTask(versionTask);
			console.info("Task #" + versionTask + " cancelled.");
		}
		if(scheduler.isCurrentlyRunning(regenerationTask) || scheduler.isQueued(regenerationTask)) {
			scheduler.cancelTask(regenerationTask);
			console.info("Task #" + regenerationTask + " cancelled.");
		}
		if(scheduler.isCurrentlyRunning(recipeTask) || scheduler.isQueued(recipeTask)) {
			scheduler.cancelTask(recipeTask);
			console.info("Task #" + recipeTask + " cancelled.");
		}
		if(scheduler.isCurrentlyRunning(autoSaveTask) || scheduler.isQueued(autoSaveTask)) {
			scheduler.cancelTask(autoSaveTask);
			console.info("Task #" + autoSaveTask + " cancelled.");
		}
		if(regenerationData != null) StorageModule.saveData(true);
	}
	/**
	 * 
	 * Method used to cast the API class.
	 * 
	 * @return The API class.
	 * 
	 */
	public static final TheDoctorRebornAPI getAPI() {
		return new TheDoctorRebornAPI();
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
	public final MetricsAPI getMetrics() {
		return metrics;
	}
	public final ConsoleModule getConsole() {
		return console;
	}
	public final FileConfiguration getConfiguration() {
		return configuration;
	}
	public final FileConfiguration getLanguage() {
		return language;
	}
	public final FileConfiguration getRegenData() {
		return regenerationData;
	}
	public final HashMap<UUID, Boolean> getPlayersRegen(){
		return isRegenerating;
	}
	public final HashMap<UUID, Integer> getRegenTasks(){
		return regenerationTaskNumber;
	}
}