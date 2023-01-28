package net.erv123.shadertoymc;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.argument.*;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ShadertoyCommand {
/*
		/shadertoy
			area
				pos size
				clear
				display
				pos1
				pos2
				size

			shader
				calculate
				paste
 */
    public ShadertoyCommand() {
    }
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("shadertoy").requires(source -> source.hasPermissionLevel(2))
                .then(literal("area").executes(context ->{
                            context.getSource().sendMessage(Text.literal("Area info"));
                            return 1;
                        })
                        .then(literal("clear").executes(context ->{
                            context.getSource().sendMessage(Text.literal("Area cleared"));
                            ShaderArea.read().clear(context.getSource());
                            return 1;
                        }))
                        .then(literal("display").executes(context ->{
                            context.getSource().sendMessage(Text.literal("Area display state toggled"));
                            return 1;
                        }))
                        .then(argument("pos1", BlockPosArgumentType.blockPos()).then(argument("pos2", BlockPosArgumentType.blockPos()).executes(context ->{
                            context.getSource().sendMessage(Text.literal("Area created with pos1 and pos2"));
                            BlockPos pos1 = BlockPosArgumentType.getBlockPos(context,"pos1");
                            BlockPos pos2 = BlockPosArgumentType.getBlockPos(context,"pos2");
                            new ShaderArea(pos1, pos2);
                            return 1;
                        })))
                        .then(literal("size").then(argument("size", Vec3ArgumentType.vec3()).executes(context ->{
                            context.getSource().sendMessage(Text.literal("Area size changed"));
                            BlockPos size = BlockPosArgumentType.getBlockPos(context,"size");
                            ShaderArea area = ShaderArea.read();
                            area.setSize(size);
                            return 1;
                        })))
                        .then(literal("origin").then(argument("origin", Vec3ArgumentType.vec3()).executes(context ->{
                            context.getSource().sendMessage(Text.literal("Area origin moved"));
                            BlockPos origin = BlockPosArgumentType.getBlockPos(context,"origin");
                            ShaderArea area = ShaderArea.read();
                            area.setOrigin(origin);
                            return 1;
                        }))))
                .then(literal("shader")
                        .then(literal("calculate").then(argument("shaderID", StringArgumentType.string())).executes(context ->{
                            context.getSource().sendMessage(Text.literal("shader caclulations in proceess"));
                            return 1;
                        }))
                        .then(literal("paste").then(argument("shaderID", StringArgumentType.string()).executes(context ->{
                            context.getSource().sendMessage(Text.literal("<shaderID> shader pasted"));
                            return 1;
                        }))))
        );

    }

}
