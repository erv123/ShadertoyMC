package net.erv123.shadertoymc.networking;

import net.erv123.shadertoymc.networking.packets.ShaderRunC2SPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class ShaderPackets {
	public static final Identifier SHADER_PACKET_ID = new Identifier("Shadertoy","shader_packet");
	public static void registerC2SPackets(){
		ServerPlayNetworking.registerGlobalReceiver(SHADER_PACKET_ID, ShaderRunC2SPacket::receive);
	}
}
