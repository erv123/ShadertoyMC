package net.erv123.shadertoymc.arucas.definitions;

import kotlin.Unit;
import me.senseiwells.arucas.builtin.BooleanDef;
import me.senseiwells.arucas.builtin.NumberDef;
import me.senseiwells.arucas.classes.CreatableDefinition;
import me.senseiwells.arucas.classes.instance.ClassInstance;
import me.senseiwells.arucas.core.Interpreter;
import me.senseiwells.arucas.utils.Arguments;
import me.senseiwells.arucas.utils.ConstructorFunction;
import me.senseiwells.arucas.utils.MemberFunction;
import net.jlibnoise.filter.Voronoi;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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

	private Unit construct0(Arguments arguments) {
		ClassInstance instance = arguments.next();
		instance.setPrimitive(this, new Voronoi());
		return null;
	}

	private Unit construct1(Arguments arguments) {
		ClassInstance instance = arguments.next();
		int seed = arguments.nextPrimitive(NumberDef.class).intValue();
		Voronoi voronoi = new Voronoi();
		instance.setPrimitive(this, voronoi);
		voronoi.setSeed(seed);
		return null;
	}

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
	private Void setDisplacement(Arguments arguments) {
		ClassInstance instance = arguments.next();
		double displacement = arguments.nextPrimitive(NumberDef.class);
		instance.asPrimitive(this).setDisplacement(displacement);
		return null;
	}

	private double getDisplacement(Arguments arguments) {
		return arguments.nextPrimitive(this).getDisplacement();
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

	private Void setSeed(Arguments arguments) {
		ClassInstance instance = arguments.next();
		int seed = arguments.nextPrimitive(NumberDef.class).intValue();
		instance.asPrimitive(this).setSeed(seed);
		return null;
	}

	private int getSeed(Arguments arguments) {
		return arguments.nextPrimitive(this).getSeed();
	}

	private Void setEnableDistance(Arguments arguments) {
		ClassInstance instance = arguments.next();
		boolean enable = arguments.nextPrimitive(BooleanDef.class);
		instance.asPrimitive(this).setEnableDistance(enable);
		return null;
	}

	private boolean isEnableDistance(Arguments arguments) {
		return arguments.nextPrimitive(this).isEnableDistance();
	}

	private double getValue3(Arguments arguments) {
		Voronoi noise = arguments.nextPrimitive(this);
		double x = arguments.nextPrimitive(NumberDef.class);
		double y = arguments.nextPrimitive(NumberDef.class);
		double z = arguments.nextPrimitive(NumberDef.class);
		return noise.getValue(x, y, z);
	}

	private double getValue1(Arguments arguments) {
		Voronoi noise = arguments.nextPrimitive(this);
		Vec3d vec = arguments.nextPrimitive(Vector3Def.class);
		return noise.getValue(vec.x, vec.y, vec.z);
	}
}