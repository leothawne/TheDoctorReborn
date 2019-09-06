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
package io.github.leothawne.TheDoctorReborn.item;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;

public final class SymbioticNucleiItem {
	private SymbioticNucleiItem() {}
	public static final ItemStack getItemStack(final FileConfiguration language) {
		ItemStack item = new ItemStack(getMaterial());
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(getName());
		meta.setLore(getLore(language));
		item.setItemMeta(meta);
		item.setAmount(1);
		return item;
	}
	public static final Material getMaterial() {
		return Material.POTION;
	}
	public static final String getRecipeId() {
		return "symbiotic_nuclei";
	}
	public static final Recipe getRecipe(final TheDoctorReborn plugin, final FileConfiguration language) {
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, getRecipeId()), getItemStack(language));
		recipe.shape("RNR", "DGD", "ELE");
		recipe.setIngredient('R', Material.REDSTONE);
		recipe.setIngredient('N', Material.NETHER_STAR);
		recipe.setIngredient('D', Material.DIAMOND);
		recipe.setIngredient('G', Material.GLASS_BOTTLE);
		recipe.setIngredient('E', Material.EMERALD);
		recipe.setIngredient('L', Material.GLOWSTONE_DUST);
		return recipe;
	}
	public static final String getName() {
		return "Symbiotic Nuclei";
	}
	public static final List<String> getLore(final FileConfiguration language){
		return Arrays.asList(language.getString("symbiotic-nuclei-item"), "Symbiotic Nuclei");
	}
}