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

public class SymbioticNucleiItem {
	public static final ItemStack getItemStack(FileConfiguration language) {
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
	public static final Recipe getRecipe(TheDoctorReborn plugin, FileConfiguration language) {
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
	public static final List<String> getLore(FileConfiguration language){
		return Arrays.asList(language.getString("symbiotic-nuclei-item"), "Symbiotic Nuclei");
	}
}