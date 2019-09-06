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
package io.github.leothawne.TheDoctorReborn.command.tabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;
import io.github.leothawne.TheDoctorReborn.api.TabCompleterAPI;

public final class RebornAdminCommandTabCompleter implements TabCompleter {
	private TheDoctorReborn plugin;
	public RebornAdminCommandTabCompleter(final TheDoctorReborn plugin) {
		this.plugin = plugin;
	}
	@Override
	public final List<String> onTabComplete(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args){
		if(sender.hasPermission("TheDoctorReborn.admin") || sender.isOp()) if(args.length == 1) {
			return TabCompleterAPI.partial(args[0], new LinkedList<String>(Arrays.asList("update", "info")));
		} else if(args.length == 2 && args[0].equalsIgnoreCase("info")) {
			final LinkedList<String> players = new LinkedList<String>();
			for(final Player player : this.plugin.getServer().getOnlinePlayers()) players.add(player.getName());
			return TabCompleterAPI.partial(args[1], players);
		}
		return new ArrayList<>();
	}
}