package net.erv123.shadertoymc.arucas.extension;

import kotlin.Unit;
import me.senseiwells.arucas.builtin.NumberDef;
import me.senseiwells.arucas.classes.PrimitiveDefinition;
import me.senseiwells.arucas.core.Interpreter;
import me.senseiwells.arucas.utils.Arguments;
import me.senseiwells.arucas.utils.BuiltInFunction;
import net.erv123.shadertoymc.util.ScriptUtils;
import net.jlibnoise.generator.Perlin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class PerlinNoiseDef extends PrimitiveDefinition<Unit> {
	// Unique to every script.
	private final Perlin noise = new Perlin();

	public PerlinNoiseDef(@NotNull Interpreter interpreter) {
		super("PerlinNoise", interpreter);
	}

	@Nullable
	@Override
	public List<BuiltInFunction> defineStaticMethods() {
		return List.of(
			BuiltInFunction.of("setFrequency", 1, this::setFrequency),
			BuiltInFunction.of("getFrequency", this::getFrequency),
			BuiltInFunction.of("setLacunarity", 1, this::setLacunarity),
			BuiltInFunction.of("getLacunarity", this::getLacunarity),
			BuiltInFunction.of("setNoiseQuality", 1, this::setNoiseQuality),
			BuiltInFunction.of("getNoiseQuality", this::getNoiseQuality),
			BuiltInFunction.of("setOctaveCount", 1, this::setOctaveCount),
			BuiltInFunction.of("getOctaveCount", this::getOctaveCount),
			BuiltInFunction.of("setPersistence", 1, this::setPersistence),
			BuiltInFunction.of("getPersistence", this::getPersistence),
			BuiltInFunction.of("setSeed", 1, this::setSeed),
			BuiltInFunction.of("getSeed", this::getSeed),
			BuiltInFunction.of("getValue", 3, this::getValue)
		);
	}

	private Void setFrequency(Arguments arguments) {
		double frequency = arguments.nextPrimitive(NumberDef.class);
		this.noise.setFrequency(frequency);
		return null;
	}

	private double getFrequency(Arguments arguments) {
		return this.noise.getFrequency();
	}

	private Void setLacunarity(Arguments arguments) {
		double lacunarity = arguments.nextPrimitive(NumberDef.class);
		this.noise.setLacunarity(lacunarity);
		return null;
	}

	private double getLacunarity(Arguments arguments) {
		return this.noise.getLacunarity();
	}

	private Void setNoiseQuality(Arguments arguments) {
		String quality = arguments.nextConstant();
		this.noise.setNoiseQuality(ScriptUtils.stringToNoiseQuality(quality));
		return null;
	}

	private String getNoiseQuality(Arguments arguments) {
		return this.noise.getNoiseQuality().name().toLowerCase(Locale.ROOT);
	}

	private Void setOctaveCount(Arguments arguments) {
		int octaveCount = arguments.nextPrimitive(NumberDef.class).intValue();
		this.noise.setOctaveCount(octaveCount);
		return null;
	}

	private int getOctaveCount(Arguments arguments) {
		return this.noise.getOctaveCount();
	}

	private Void setPersistence(Arguments arguments) {
		double persistence = arguments.nextPrimitive(NumberDef.class);
		this.noise.setPersistence(persistence);
		return null;
	}

	private double getPersistence(Arguments arguments) {
		return this.noise.getPersistence();
	}

	private Void setSeed(Arguments arguments) {
		int seed = arguments.nextPrimitive(NumberDef.class).intValue();
		this.noise.setSeed(seed);
		return null;
	}

	private int getSeed(Arguments arguments) {
		return this.noise.getSeed();
	}

	private Void getValue(Arguments arguments) {
		double frequency = arguments.nextPrimitive(NumberDef.class);
		this.noise.setFrequency(frequency);
		return null;
	}
}
