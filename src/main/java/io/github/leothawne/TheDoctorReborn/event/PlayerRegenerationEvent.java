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
package io.github.leothawne.TheDoctorReborn.event;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import io.github.leothawne.TheDoctorReborn.module.StorageModule;
import io.github.leothawne.TheDoctorReborn.type.DataType;

public class PlayerRegenerationEvent extends Event {
	private Player timelord;
	private FileConfiguration regenerationData;
	private boolean usingSymbioticNuclei;
	private double healthLevel;
	private int foodLevel;
	private int regenerationTaskNumber;
	private HandlerList handlers;
	public PlayerRegenerationEvent(final Player timelord, final FileConfiguration regenerationData, final boolean usingSymbioticNuclei, final double healthLevel, final int foodLevel, final int regenerationTaskNumber) {
		this.timelord = timelord;
		this.regenerationData = regenerationData;
		this.usingSymbioticNuclei = usingSymbioticNuclei;
		this.healthLevel = healthLevel;
		this.foodLevel = foodLevel;
		this.regenerationTaskNumber = regenerationTaskNumber;
		this.handlers = new HandlerList();
	}
	public final Player getTimelord() {
		return this.timelord;
	}
	public final int getRegenerationNumber() {
		return (int) StorageModule.getPlayer(this.regenerationData, this.timelord, DataType.REGENERATION_NUMBER);
	}
	public final int getRegenerationCycle() {
		return (int) StorageModule.getPlayer(this.regenerationData, this.timelord, DataType.REGENERATION_CYCLE);
	}
	public final boolean isUsingSymbioticNuclei() {
		return this.usingSymbioticNuclei;
	}
	public final double getHealthLevel() {
		return this.healthLevel;
	}
	public final int getFoodLevel() {
		return this.foodLevel;
	}
	public final int getRegenerationTaskId() {
		return this.regenerationTaskNumber;
	}
	@Override
	public final HandlerList getHandlers() {
		return this.handlers;
	}
	public final HandlerList getHandlerList() {
		return this.handlers;
	}
}