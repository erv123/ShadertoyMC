package net.erv123.shadertoymc.util;

import net.minecraft.entity.boss.CommandBossBar;
import net.minecraft.server.command.ServerCommandSource;

public record ScriptData(ServerCommandSource source, CommandBossBar bossBar) {

}
