package net.erv123.shadertoymc.util;

import me.senseiwells.arucas.api.ArucasAPI;
import me.senseiwells.arucas.api.impl.GitHubArucasLibrary;
import me.senseiwells.arucas.api.impl.MultiArucasLibrary;
import me.senseiwells.arucas.api.impl.ResourceArucasLibrary;
import me.senseiwells.arucas.core.Interpreter;
import net.erv123.shadertoymc.arucas.extension.ShaderExtension;
import net.erv123.shadertoymc.arucas.impl.MinecraftExecutor;
import net.erv123.shadertoymc.arucas.impl.MinecraftServerPoller;
import net.erv123.shadertoymc.arucas.impl.ShaderErrorHandler;
import net.erv123.shadertoymc.arucas.impl.ShaderOutput;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.MinecraftVersion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShaderUtils {
	public static final Path SHADERTOY_PATH = FabricLoader.getInstance().getConfigDir().resolve("Shadertoy");

	private static final Map<UUID, ServerPlayerEntity> SCRIPT_PLAYERS = new HashMap<>();
	private static final GitHubArucasLibrary GIT_LIBRARY = new GitHubArucasLibrary(SHADERTOY_PATH.resolve("libs"), "https://api.github.com/repos/erv123/ShadertoyMC_Libraries/contents/libs");
	private static final ResourceArucasLibrary RESOURCE_LIBRARY = new ResourceArucasLibrary("assets/libraries");
	private static final ArucasAPI ARUCAS_API = new ArucasAPI.Builder()
		.setLibraryManager(new MultiArucasLibrary())
		.addDefault()
		.addArucasLibrary("ShaderGithub", GIT_LIBRARY)
		.addArucasLibrary("ResourceLibrary", RESOURCE_LIBRARY)
		.setErrorHandler(ShaderErrorHandler.INSTANCE)
		.setMainExecutor(MinecraftExecutor.INSTANCE)
		.addPoller(MinecraftServerPoller.INSTANCE)
		.setOutput(ShaderOutput.INSTANCE)
		.addBuiltInExtension(new ShaderExtension())
		.build();

	public static MinecraftServer SERVER;
	public static boolean canBlocksFall = true;
	public static boolean running = false;

	private ShaderUtils() {

	}

	public static ServerPlayerEntity getPlayerForInterpreter(Interpreter interpreter) {
		return SCRIPT_PLAYERS.get(interpreter.getProperties().getId());
	}

	public static void sendMessageToPlayer(Interpreter interpreter, Text text) {
		getPlayerForInterpreter(interpreter).sendMessage(text);
	}

	public static void setPlayerForInterpreter(ServerPlayerEntity entity, Interpreter interpreter) {
		SCRIPT_PLAYERS.put(interpreter.getProperties().getId(), entity);
	}

	public static void executeScript(String fileName, ServerCommandSource source) {
		running = false;
		String fileContent;
		Path scriptPath = ShaderUtils.SHADERTOY_PATH.resolve(fileName);
		try {
			fileContent = Files.readString(scriptPath);
			if (fileContent == null) {
				throw new IOException("File content was null!");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Interpreter interpreter = Interpreter.of(fileContent, "Shader", ShaderUtils.ARUCAS_API);
		interpreter.addStopEvent(() -> {
			sendMessageToPlayer(interpreter, Text.of("Done!"));
			running = false;
			ProgressBar.deleteAllBossBars();
			ProgressBar.hideBossBar();
		});
		ShaderUtils.setPlayerForInterpreter(source.getPlayer(), interpreter);
		interpreter.executeAsync();
		running = true;
		ProgressBar.generateBossBar(interpreter);
	}

	public static String getMinecraftVersion() {
		return MinecraftVersion.CURRENT.getName();
	}

	@SuppressWarnings("unchecked")
	public static <T extends Throwable, V> V throwUnchecked(Throwable throwable) throws T {
		throw (T) throwable;
	}
}
