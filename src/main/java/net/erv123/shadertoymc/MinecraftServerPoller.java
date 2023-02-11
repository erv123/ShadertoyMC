package net.erv123.shadertoymc;

import me.senseiwells.arucas.api.ArucasPoller;
import me.senseiwells.arucas.core.Interpreter;
import org.jetbrains.annotations.NotNull;

public enum MinecraftServerPoller implements ArucasPoller {
    INSTANCE;

    private static final long TICK_TIME =  100_000_000;

    private long lastPoll = 0;

    @Override
    public boolean poll(@NotNull Interpreter interpreter) {
        if(this.lastPoll + TICK_TIME < System.nanoTime()) {
            ((MinecraftServerTicker) ShaderUtils.SERVER).forceTick(() -> System.nanoTime() - lastPoll < 50000000L);
            this.lastPoll = System.nanoTime();
        }
        return true;
    }
}
