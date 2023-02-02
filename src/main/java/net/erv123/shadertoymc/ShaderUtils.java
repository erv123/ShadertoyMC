package net.erv123.shadertoymc;

import me.senseiwells.arucas.api.ArucasAPI;
import me.senseiwells.arucas.core.Interpreter;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShaderUtils {
    public static final Path SHADERTOY_PATH = FabricLoader.getInstance().getConfigDir().resolve("Shadertoy");
    private static final Map<UUID, ServerPlayerEntity> SCRIPT_PLAYERS = new HashMap<>();
    private static final ArucasAPI ARUCAS_API = new ArucasAPI.Builder()
            .addDefault()
            .addBuiltInExtension(new ShaderExtension())
            .build();
    private ShaderUtils(){

    }
    public static ServerPlayerEntity getPlayerForInterpreter(Interpreter interpreter){
        return SCRIPT_PLAYERS.get(interpreter.getProperties().getId());
    }
    public static void setPlayerForInterpreter(ServerPlayerEntity entity, Interpreter interpreter){
        SCRIPT_PLAYERS.put(interpreter.getProperties().getId(), entity);
    }
    public static void executeScript(String fileName, ServerCommandSource source){
        String fileContent;
        Path scriptPath = Path.of(ShaderUtils.SHADERTOY_PATH.toString(),"/",fileName);
        try {
            fileContent = Files.readString(scriptPath);
            if (fileContent == null) {
                throw new IOException("File content was null!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Interpreter interpreter = Interpreter.of(fileContent,fileName,ShaderUtils.ARUCAS_API);
        ShaderUtils.setPlayerForInterpreter(source.getPlayer(),interpreter);
        interpreter.executeAsync();
    }
}
