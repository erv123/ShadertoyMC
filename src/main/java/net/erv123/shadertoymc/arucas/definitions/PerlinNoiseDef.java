package net.erv123.shadertoymc.arucas.definitions;

import kotlin.Unit;
import me.senseiwells.arucas.builtin.NumberDef;
import me.senseiwells.arucas.builtin.StringDef;
import me.senseiwells.arucas.classes.CreatableDefinition;
import me.senseiwells.arucas.classes.instance.ClassInstance;
import me.senseiwells.arucas.core.Interpreter;
import me.senseiwells.arucas.utils.Arguments;
import me.senseiwells.arucas.utils.ConstructorFunction;
import me.senseiwells.arucas.utils.MemberFunction;
import net.erv123.shadertoymc.util.ScriptUtils;
import net.jlibnoise.generator.Perlin;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class PerlinNoiseDef extends CreatableDefinition<Perlin> {
	public PerlinNoiseDef(@NotNull Interpreter interpreter) {
		super("PerlinNoise", interpreter);
	}

	@Nullable
	@Override
	public List<MemberFunction> defineMethods() {
		return List.of(
			MemberFunction.of("setSeed", 1, this::setSeed),
			MemberFunction.of("getSeed", this::getSeed),
			MemberFunction.of("setFrequency", 1, this::setFrequency),
			MemberFunction.of("getFrequency", this::getFrequency),
			MemberFunction.of("setOctaveCount", 1, this::setOctaveCount),
			MemberFunction.of("getOctaveCount", this::getOctaveCount),
			MemberFunction.of("setLacunarity", 1, this::setLacunarity),
			MemberFunction.of("getLacunarity", this::getLacunarity),
			MemberFunction.of("setPersistence", 1, this::setPersistence),
			MemberFunction.of("getPersistence", this::getPersistence),
			MemberFunction.of("setNoiseQuality", 1, this::setNoiseQuality),
			MemberFunction.of("getNoiseQuality", this::getNoiseQuality),
			MemberFunction.of("getValue", 3, this::getValue3),
			MemberFunction.of("getValue", 1, this::getValue1)
		);
	}

	@Nullable
	@Override
	public List<ConstructorFunction> defineConstructors() {
		return List.of(
			ConstructorFunction.of(0, this::construct0),
			ConstructorFunction.of(1, this::construct1),
			ConstructorFunction.of(2, this::construct2),
			ConstructorFunction.of(3, this::construct3),
			ConstructorFunction.of(4, this::construct4),
			ConstructorFunction.of(5, this::construct5),
			ConstructorFunction.of(6, this::construct6)
		);
	}

	private Unit construct0(Arguments arguments) {
		ClassInstance instance = arguments.next();
		instance.setPrimitive(this, new Perlin());
		return null;
	}

	private Unit construct1(Arguments arguments) {
		ClassInstance instance = arguments.next();
		int seed = arguments.nextPrimitive(NumberDef.class).intValue();
		Perlin perlin = new Perlin();
		instance.setPrimitive(this, perlin);
		perlin.setSeed(seed);
		return null;
	}

	private Unit construct2(Arguments arguments) {
		ClassInstance instance = arguments.next();
		int seed = arguments.nextPrimitive(NumberDef.class).intValue();
		double frequency = arguments.nextPrimitive(NumberDef.class);
		Perlin perlin = new Perlin();
		instance.setPrimitive(this, perlin);
		perlin.setSeed(seed);
		perlin.setFrequency(frequency);
		return null;
	}

	private Unit construct3(Arguments arguments) {
		ClassInstance instance = arguments.next();
		int seed = arguments.nextPrimitive(NumberDef.class).intValue();
		double frequency = arguments.nextPrimitive(NumberDef.class);
		int octaves = arguments.nextPrimitive(NumberDef.class).intValue();
		Perlin perlin = new Perlin();
		instance.setPrimitive(this, perlin);
		perlin.setSeed(seed);
		perlin.setFrequency(frequency);
		perlin.setOctaveCount(octaves);
		return null;
	}

	private Unit construct4(Arguments arguments) {
		ClassInstance instance = arguments.next();
		int seed = arguments.nextPrimitive(NumberDef.class).intValue();
		double frequency = arguments.nextPrimitive(NumberDef.class);
		int octaves = arguments.nextPrimitive(NumberDef.class).intValue();
		double lacunarity = arguments.nextPrimitive(NumberDef.class);
		Perlin perlin = new Perlin();
		instance.setPrimitive(this, perlin);
		perlin.setSeed(seed);
		perlin.setFrequency(frequency);
		perlin.setOctaveCount(octaves);
		perlin.setLacunarity(lacunarity);
		return null;
	}

	private Unit construct5(Arguments arguments) {
		ClassInstance instance = arguments.next();
		int seed = arguments.nextPrimitive(NumberDef.class).intValue();
		double frequency = arguments.nextPrimitive(NumberDef.class);
		int octaves = arguments.nextPrimitive(NumberDef.class).intValue();
		double lacunarity = arguments.nextPrimitive(NumberDef.class);
		double persistence = arguments.nextPrimitive(NumberDef.class);
		Perlin perlin = new Perlin();
		instance.setPrimitive(this, perlin);
		perlin.setSeed(seed);
		perlin.setFrequency(frequency);
		perlin.setOctaveCount(octaves);
		perlin.setLacunarity(lacunarity);
		perlin.setPersistence(persistence);
		return null;
	}

	private Unit construct6(Arguments arguments) {
		ClassInstance instance = arguments.next();
		int seed = arguments.nextPrimitive(NumberDef.class).intValue();
		double frequency = arguments.nextPrimitive(NumberDef.class);
		int octaves = arguments.nextPrimitive(NumberDef.class).intValue();
		double lacunarity = arguments.nextPrimitive(NumberDef.class);
		double persistence = arguments.nextPrimitive(NumberDef.class);
		String quality = arguments.nextPrimitive(StringDef.class);
		Perlin perlin = new Perlin();
		instance.setPrimitive(this, perlin);
		perlin.setSeed(seed);
		perlin.setFrequency(frequency);
		perlin.setOctaveCount(octaves);
		perlin.setLacunarity(lacunarity);
		perlin.setPersistence(persistence);
		perlin.setNoiseQuality(ScriptUtils.stringToNoiseQuality(quality));

		return null;
	}

	private Void setFrequency(Arguments arguments) {
		ClassInstance instance = arguments.next();
		double frequency = arguments.nextPrimitive(NumberDef.class);
		instance.asPrimitive(this).setFrequency(frequency);
		return null;
	}

	private double getFrequency(Arguments arguments) {
		return arguments.nextPrimitive(this).getFrequency();
	}

	private Void setLacunarity(Arguments arguments) {
		ClassInstance instance = arguments.next();
		int lacunarity = arguments.nextPrimitive(NumberDef.class).intValue();
		instance.asPrimitive(this).setOctaveCount(lacunarity);
		return null;
	}

	private double getLacunarity(Arguments arguments) {
		return arguments.nextPrimitive(this).getLacunarity();
	}

	private Void setNoiseQuality(Arguments arguments) {
		ClassInstance instance = arguments.next();
		String quality = arguments.nextConstant();
		instance.asPrimitive(this).setNoiseQuality(ScriptUtils.stringToNoiseQuality(quality));
		return null;
	}

	private String getNoiseQuality(Arguments arguments) {
		return arguments.nextPrimitive(this).getNoiseQuality().name().toLowerCase(Locale.ROOT);
	}

	private Void setOctaveCount(Arguments arguments) {
		ClassInstance instance = arguments.next();
		int octaves = arguments.nextPrimitive(NumberDef.class).intValue();
		instance.asPrimitive(this).setOctaveCount(octaves);
		return null;
	}

	private int getOctaveCount(Arguments arguments) {
		return arguments.nextPrimitive(this).getOctaveCount();
	}

	private Void setPersistence(Arguments arguments) {
		ClassInstance instance = arguments.next();
		double persistence = arguments.nextPrimitive(NumberDef.class);
		instance.asPrimitive(this).setPersistence(persistence);
		return null;
	}

	private double getPersistence(Arguments arguments) {
		return arguments.nextPrimitive(this).getPersistence();
	}

	private Void setSeed(Arguments arguments) {
		ClassInstance instance = arguments.next();
		int seed = arguments.nextPrimitive(NumberDef.class).intValue();
		instance.asPrimitive(this).setSeed(seed);
		return null;
	}

	private int getSeed(Arguments arguments) {
		return arguments.nextPrimitive(this).getSeed();
	}

	private double getValue3(Arguments arguments) {
		Perlin noise = arguments.nextPrimitive(this);
		double x = arguments.nextPrimitive(NumberDef.class);
		double y = arguments.nextPrimitive(NumberDef.class);
		double z = arguments.nextPrimitive(NumberDef.class);
		return noise.getValue(x, y, z);
	}

	private double getValue1(Arguments arguments) {
		Perlin noise = arguments.nextPrimitive(this);
		Vec3d vec = arguments.nextPrimitive(Vector3Def.class);
		return noise.getValue(vec.x, vec.y, vec.z);
	}
}
