package net.erv123.shadertoymc.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import io.netty.buffer.Unpooled;
import me.senseiwells.arucas.utils.FileUtils;
import net.erv123.shadertoymc.networking.RegisterPackets;
import net.erv123.shadertoymc.util.ScriptUtils;
import net.erv123.shadertoymc.util.ShaderUtils;
import net.erv123.shadertoymc.util.StringCompressor;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.command.CommandSource;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class ClientCommands {
	private static final DynamicCommandExceptionType FAILED_TO_READ_SCRIPT;
	static {
		FAILED_TO_READ_SCRIPT = new DynamicCommandExceptionType(o -> Text.literal("Failed to read script: " + o));
	}
	private ClientCommands() {

	}

	public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
		dispatcher.register(literal("shadertoy")
			.then(literal("run")
				.then(argument("shader_file", StringArgumentType.string())
					.suggests((context, builder) -> CommandSource.suggestMatching(ScriptUtils.SCRIPTS, builder))
					.executes(context -> {
						String shaderFile = StringArgumentType.getString(context, "shader_file");
						Path scriptPath = ShaderUtils.SHADERTOY_PATH.resolve(shaderFile);
						String fileContent;
						try {
							fileContent = Files.readString(scriptPath);
						} catch (IOException e) {
							throw FAILED_TO_READ_SCRIPT.create(shaderFile);
						}
						PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
						buf.writeInt(ShaderNetworkHandler.VERSION);
						buf.writeString(shaderFile);
						byte[] compressedString = StringCompressor.compress(fileContent);
						buf.writeByteArray(compressedString);
						ClientPlayNetworking.send(RegisterPackets.SHADER_RUN_PACKET_ID, buf);
						context.getSource().getPlayer().sendMessage(Text.literal("Sending data to server"));
						return 1;
					})
				)
			)
			.then(literal("new")
				.then(argument("shader_file", StringArgumentType.string())
					.executes(context -> {
						String shaderFile = StringArgumentType.getString(context, "shader_file");
						if (!shaderFile.endsWith(".arucas")) {
							shaderFile += ".arucas";
						}
						Path shaderPath = ShaderUtils.SHADERTOY_PATH.resolve(shaderFile);
						try {
							FileUtils.ensureParentExists(shaderPath);
							Files.writeString(shaderPath, ScriptUtils.EXAMPLE_SCRIPT);
						} catch (IOException exception) {
							context.getSource().sendFeedback(Text.literal("Failed to write to: " + shaderPath));
							return 0;
						}
						context.getSource().getPlayer().sendMessage(Text.literal("New shader file §n" + shaderFile + "§r created")
							.styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, shaderPath.toString()))));
						return 1;
					})
				)
			)
			.then(literal("open")
				.then(argument("shader_file", StringArgumentType.string())
					.suggests((context, builder) -> CommandSource.suggestMatching(ScriptUtils.SCRIPTS, builder))
					.executes(context -> {
						String shaderFile = StringArgumentType.getString(context, "shader_file");
						File shader = new File(ShaderUtils.SHADERTOY_PATH.resolve(shaderFile).toUri());
						Util.getOperatingSystem().open(shader);
						return 1;
					})
				)
			)
			.then(literal("stop")
				.executes(context -> {
					PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
					buf.writeInt(ShaderNetworkHandler.VERSION);
					buf.writeInt(1);
					ClientPlayNetworking.send(ShaderNetworkHandler.SHADER_STOP_PACKET_CHANNEL, buf);
					return 1;
				})
				.then(argument("name", StringArgumentType.string())
					.suggests((context, builder) -> CommandSource.suggestMatching(ScriptUtils.SCRIPTS, builder))
					.executes(context -> {
						String name = StringArgumentType.getString(context, "name");
						PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
						buf.writeInt(ShaderNetworkHandler.VERSION);
						buf.writeInt(0);
						buf.writeString(name);
						ClientPlayNetworking.send(ShaderNetworkHandler.SHADER_STOP_PACKET_CHANNEL, buf);
						return 1;
					}))
			)
			.then(literal("stopAll")
				.executes(context -> {
					PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
					buf.writeInt(ShaderNetworkHandler.VERSION);
					buf.writeInt(2);
					ClientPlayNetworking.send(ShaderNetworkHandler.SHADER_STOP_PACKET_CHANNEL, buf);
					return 1;
				})
			)
		);
	}
}
