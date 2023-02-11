package net.erv123.shadertoymc.arucas.impl;

import me.senseiwells.arucas.api.ArucasPoller;
import me.senseiwells.arucas.core.Interpreter;
import net.erv123.shadertoymc.util.ShaderUtils;
import net.erv123.shadertoymc.util.MinecraftServerTicker;
import org.jetbrains.annotations.NotNull;

public enum MinecraftServerPoller implements ArucasPoller {
	INSTANCE;

	private static final long TICK_TIME_MS = 100;
	private static final long TICK_TIME_NS = TICK_TIME_MS * 1_000_000;

	private long lastPoll = 0;

	@Override
	public boolean poll(@NotNull Interpreter interpreter) {
		if (ShaderUtils.running) {
			if (this.lastPoll + TICK_TIME_NS < System.nanoTime()) {
				((MinecraftServerTicker) ShaderUtils.SERVER).forceTick(() -> System.nanoTime() - this.lastPoll < 50000000L);
				this.lastPoll = System.nanoTime();
			}
			return true;
		}
		return false;
	}
}
