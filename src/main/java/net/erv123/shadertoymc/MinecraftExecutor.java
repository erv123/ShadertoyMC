package net.erv123.shadertoymc;


import me.senseiwells.arucas.api.ArucasExecutor;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public enum MinecraftExecutor implements ArucasExecutor {
    INSTANCE;
    public <T> @NotNull Future<T> submit(@NotNull Callable<T> callable) {
        MinecraftServer server = ShaderUtils.SERVER;
        return server.submit(() -> {
            try {
                return callable.call();
            } catch (Throwable throwable) {
                return throwUnchecked(throwable);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static <T extends Throwable, V> V throwUnchecked(Throwable throwable) throws T {
        throw (T) throwable;
    }
}