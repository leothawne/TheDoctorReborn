package io.github.leothawne.TheDoctorReborn.module;

import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import io.github.leothawne.TheDoctorReborn.item.model.Item;
import io.github.leothawne.TheDoctorReborn.item.model.ItemType;
import net.minecraft.nbt.NBTTagCompound;

public final class NBTModule {
	private NBTModule() {}
	public static final ItemStack setItemTag(ItemStack item, final String name, final String tag) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		final NBTTagCompound nbt = new NBTTagCompound();
		nbt.a(name, tag);
		nmsItem.c(nbt);
		item = CraftItemStack.asBukkitCopy(nmsItem);
		return item;
	}
	public static final ItemStack setTheDoctorRebornItemTag(final ItemStack item, final ItemType type) {
		return setItemTag(item, "TDRType", type.name());
	}
	public static final String getItemTag(final ItemStack item, final String name) {
		final net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		final NBTTagCompound nbt = nmsItem.v();
		if(nbt != null) if(nbt.e(name)) return nbt.l(name);
		return null;
	}
	public static final String getTheDoctorRebornItemTag(final ItemStack item) {
		return getItemTag(item, "TDRType");
	}
	public static final boolean isTheDoctorRebornItem(final Item item, final ItemStack i) {
		return item.getType().name().equals(getTheDoctorRebornItemTag(i));
	}
}