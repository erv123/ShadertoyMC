package net.erv123.shadertoymc.util;

import net.erv123.shadertoymc.ShadertoyMC;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

public enum StringCompressor {
	;

	public static byte[] compress(String text) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			OutputStream out = new DeflaterOutputStream(baos);
			out.write(text.getBytes(StandardCharsets.UTF_8));
			out.close();
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		return baos.toByteArray();
	}

	@Nullable
	public static String decompress(byte[] bytes) {
		try (
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			OutputStream out = new InflaterOutputStream(baos)
		) {
			out.write(bytes);
			return baos.toString(StandardCharsets.UTF_8);
		} catch (IOException e) {
			ShadertoyMC.LOGGER.error(e);
		}
		return null;
	}
}
