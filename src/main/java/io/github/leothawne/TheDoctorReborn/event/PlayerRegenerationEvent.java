package io.github.leothawne.TheDoctorReborn.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import io.github.leothawne.TheDoctorReborn.module.StorageModule;
import io.github.leothawne.TheDoctorReborn.type.DataType;

public class PlayerRegenerationEvent extends Event {
	private Player timelord;
	private boolean usingSymbioticNuclei;
	private double healthLevel;
	private int foodLevel;
	private int regenerationTaskNumber;
	private HandlerList handlers;
	public PlayerRegenerationEvent(final Player timelord, final boolean usingSymbioticNuclei, final double healthLevel, final int foodLevel, final int regenerationTaskNumber) {
		this.timelord = timelord;
		this.usingSymbioticNuclei = usingSymbioticNuclei;
		this.healthLevel = healthLevel;
		this.foodLevel = foodLevel;
		this.regenerationTaskNumber = regenerationTaskNumber;
		this.handlers = new HandlerList();
	}
	public final Player getTimelord() {
		return timelord;
	}
	public final int getRegenerationNumber() {
		return (int) StorageModule.getPlayer(timelord, DataType.REGENERATION_NUMBER);
	}
	public final int getRegenerationCycle() {
		return (int) StorageModule.getPlayer(timelord, DataType.REGENERATION_CYCLE);
	}
	public final boolean isUsingSymbioticNuclei() {
		return usingSymbioticNuclei;
	}
	public final double getHealthLevel() {
		return healthLevel;
	}
	public final int getFoodLevel() {
		return foodLevel;
	}
	public final int getRegenerationTaskId() {
		return regenerationTaskNumber;
	}
	@Override
	public final HandlerList getHandlers() {
		return handlers;
	}
	public final HandlerList getHandlerList() {
		return handlers;
	}
}