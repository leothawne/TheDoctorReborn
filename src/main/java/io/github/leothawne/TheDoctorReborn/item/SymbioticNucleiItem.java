package io.github.leothawne.TheDoctorReborn.item;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import io.github.leothawne.TheDoctorReborn.TheDoctorReborn;
import io.github.leothawne.TheDoctorReborn.item.model.Item;
import io.github.leothawne.TheDoctorReborn.item.model.ItemType;
import io.github.leothawne.TheDoctorReborn.module.NBTModule;

public final class SymbioticNucleiItem implements Item {
	@Override
	public final String getName() {
		return "Symbiotic Nuclei";
	}
	@Override
	public final List<String> getDescription(final String data, FileConfiguration language) {
		return Arrays.asList(language.getString("symbiotic-nuclei-item"), "", "Symbiotic Nuclei");
	}
	@Override
	public final ItemType getType() {
		return ItemType.SYMBIOTIC_NUCLEI;
	}
	@Override
	public final Material getMaterial() {
		return Material.POTION;
	}
	@Override
	public final String[] getData() {
		final String[] data = {};
		return data;
	}
	@Override
	public final LinkedList<Listener> getListeners(){
		return null;
	}
	@Override
	public final void runTasks() {}
	@Override
	public final LinkedList<BukkitTask> getTasks(){
		return null;
	}
	@Override
	public final ItemStack getItem(final String data, FileConfiguration language) {
		ItemStack item = new ItemStack(getMaterial());
		item = NBTModule.setTheDoctorRebornItemTag(item, getType());
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(getName());
		meta.setLore(getDescription(data, language));
		//meta.addEnchant(new GlowManager(), 1, true);
		item.setItemMeta(meta);
		return item;
	}
	@Override
	public final Recipe getRecipe(final TheDoctorReborn plugin, final FileConfiguration language) {
		final ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, getType().toString().toLowerCase()), getItem(null, language));
		recipe.shape("RNR", "DPD", "ELE");
		recipe.setIngredient('R', Material.REDSTONE);
		recipe.setIngredient('N', Material.NETHER_STAR);
		recipe.setIngredient('D', Material.DIAMOND);
		recipe.setIngredient('P', Material.POTION);
		recipe.setIngredient('E', Material.EMERALD);
		recipe.setIngredient('L', Material.GLOWSTONE_DUST);
		return recipe;
	}
}