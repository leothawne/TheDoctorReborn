package io.github.leothawne.TheDoctorReborn.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerRegenerationEvent extends Event {
	private static Player player;
	private static int regenerationNumber;
	private static int regenerationCycle;
	private static boolean usingSymbioticNuclei;
	private static double healthLevel;
	private static int foodLevel;
	private static int regenerationTaskNumber;
	private static final HandlerList handlers = new HandlerList();
	public PlayerRegenerationEvent(Player player, int regenerationNumber, int regenerationCycle, boolean usingSymbioticNuclei, double healthLevel, int foodLevel, int regenerationTaskNumber) {
		PlayerRegenerationEvent.player = player;
		PlayerRegenerationEvent.regenerationNumber = regenerationNumber;
		PlayerRegenerationEvent.regenerationCycle = regenerationCycle;
		PlayerRegenerationEvent.usingSymbioticNuclei = usingSymbioticNuclei;
		PlayerRegenerationEvent.healthLevel = healthLevel;
		PlayerRegenerationEvent.foodLevel = foodLevel;
		PlayerRegenerationEvent.regenerationTaskNumber = regenerationTaskNumber;
	}
	public final Player getPlayer() {
		return PlayerRegenerationEvent.player;
	}
	public final int getRegenerationNumber() {
		return PlayerRegenerationEvent.regenerationNumber;
	}
	public final int getRegenerationCycle() {
		return PlayerRegenerationEvent.regenerationCycle;
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