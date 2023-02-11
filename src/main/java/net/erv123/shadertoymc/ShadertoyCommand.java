package net.erv123.shadertoymc;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

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
                .then(literal("area").executes(context -> {
                            context.getSource().sendMessage(Text.literal("Area info"));
                            return 1;
                        })
                        .then(literal("clear").executes(context -> {
                            context.getSource().sendMessage(Text.literal("Area cleared"));
                            Objects.requireNonNull(ShaderArea.read()).clear(context.getSource());
                            return 1;
                        }))
                        .then(literal("display").executes(context -> {
                            context.getSource().sendMessage(Text.literal("Area display state toggled"));
                            return 1;
                        }))
                        .then(argument("pos1", BlockPosArgumentType.blockPos()).then(argument("pos2", BlockPosArgumentType.blockPos()).executes(context -> {
                            context.getSource().sendMessage(Text.literal("Area created with pos1 and pos2"));
                            BlockPos pos1 = BlockPosArgumentType.getBlockPos(context, "pos1");
                            BlockPos pos2 = BlockPosArgumentType.getBlockPos(context, "pos2");
                            new ShaderArea(pos1, pos2);
                            return 1;
                        })))
                        .then(literal("size").then(argument("size", Vec3ArgumentType.vec3()).executes(context -> {
                            context.getSource().sendMessage(Text.literal("Area size changed"));
                            BlockPos size = BlockPosArgumentType.getBlockPos(context, "size");
                            ShaderArea area = ShaderArea.read();
                            assert area != null;
                            area.setSize(size);
                            return 1;
                        })))
                        .then(literal("origin").then(argument("origin", Vec3ArgumentType.vec3()).executes(context -> {
                            context.getSource().sendMessage(Text.literal("Area origin moved"));
                            BlockPos origin = BlockPosArgumentType.getBlockPos(context, "origin");
                            ShaderArea area = ShaderArea.read();
                            assert area != null;
                            area.setOrigin(origin);
                            return 1;
                        }))))
                .then(literal("run")
                        .then(argument("shaderID", StringArgumentType.string()).suggests((context, builder) -> {
                            Stream<Path> files;
                            try {
                                files = Files.list(ShaderUtils.SHADERTOY_PATH);
                            } catch (IOException e) {
                                ShadertoyMC.LOGGER.error("File error go brrrrrrrrrrr", e);
                                throw new RuntimeException(e);
                            }
                            Stream<String> suggestions = files.filter(Predicate.not(Predicate.isEqual(ShaderUtils.SHADERTOY_PATH.resolve("area.json")).or(Predicate.isEqual(ShaderUtils.SHADERTOY_PATH.resolve("libs"))).or(Predicate.isEqual(ShaderUtils.SHADERTOY_PATH.resolve("crashes"))))).map(Path::toString).map(s -> s.substring(ShaderUtils.SHADERTOY_PATH.toString().length() + 1));
                            return CommandSource.suggestMatching(suggestions, builder);
                        }).executes(context -> {
                            String shaderID = StringArgumentType.getString(context, "shaderID");
                            context.getSource().sendMessage(Text.literal("Running shader: " + shaderID));
                            ShaderUtils.executeScript(shaderID, context.getSource());
                            return 1;
                        })))
                .then(literal("new")
                        .then(argument("shaderID", StringArgumentType.string()).executes(context -> {
                            String shaderID = StringArgumentType.getString(context, "shaderID");
                            if(!shaderID.endsWith(".arucas")){
                                shaderID += ".arucas";
                            }
                            Path pastePath = ShaderUtils.SHADERTOY_PATH.resolve(shaderID);
                            try {
                                InputStream inputStream = ShadertoyMC.class.getResourceAsStream("/assets/exampleShader.arucas");
                                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                                StringBuilder builder = new StringBuilder();
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    builder.append(line).append("\n");
                                }
                                File file = new File(pastePath.toUri());
                                FileWriter writer = new FileWriter(file);
                                writer.write(builder.toString());
                                writer.close();
                            }
                            catch (IOException e){
                                ShadertoyMC.LOGGER.error("File error go brrr", e);
                                throw new RuntimeException(e);
                            }
                            Text pathText = Text.literal("\nNew shader file §n" + shaderID + "§r created!\n")
                                    .styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, pastePath.toString())));
                            ShaderUtils.sendMessage(pathText);
                            return 1;
                        })))
                .then(literal("stop").executes(context -> {
                    ShaderUtils.running = false;
                    return 1;
                }))
        );
    }
}
