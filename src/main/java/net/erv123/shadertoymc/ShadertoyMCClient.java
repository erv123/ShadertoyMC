package net.erv123.shadertoymc;

import net.erv123.shadertoymc.commands.ShadertoyCommand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ShadertoyMCClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> ShadertoyCommand.register(dispatcher));
	}
}
