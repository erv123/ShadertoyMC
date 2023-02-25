package net.erv123.shadertoymc.arucas.definitions;

import kotlin.Unit;
import me.senseiwells.arucas.api.docs.annotations.*;
import me.senseiwells.arucas.builtin.NumberDef;
import me.senseiwells.arucas.builtin.StringDef;
import me.senseiwells.arucas.classes.CreatableDefinition;
import me.senseiwells.arucas.classes.instance.ClassInstance;
import me.senseiwells.arucas.core.Interpreter;
import me.senseiwells.arucas.utils.Arguments;
import me.senseiwells.arucas.utils.ConstructorFunction;
import me.senseiwells.arucas.utils.MemberFunction;
import me.senseiwells.arucas.utils.Util;
import net.erv123.shadertoymc.util.ScriptUtils;
import net.jlibnoise.generator.Perlin;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

@ClassDoc(
	name = "PerlinNoise",
	desc = "Class that is used to generate perlin noise",
	language = Util.Language.Java
)
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

	@ConstructorDoc(
		desc = "Used to construct a PerlinNoise class object that is necessary to generate perlin noise",
		examples = "perlin = new PerlinNoise();"
	)
	private Unit construct0(Arguments arguments) {
		ClassInstance instance = arguments.next();
		instance.setPrimitive(this, new Perlin());
		return null;
	}

	@ConstructorDoc(
		desc = "Used to construct a PerlinNoise class object that is necessary to generate perlin noise",
		params = {
			@ParameterDoc(type = NumberDef.class, name = "seed", desc = "A number that is used as a seed for calculating the noise"),
		},
		examples = "perlin = new PerlinNoise(1);"
	)
	private Unit construct1(Arguments arguments) {
		ClassInstance instance = arguments.next();
		int seed = arguments.nextPrimitive(NumberDef.class).intValue();
		Perlin perlin = new Perlin();
		instance.setPrimitive(this, perlin);
		perlin.setSeed(seed);
		return null;
	}

	@ConstructorDoc(
		desc = "Used to construct a PerlinNoise class object that is necessary to generate perlin noise",
		params = {
			@ParameterDoc(type = NumberDef.class, name = "seed", desc = "A number that is used as a seed for calculating the noise"),
			@ParameterDoc(type = NumberDef.class, name = "frequency", desc = {"Frequency of the first octave",
				"Recommended value 0.01-0.1 when using raw absolute coordinates"}),
		},
		examples = "perlin = new PerlinNoise(1,0.1);"
	)
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

	@ConstructorDoc(
		desc = "Used to construct a PerlinNoise class object that is necessary to generate perlin noise",
		params = {
			@ParameterDoc(type = NumberDef.class, name = "seed", desc = "A number that is used as a seed for calculating the noise"),
			@ParameterDoc(type = NumberDef.class, name = "frequency", desc = {"Frequency of the first octave",
				"Recommended value 0.01-0.1 when using raw absolute coordinates"}),
			@ParameterDoc(type = NumberDef.class, name = "octaveCount", desc = {"The number of octaves control the amount of detail of the Perlin noise.",
				"An octave is one of the coherent-noise functions in a series of coherent-noise functions that are added together to form Perlin noise.",
				"Should be an integer value, preferably between 1 and 9 (going much higher isn't too useful and will impact performance)"}),
		},
		examples = "perlin = new PerlinNoise(1,0.1,2);"
	)
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

	@ConstructorDoc(
		desc = "Used to construct a PerlinNoise class object that is necessary to generate perlin noise",
		params = {
			@ParameterDoc(type = NumberDef.class, name = "seed", desc = "A number that is used as a seed for calculating the noise"),
			@ParameterDoc(type = NumberDef.class, name = "frequency", desc = {"Frequency of the first octave",
				"Recommended value 0.01-0.1 when using raw absolute coordinates"}),
			@ParameterDoc(type = NumberDef.class, name = "octaveCount", desc = {"The number of octaves control the amount of detail of the Perlin noise.",
				"An octave is one of the coherent-noise functions in a series of coherent-noise functions that are added together to form Perlin noise.",
				"Should be an integer value, preferably between 1 and 9 (going much higher isn't too useful and will impact performance)"}),
			@ParameterDoc(type = NumberDef.class, name = "lacunarity", desc = {"The lacunarity specifies the frequency multiplier between successive octaves.",
				"For best results, set the lacunarity to a number between 1.5 and 3.5"}),
		},
		examples = "perlin = new PerlinNoise(1,0.1,2,2);"
	)
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

	@ConstructorDoc(
		desc = "Used to construct a PerlinNoise class object that is necessary to generate perlin noise",
		params = {
			@ParameterDoc(type = NumberDef.class, name = "seed", desc = "A number that is used as a seed for calculating the noise"),
			@ParameterDoc(type = NumberDef.class, name = "frequency", desc = {"Frequency of the first octave",
				"Recommended value 0.01-0.1 when using raw absolute coordinates"}),
			@ParameterDoc(type = NumberDef.class, name = "octaveCount", desc = {"The number of octaves control the amount of detail of the Perlin noise.",
				"An octave is one of the coherent-noise functions in a series of coherent-noise functions that are added together to form Perlin noise.",
				"Should be an integer value, preferably between 1 and 9 (going much higher isn't too useful and will impact performance)"}),
			@ParameterDoc(type = NumberDef.class, name = "lacunarity", desc = {"The lacunarity specifies the frequency multiplier between successive octaves.",
				"For best results, set the lacunarity to a number between 1.5 and 3.5"}),
			@ParameterDoc(type = NumberDef.class, name = "persistence", desc = {"The persistence value controls the roughness of the Perlin noise. Larger values produce rougher noise.",
				"The persistence value determines how quickly the amplitudes diminish for successive octaves.",
				"0.5 works well, but recommended values 0 -1"}),
		},
		examples = "perlin = new PerlinNoise(1,0.1,2,2,2);"
	)
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

	@ConstructorDoc(
		desc = "Used to construct a PerlinNoise class object that is necessary to generate perlin noise",
		params = {
			@ParameterDoc(type = NumberDef.class, name = "seed", desc = "A number that is used as a seed for calculating the noise"),
			@ParameterDoc(type = NumberDef.class, name = "frequency", desc = {"Frequency of the first octave",
				"Recommended value 0.01-0.1 when using raw absolute coordinates"}),
			@ParameterDoc(type = NumberDef.class, name = "octaveCount", desc = {"The number of octaves control the amount of detail of the Perlin noise.",
				"An octave is one of the coherent-noise functions in a series of coherent-noise functions that are added together to form Perlin noise.",
				"Should be an integer value, preferably between 1 and 9 (going much higher isn't too useful and will impact performance)"}),
			@ParameterDoc(type = NumberDef.class, name = "lacunarity", desc = {"The lacunarity specifies the frequency multiplier between successive octaves.",
				"For best results, set the lacunarity to a number between 1.5 and 3.5"}),
			@ParameterDoc(type = NumberDef.class, name = "persistence", desc = {"The persistence value controls the roughness of the Perlin noise. Larger values produce rougher noise.",
				"The persistence value determines how quickly the amplitudes diminish for successive octaves.",
				"0.5 works well, but recommended values 0 -1"}),
			@ParameterDoc(type = StringDef.class, name = "noiseQuality", desc = "Noise quality should be one of the following: fast, standard, best")
		},
		examples = "perlin = new PerlinNoise(1,0.1,2,2,2,\"best\");"
	)
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

	@FunctionDoc(name = "setFrequency",
		desc = "Used to change the frequency value for a PerlinNoise object",
		params = @ParameterDoc(type = NumberDef.class, name = "frequency", desc = {"Frequency of the first octave",
			"Recommended value 0.01-0.1 when using raw absolute coordinates"}),
		examples = "perlin.setFrequency(0.3);"
	)
	private Void setFrequency(Arguments arguments) {
		ClassInstance instance = arguments.next();
		double frequency = arguments.nextPrimitive(NumberDef.class);
		instance.asPrimitive(this).setFrequency(frequency);
		return null;
	}

	@FunctionDoc(name = "getFrequency",
		desc = "Used to query the frequency value for a PerlinNoise object",
		returns = @ReturnDoc(type = NumberDef.class, desc = "Current frequency for the PerlinNoise object"),
		examples = "perlin.getFrequency();"
	)
	private double getFrequency(Arguments arguments) {
		return arguments.nextPrimitive(this).getFrequency();
	}

	@FunctionDoc(name = "setLacunarity",
		desc = "Used to change the lacunarity value for a PerlinNoise object",
		params = @ParameterDoc(type = NumberDef.class, name = "lacunarity", desc = {"The lacunarity specifies the frequency multiplier between successive octaves.",
			"For best results, set the lacunarity to a number between 1.5 and 3.5"}),
		examples = "perlin.setLacunarity(2);"
	)
	private Void setLacunarity(Arguments arguments) {
		ClassInstance instance = arguments.next();
		int lacunarity = arguments.nextPrimitive(NumberDef.class).intValue();
		instance.asPrimitive(this).setOctaveCount(lacunarity);
		return null;
	}

	@FunctionDoc(name = "getLacunarity",
		desc = "Used to query the frequency value for a PerlinNoise object",
		returns = @ReturnDoc(type = NumberDef.class, desc = "Current lacunarity for the PerlinNoise object"),
		examples = "perlin.getLacunarity();"
	)
	private double getLacunarity(Arguments arguments) {
		return arguments.nextPrimitive(this).getLacunarity();
	}

	@FunctionDoc(name = "setNoiseQuality",
		desc = "Used to change the noise quality value for a PerlinNoise object",
		params = @ParameterDoc(type = StringDef.class, name = "noiseQuality", desc = "Noise quality should be one of the following: fast, standard, best"),
		examples = "perlin.setNoiseQuality(\"fast\");"
	)
	private Void setNoiseQuality(Arguments arguments) {
		ClassInstance instance = arguments.next();
		String quality = arguments.nextConstant();
		instance.asPrimitive(this).setNoiseQuality(ScriptUtils.stringToNoiseQuality(quality));
		return null;
	}

	@FunctionDoc(name = "getNoiseQuality",
		desc = "Used to query the noise quality value for a PerlinNoise object",
		returns = @ReturnDoc(type = StringDef.class, desc = "Current noise quality for the PerlinNoise object"),
		examples = "perlin.getNoiseQuality();"
	)
	private String getNoiseQuality(Arguments arguments) {
		return arguments.nextPrimitive(this).getNoiseQuality().name().toLowerCase(Locale.ROOT);
	}

	@FunctionDoc(name = "setOctaveCount",
		desc = "Used to change the octave count for a PerlinNoise object",
		params = @ParameterDoc(type = NumberDef.class, name = "octaveCount", desc = {"The number of octaves control the amount of detail of the Perlin noise.",
			"An octave is one of the coherent-noise functions in a series of coherent-noise functions that are added together to form Perlin noise.",
			"Should be an integer value, preferably between 1 and 9 (going much higher isn't too useful and will impact performance)"}),
		examples = "perlin.setOctaveCount(4);"
	)
	private Void setOctaveCount(Arguments arguments) {
		ClassInstance instance = arguments.next();
		int octaves = arguments.nextPrimitive(NumberDef.class).intValue();
		instance.asPrimitive(this).setOctaveCount(octaves);
		return null;
	}

	@FunctionDoc(name = "getOctaveCount",
		desc = "Used to query the octave count value for a PerlinNoise object",
		returns = @ReturnDoc(type = NumberDef.class, desc = "Current octave count for the PerlinNoise object"),
		examples = "perlin.getOctaveCount();"
	)
	private int getOctaveCount(Arguments arguments) {
		return arguments.nextPrimitive(this).getOctaveCount();
	}

	@FunctionDoc(name = "setPersistence",
		desc = "Used to change the persistence value for a PerlinNoise object",
		params = @ParameterDoc(type = NumberDef.class, name = "persistence", desc = {"The persistence value controls the roughness of the Perlin noise. Larger values produce rougher noise.",
			"The persistence value determines how quickly the amplitudes diminish for successive octaves.",
			"0.5 works well, but recommended values 0 -1"}),
		examples = "perlin.setPersistence(0.9);"
	)
	private Void setPersistence(Arguments arguments) {
		ClassInstance instance = arguments.next();
		double persistence = arguments.nextPrimitive(NumberDef.class);
		instance.asPrimitive(this).setPersistence(persistence);
		return null;
	}

	@FunctionDoc(name = "getPersistence",
		desc = "Used to query the persistence value for a PerlinNoise object",
		returns = @ReturnDoc(type = NumberDef.class, desc = "Current persistence for the PerlinNoise object"),
		examples = "perlin.getPersistence();"
	)
	private double getPersistence(Arguments arguments) {
		return arguments.nextPrimitive(this).getPersistence();
	}

	@FunctionDoc(name = "setSeed",
		desc = "Used to change the seed value for a PerlinNoise object",
		params = @ParameterDoc(type = NumberDef.class, name = "seed", desc = "A number that is used as a seed for calculating the noise"),
		examples = "perlin.setSeed(12345);"
	)
	private Void setSeed(Arguments arguments) {
		ClassInstance instance = arguments.next();
		int seed = arguments.nextPrimitive(NumberDef.class).intValue();
		instance.asPrimitive(this).setSeed(seed);
		return null;
	}

	@FunctionDoc(name = "getSeed",
		desc = "Used to query the seed value for a PerlinNoise object",
		returns = @ReturnDoc(type = NumberDef.class, desc = "Current seed for the PerlinNoise object"),
		examples = "perlin.getSeed();"
	)
	private int getSeed(Arguments arguments) {
		return arguments.nextPrimitive(this).getSeed();
	}

	@FunctionDoc(name = "getValue",
		desc = "This calculates and returns the PerlinNoise value at the specified coordinates",
		params = {
			@ParameterDoc(type = NumberDef.class, name = "x", desc = "The x coordinate"),
			@ParameterDoc(type = NumberDef.class, name = "y", desc = "The y coordinate"),
			@ParameterDoc(type = NumberDef.class, name = "z", desc = "The z coordinate")
		},
		returns = @ReturnDoc(type = NumberDef.class, desc = "Value between -1 to 1, but it is not guaranteed to stay within that range"),
		examples = "perlin.getValue(x,y,z);"
	)
	private double getValue3(Arguments arguments) {
		Perlin noise = arguments.nextPrimitive(this);
		double x = arguments.nextPrimitive(NumberDef.class);
		double y = arguments.nextPrimitive(NumberDef.class);
		double z = arguments.nextPrimitive(NumberDef.class);
		return noise.getValue(x, y, z);
	}

	@FunctionDoc(name = "getValue",
		desc = "This calculates and returns the PerlinNoise value at the specified coordinates",
		params = @ParameterDoc(type = Vector3Def.class, name = "vector", desc = "Vector3 object containing x, y, and z coordinates"),
		returns = @ReturnDoc(type = NumberDef.class, desc = "Value between -1 to 1, but it is not guaranteed to stay within that range"),
		examples = "perlin.getValue(new Vector3(x,y,z));"
	)
	private double getValue1(Arguments arguments) {
		Perlin noise = arguments.nextPrimitive(this);
		Vec3d vec = arguments.nextPrimitive(Vector3Def.class);
		return noise.getValue(vec.x, vec.y, vec.z);
	}
}
