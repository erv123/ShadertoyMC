package net.erv123.shadertoymc.arucas.definitions;

import kotlin.Unit;
import me.senseiwells.arucas.builtin.NumberDef;
import me.senseiwells.arucas.classes.PrimitiveDefinition;
import me.senseiwells.arucas.classes.instance.ClassInstance;
import me.senseiwells.arucas.core.Interpreter;
import me.senseiwells.arucas.utils.Arguments;
import me.senseiwells.arucas.utils.BuiltInFunction;
import me.senseiwells.arucas.utils.ConstructorFunction;
import net.erv123.shadertoymc.arucas.impl.VoronoiWrapper;
import net.jlibnoise.filter.Voronoi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VoronoiNoiseDef extends PrimitiveDefinition<VoronoiWrapper> {
	// Unique to every script.
	private final Voronoi noise = new Voronoi();

	public VoronoiNoiseDef(@NotNull Interpreter interpreter) {
		super("VoronoiNoise", interpreter);
	}

	@Nullable
	@Override
	public List<BuiltInFunction> defineStaticMethods() {
		return List.of(
			BuiltInFunction.of("setDisplacement", 1, this::setDisplacement),
			BuiltInFunction.of("getDisplacement", this::getDisplacement),
			BuiltInFunction.of("setFrequency", 1, this::setFrequency),
			BuiltInFunction.of("getFrequency", this::getFrequency),
			BuiltInFunction.of("setSeed", 1, this::setSeed),
			BuiltInFunction.of("getSeed", this::getSeed),
			BuiltInFunction.of("getValue", 3, this::getValue)
		);
	}

	@Nullable
	@Override
	public List<ConstructorFunction> defineConstructors() {
		return List.of(
			ConstructorFunction.of(0, this::construct0),
			ConstructorFunction.of(1, this::construct1),
			ConstructorFunction.of(2, this::construct2),
			ConstructorFunction.of(3, this::construct3)
		);
	}

	private Unit construct0(Arguments arguments) {
		ClassInstance instance = arguments.next();
		instance.setPrimitive(this,new VoronoiWrapper());
		return null;
	}
	private Unit construct1(Arguments arguments) {
		ClassInstance instance = arguments.next();
		double seed = arguments.nextPrimitive(NumberDef.class);
		instance.setPrimitive(this, new VoronoiWrapper(seed));
		return null;
	}
	private Unit construct2(Arguments arguments) {
		ClassInstance instance = arguments.next();
		double seed = arguments.nextPrimitive(NumberDef.class);
		double frequency = arguments.nextPrimitive(NumberDef.class);
		instance.setPrimitive(this, new VoronoiWrapper(seed, frequency));
		return null;
	}
	private Unit construct3(Arguments arguments) {
		ClassInstance instance = arguments.next();
		double seed = arguments.nextPrimitive(NumberDef.class);
		double frequency = arguments.nextPrimitive(NumberDef.class);
		double displacement = arguments.nextPrimitive(NumberDef.class);
		instance.setPrimitive(this, new VoronoiWrapper(seed, frequency, displacement));
		return null;
	}
	private Void setDisplacement(Arguments arguments) {
		double displacement = arguments.nextPrimitive(NumberDef.class);
		this.noise.setDisplacement(displacement);
		return null;
	}

	private double getDisplacement(Arguments arguments) {
		return this.noise.getDisplacement();
	}

	private Void setFrequency(Arguments arguments) {
		double frequency = arguments.nextPrimitive(NumberDef.class);
		this.noise.setFrequency(frequency);
		return null;
	}

	private double getFrequency(Arguments arguments) {
		return this.noise.getFrequency();
	}

	private Void setSeed(Arguments arguments) {
		int seed = arguments.nextPrimitive(NumberDef.class).intValue();
		this.noise.setSeed(seed);
		return null;
	}

	private int getSeed(Arguments arguments) {
		return this.noise.getSeed();
	}

	private double getValue(Arguments arguments) {
		double x = arguments.nextPrimitive(NumberDef.class);
		double y = arguments.nextPrimitive(NumberDef.class);
		double z = arguments.nextPrimitive(NumberDef.class);
		return this.noise.getValue(x, y, z);
	}
}