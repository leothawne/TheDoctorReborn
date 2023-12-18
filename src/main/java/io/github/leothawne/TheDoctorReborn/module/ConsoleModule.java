package io.github.leothawne.TheDoctorReborn.module;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;
import io.github.leothawne.TheDoctorReborn.type.VersionType;

public final class ConsoleModule {
	public ConsoleModule() {}
	private final ConsoleCommandSender getConsoleSender() {
		return Bukkit.getConsoleSender();
	}
	public final void Hello() {
		this.getConsoleSender().sendMessage(ChatColor.AQUA + " _______ _____  _____  ");
		this.getConsoleSender().sendMessage(ChatColor.AQUA + "|__   __|  __ \\|  __ \\ ");
		this.getConsoleSender().sendMessage(ChatColor.AQUA + "   | |  | |  | | |__) |" + ChatColor.WHITE + "  Plugin version: " + TheDoctorReborn.getInstance().getDescription().getVersion() + " (API version: " + DataModule.getBukkitAPI() + ")");
		this.getConsoleSender().sendMessage(ChatColor.AQUA + "   | |  | |  | |  _  / " + ChatColor.WHITE + "  Java required: " + DataModule.getVersion(VersionType.JAVA));
		this.getConsoleSender().sendMessage(ChatColor.AQUA + "   | |  | |__| | | \\ \\ " + ChatColor.WHITE + "  Released on: " + DataModule.getVersionDate());
		this.getConsoleSender().sendMessage(ChatColor.AQUA + "   |_|  |_____/|_|  \\_\\");
	}
	public final void info(final String message) {
		this.getConsoleSender().sendMessage("[" + TheDoctorReborn.getInstance().getDescription().getName() + "] " + ChatColor.GREEN + message);
	}
	public final void warning(final String message) {
		this.getConsoleSender().sendMessage("[" + TheDoctorReborn.getInstance().getDescription().getName() + "] " + ChatColor.YELLOW + message);
	}
	public final void severe(final String message) {
		this.getConsoleSender().sendMessage("[" + TheDoctorReborn.getInstance().getDescription().getName() + "] " + ChatColor.RED + message);
	}
	public final void development(final String message) {
		this.getConsoleSender().sendMessage("[" + TheDoctorReborn.getInstance().getDescription().getName() + " " + ChatColor.DARK_PURPLE + "DEV" + ChatColor.RESET + "] " + ChatColor.LIGHT_PURPLE + message);
	}
}