package io.github.leothawne.thedoctorreborn;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

public class ConsoleLoader {
	private TheDoctorRebornLoader plugin;
	public ConsoleLoader(TheDoctorRebornLoader plugin) {
		this.plugin = plugin;
	}
	private final ConsoleCommandSender newLogger() {
		return plugin.getServer().getConsoleSender();
	}
	private final String Plugin_Version = Version.getVersionNumber();
	private final String Plugin_Date = Version.getVersionDate();
	private final String Minecraft_Version = Version.getMinecraftVersion();
	private final String Java_Version = Version.getJavaVersion();
	public final void Hello() {
		newLogger().sendMessage(ChatColor.AQUA + "  _______ _            _____             _               _____      _                      ");
		newLogger().sendMessage(ChatColor.AQUA + " |__   __| |          |  __ \\           | |             |  __ \\    | |                     ");
		newLogger().sendMessage(ChatColor.AQUA + "    | |  | |__   ___  | |  | | ___   ___| |_ ___  _ __  | |__) |___| |__   ___  _ __ _ __  " + ChatColor.LIGHT_PURPLE + "  V " + Plugin_Version + " (Minecraft " + Minecraft_Version + ")");
		newLogger().sendMessage(ChatColor.AQUA + "    | |  | '_ \\ / _ \\ | |  | |/ _ \\ / __| __/ _ \\| '__| |  _  // _ | '_ \\ / _ \\| '__| '_ \\ " + ChatColor.LIGHT_PURPLE + "  Works with Java " + Java_Version);
		newLogger().sendMessage(ChatColor.AQUA + "    | |  | | | |  __/ | |__| | (_) | (__| || (_) | |    | | \\ |  __| |_) | (_) | |  | | | |" + ChatColor.LIGHT_PURPLE + "  Released on " + Plugin_Date);
		newLogger().sendMessage(ChatColor.AQUA + "    |_|  |_| |_|\\___| |_____/ \\___/ \\___|\\__\\___/|_|    |_|  \\_\\___|_.__/ \\___/|_|  |_| |_|" + ChatColor.LIGHT_PURPLE + "  Twitter @leonappi_");
		newLogger().sendMessage("");
	}
	public final void Goodbye() {
		newLogger().sendMessage(ChatColor.AQUA + "   _____                 _ _                _  ");
		newLogger().sendMessage(ChatColor.AQUA + "  / ____|               | | |              | | ");
		newLogger().sendMessage(ChatColor.AQUA + " | |  __  ___   ___   __| | |__  _   _  ___| | ");
		newLogger().sendMessage(ChatColor.AQUA + " | | |_ |/ _ \\ / _ \\ / _` | '_ \\| | | |/ _ | | ");
		newLogger().sendMessage(ChatColor.AQUA + " | |__| | (_) | (_) | (_| | |_) | |_| |  __|_| ");
		newLogger().sendMessage(ChatColor.AQUA + "  \\_____|\\___/ \\___/ \\__,_|_.__/ \\__, |\\___(_) ");
		newLogger().sendMessage(ChatColor.AQUA + "                                  __/ |        ");
		newLogger().sendMessage(ChatColor.AQUA + "                                 |___/         ");
		newLogger().sendMessage("");
	}
	public final void info(String message) {
		newLogger().sendMessage(ChatColor.AQUA + "[The Doctor Reborn " + ChatColor.WHITE + "INFO" + ChatColor.AQUA + "] " + ChatColor.LIGHT_PURPLE + "" + message);
	}
	public final void warning(String message) {
		newLogger().sendMessage(ChatColor.AQUA + "[The Doctor Reborn " + ChatColor.YELLOW + "WARNING" + ChatColor.AQUA + "] " + ChatColor.LIGHT_PURPLE + "" + message);
	}
	public final void severe(String message) {
		newLogger().sendMessage(ChatColor.AQUA + "[The Doctor Reborn " + ChatColor.RED + "ERROR" + ChatColor.AQUA + "] " + ChatColor.LIGHT_PURPLE + "" + message);
	}
}
