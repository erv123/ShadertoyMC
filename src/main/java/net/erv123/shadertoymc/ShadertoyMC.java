package net.erv123.shadertoymc;

import net.erv123.shadertoymc.commands.ShadertoyCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * TODO:
 * Make noise in Java
 * Make Area in Java
 */
public class ShadertoyMC implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("Shaydertoy");
	public static final String VERSION = "1.0.0";
	public static MinecraftServer SERVER;

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> ShadertoyCommand.register(dispatcher));
	}
}
