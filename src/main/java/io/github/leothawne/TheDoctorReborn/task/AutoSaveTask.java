package io.github.leothawne.TheDoctorReborn.task;

import io.github.leothawne.TheDoctorReborn.module.StorageModule;

public final class AutoSaveTask implements Runnable {
	public AutoSaveTask() {}
	@Override
	public final void run() {
		StorageModule.saveData(false);
	}
}