package net.erv123.shadertoymc.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.erv123.shadertoymc.util.ScriptUtils;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;

public class ServerAdminCommands {
	ServerAdminCommands(){

	}
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal("shaderadmin")
			.requires(source -> source.hasPermissionLevel(3))
			.then(literal("stopAll"))
				.executes(context -> {
					ScriptUtils.stopAllScripts();
					return 1;
				})
		);
	}
}
