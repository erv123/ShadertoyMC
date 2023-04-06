package net.erv123.shadertoymc;

import net.erv123.shadertoymc.commands.ClientCommands;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;


public class ShadertoyMCClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> ClientCommands.register(dispatcher));
	}
}
