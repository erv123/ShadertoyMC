package net.erv123.shadertoymc.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import me.senseiwells.arucas.utils.Util;
import net.erv123.shadertoymc.util.Area;
import net.erv123.shadertoymc.util.ScriptUtils;
import net.erv123.shadertoymc.util.ShaderUtils;
import net.erv123.shadertoymc.util.WorldUtils;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ShadertoyCommand {
	private ShadertoyCommand() {

	}

	/*
	TODO: stop command
	 */
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal("shadertoy")
			.requires(source -> source.hasPermissionLevel(2))
			.then(literal("area")
				.then(literal("pos1")
					.then(argument("position", BlockPosArgumentType.blockPos())
						.executes(context -> {
							ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
							BlockPos pos = BlockPosArgumentType.getBlockPos(context, "position");
							ScriptUtils.getOrCreateArea(player, pos).setA(pos);
							player.sendMessage(Text.literal("Successfully set area position 1 to: " + pos.toShortString()));
							return 1;
						})
					)
					.executes(context -> {
						ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
						BlockPos pos = player.getBlockPos();
						ScriptUtils.getOrCreateArea(player, pos).setA(pos);
						player.sendMessage(Text.literal("Successfully set area position 1 to: " + pos.toShortString()));
						return 1;
					})
				)
				.then(literal("pos2")
					.then(argument("position", BlockPosArgumentType.blockPos())
						.executes(context -> {
							ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
							BlockPos pos = BlockPosArgumentType.getBlockPos(context, "position");
							ScriptUtils.getOrCreateArea(player, pos).setB(pos);
							player.sendMessage(Text.literal("Successfully set area position 2 to: " + pos.toShortString()));
							return 1;
						})
					)
					.executes(context -> {
						ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
						BlockPos pos = player.getBlockPos();
						ScriptUtils.getOrCreateArea(player, pos).setB(pos);
						player.sendMessage(Text.literal("Successfully set area position 2 to: " + pos.toShortString()));
						return 1;
					})
				)
				.then(literal("origin")
					.then(argument("position", BlockPosArgumentType.blockPos())
						.then(literal("size")
							.then(argument("sizeX", IntegerArgumentType.integer(1))
								.then(argument("sizeY", IntegerArgumentType.integer(1))
									.then(argument("sizeZ", IntegerArgumentType.integer(1))
										.executes(context -> {
											ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
											BlockPos origin = BlockPosArgumentType.getBlockPos(context, "position");
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
				.then(argument("pos1", BlockPosArgumentType.blockPos())
					.then(argument("pos2", BlockPosArgumentType.blockPos())
						.executes(context -> {
							ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
							BlockPos pos1 = BlockPosArgumentType.getBlockPos(context, "pos1");
							BlockPos pos2 = BlockPosArgumentType.getBlockPos(context, "pos2");
							Area area = ScriptUtils.getOrCreateArea(player, pos1);
							area.setA(pos1);
							area.setB(pos2);
							player.sendMessage(Text.literal("Successfully set area position 1 to: " + pos1.toShortString() + " and position 2 to " + pos2.toShortString()));
							return 1;
						})
					)
				)
				.then(literal("origin")
					.then(argument("position", BlockPosArgumentType.blockPos())
						.executes(context -> {
							ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
							BlockPos origin = BlockPosArgumentType.getBlockPos(context, "position");
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
									ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
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
						ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
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
							ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
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
						context.getSource().sendMessage(Text.literal("Running shader: " + shaderFile));
						ScriptUtils.executeScript(shaderFile, context.getSource());
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
							Util.File.INSTANCE.ensureParentExists(shaderPath);
							Files.writeString(shaderPath, ScriptUtils.EXAMPLE_SCRIPT);
						} catch (IOException exception) {
							context.getSource().sendFeedback(Text.literal("Failed to write to: " + shaderPath), true);
							return 0;
						}
						context.getSource().sendMessage(Text.literal("New shader file §n" + shaderFile + "§r created")
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
