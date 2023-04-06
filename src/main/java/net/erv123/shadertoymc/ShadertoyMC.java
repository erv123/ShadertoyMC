package net.erv123.shadertoymc;

import net.erv123.shadertoymc.networking.RegisterPackets;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShadertoyMC implements DedicatedServerModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("Shadertoy");
    public static final String VERSION = "1.0.0";
    public static MinecraftServer SERVER;

    @Override
    public void onInitializeServer() {
        RegisterPackets.registerC2SPackets();
    }
}

