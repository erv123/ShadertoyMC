package net.erv123.shadertoymc.util;

import me.senseiwells.arucas.utils.Util;
import net.erv123.shadertoymc.ShadertoyMC;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class ShaderUtils {
	public static final Path SHADERTOY_PATH = FabricLoader.getInstance().getConfigDir().resolve("Shadertoy");

	public static boolean canBlocksFall = true;

	static  {
		Util.File.INSTANCE.ensureExists(SHADERTOY_PATH);
	}

	private ShaderUtils() {

	}

	public static List<ServerPlayerEntity> getAllPlayers() {
		return ShadertoyMC.SERVER.getPlayerManager().getPlayerList();
	}

	public static void sendMessageToPlayers(Text text) {
		for (ServerPlayerEntity player : getAllPlayers()) {
			player.sendMessage(text);
		}
	}

	public static void sendMessageToOps(Text text) {
		for (ServerPlayerEntity player : getAllPlayers()) {
			if (player.hasPermissionLevel(2)) {
				player.sendMessage(text);
			}
		}
	}

	public static String readResourceAsString(String resource) {
		try (InputStream stream = ShaderUtils.class.getClassLoader().getResourceAsStream(resource)) {
			if (stream == null) {
				return null;
			}
			try (InputStreamReader reader = new InputStreamReader(stream); BufferedReader buffer = new BufferedReader(reader)) {
				return buffer.lines().collect(Collectors.joining(System.lineSeparator()));
			}
		} catch (IOException exception) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends Throwable, V> V throwUnchecked(Throwable throwable) throws T {
		throw (T) throwable;
	}
}
