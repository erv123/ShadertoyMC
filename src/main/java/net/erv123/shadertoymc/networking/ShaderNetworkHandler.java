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

public class RegisterPackets {
	public static final Identifier SHADER_RUN_PACKET_ID = new Identifier("shadertoy","run_packet");
	public static void registerC2SPackets(){
		ServerPlayNetworking.registerGlobalReceiver(SHADER_RUN_PACKET_ID, ShaderRunC2SPacket::receive);
	}
}
