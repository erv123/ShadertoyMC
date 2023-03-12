package net.erv123.shadertoymc.arucas.definitions;

import kotlin.Unit;
import me.senseiwells.arucas.api.docs.annotations.*;
import me.senseiwells.arucas.builtin.ListDef;
import me.senseiwells.arucas.builtin.NumberDef;
import me.senseiwells.arucas.classes.CreatableDefinition;
import me.senseiwells.arucas.classes.instance.ClassInstance;
import me.senseiwells.arucas.core.Interpreter;
import me.senseiwells.arucas.exceptions.RuntimeError;
import me.senseiwells.arucas.functions.builtin.BuiltInFunction;
import me.senseiwells.arucas.functions.builtin.ConstructorFunction;
import me.senseiwells.arucas.functions.builtin.MemberFunction;
import me.senseiwells.arucas.utils.Arguments;
import me.senseiwells.arucas.utils.LocatableTrace;
import me.senseiwells.arucas.utils.Util;
import me.senseiwells.arucas.utils.impl.ArucasList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@ClassDoc(
	name = "Vector3",
	desc = "A 3 dimensional vector class, overrides +, -, *, /, ^, and [] operators, only bracket access.",
	language = Util.Language.Java
)
public class Vector3Def extends CreatableDefinition<Vec3d> {
	public Vector3Def(@NotNull Interpreter interpreter) {
		super("Vector3", interpreter);
	}

	@Nullable
	@Override
	protected Object minus(@NotNull ClassInstance instance, @NotNull Interpreter interpreter, @NotNull LocatableTrace trace) {
		return instance.asPrimitive(this).negate();
	}

	@Nullable
	@Override
	protected Object minus(@NotNull ClassInstance instance, @NotNull Interpreter interpreter, @NotNull ClassInstance other, @NotNull LocatableTrace trace) {
		Vec3d vec2 = other.getPrimitive(this);
		if (vec2 == null) {
			throw new RuntimeError("Expected a vector to subtract");
		}
		return instance.asPrimitive(this).subtract(vec2);
	}

	@Nullable
	@Override
	protected Object plus(@NotNull ClassInstance instance, @NotNull Interpreter interpreter, @NotNull LocatableTrace trace) {
		return instance.asPrimitive(this);
	}

	@Nullable
	@Override
	protected Object plus(@NotNull ClassInstance instance, @NotNull Interpreter interpreter, @NotNull ClassInstance other, @NotNull LocatableTrace trace) {
		Vec3d vec2 = other.getPrimitive(this);
		if (vec2 == null) {
			throw new RuntimeError("Expected a vector to add");
		}
		return instance.asPrimitive(this).add(vec2);
	}

	@Nullable
	@Override
	protected Object multiply(@NotNull ClassInstance instance, @NotNull Interpreter interpreter, @NotNull ClassInstance other, @NotNull LocatableTrace trace) {
		Vec3d vec2 = other.getPrimitive(this);
		if (vec2 == null) {
			throw new RuntimeError("Expected a vector to multiply with");
		}
		return instance.asPrimitive(this).multiply(vec2);
	}

	@Nullable
	@Override
	protected Object divide(@NotNull ClassInstance instance, @NotNull Interpreter interpreter, @NotNull ClassInstance other, @NotNull LocatableTrace trace) {
		Vec3d vec1 = instance.asPrimitive(this);
		Vec3d vec2 = other.getPrimitive(this);
		if (vec2 == null) {
			throw new RuntimeError("Expected a vector to divide with");
		}
		return vec1.multiply(1 / vec2.x, 1 / vec2.y, 1 / vec2.z);
	}

	@Nullable
	@Override
	protected Object power(@NotNull ClassInstance instance, @NotNull Interpreter interpreter, @NotNull ClassInstance other, @NotNull LocatableTrace trace) {
		Vec3d vec = instance.asPrimitive(this);
		Double num = other.getPrimitive(NumberDef.class);
		if (num == null) {
			throw new RuntimeError("Expected a number as a power");
		}
		return new Vec3d(Math.pow(vec.x, num), Math.pow(vec.y, num), Math.pow(vec.z, num));
	}

	@NotNull
	@Override
	public ClassInstance bracketAccess(@NotNull ClassInstance instance, @NotNull Interpreter interpreter, @NotNull ClassInstance index, @NotNull LocatableTrace trace) {
		Vec3d vec = instance.asPrimitive(this);
		Double i = index.getPrimitive(NumberDef.class);
		if (i == null) {
			throw new RuntimeError("Expected an integer index");
		}
		return switch (i.intValue()) {
			case 0 -> interpreter.convertValue(vec.x);
			case 1 -> interpreter.convertValue(vec.y);
			case 2 -> interpreter.convertValue(vec.z);
			default -> throw new RuntimeError("Index " + i + " out of bounds for vector!");
		};
	}

	@NotNull
	@Override
	public String toString(@NotNull ClassInstance instance, @NotNull Interpreter interpreter, @NotNull LocatableTrace trace) {
		Vec3d vec = instance.asPrimitive(this);
		return "(" + vec.x + ", " + vec.y + ", " + vec.z + ")";
	}

	@Override
	public List<ConstructorFunction> defineConstructors() {
		return List.of(
			ConstructorFunction.of(1, this::construct1),
			ConstructorFunction.of(3, this::construct3)
		);
	}

	@ConstructorDoc(
		desc = "Used to construct a Vector3 object from 3 coordinates.",
		params = {
			@ParameterDoc(type = NumberDef.class, name = "x", desc = "The x coordinate."),
			@ParameterDoc(type = NumberDef.class, name = "y", desc = "The y coordinate."),
			@ParameterDoc(type = NumberDef.class, name = "z", desc = "The z coordinate.")
		},
		examples = "vec = new Vector3(1, 2, 3);"
	)
	private Unit construct3(Arguments arguments) {
		ClassInstance instance = arguments.next();
		double x = arguments.nextPrimitive(NumberDef.class);
		double y = arguments.nextPrimitive(NumberDef.class);
		double z = arguments.nextPrimitive(NumberDef.class);
		instance.setPrimitive(this, new Vec3d(x, y, z));
		return null;
	}

	@ConstructorDoc(
		desc = "Used to construct a Vector3 object from a list of 3 number values.",
		params = {
			@ParameterDoc(type = ListDef.class, name = "coordinatesList", desc = "A list containing 3 number values.")
		},
		examples = "vec = new Vector3([1, 2, 3]);"
	)
	private Unit construct1(Arguments arguments) {
		ClassInstance instance = arguments.next();
		ArucasList list = arguments.nextPrimitive(ListDef.class);
		if (list.size() != 3) {
			throw new RuntimeError("Expected a list with 3 coordinates");
		}
		
		double[] coords = new double[3];
		for (int i = 0; i < 3; i++) {
			Double value = list.get(i).getPrimitive(NumberDef.class);
			if (value == null) {
				throw new RuntimeError("Expected a number at index " + i);
			}
			coords[i] = value;
		}
		instance.setPrimitive(this, new Vec3d(coords[0], coords[1], coords[2]));
		return null;
	}

	@Nullable
	@Override
	public List<MemberFunction> defineMethods() {
		return List.of(
			MemberFunction.of("sub", 1, this::subtract1),
			MemberFunction.of("sub", 3, this::subtract3),
			MemberFunction.of("subScalar", 1, this::subtractScalar),
			MemberFunction.of("add", 1, this::add1),
			MemberFunction.of("add", 3, this::add3),
			MemberFunction.of("addScalar", 1, this::addScalar),
			MemberFunction.of("multiply", 1, this::multiply1),
			MemberFunction.of("multiply", 3, this::multiply3),
			MemberFunction.of("multiplyScalar", 1, this::multiplyScalar),
			MemberFunction.of("divide", 1, this::divide1),
			MemberFunction.of("divide", 3, this::divide3),
			MemberFunction.of("divideScalar", 1, this::divideScalar),
			MemberFunction.of("normalize", 0, this::normalize),
			MemberFunction.of("dot", 1, this::dotProduct),
			MemberFunction.of("cross", 1, this::crossProduct),
			MemberFunction.of("distanceTo", 1, this::distanceTo),
			MemberFunction.of("distanceToSquared", 1, this::squaredDistanceTo),
			MemberFunction.of("length", this::length),
			MemberFunction.of("lengthSquared", this::lengthSquared),
			MemberFunction.of("horizontalLength", this::horizontalLength),
			MemberFunction.of("horizontalLengthSquared", this::horizontalLengthSquared),
			MemberFunction.of("lerp", 2, this::lerp),
			MemberFunction.of("rotateX", 1, this::rotateX),
			MemberFunction.of("rotateY", 1, this::rotateY),
			MemberFunction.of("rotateZ", 1, this::rotateZ),
			MemberFunction.of("floor", this::floor),
			MemberFunction.of("ceil", this::ceil),
			MemberFunction.of("round", this::round),
			MemberFunction.of("getX", this::getX),
			MemberFunction.of("getY", this::getY),
			MemberFunction.of("getZ", this::getZ)
		);
	}

	@FunctionDoc(
		name = "sub",
		desc = "Used to subtract another Vector3.",
		params = @ParameterDoc(type = Vector3Def.class, name = "vector", desc = "A Vector3 object to subtract."),
		examples = "vec.sub(new Vector3(1, 2, 3));"
	)
	public Vec3d subtract1(Arguments arguments) {
		Vec3d vec1 = arguments.nextPrimitive(this);
		Vec3d vec2 = arguments.nextPrimitive(this);
		return vec1.subtract(vec2);
	}

	@FunctionDoc(
		name = "sub",
		desc = "Used to subtract 3 separate number values.",
		params = {
			@ParameterDoc(type = NumberDef.class, name = "x", desc = "The value to subtract from vector x."),
			@ParameterDoc(type = NumberDef.class, name = "y", desc = "The value to subtract from vector y."),
			@ParameterDoc(type = NumberDef.class, name = "z", desc = "The value to subtract from vector z.")
		},
		examples = "vec.sub(1,2,3);"
	)
	public Vec3d subtract3(Arguments arguments) {
		Vec3d vec1 = arguments.nextPrimitive(this);
		double x = arguments.nextPrimitive(NumberDef.class);
		double y = arguments.nextPrimitive(NumberDef.class);
		double z = arguments.nextPrimitive(NumberDef.class);
		return vec1.subtract(x, y, z);
	}

	@FunctionDoc(
		name = "sub",
		desc = "Used to subtract a number from each of the vector coordinates.",
		params = {
			@ParameterDoc(type = NumberDef.class, name = "scalar", desc = "The value to subtract from vector x, y, and z."),
		},
		examples = "vec.sub(1);"
	)
	public Vec3d subtractScalar(Arguments arguments) {
		Vec3d vec1 = arguments.nextPrimitive(this);
		double num = arguments.nextPrimitive(NumberDef.class);
		return vec1.subtract(num, num, num);
	}

	@FunctionDoc(
		name = "add",
		desc = "Used to add another Vector3.",
		params = @ParameterDoc(type = Vector3Def.class, name = "vector", desc = "A Vector3 object to add."),
		examples = "vec.add(new Vector3(1, 2, 3));"
	)
	public Vec3d add1(Arguments arguments) {
		Vec3d vec1 = arguments.nextPrimitive(this);
		Vec3d vec2 = arguments.nextPrimitive(this);
		return vec1.add(vec2);
	}

	@FunctionDoc(
		name = "add",
		desc = "Used to add 3 separate number values.",
		params = {
			@ParameterDoc(type = NumberDef.class, name = "x", desc = "The value to add to vector x."),
			@ParameterDoc(type = NumberDef.class, name = "y", desc = "The value to add to vector y."),
			@ParameterDoc(type = NumberDef.class, name = "z", desc = "The value to add to vector z.")
		},
		examples = "vec.add(1, 2, 3);"
	)
	public Vec3d add3(Arguments arguments) {
		Vec3d vec1 = arguments.nextPrimitive(this);
		double x = arguments.nextPrimitive(NumberDef.class);
		double y = arguments.nextPrimitive(NumberDef.class);
		double z = arguments.nextPrimitive(NumberDef.class);
		return vec1.add(x, y, z);
	}

	@FunctionDoc(
		name = "addScalar",
		desc = "Used to add a number to each of the vector coordinates.",
		params = @ParameterDoc(type = NumberDef.class, name = "scalar", desc = "The value to add to vector x, y, and z."),
		examples = "vec.add(1);"
	)
	public Vec3d addScalar(Arguments arguments) {
		Vec3d vec1 = arguments.nextPrimitive(this);
		double num = arguments.nextPrimitive(NumberDef.class);
		return vec1.add(num, num, num);
	}

	@FunctionDoc(
		name = "multiply",
		desc = "Used to multiply with another Vector3.",
		params = @ParameterDoc(type = Vector3Def.class, name = "vector", desc = "A Vector3 object to multiply with."),
		examples = "vec.multiply(new Vector3(1, 2, 3));"
	)
	public Vec3d multiply1(Arguments arguments) {
		Vec3d vec1 = arguments.nextPrimitive(this);
		Vec3d vec2 = arguments.nextPrimitive(this);
		return vec1.multiply(vec2);
	}

	@FunctionDoc(
		name = "multiply",
		desc = "Used to multiply each of the vector coordinates with a separate number value.",
		params = {
			@ParameterDoc(type = NumberDef.class, name = "x", desc = "The value to multiply vector x with."),
			@ParameterDoc(type = NumberDef.class, name = "y", desc = "The value to multiply vector y with."),
			@ParameterDoc(type = NumberDef.class, name = "z", desc = "The value to multiply vector z with.")
		},
		examples = "vec.multiply(1, 2, 3);"
	)
	public Vec3d multiply3(Arguments arguments) {
		Vec3d vec1 = arguments.nextPrimitive(this);
		double x = arguments.nextPrimitive(NumberDef.class);
		double y = arguments.nextPrimitive(NumberDef.class);
		double z = arguments.nextPrimitive(NumberDef.class);
		return vec1.multiply(x, y, z);
	}

	@FunctionDoc(
		name = "multiplyScalar",
		desc = "Used to multiply to each of the vector coordinates with a single number.",
		params = @ParameterDoc(type = NumberDef.class, name = "scalar", desc = "The value to multiply vector x, y, and z with."),
		examples = "vec.multiply(1);"
	)
	public Vec3d multiplyScalar(Arguments arguments) {
		Vec3d vec1 = arguments.nextPrimitive(this);
		double num = arguments.nextPrimitive(NumberDef.class);
		return vec1.multiply(num, num, num);
	}

	@FunctionDoc(
		name = "divide",
		desc = "Used to divide by another Vector3.",
		params = @ParameterDoc(type = Vector3Def.class, name = "vector", desc = "A Vector3 object to divide by."),
		examples = "vec.divide(new Vector3(1, 2, 3));"
	)
	public Vec3d divide1(Arguments arguments) {
		Vec3d vec1 = arguments.nextPrimitive(this);
		Vec3d vec2 = arguments.nextPrimitive(this);
		return vec1.multiply(1 / vec2.x, 1 / vec2.y, 1 / vec2.z);
	}

	@FunctionDoc(
		name = "divide",
		desc = "Used to divide each of the vector coordinates by a separate number value.",
		params = {
			@ParameterDoc(type = NumberDef.class, name = "x", desc = "The value to divide vector x by."),
			@ParameterDoc(type = NumberDef.class, name = "y", desc = "The value to divide vector y by."),
			@ParameterDoc(type = NumberDef.class, name = "z", desc = "The value to divide vector z by.")
		},
		examples = "vec.divide(1, 2, 3);"
	)
	public Vec3d divide3(Arguments arguments) {
		Vec3d vec1 = arguments.nextPrimitive(this);
		double x = arguments.nextPrimitive(NumberDef.class);
		double y = arguments.nextPrimitive(NumberDef.class);
		double z = arguments.nextPrimitive(NumberDef.class);
		return vec1.multiply(1 / x, 1 / y, 1 / z);
	}

	@FunctionDoc(
		name = "divideScalar",
		desc = "Used to divide each of the vector coordinates by a single number.",
		params = @ParameterDoc(type = NumberDef.class, name = "scalar", desc = "The value to divide vector x, y, and z by."),
		examples = "vec.multiply(1);"
	)
	public Vec3d divideScalar(Arguments arguments) {
		Vec3d vec1 = arguments.nextPrimitive(this);
		double num = arguments.nextPrimitive(NumberDef.class);
		return vec1.multiply(1 / num, 1 / num, 1 / num);
	}

	@FunctionDoc(
		name = "normalize",
		desc = "Returns a new vector in the same direction but with a length of 1.",
		returns = @ReturnDoc(type = Vector3Def.class, desc = "The normalized vector of this vector."),
		examples = "vec = vec.normalize();"
	)
	private Vec3d normalize(Arguments arguments) {
		return arguments.nextPrimitive(this).normalize();
	}

	@FunctionDoc(
		name = "dot",
		desc = "Returns the dot product of this vector and the given vector.",
		params = @ParameterDoc(type = Vector3Def.class, name = "vector", desc = "The other Vector3."),
		returns = @ReturnDoc(type = NumberDef.class, desc = "The dot product."),
		examples = "dot = vec.dot(new Vector3(1, 1, 1));"
	)
	public double dotProduct(Arguments arguments) {
		return arguments.nextPrimitive(this).dotProduct(arguments.nextPrimitive(this));
	}

	@FunctionDoc(
		name = "cross",
		desc = "Returns the cross product of this vector and the given vector.",
		params = @ParameterDoc(type = Vector3Def.class, name = "vector", desc = "The other Vector3."),
		returns = @ReturnDoc(type = NumberDef.class, desc = "The cross product."),
		examples = "cross = vec.cross(new Vector3(1, 1, 1));"
	)
	public Vec3d crossProduct(Arguments arguments) {
		return arguments.nextPrimitive(this).crossProduct(arguments.nextPrimitive(this));
	}


	@FunctionDoc(
		name = "distanceTo",
		desc = "Returns the euclidean distance between this vector and the given vector.",
		params = @ParameterDoc(type = Vector3Def.class, name = "vector", desc = "The other Vector3."),
		returns = @ReturnDoc(type = NumberDef.class, desc = "The euclidean distance."),
		examples = "distance = vec.distanceTo(new Vector3(1, 1, 1));"
	)
	public double distanceTo(Arguments arguments) {
		Vec3d vec1 = arguments.nextPrimitive(this);
		Vec3d vec2 = arguments.nextPrimitive(this);
		double d = vec1.x - vec2.x;
		double e = vec1.y - vec2.y;
		double f = vec1.z - vec2.z;
		return Math.sqrt(d * d + e * e + f * f);
	}

	@FunctionDoc(
		name = "squaredDistanceTo",
		desc = "Returns the squared euclidean distance between this vector and the given vector.",
		params = @ParameterDoc(type = Vector3Def.class, name = "vector", desc = "The other Vector3."),
		returns = @ReturnDoc(type = NumberDef.class, desc = "The squared euclidean distance."),
		examples = "distance = vec.squaredDistanceTo(new Vector3(1,1,1));"
	)
	public double squaredDistanceTo(Arguments arguments) {
		Vec3d vec1 = arguments.nextPrimitive(this);
		Vec3d vec2 = arguments.nextPrimitive(this);
		double d = vec1.x - vec2.x;
		double e = vec1.y - vec2.y;
		double f = vec1.z - vec2.z;
		return d * d + e * e + f * f;
	}

	@FunctionDoc(
		name = "length",
		desc = "Returns the length of this vector.",
		returns = @ReturnDoc(type = NumberDef.class, desc = "The length."),
		examples = "length = vec.length();"
	)
	public double length(Arguments arguments) {
		Vec3d vec = arguments.nextPrimitive(this);
		return Math.sqrt(vec.x * vec.x + vec.y * vec.y + vec.z * vec.z);
	}

	@FunctionDoc(
		name = "lengthSquared",
		desc = "Returns the squared length of this vector.",
		returns = @ReturnDoc(type = NumberDef.class, desc = "The squared length."),
		examples = "length = vec.lengthSquared();"
	)
	public double lengthSquared(Arguments arguments) {
		Vec3d vec = arguments.nextPrimitive(this);
		return vec.x * vec.x + vec.y * vec.y + vec.z * vec.z;
	}

	@FunctionDoc(
		name = "horizontalLength",
		desc = "Returns the horizontal length of this vector.",
		returns = @ReturnDoc(type = NumberDef.class, desc = "The horizontal length."),
		examples = "length = vec.horizontalLength();"
	)
	public double horizontalLength(Arguments arguments) {
		Vec3d vec = arguments.nextPrimitive(this);
		return Math.sqrt(vec.x * vec.x + vec.z * vec.z);
	}

	@FunctionDoc(
		name = "horizontalLengthSquared",
		desc = "Returns the squared horizontal length of this vector.",
		returns = @ReturnDoc(type = NumberDef.class, desc = "The squared horizontal length."),
		examples = "length = vec.horizontalLengthSquared();"
	)
	public double horizontalLengthSquared(Arguments arguments) {
		Vec3d vec = arguments.nextPrimitive(this);
		return vec.x * vec.x + vec.z * vec.z;
	}


	@FunctionDoc(
		name = "lerp",
		desc = "Performs linear interpolation from this vector to the given vector.",
		params = {
			@ParameterDoc(type = Vector3Def.class, name = "vector", desc = "The other vector to interpolate to."),
			@ParameterDoc(type = NumberDef.class, name = "delta", desc = "The interpolation coefficient in the range between 0 and 1.")
		},
		returns = @ReturnDoc(type = Vector3Def.class, desc = "The interpolated vector."),
		examples = "vec = vec.lerp(new Vector3(1, 1, 1), 0.5);"
	)
	public Vec3d lerp(Arguments arguments) {
		Vec3d vec = arguments.nextPrimitive(this);
		Vec3d to = arguments.nextPrimitive(this);
		double delta = arguments.nextPrimitive(NumberDef.class);
		return new Vec3d(MathHelper.lerp(delta, vec.x, to.x), MathHelper.lerp(delta, vec.y, to.y), MathHelper.lerp(delta, vec.z, to.z));
	}

	@FunctionDoc(
		name = "rotateX",
		desc = "Rotates this vector by the given angle counterclockwise around the X axis.",
		params = @ParameterDoc(type = NumberDef.class, name = "angle", desc = "The angle in radians."),
		returns = @ReturnDoc(type = Vector3Def.class, desc = "The rotated vector."),
		examples = "vec = vec.rotateX(Math.pi);"
	)
	public Vec3d rotateX(Arguments arguments) {
		Vec3d vec = arguments.nextPrimitive(this);
		double angle = arguments.nextPrimitive(NumberDef.class);
		double f = Math.cos(angle);
		double g = Math.sin(angle);
		double d = vec.x;
		double e = vec.y * f + vec.z * g;
		double h = vec.z * f - vec.y * g;
		return new Vec3d(d, e, h);
	}

	@FunctionDoc(
		name = "rotateY",
		desc = "Rotates this vector by the given angle counterclockwise around the Y axis.",
		params = @ParameterDoc(type = NumberDef.class, name = "angle", desc = "The angle in radians."),
		returns = @ReturnDoc(type = Vector3Def.class, desc = "The rotated vector."),
		examples = "vec = vec.rotateY(Math.pi);"
	)
	public Vec3d rotateY(Arguments arguments) {
		Vec3d vec = arguments.nextPrimitive(this);
		double angle = arguments.nextPrimitive(NumberDef.class);
		double f = Math.cos(angle);
		double g = Math.sin(angle);
		double d = vec.x * f + vec.z * g;
		double e = vec.y;
		double h = vec.z * f - vec.x * g;
		return new Vec3d(d, e, h);
	}

	@FunctionDoc(
		name = "rotateZ",
		desc = "Rotates this vector by the given angle counterclockwise around the Z axis.",
		params = @ParameterDoc(type = NumberDef.class, name = "angle", desc = "The angle in radians."),
		returns = @ReturnDoc(type = Vector3Def.class, desc = "The rotated vector."),
		examples = "vec = vec.rotateZ(Math.pi * 2 / 3);"
	)
	public Vec3d rotateZ(Arguments arguments) {
		Vec3d vec = arguments.nextPrimitive(this);
		double angle = arguments.nextPrimitive(NumberDef.class);
		double f = Math.cos(angle);
		double g = Math.sin(angle);
		double d = vec.x * f + vec.y * g;
		double e = vec.y * f - vec.x * g;
		double h = vec.z;
		return new Vec3d(d, e, h);
	}

	@FunctionDoc(
		name = "floor",
		desc = "Creates a new vector with the floors each of the vector components.",
		returns = @ReturnDoc(type = Vector3Def.class, desc = "The floored vector."),
		examples = "new Vector(1.5, 2.9, 3.01).floor(); // (1, 2, 3)"
	)
	public Vec3d floor(Arguments arguments) {
		Vec3d vec = arguments.nextPrimitive(this);
		double d = Math.floor(vec.x);
		double e = Math.floor(vec.y);
		double f = Math.floor(vec.z);
		return new Vec3d(d, e, f);
	}

	@FunctionDoc(
		name = "ceil",
		desc = "Creates a new vector with the rounded up vector components.",
		returns = @ReturnDoc(type = Vector3Def.class, desc = "The rounded up vector."),
		examples = "new Vector(1.5, 2.9, 3.01).ceil(); // (2, 3, 4)"
	)
	public Vec3d ceil(Arguments arguments) {
		Vec3d vec = arguments.nextPrimitive(this);
		double d = Math.ceil(vec.x);
		double e = Math.ceil(vec.y);
		double f = Math.ceil(vec.z);
		return new Vec3d(d, e, f);
	}

	@FunctionDoc(
		name = "round",
		desc = "Creates a new vector with the rounded vector components.",
		returns = @ReturnDoc(type = Vector3Def.class, desc = "The rounded vector."),
		examples = "new Vector(1.5, 2.9, 3.01).ceil(); // (2, 3, 3)"
	)
	public Vec3d round(Arguments arguments) {
		Vec3d vec = arguments.nextPrimitive(this);
		double d = Math.round(vec.x);
		double e = Math.round(vec.y);
		double f = Math.round(vec.z);
		return new Vec3d(d, e, f);
	}


	@FunctionDoc(
		name = "getX",
		desc = "Returns the x coordinate of the vector.",
		returns = @ReturnDoc(type = NumberDef.class, desc = "The x coordinate."),
		examples = "x = vec.getX();"
	)
	public final double getX(Arguments arguments) {
		return arguments.nextPrimitive(this).x;
	}

	@FunctionDoc(
		name = "getY",
		desc = "Returns the y coordinate of the vector.",
		returns = @ReturnDoc(type = NumberDef.class, desc = "The y coordinate."),
		examples = "y = vec.getY();"
	)
	public final double getY(Arguments arguments) {
		return arguments.nextPrimitive(this).y;
	}

	@FunctionDoc(
		name = "getZ",
		desc = "Returns the z coordinate of the vector.",
		returns = @ReturnDoc(type = NumberDef.class, desc = "The z coordinate."),
		examples = "z = vec.getZ();"
	)
	public final double getZ(Arguments arguments) {
		return arguments.nextPrimitive(this).z;
	}

	@Nullable
	@Override
	public List<BuiltInFunction> defineStaticMethods() {
		return List.of(
			BuiltInFunction.of("fromPolar", 2, this::fromPolar),
			BuiltInFunction.of("zero", this::zero),
			BuiltInFunction.of("fromScalar", 1, this::fromScalar),
			BuiltInFunction.of("min", 2, this::min),
			BuiltInFunction.of("max", 2, this::max)
		);
	}

	@FunctionDoc(
		isStatic = true,
		name = "fromPolar",
		desc = "Returns a new Vector3 from pitch and yaw angles.",
		params = {
			@ParameterDoc(type = NumberDef.class, name = "pitch", desc = "The pitch angle in radians."),
			@ParameterDoc(type = NumberDef.class, name = "yaw", desc = "The yaw angle in radians.")
		},
		returns = @ReturnDoc(type = Vector3Def.class, desc = "The return Vector3."),
		examples = "vec = Vector3.fromPolar(Math.pi, Math.pi / 2);"
	)
	public Vec3d fromPolar(Arguments arguments) {
		double yaw = arguments.nextPrimitive(NumberDef.class);
		double pitch = arguments.nextPrimitive(NumberDef.class);
		double f = Math.cos(-yaw * 0.017453292D - 3.1415927D);
		double g = Math.sin(-yaw * 0.017453292D - 3.1415927D);
		double h = -Math.cos(-pitch * 0.017453292D);
		double i = Math.sin(-pitch * 0.017453292D);
		return new Vec3d(g * h, i, f * h);
	}

	@FunctionDoc(
		isStatic = true,
		name = "fromScalar",
		desc = "Returns a zero vector (0, 0, 0).",
		returns = @ReturnDoc(type = Vector3Def.class, desc = "The zero vector."),
		examples = "vec = Vector3.zero();"
	)
	public ClassInstance zero(Arguments arguments) {
		return this.create(new Vec3d(0, 0, 0));
	}

	@FunctionDoc(
		isStatic = true,
		name = "fromScalar",
		desc = "Creates a new vector with the components having the value of a given scalar.",
		params = @ParameterDoc(type = NumberDef.class, name = "scalar", desc = "The number to create the vector with."),
		returns = @ReturnDoc(type = Vector3Def.class, desc = "The new Vector3."),
		examples = "vec = Vector3.fromScalar(10);"
	)
	public ClassInstance fromScalar(Arguments arguments) {
		double s = arguments.nextPrimitive(NumberDef.class);
		return this.create(new Vec3d(s, s, s));
	}

	public Vec3d min(Arguments arguments) {
		Vec3d vec1 = arguments.nextPrimitive(this);
		Vec3d vec2 = arguments.nextPrimitive(this);
		double x = Math.min(vec1.x, vec2.x);
		double y = Math.min(vec1.y, vec2.y);
		double z = Math.min(vec1.z, vec2.z);
		return new Vec3d(x, y, z);
	}

	public Vec3d max(Arguments arguments) {
		Vec3d vec1 = arguments.nextPrimitive(this);
		Vec3d vec2 = arguments.nextPrimitive(this);
		double x = Math.max(vec1.x, vec2.x);
		double y = Math.max(vec1.y, vec2.y);
		double z = Math.max(vec1.z, vec2.z);
		return new Vec3d(x, y, z);
	}
}
