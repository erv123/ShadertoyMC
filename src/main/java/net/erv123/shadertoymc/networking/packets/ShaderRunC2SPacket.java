package net.erv123.shadertoymc.networking.packets;

import net.erv123.shadertoymc.util.ScriptUtils;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class ShaderRunC2SPacket {
	public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
							   PacketByteBuf buf, PacketSender responseSender){
		String shaderFile = buf.readString();
		ScriptUtils.executeScript(shaderFile,"tempName",player, server);
	}
}
