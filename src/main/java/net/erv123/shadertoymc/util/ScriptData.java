package net.erv123.shadertoymc.util;

import net.minecraft.entity.boss.CommandBossBar;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public record ScriptData(
	ServerPlayerEntity player,
	MinecraftServer server,
	CommandBossBar bossBar
) {

}
