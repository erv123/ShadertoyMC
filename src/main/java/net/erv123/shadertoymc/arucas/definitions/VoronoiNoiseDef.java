package net.erv123.shadertoymc.arucas.definitions;

import kotlin.Unit;
import me.senseiwells.arucas.api.docs.annotations.*;
import me.senseiwells.arucas.builtin.BooleanDef;
import me.senseiwells.arucas.builtin.NumberDef;
import me.senseiwells.arucas.classes.CreatableDefinition;
import me.senseiwells.arucas.classes.instance.ClassInstance;
import me.senseiwells.arucas.core.Interpreter;
import me.senseiwells.arucas.functions.builtin.ConstructorFunction;
import me.senseiwells.arucas.functions.builtin.MemberFunction;
import me.senseiwells.arucas.utils.Arguments;
import me.senseiwells.arucas.utils.Util;
import net.jlibnoise.filter.Voronoi;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@ClassDoc(
	name = "VoronoiNoise",
	desc = "Class that is used to generate voronoi noise.",
	language = Util.Language.Java
)
public class VoronoiNoiseDef extends CreatableDefinition<Voronoi> {
	public VoronoiNoiseDef(@NotNull Interpreter interpreter) {
		super("VoronoiNoise", interpreter);
	}

	@Nullable
	@Override
	public List<MemberFunction> defineMethods() {
		return List.of(
			MemberFunction.of("setSeed", 1, this::setSeed),
			MemberFunction.of("getSeed", this::getSeed),
			MemberFunction.of("setEnableDistance", 1, this::setEnableDistance),
			MemberFunction.of("isEnableDistance", this::isEnableDistance),
			MemberFunction.of("setFrequency", 1, this::setFrequency),
			MemberFunction.of("getFrequency", this::getFrequency),
			MemberFunction.of("setDisplacement", 1, this::setDisplacement),
			MemberFunction.of("getDisplacement", this::getDisplacement),
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
			ConstructorFunction.of(4, this::construct4)
		);
	}

	@ConstructorDoc(
		desc = "Used to construct a VoronoiNoise class object that is necessary to generate voronoi noise.",
		examples = "noise = new VoronoiNoise();"
	)
	private Unit construct0(Arguments arguments) {
		ClassInstance instance = arguments.next();
		instance.setPrimitive(this, new Voronoi());
		return null;
	}

	@ConstructorDoc(
		desc = "Used to construct a VoronoiNoise class object that is necessary to generate voronoi noise.",
		params = {
			@ParameterDoc(type = NumberDef.class, name = "seed", desc = "A number that is used as a seed for calculating the noise.")
		},
		examples = "noise = new VoronoiNoise(1);"
	)
	private Unit construct1(Arguments arguments) {
		ClassInstance instance = arguments.next();
		int seed = arguments.nextPrimitive(NumberDef.class).intValue();
		Voronoi voronoi = new Voronoi();
		instance.setPrimitive(this, voronoi);
		voronoi.setSeed(seed);
		return null;
	}

	@ConstructorDoc(
		desc = "Used to construct a VoronoiNoise class object that is necessary to generate voronoi noise.",
		params = {
			@ParameterDoc(type = NumberDef.class, name = "seed", desc = "A number that is used as a seed for calculating the noise."),
			@ParameterDoc(type = BooleanDef.class, name = "enableDistance", desc = "Used to control if the output includes the distance to the point.")
		},
		examples = "noise = new VoronoiNoise(1, false);"
	)
	private Unit construct2(Arguments arguments) {
		ClassInstance instance = arguments.next();
		int seed = arguments.nextPrimitive(NumberDef.class).intValue();
		boolean enable = arguments.nextPrimitive(BooleanDef.class);
		Voronoi voronoi = new Voronoi();
		instance.setPrimitive(this, voronoi);
		voronoi.setSeed(seed);
		voronoi.setEnableDistance(enable);
		return null;
	}

	@ConstructorDoc(
		desc = "Used to construct a VoronoiNoise class object that is necessary to generate voronoi noise.",
		params = {
			@ParameterDoc(type = NumberDef.class, name = "seed", desc = "A number that is used as a seed for calculating the noise."),
			@ParameterDoc(type = BooleanDef.class, name = "enableDistance", desc = "Used to control if the output includes the distance to the point."),
			@ParameterDoc(
				type = NumberDef.class,
				name = "frequency",
				desc = {
					"A number that represents how close voronoi points will are located to each other.",
					"Recommended value 0.05 - 0.3 when using raw absolute coordinates."
				}
			)
		},
		examples = "noise = new VoronoiNoise(1, false, 0.1);"
	)
	private Unit construct3(Arguments arguments) {
		ClassInstance instance = arguments.next();
		int seed = arguments.nextPrimitive(NumberDef.class).intValue();
		boolean enable = arguments.nextPrimitive(BooleanDef.class);
		double frequency = arguments.nextPrimitive(NumberDef.class);
		Voronoi voronoi = new Voronoi();
		instance.setPrimitive(this, voronoi);
		voronoi.setSeed(seed);
		voronoi.setEnableDistance(enable);
		voronoi.setFrequency(frequency);
		return null;
	}

	@ConstructorDoc(
		desc = "Used to construct a VoronoiNoise class object that is necessary to generate voronoi noise.",
		params = {
			@ParameterDoc(type = NumberDef.class, name = "seed", desc = "A number that is used as a seed for calculating the noise."),
			@ParameterDoc(type = BooleanDef.class, name = "enableDistance", desc = "Used to control if the output includes the distance to the point."),
			@ParameterDoc(
				type = NumberDef.class,
				name = "frequency",
				desc = {
					"A number that represents how close voronoi points will are located to each other.",
					"Recommended value 0.05-0.3 when using raw absolute coordinates."
				}
			),
			@ParameterDoc(
				type = NumberDef.class,
				name = "displacement",
				desc = {
					"The displacement value controls the range of random values to assign to each cell.",
					"The range of random values is +/- the displacement value."
				}
			)
		},
		examples = "noise = new VoronoiNoise(1, false, 0.1, 1);"
	)
	private Unit construct4(Arguments arguments) {
		ClassInstance instance = arguments.next();
		int seed = arguments.nextPrimitive(NumberDef.class).intValue();
		boolean enable = arguments.nextPrimitive(BooleanDef.class);
		double frequency = arguments.nextPrimitive(NumberDef.class);
		double displacement = arguments.nextPrimitive(NumberDef.class);
		Voronoi voronoi = new Voronoi();
		instance.setPrimitive(this, voronoi);
		voronoi.setSeed(seed);
		voronoi.setEnableDistance(enable);
		voronoi.setFrequency(frequency);
		voronoi.setDisplacement(displacement);
		return null;
	}

	@FunctionDoc(
		name = "setDisplacement",
		desc = "Used to change the displacement value for a VoronoiNoise object",
		params = @ParameterDoc(
			type = NumberDef.class,
			name = "displacement",
			desc = {
				"The displacement value controls the range of random values to assign to each cell.",
				"The range of random values is +/- the displacement value."
			}
		),
		examples = "noise.setDisplacement(0.1);"
	)
	private Void setDisplacement(Arguments arguments) {
		ClassInstance instance = arguments.next();
		double displacement = arguments.nextPrimitive(NumberDef.class);
		instance.asPrimitive(this).setDisplacement(displacement);
		return null;
	}

	@FunctionDoc(
		name = "getDisplacement",
		desc = "Used to query the displacement value for a VoronoiNoise object.",
		returns = @ReturnDoc(type = NumberDef.class, desc = "Current displacement for the VoronoiNoise object."),
		examples = "noise.getDisplacement();"
	)
	private double getDisplacement(Arguments arguments) {
		return arguments.nextPrimitive(this).getDisplacement();
	}

	@FunctionDoc(
		name = "setFrequency",
		desc = "Used to change the frequency value for a VoronoiNoise object.",
		params = @ParameterDoc(
			type = NumberDef.class,
			name = "frequency",
			desc = {
				"A number that represents how close voronoi points will are located to each other.",
				"Recommended value 0.05 - 0.3 when using raw absolute coordinates."
			}
		),
		examples = "noise.setFrequency(0.3);"
	)
	private Void setFrequency(Arguments arguments) {
		ClassInstance instance = arguments.next();
		double frequency = arguments.nextPrimitive(NumberDef.class);
		instance.asPrimitive(this).setFrequency(frequency);
		return null;
	}

	@FunctionDoc(
		name = "getFrequency",
		desc = "Used to query the frequency value for a VoronoiNoise object.",
		returns = @ReturnDoc(type = NumberDef.class, desc = "Current frequency for the VoronoiNoise object."),
		examples = "noise.getFrequency();"
	)
	private double getFrequency(Arguments arguments) {
		return arguments.nextPrimitive(this).getFrequency();
	}

	@FunctionDoc(
		name = "setSeed",
		desc = "Used to change the seed value for a VoronoiNoise object.",
		params = @ParameterDoc(type = NumberDef.class, name = "seed", desc = "A number that is used as a seed for calculating the noise."),
		examples = "noise.setSeed(12345);"
	)
	private Void setSeed(Arguments arguments) {
		ClassInstance instance = arguments.next();
		int seed = arguments.nextPrimitive(NumberDef.class).intValue();
		instance.asPrimitive(this).setSeed(seed);
		return null;
	}

	@FunctionDoc(
		name = "getSeed",
		desc = "Used to query the seed value for a VoronoiNoise object.",
		returns = @ReturnDoc(type = NumberDef.class, desc = "Current seed for the VoronoiNoise object."),
		examples = "noise.getSeed();"
	)
	private int getSeed(Arguments arguments) {
		return arguments.nextPrimitive(this).getSeed();
	}

	@FunctionDoc(
		name = "setEnableDistance",
		desc = "Used to change the if including the distance in the value calculation is enabled.",
		params = @ParameterDoc(
			type = NumberDef.class,
			name = "displacement",
			desc = {
				"The displacement value controls the range of random values to assign to each cell.",
				"The range of random values is +/- the displacement value."
			}
		),
		examples = "noise.setDisplacement(0.1);"
	)
	private Void setEnableDistance(Arguments arguments) {
		ClassInstance instance = arguments.next();
		boolean enable = arguments.nextPrimitive(BooleanDef.class);
		instance.asPrimitive(this).setEnableDistance(enable);
		return null;
	}

	@FunctionDoc(
		name = "isEnableDistance",
		desc = "Used to query if the distance is enabled for a VoronoiNoise object.",
		returns = @ReturnDoc(type = BooleanDef.class, desc = "true if distance is enabled, false if it isn't."),
		examples = "noise.isEnableDistance();"
	)
	private boolean isEnableDistance(Arguments arguments) {
		return arguments.nextPrimitive(this).isEnableDistance();
	}

	@FunctionDoc(
		name = "getValue",
		desc = "This calculates and returns the VoronoiNoise value at the specified coordinates.",
		params = {
			@ParameterDoc(type = NumberDef.class, name = "x", desc = "The x coordinate."),
			@ParameterDoc(type = NumberDef.class, name = "y", desc = "The y coordinate."),
			@ParameterDoc(type = NumberDef.class, name = "z", desc = "The z coordinate.")
		},
		returns = @ReturnDoc(type = NumberDef.class, desc = "Value from -displacement to +displacement."),
		examples = "noise.getValue(x, y, z);"
	)
	private double getValue3(Arguments arguments) {
		Voronoi noise = arguments.nextPrimitive(this);
		double x = arguments.nextPrimitive(NumberDef.class);
		double y = arguments.nextPrimitive(NumberDef.class);
		double z = arguments.nextPrimitive(NumberDef.class);
		return noise.getValue(x, y, z);
	}

	@FunctionDoc(
		name = "getValue",
		desc = "This calculates and returns the VoronoiNoise value at the specified coordinates.",
		params = @ParameterDoc(type = Vector3Def.class, name = "vector", desc = "Vector3 object containing x, y, and z coordinates."),
		returns = @ReturnDoc(type = NumberDef.class, desc = "Value from -displacement to +displacement."),
		examples = "noise.getValue(new Vector3(x, y, z));"
	)
	private double getValue1(Arguments arguments) {
		Voronoi noise = arguments.nextPrimitive(this);
		Vec3d vec = arguments.nextPrimitive(Vector3Def.class);
		return noise.getValue(vec.x, vec.y, vec.z);
	}
}