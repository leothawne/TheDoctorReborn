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
package io.github.leothawne.TheDoctorReborn.api;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.util.StringUtil;

public final class TabCompleterAPI {
	private TabCompleterAPI() {}
	public static final ArrayList<String> partial(final String token, final Collection<String> from) {
		final ArrayList<String> strings = new ArrayList<String>();
		for(final Object object : StringUtil.copyPartialMatches(token, from, new ArrayList<>(from.size()))) {
			strings.add(object.toString());
		}
        return strings;
    }
}