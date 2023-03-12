package net.erv123.shadertoymc.arucas.impl;

import me.senseiwells.arucas.api.ArucasOutput;
import me.senseiwells.arucas.compiler.LocatableTrace;
import me.senseiwells.arucas.interpreter.Interpreter;
import net.erv123.shadertoymc.ShadertoyMC;
import net.erv123.shadertoymc.util.ShaderUtils;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum ShaderOutput implements ArucasOutput {
	INSTANCE;

	@NotNull
	@Override
	public String formatError(@NotNull String s) {
		return "§c" + s + "§r";
	}

	@NotNull
	@Override
	public String formatErrorBold(@NotNull String s) {
		return "§l" + this.formatError(s);
	}

	@NotNull
	@Override
	public String formatStackTrace(@NotNull Interpreter interpreter, @Nullable String s, @NotNull LocatableTrace locatableTrace) {
		return ArucasOutput.defaultFormatStackTrace(interpreter, s, locatableTrace);
	}

	@Override
	public void log(@Nullable Object o) {
		ShadertoyMC.LOGGER.info(o);
	}

	@Override
	public void logError(@Nullable Object o) {
		this.logln(this.formatErrorBold(String.valueOf(o)));
	}

	@Override
	public void logln() {
		this.log("");
	}

	@Override
	public void logln(@Nullable Object o) {
		this.log(o);
	}

	@Override
	public void print(@Nullable Object o) {
		ShaderUtils.sendMessageToOps(Text.literal(String.valueOf(o)));
	}

	@Override
	public void printError(@Nullable Object o) {
		this.println(this.formatErrorBold(String.valueOf(o)));
	}

	@Override
	public void println() {
		this.println("");
	}

	@Override
	public void println(@Nullable Object o) {
		this.print(o);
	}
}
