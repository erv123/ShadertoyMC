package net.erv123.shadertoymc.networking;

import net.erv123.shadertoymc.networking.packets.ShaderRunC2SPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class RegisterPackets {
	public static final Identifier SHADER_RUN_PACKET_ID = new Identifier("shadertoy","run_packet");
	public static void registerC2SPackets(){
		ServerPlayNetworking.registerGlobalReceiver(SHADER_RUN_PACKET_ID, ShaderRunC2SPacket::receive);
	}
}
