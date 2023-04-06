package net.erv123.shadertoymc.networking.packets;

import net.erv123.shadertoymc.util.ScriptUtils;
import net.erv123.shadertoymc.util.StringCompressor;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ShaderRunC2SPacket {
	public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
							   PacketByteBuf buf, PacketSender responseSender){
		player.sendMessage(Text.literal("Packet received"));
		int version = buf.readInt();
		String name = buf.readString();
		String shaderFile = StringCompressor.decompress(buf.readByteArray());
		//player.sendMessage(Text.literal("Version: "+version));
		//player.sendMessage(Text.literal("Name: "+name));
		//player.sendMessage(Text.literal("Script: "+shaderFile));
		ScriptUtils.executeScript(shaderFile,name,player, server);
	}
}