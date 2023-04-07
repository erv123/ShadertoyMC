package net.erv123.shadertoymc.networking;

import net.erv123.shadertoymc.util.ScriptUtils;
import net.erv123.shadertoymc.util.StringCompressor;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ShaderNetworkHandler {
	public static final int VERSION = 1;
	public static final Identifier SHADER_RUN_PACKET_CHANNEL = new Identifier("shadertoy", "run_packet");
	public static final Identifier SHADER_STOP_PACKET_CHANNEL = new Identifier("shadertoy", "stop_packet");

	static {
		ServerPlayNetworking.registerGlobalReceiver(SHADER_RUN_PACKET_CHANNEL, ShaderNetworkHandler::receiveRunPacket);
		ServerPlayNetworking.registerGlobalReceiver(SHADER_STOP_PACKET_CHANNEL, ShaderNetworkHandler::receiveStopPacket);
	}

	public static void receiveRunPacket(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
										PacketByteBuf buf, PacketSender responseSender) {
		int version = buf.readInt();
		if (version == VERSION) {
			String name = buf.readString();
			String shaderFile = StringCompressor.decompress(buf.readByteArray());
			//player.sendMessage(Text.literal("Version: "+version));
			//player.sendMessage(Text.literal("Name: "+name));
			//player.sendMessage(Text.literal("Script: "+shaderFile));

			player.sendMessage(Text.literal("Shader %s started!".formatted(name)));

			ScriptUtils.executeScript(shaderFile, name, player.getCommandSource());
		} else {
			player.sendMessage(Text.literal("Incompatible packet version, please update mod!"));
		}
	}

	public static void receiveStopPacket(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
										 PacketByteBuf buf, PacketSender responseSender) {
		int version = buf.readInt();
		if (version == VERSION) {
			int type = buf.readInt();
			switch (type) {
				case 0: {
					String name = buf.readString();
					ScriptUtils.stopScript(player, name);
					player.sendMessage(Text.literal("Stopped script: " + name));
				}
				case 1: {
					ScriptUtils.stopScripts(player);
					player.sendMessage(Text.literal("Stopped all my scripts"));
				}
				case 2: {
					ScriptUtils.stopAllScripts();
					player.sendMessage(Text.literal("Stopped all scripts for everyone"));
				}
				default: {
					player.sendMessage(Text.literal("Wrong stop packet type, submit bug report!"));
				}
			}
		} else {
			player.sendMessage(Text.literal("Incompatible packet version, please update mod!"));
		}
	}

	public static void noop() {

	}
}
