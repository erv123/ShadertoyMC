package net.erv123.shadertoymc.util;

import java.util.function.BooleanSupplier;

public interface MinecraftServerTicker {
	void forceTick(BooleanSupplier bool);
}
