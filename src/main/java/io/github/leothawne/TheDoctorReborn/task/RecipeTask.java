package io.github.leothawne.TheDoctorReborn.task;

import org.bukkit.Bukkit;

import io.github.leothawne.TheDoctorReborn.item.SymbioticNucleiItem;

public final class RecipeTask implements Runnable {
	public RecipeTask() {}
	@Override
	public final void run() {
		try {
			Bukkit.addRecipe(new SymbioticNucleiItem().getRecipe());
		} catch(IllegalStateException exception) {}
	}
}