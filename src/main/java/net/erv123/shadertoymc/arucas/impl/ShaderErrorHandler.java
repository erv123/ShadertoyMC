package net.erv123.shadertoymc.arucas.impl;

import me.senseiwells.arucas.api.ArucasErrorHandler;
import me.senseiwells.arucas.core.Arucas;
import me.senseiwells.arucas.core.Interpreter;
import me.senseiwells.arucas.exceptions.ArucasError;
import me.senseiwells.arucas.exceptions.FatalError;
import me.senseiwells.arucas.exceptions.Propagator;
import me.senseiwells.arucas.utils.Util;
import net.erv123.shadertoymc.util.ScriptUtils;
import net.erv123.shadertoymc.util.ShaderUtils;
import net.erv123.shadertoymc.ShadertoyMC;
import net.minecraft.MinecraftVersion;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public enum ShaderErrorHandler implements ArucasErrorHandler {
	INSTANCE;

	@Override
	public void handleArucasError(@NotNull ArucasError arucasError, @NotNull Interpreter interpreter) {
		ScriptUtils.sendMessageToHolder(interpreter, Text.literal(arucasError.format(interpreter)).styled(s -> s.withColor(Formatting.RED)));
	}

	@Override
	public void handleFatalError(@NotNull Throwable throwable, @NotNull Interpreter interpreter) {
		Text error = Text.literal("\n").formatted(Formatting.RED).append("An error has occurred while running '§s'" + interpreter.getName());
		ScriptUtils.sendMessageToHolder(interpreter, error);
		String path = this.writeCrashReport(interpreter, throwable).toAbsolutePath().toString();
		Text crashReport = Text.literal("A crash report has been saved to: §s")
			.formatted(Formatting.RED);
		ScriptUtils.sendMessageToHolder(interpreter, crashReport);
		Text pathText = Text.literal("\n" + path + "\n")
			.formatted(Formatting.UNDERLINE);
		ScriptUtils.sendMessageToHolder(interpreter, pathText);
	}

	@Override
	public void handleFatalError(@NotNull FatalError fatalError, @NotNull Interpreter interpreter) {
		this.handleFatalError((Throwable) fatalError, interpreter);
	}

	@Override
	public void handleInvalidPropagator(@NotNull Propagator propagator, @NotNull Interpreter interpreter) {
		ArucasErrorHandler.getDefault().handleInvalidPropagator(propagator, interpreter);
	}

	private Path writeCrashReport(Interpreter interpreter, Throwable throwable) {
		String stacktrace = ExceptionUtils.getStackTrace(throwable);
		String scriptTrace = "";
		if (throwable instanceof FatalError error) {
			scriptTrace = """
				### StackTrace:
				```
				%s
				```
				""".formatted(error.format(interpreter));
		}
		String report = """
			### Minecraft Version: `%s`
			### ShaderToy Version: `%s`
			### Arucas Version: `%s`
			### Script:
			```kt
			// %s
			%s
			```
			%s### Crash:
			```
			%s
			```
			""".formatted(
			MinecraftVersion.CURRENT.getName(),
			ShadertoyMC.VERSION,
			Arucas.getVERSION(),
			interpreter.getName(),
			interpreter.getContent(),
			scriptTrace,
			stacktrace
		);
		String date = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date());
		Path crashPath = ShaderUtils.SHADERTOY_PATH.resolve("crashes").resolve("crash-" + date + ".txt");
		Util.File.INSTANCE.ensureParentExists(crashPath);
		try {
			Files.writeString(crashPath, report);
		} catch (IOException e) {
			ShadertoyMC.LOGGER.error("Failed to write script crash report:\n{}\n\n{}", report, e);
		}
		return crashPath;
	}
}
