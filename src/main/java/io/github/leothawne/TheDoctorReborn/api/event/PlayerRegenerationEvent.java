package io.github.leothawne.TheDoctorReborn.api.event;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import io.github.leothawne.TheDoctorReborn.PlayerDataLoader;
import io.github.leothawne.TheDoctorReborn.type.DataSectionType;

public class PlayerRegenerationEvent extends Event {
	private static Player player;
	private static FileConfiguration regenerationData;
	private static boolean usingSymbioticNuclei;
	private static double healthLevel;
	private static int foodLevel;
	private static int regenerationTaskNumber;
	private static final HandlerList handlers = new HandlerList();
	public PlayerRegenerationEvent(Player player, FileConfiguration regenerationData, boolean usingSymbioticNuclei, double healthLevel, int foodLevel, int regenerationTaskNumber) {
		PlayerRegenerationEvent.player = player;
		PlayerRegenerationEvent.regenerationData = regenerationData;
		PlayerRegenerationEvent.usingSymbioticNuclei = usingSymbioticNuclei;
		PlayerRegenerationEvent.healthLevel = healthLevel;
		PlayerRegenerationEvent.foodLevel = foodLevel;
		PlayerRegenerationEvent.regenerationTaskNumber = regenerationTaskNumber;
	}
	public final Player getPlayer() {
		return PlayerRegenerationEvent.player;
	}
	public final int getRegenerationNumber() {
		return (int) PlayerDataLoader.getPlayer(regenerationData, player, DataSectionType.REGENERATION_NUMBER);
	}
	public final int getRegenerationCycle() {
		return (int) PlayerDataLoader.getPlayer(regenerationData, player, DataSectionType.REGENERATION_CYCLE);
	}
	public final boolean isUsingSymbioticNuclei() {
		return PlayerRegenerationEvent.usingSymbioticNuclei;
	}
	public final double getHealthLevel() {
		return PlayerRegenerationEvent.healthLevel;
	}
	public final int getFoodLevel() {
		return PlayerRegenerationEvent.foodLevel;
	}
	public final int getRegenerationTaskId() {
		return PlayerRegenerationEvent.regenerationTaskNumber;
	}
	@Override
	public final HandlerList getHandlers() {
		return PlayerRegenerationEvent.handlers;
	}
	public final HandlerList getHandlerList() {
		return PlayerRegenerationEvent.handlers;
	}
}