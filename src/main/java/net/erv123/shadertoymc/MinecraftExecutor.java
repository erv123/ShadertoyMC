package net.erv123.shadertoymc;


import me.senseiwells.arucas.utils.ArucasExecutor;
import net.minecraft.server.MinecraftServer;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

class MinecraftExecutor implements ArucasExecutor {
    public <T> Future<T> submit(Callable<T> callable) {
        MinecraftServer server = ShaderUtils.server;
        server.submit(() -> {
            try {
                return callable.call();
            } catch (Throwable throwable) {
                throwUnchecked(throwable);
            }
            return null;
        });
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Throwable, V> V throwUnchecked(Throwable throwable) throws T {
        throw (T) throwable;
    }
}