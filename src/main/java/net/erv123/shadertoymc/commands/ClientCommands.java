package net.erv123.shadertoymc.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import io.netty.buffer.Unpooled;
import me.senseiwells.arucas.utils.FileUtils;
import net.erv123.shadertoymc.networking.RegisterPackets;
import net.erv123.shadertoymc.util.Area;
import net.erv123.shadertoymc.util.ScriptUtils;
import net.erv123.shadertoymc.util.ShaderUtils;
import net.erv123.shadertoymc.util.WorldUtils;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.network.PacketByteBuf;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static dev.xpple.clientarguments.arguments.CBlockPosArgumentType.*;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;

public class ClientCommands {
	private static final DynamicCommandExceptionType FAILED_TO_READ_SCRIPT;
	static {
		FAILED_TO_READ_SCRIPT = new DynamicCommandExceptionType(o -> Text.literal("Failed to read script: " + o));
	}
	private ClientCommands() {

	}

	public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
		dispatcher.register(literal("shadertoy")
			.then(literal("area")
				.executes(context -> {
					ClientPlayerEntity player = context.getSource().getPlayer();
					Area area = ScriptUtils.getArea(player);
					if (area == null) {
						throw new CommandSyntaxException(CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException(), () -> "Area not initialized");
					}
					player.sendMessage(Text.literal(area.toString()));
					return 1;
				})
				.then(literal("pos1")
					.then(argument("position", blockPos())
						.executes(context -> {
							ClientPlayerEntity player = context.getSource().getPlayer();
							BlockPos pos = getCBlockPos(context, "position");
							ScriptUtils.getOrCreateArea(player, pos).setA(pos);
							player.sendMessage(Text.literal("Successfully set area position 1 to: " + pos.toShortString()));
							return 1;
						})
					)
					.executes(context -> {
						ClientPlayerEntity player = context.getSource().getPlayer();
						BlockPos pos = player.getBlockPos();
						ScriptUtils.getOrCreateArea(player, pos).setA(pos);
						player.sendMessage(Text.literal("Successfully set area position 1 to: " + pos.toShortString()));
						return 1;
					})
				)
				.then(literal("pos2")
					.then(argument("position", blockPos())
						.executes(context -> {
							ClientPlayerEntity player = context.getSource().getPlayer();
							BlockPos pos = getCBlockPos(context, "position");
							ScriptUtils.getOrCreateArea(player, pos).setB(pos);
							player.sendMessage(Text.literal("Successfully set area position 2 to: " + pos.toShortString()));
							return 1;
						})
					)
					.executes(context -> {
						ClientPlayerEntity player = context.getSource().getPlayer();
						BlockPos pos = player.getBlockPos();
						ScriptUtils.getOrCreateArea(player, pos).setB(pos);
						player.sendMessage(Text.literal("Successfully set area position 2 to: " + pos.toShortString()));
						return 1;
					})
				)
				.then(literal("origin")
					.then(argument("position", blockPos())
						.then(literal("size")
							.then(argument("sizeX", IntegerArgumentType.integer(1))
								.then(argument("sizeY", IntegerArgumentType.integer(1))
									.then(argument("sizeZ", IntegerArgumentType.integer(1))
										.executes(context -> {
											ClientPlayerEntity player = context.getSource().getPlayer();
											BlockPos origin = getCBlockPos(context, "position");
											int sizeX = IntegerArgumentType.getInteger(context, "sizeX");
											int sizeY = IntegerArgumentType.getInteger(context, "sizeY");
											int sizeZ = IntegerArgumentType.getInteger(context, "sizeZ");
											Area area = ScriptUtils.getOrCreateArea(player, origin);
											area.setA(origin);
											area.setB(origin.add(sizeX, sizeY, sizeZ));
											String success = "Successfully set area origin to: " + origin.toShortString() +
												", with size: " + sizeX + ", " + sizeY + ", " + sizeZ;
											player.sendMessage(Text.literal(success));
											return 1;
										})
									)
								)
							)
						)
					)
				)
				.then(argument("pos1", blockPos())
					.then(argument("pos2", blockPos())
						.executes(context -> {
							ClientPlayerEntity player = context.getSource().getPlayer();
							BlockPos pos1 = getCBlockPos(context, "pos1");
							BlockPos pos2 = getCBlockPos(context, "pos2");
							Area area = ScriptUtils.getOrCreateArea(player, pos1);
							area.setA(pos1);
							area.setB(pos2);
							player.sendMessage(Text.literal("Successfully set area position 1 to: " + pos1.toShortString() + " and position 2 to " + pos2.toShortString()));
							return 1;
						})
					)
				)
				.then(literal("origin")
					.then(argument("position", blockPos())
						.executes(context -> {
							ClientPlayerEntity player = context.getSource().getPlayer();
							BlockPos origin = getCBlockPos(context, "position");
							ScriptUtils.getOrCreateArea(player, origin).setOrigin(origin);
							String success = "Successfully set area origin to: " + origin.toShortString();
							player.sendMessage(Text.literal(success));
							return 1;
						})
					)
				)
				.then(literal("size")
					.then(argument("sizeX", IntegerArgumentType.integer(1))
						.then(argument("sizeY", IntegerArgumentType.integer(1))
							.then(argument("sizeZ", IntegerArgumentType.integer(1))
								.executes(context -> {
									ClientPlayerEntity player = context.getSource().getPlayer();
									int sizeX = IntegerArgumentType.getInteger(context, "sizeX");
									int sizeY = IntegerArgumentType.getInteger(context, "sizeY");
									int sizeZ = IntegerArgumentType.getInteger(context, "sizeZ");
									Area area = ScriptUtils.getArea(player);
									if (area == null) {
										String fail = "Initialize the area with any other command before using this!";
										player.sendMessage(Text.literal(fail));
										return 0;
									}
									area.setSize(new Vec3i(sizeX, sizeY, sizeZ));
									String success = "Successfully set area size to: " + sizeX + ", " + sizeY + ", " + sizeZ;
									player.sendMessage(Text.literal(success));
									return 1;
								})
							)
						)
					)
				)
				.then(literal("clear")
					.executes(context -> {
						ClientPlayerEntity player = context.getSource().getPlayer();
						Area area = ScriptUtils.getArea(player);
						if (area == null) {
							throw new RuntimeException("Initialize area before clearing it!");
						}
						for (BlockPos pos : area) {
							WorldUtils.setBlockWithNoUpdates(player.world, pos, Blocks.AIR.getDefaultState());
						}
						player.sendMessage(Text.literal("Successfully cleared area!"));
						return 1;
					})
				)
				.then(literal("doBlockUpdates")
					.then(argument("update", BoolArgumentType.bool())
						.executes(context -> {
							ClientPlayerEntity player = context.getSource().getPlayer();
							WorldUtils.doUpdates = BoolArgumentType.getBool(context, "update");
							player.sendMessage(Text.literal("Block updates set to: " + WorldUtils.doUpdates));
							return 1;
						})
					)
				)
			)
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
						context.getSource().getPlayer().sendMessage(Text.literal("Running shader: " + shaderFile));
						ClientPlayNetworking.send(RegisterPackets.SHADER_PACKET_ID, new PacketByteBuf(Unpooled.buffer()).writeString(fileContent));
						//ScriptUtils.executeScript(fileContent, context.getSource());
						return 1;
					})
				)
			)
			.then(literal("stop")
				.executes(context -> {
					ScriptUtils.stopAllScripts();
					return 1;
				})
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
						ScriptUtils.getOperatingSystem().open(shader);
						return 1;
					})
				)
			)
		);
	}
}
