package net.erv123.shadertoymc.arucas.definitions;

import kotlin.Unit;
import me.senseiwells.arucas.builtin.NumberDef;
import me.senseiwells.arucas.classes.PrimitiveDefinition;
import me.senseiwells.arucas.core.Interpreter;
import me.senseiwells.arucas.utils.Arguments;
import me.senseiwells.arucas.utils.BuiltInFunction;
import net.jlibnoise.filter.Voronoi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VoronoiNoiseDef extends PrimitiveDefinition<Unit> {
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

	private Void setDisplacement(Arguments arguments) {
		double lacunarity = arguments.nextPrimitive(NumberDef.class);
		this.noise.setDisplacement(lacunarity);
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