package net.erv123.shadertoymc.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import me.senseiwells.arucas.api.ArucasAPI;
import me.senseiwells.arucas.api.impl.GitHubArucasLibrary;
import me.senseiwells.arucas.api.impl.MultiArucasLibrary;
import me.senseiwells.arucas.api.impl.ResourceArucasLibrary;
import me.senseiwells.arucas.exceptions.RuntimeError;
import me.senseiwells.arucas.interpreter.Interpreter;
import net.erv123.shadertoymc.ShadertoyMC;
import net.erv123.shadertoymc.arucas.definitions.PerlinNoiseDef;
import net.erv123.shadertoymc.arucas.definitions.Vector3Def;
import net.erv123.shadertoymc.arucas.definitions.VoronoiNoiseDef;
import net.erv123.shadertoymc.arucas.extension.ShaderExtension;
import net.erv123.shadertoymc.arucas.impl.MinecraftExecutor;
import net.erv123.shadertoymc.arucas.impl.MinecraftServerPoller;
import net.erv123.shadertoymc.arucas.impl.ShaderErrorHandler;
import net.erv123.shadertoymc.arucas.impl.ShaderOutput;
import net.jlibnoise.NoiseQuality;
import net.minecraft.entity.boss.BossBarManager;
import net.minecraft.entity.boss.CommandBossBar;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class ScriptUtils {
	private static final DynamicCommandExceptionType FAILED_TO_READ_SCRIPT;
	private static final Map<UUID, ScriptData> SCRIPT_DATA;
	private static final Map<UUID, Area> AREA_DATA;
	private static final GitHubArucasLibrary GIT_LIBRARY;
	private static final ResourceArucasLibrary RESOURCE_LIBRARY;
	private static final ArucasAPI ARUCAS_API;

	public static final List<String> SCRIPTS;
	public static final String EXAMPLE_SCRIPT;
	public static List<Interpreter> activeInterpreters = new ArrayList<>();
	private static boolean holdRemoval = false;

	static {
		FAILED_TO_READ_SCRIPT = new DynamicCommandExceptionType(o -> Text.literal("Failed to read script: " + o));
		SCRIPT_DATA = new HashMap<>();
		AREA_DATA = new HashMap<>();
		GIT_LIBRARY = new GitHubArucasLibrary(
			ShaderUtils.SHADERTOY_PATH.resolve("libs"),
			"https://api.github.com/repos/erv123/ShadertoyMC_Libraries/contents/libs"
		);
		RESOURCE_LIBRARY = new ResourceArucasLibrary("assets/libraries");
		ARUCAS_API = generateApi();
		EXAMPLE_SCRIPT = Objects.requireNonNull(ShaderUtils.readResourceAsString("assets/ExampleShader.arucas"));
		SCRIPTS = new ArrayList<>();

		reloadScripts();
	}

	private ScriptUtils() {

	}

	public static BlockPos createBlockPos(double x, double y, double z){
		return new BlockPos(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
	}
	public static BlockPos createBlockPos(Vec3d vec){
		return createBlockPos(vec.x, vec.y, vec.z);
	}

	public static void sendMessageToHolder(Interpreter interpreter, Text text) {
		getScriptHolder(interpreter).sendMessage(text);
	}

	public static Util.OperatingSystem getOperatingSystem() {
		return Util.getOperatingSystem();
	}

	public static ServerCommandSource getScriptHolder(Interpreter interpreter) {
		return getScriptData(interpreter).source();
	}

	public static CommandBossBar getBossBar(Interpreter interpreter) {
		return getScriptData(interpreter).bossBar();
	}

	public static Area getArea(Interpreter interpreter) {
		ServerPlayerEntity player = getScriptHolder(interpreter).getPlayer();
		if (player == null) {
			return null;
		}
		return getArea(player);
	}

	public static Area getArea(ServerPlayerEntity player) {
		return AREA_DATA.get(player.getUuid());
	}

	public static Area getOrCreateArea(ServerPlayerEntity player, BlockPos initial) {
		return AREA_DATA.computeIfAbsent(player.getUuid(), id -> new Area(initial));
	}

	public static Area getOrCreateArea(Interpreter interpreter, BlockPos initial) {
		ServerPlayerEntity player = getScriptHolder(interpreter).getPlayer();
		if (player == null) {
			return null;
		}
		return AREA_DATA.computeIfAbsent(player.getUuid(), id -> new Area(initial));
	}

	public static void showProgressBar(Interpreter interpreter) {
		ScriptData data = getScriptData(interpreter);
		ServerPlayerEntity player = data.source().getPlayer();
		if (player != null) {
			data.bossBar().addPlayer(player);
		}
	}

	public static void hideProgressBar(Interpreter interpreter) {
		getBossBar(interpreter).clearPlayers();
	}

	public static ScriptData getScriptData(Interpreter interpreter) {
		return SCRIPT_DATA.get(interpreter.getProperties().getId());
	}

	public static NoiseQuality stringToNoiseQuality(String string) {
		return switch (string.toLowerCase(Locale.ROOT)) {
			case "fast", "f" -> NoiseQuality.FAST;
			case "standard", "s" -> NoiseQuality.STANDARD;
			case "best", "b" -> NoiseQuality.BEST;
			default -> throw new RuntimeError("Unknown noise quality: " + string);
		};
	}

	public static void executeScript(String scriptName, ServerCommandSource source) throws CommandSyntaxException {
		Path scriptPath = ShaderUtils.SHADERTOY_PATH.resolve(scriptName);
		String fileContent;
		try {
			fileContent = Files.readString(scriptPath);
		} catch (IOException e) {
			throw FAILED_TO_READ_SCRIPT.create(scriptName);
		}
		Interpreter interpreter = Interpreter.of(fileContent, scriptName, ARUCAS_API);

		Identifier identifier = new Identifier("shadertoy", scriptName.substring(0, scriptName.lastIndexOf('.')).toLowerCase(Locale.ROOT));
		Text text = Text.literal("Progress: " + scriptName);

		BossBarManager manager = ShadertoyMC.SERVER.getBossBarManager();
		CommandBossBar bossBar = manager.add(identifier, text);

		UUID uuid = interpreter.getProperties().getId();
		SCRIPT_DATA.put(interpreter.getProperties().getId(), new ScriptData(source, bossBar));

		interpreter.addStopEvent(() -> {
			sendMessageToHolder(interpreter, Text.literal("Done!"));
			SCRIPT_DATA.remove(uuid);
			bossBar.clearPlayers();
			manager.remove(bossBar);
			if(!holdRemoval) {
				activeInterpreters.remove(interpreter);
			}
		});
		activeInterpreters.add(interpreter);
		interpreter.executeAsync();
	}

	public static void stopAllScripts() {
		holdRemoval = true;
		for (Interpreter interpreter : activeInterpreters) {
			interpreter.stop();
		}
		holdRemoval = false;
		activeInterpreters.clear();
	}

	public static void reloadScripts() {
		try (Stream<Path> paths = Files.list(ShaderUtils.SHADERTOY_PATH)) {
			List<String> scripts = paths
				.filter(path -> !Files.isDirectory(path))
				.map(path -> path.getFileName().toString())
				.filter(path -> path.endsWith(".arucas"))
				.toList();
			SCRIPTS.clear();
			SCRIPTS.addAll(scripts);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static ArucasAPI generateApi() {
		return new ArucasAPI.Builder()
			.setLibraryManager(new MultiArucasLibrary())
			.addDefault()
			.addArucasLibrary("ShaderGithub", GIT_LIBRARY)
			.addArucasLibrary("ResourceLibrary", RESOURCE_LIBRARY)
			.setErrorHandler(ShaderErrorHandler.INSTANCE)
			.setMainExecutor(MinecraftExecutor.INSTANCE)
			.addPoller(MinecraftServerPoller.INSTANCE)
			.setOutput(ShaderOutput.INSTANCE)
			.addBuiltInExtension(new ShaderExtension())
			.addClassDefinitions("util.Noise", PerlinNoiseDef::new, VoronoiNoiseDef::new)
			.addClassDefinitions("util.Vector", Vector3Def::new)
			.addConversion(Vec3d.class, (vec, i) -> i.create(Vector3Def.class, vec))
			.addConversion(Vec3i.class, (vec, i) -> i.create(Vector3Def.class, new Vec3d(vec.getX(), vec.getY(), vec.getZ())))
			.build();
	}
}
