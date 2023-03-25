package net.erv123.shadertoymc;

import net.erv123.shadertoymc.networking.ShaderPackets;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShadertoyMC implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("Shadertoy");
    public static final String VERSION = "1.0.0";
    public static MinecraftServer SERVER;

    @Override
    public void onInitialize() {
        ShaderPackets.registerC2SPackets();
    }
}

