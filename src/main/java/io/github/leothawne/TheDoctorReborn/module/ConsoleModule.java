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
package io.github.leothawne.TheDoctorReborn.module;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;
import io.github.leothawne.TheDoctorReborn.type.VersionType;

public final class ConsoleModule {
	private TheDoctorReborn plugin;
	public ConsoleModule(final TheDoctorReborn plugin) {
		this.plugin = plugin;
	}
	private final ConsoleCommandSender getConsoleSender() {
		return this.plugin.getServer().getConsoleSender();
	}
	public final void Hello() {
		this.getConsoleSender().sendMessage(ChatColor.AQUA + " _______ _____  _____  ");
		this.getConsoleSender().sendMessage(ChatColor.AQUA + "|__   __|  __ \\|  __ \\ ");
		this.getConsoleSender().sendMessage(ChatColor.AQUA + "   | |  | |  | | |__) |" + ChatColor.WHITE + "  Plugin version: " + this.plugin.getDescription().getVersion() + " (API version: " + DataModule.getBukkitAPI() + ")");
		this.getConsoleSender().sendMessage(ChatColor.AQUA + "   | |  | |  | |  _  / " + ChatColor.WHITE + "  Java required: " + DataModule.getVersion(VersionType.JAVA));
		this.getConsoleSender().sendMessage(ChatColor.AQUA + "   | |  | |__| | | \\ \\ " + ChatColor.WHITE + "  Released on: " + DataModule.getVersionDate());
		this.getConsoleSender().sendMessage(ChatColor.AQUA + "   |_|  |_____/|_|  \\_\\");
	}
	public final void info(final String message) {
		this.getConsoleSender().sendMessage("[" + this.plugin.getDescription().getName() + "] " + ChatColor.GREEN + message);
	}
	public final void warning(final String message) {
		this.getConsoleSender().sendMessage("[" + this.plugin.getDescription().getName() + "] " + ChatColor.YELLOW + message);
	}
	public final void severe(final String message) {
		this.getConsoleSender().sendMessage("[" + this.plugin.getDescription().getName() + "] " + ChatColor.RED + message);
	}
	public final void development(final String message) {
		this.getConsoleSender().sendMessage("[" + this.plugin.getDescription().getName() + " " + ChatColor.DARK_PURPLE + "DEV" + ChatColor.RESET + "] " + ChatColor.LIGHT_PURPLE + message);
	}
}