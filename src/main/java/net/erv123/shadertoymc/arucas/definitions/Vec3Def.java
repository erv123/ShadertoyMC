package net.erv123.shadertoymc.arucas.definitions;

import kotlin.Unit;
import me.senseiwells.arucas.builtin.ListDef;
import me.senseiwells.arucas.builtin.NumberDef;
import me.senseiwells.arucas.classes.PrimitiveDefinition;
import me.senseiwells.arucas.classes.instance.ClassInstance;
import me.senseiwells.arucas.core.Interpreter;
import me.senseiwells.arucas.exceptions.RuntimeError;
import me.senseiwells.arucas.utils.*;
import me.senseiwells.arucas.utils.impl.ArucasList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Vec3Def extends PrimitiveDefinition<Vec3d> {


    public Vec3Def(@NotNull Interpreter interpreter) {
        super("Vector3", interpreter);
    }

    @Nullable
    @Override
    public List<BuiltInFunction> defineStaticMethods() {
        return List.of(
            BuiltInFunction.of("fromPolar",2,this::fromPolar),
            BuiltInFunction.of("zero",0,this::zero),
            BuiltInFunction.of("fromScalar",1,this::fromScalar)
        );
    }
    @Override
    public List<ConstructorFunction> defineConstructors() {
        return List.of(
            ConstructorFunction.of(1, this::construct1),
            ConstructorFunction.of(3, this::construct3)
        );
    }

    @Nullable
    @Override
    public List<MemberFunction> defineMethods() {
        return List.of(
                MemberFunction.of("sub",1, this::subtract1),
                MemberFunction.of("sub",3, this::subtract3),
                MemberFunction.of("subScalar",1, this::subtractScalar),
                MemberFunction.of("add",1, this::add1),
                MemberFunction.of("add",3, this::add3),
                MemberFunction.of("addScalar",1, this::addScalar),
                MemberFunction.of("multiply",1, this::multiply1),
                MemberFunction.of("multiply",3, this::multiply3),
                MemberFunction.of("multiplyScalar",1, this::multiplyScalar),
                MemberFunction.of("divide",1, this::divide1),
                MemberFunction.of("divide",3, this::divide3),
                MemberFunction.of("divideScalar",1, this::divideScalar),
                MemberFunction.of("normalize",0, this::normalize),
                MemberFunction.of("dot",1, this::dotProduct),
                MemberFunction.of("cross",1, this::crossProduct),
                MemberFunction.of("distanceTo",1, this::distanceTo),
                MemberFunction.of("distanceToSquared",1, this::squaredDistanceTo),
                MemberFunction.of("length",0, this::length),
                MemberFunction.of("lengthSquared",0, this::lengthSquared),
                MemberFunction.of("horizontalLength",0, this::horizontalLength),
                MemberFunction.of("horizontalLengthSquared",0, this::horizontalLengthSquared),
                MemberFunction.of("toString",0, this::toString),
                MemberFunction.of("lerp",2, this::lerp),
                MemberFunction.of("rotateX",1, this::rotateX),
                MemberFunction.of("rotateY",1, this::rotateY),
                MemberFunction.of("rotateZ",1, this::rotateZ),
                MemberFunction.of("floor",0, this::floor),
                MemberFunction.of("getX",0, this::getX),
                MemberFunction.of("getY",0, this::getY),
                MemberFunction.of("getZ",0, this::getZ)
        );
    }

    /**
     * Constructors
     */
    private Unit construct3(Arguments arguments) {
        ClassInstance instance = arguments.next();
        double x = arguments.nextPrimitive(NumberDef.class);
        double y = arguments.nextPrimitive(NumberDef.class);
        double z = arguments.nextPrimitive(NumberDef.class);
        instance.setPrimitive(this, new Vec3d(x, y, z));
        return null;
    }

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



    /**
    operators
     */
    @Nullable
    @Override
    protected Object minus(@NotNull ClassInstance instance, @NotNull Interpreter interpreter, @NotNull LocatableTrace trace) {
        return instance.asPrimitive(this).negate();
    }
    @Nullable
    @Override
    protected Object minus(@NotNull ClassInstance instance, @NotNull Interpreter interpreter, @NotNull ClassInstance other, @NotNull LocatableTrace trace) {
        return instance.asPrimitive(this).subtract(other.asPrimitive(this));
    }
    @Nullable
    @Override
    protected Object plus(@NotNull ClassInstance instance, @NotNull Interpreter interpreter, @NotNull LocatableTrace trace) {
        return instance.asPrimitive(this);
    }
    @Nullable
    @Override
    protected Object plus(@NotNull ClassInstance instance, @NotNull Interpreter interpreter, @NotNull ClassInstance other, @NotNull LocatableTrace trace) {
        return instance.asPrimitive(this).add(other.asPrimitive(this));
    }
    @Nullable
    @Override
    protected Object multiply(@NotNull ClassInstance instance, @NotNull Interpreter interpreter, @NotNull ClassInstance other, @NotNull LocatableTrace trace) {
        return instance.asPrimitive(this).multiply(other.asPrimitive(this));
    }
    @Nullable
    @Override
    protected Object divide(@NotNull ClassInstance instance, @NotNull Interpreter interpreter, @NotNull ClassInstance other, @NotNull LocatableTrace trace) {
        Vec3d vec1 = instance.asPrimitive(this);
        Vec3d vec2 = other.asPrimitive(this);
        return vec1.multiply(1/vec2.x,1/vec2.y,1/vec2.z);
    }
    @Nullable
    @Override
    protected Object power(@NotNull ClassInstance instance, @NotNull Interpreter interpreter, @NotNull ClassInstance other, @NotNull LocatableTrace trace) {
        Vec3d vec = instance.asPrimitive(this);
        double num = other.asPrimitive(NumberDef.class);
        return new Vec3d(Math.pow(vec.x,num),Math.pow(vec.y,num),Math.pow(vec.z,num));
    }
    @NotNull
    @Override
    public ClassInstance bracketAccess(@NotNull ClassInstance instance, @NotNull Interpreter interpreter, @NotNull ClassInstance index, @NotNull LocatableTrace trace) {
        Vec3d vec = instance.asPrimitive(this);
        double indexD = index.asPrimitive(NumberDef.class);
        int i = (int) indexD;
        return switch (i) {
            case 0 -> interpreter.convertValue(vec.x);
            case 1 -> interpreter.convertValue(vec.y);
            case 2 -> interpreter.convertValue(vec.z);
            default -> throw new IndexOutOfBoundsException();
        };
    }
    @NotNull
    @Override
    public ClassInstance bracketAssign(@NotNull ClassInstance instance, @NotNull Interpreter interpreter, @NotNull ClassInstance index, @NotNull ClassInstance assignee, @NotNull LocatableTrace trace) {
        Vec3d vec = instance.asPrimitive(this);
        double indexD = index.asPrimitive(NumberDef.class);
        double number = assignee.asPrimitive(NumberDef.class);
        int i = (int) indexD;
        return switch (i) {
            case 0 -> interpreter.convertValue(new Vec3d(number, vec.y, vec.z));
            case 1 -> interpreter.convertValue(new Vec3d(vec.x, number, vec.z));
            case 2 -> interpreter.convertValue(new Vec3d(vec.x, vec.y, number));
            default -> throw new IndexOutOfBoundsException();
        };

    }

    /**
     Arithmetic functions
     */
    public Vec3d subtract1(Arguments arguments) {
        Vec3d vec1 = arguments.nextPrimitive(this);
        Vec3d vec2 = arguments.nextPrimitive(this);
        return vec1.subtract(vec2);
    }

    public Vec3d subtract3(Arguments arguments) {
        Vec3d vec1 = arguments.nextPrimitive(this);
        double x = arguments.nextPrimitive(NumberDef.class);
        double y = arguments.nextPrimitive(NumberDef.class);
        double z = arguments.nextPrimitive(NumberDef.class);
        return vec1.subtract(x,y,z);
    }
    public Vec3d subtractScalar(Arguments arguments) {
        Vec3d vec1 = arguments.nextPrimitive(this);
        double num = arguments.nextPrimitive(NumberDef.class);
        return vec1.subtract(num,num,num);
    }

    public Vec3d add1(Arguments arguments) {
        Vec3d vec1 = arguments.nextPrimitive(this);
        Vec3d vec2 = arguments.nextPrimitive(this);
        return vec1.add(vec2);
    }

    public Vec3d add3(Arguments arguments) {
        Vec3d vec1 = arguments.nextPrimitive(this);
        double x = arguments.nextPrimitive(NumberDef.class);
        double y = arguments.nextPrimitive(NumberDef.class);
        double z = arguments.nextPrimitive(NumberDef.class);
        return vec1.add(x,y,z);
    }
    public Vec3d addScalar(Arguments arguments) {
        Vec3d vec1 = arguments.nextPrimitive(this);
        double num = arguments.nextPrimitive(NumberDef.class);
        return vec1.add(num,num,num);
    }
    public Vec3d multiply1(Arguments arguments) {
        Vec3d vec1 = arguments.nextPrimitive(this);
        Vec3d vec2 = arguments.nextPrimitive(this);
        return vec1.multiply(vec2);
    }

    public Vec3d multiply3(Arguments arguments) {
        Vec3d vec1 = arguments.nextPrimitive(this);
        double x = arguments.nextPrimitive(NumberDef.class);
        double y = arguments.nextPrimitive(NumberDef.class);
        double z = arguments.nextPrimitive(NumberDef.class);
        return vec1.multiply(x,y,z);
    }
    public Vec3d multiplyScalar(Arguments arguments) {
        Vec3d vec1 = arguments.nextPrimitive(this);
        double num = arguments.nextPrimitive(NumberDef.class);
        return vec1.multiply(num,num,num);
    }
    public Vec3d divide1(Arguments arguments) {
        Vec3d vec1 = arguments.nextPrimitive(this);
        Vec3d vec2 = arguments.nextPrimitive(this);
        return vec1.multiply(1/vec2.x,1/vec2.y,1/vec2.z);
    }

    public Vec3d divide3(Arguments arguments) {
        Vec3d vec1 = arguments.nextPrimitive(this);
        double x = arguments.nextPrimitive(NumberDef.class);
        double y = arguments.nextPrimitive(NumberDef.class);
        double z = arguments.nextPrimitive(NumberDef.class);
        return vec1.multiply(1/x,1/y,1/z);
    }
    public Vec3d divideScalar(Arguments arguments) {
        Vec3d vec1 = arguments.nextPrimitive(this);
        double num = arguments.nextPrimitive(NumberDef.class);
        return vec1.multiply(1/num,1/num,1/num);
    }



    /**
     *Vector functions
     */
    private Vec3d normalize(Arguments arguments) {
        return arguments.nextPrimitive(this).normalize();
    }


    public double dotProduct(Arguments arguments) {
        return arguments.nextPrimitive(this).dotProduct(arguments.nextPrimitive(this));
    }


    public Vec3d crossProduct(Arguments arguments) {
        return arguments.nextPrimitive(this).crossProduct(arguments.nextPrimitive(this));
    }



    public double distanceTo(Arguments arguments) {
        Vec3d vec1 = arguments.nextPrimitive(this);
        Vec3d vec2 = arguments.nextPrimitive(this);
        double d = vec1.x - vec2.x;
        double e = vec1.y - vec2.y;
        double f = vec1.z - vec2.z;
        return Math.sqrt(d * d + e * e + f * f);
    }


    public double squaredDistanceTo(Arguments arguments) {
        Vec3d vec1 = arguments.nextPrimitive(this);
        Vec3d vec2 = arguments.nextPrimitive(this);
        double d = vec1.x - vec2.x;
        double e = vec1.y - vec2.y;
        double f = vec1.z - vec2.z;
        return d * d + e * e + f * f;
    }

    public double length(Arguments arguments) {
        Vec3d vec = arguments.nextPrimitive(this);
        return Math.sqrt(vec.x * vec.x + vec.y * vec.y + vec.z * vec.z);
    }

    public double lengthSquared(Arguments arguments) {
        Vec3d vec = arguments.nextPrimitive(this);
        return vec.x * vec.x + vec.y * vec.y + vec.z * vec.z;
    }

    public double horizontalLength(Arguments arguments) {
        Vec3d vec = arguments.nextPrimitive(this);
        return Math.sqrt(vec.x * vec.x + vec.z * vec.z);
    }

    public double horizontalLengthSquared(Arguments arguments) {
        Vec3d vec = arguments.nextPrimitive(this);
        return vec.x * vec.x + vec.z * vec.z;
    }


    public String toString(Arguments arguments) {
        Vec3d vec = arguments.nextPrimitive(this);
        return "(" + vec.x + ", " + vec.y + ", " + vec.z + ")";
    }

    public Vec3d lerp(Arguments arguments) {
        Vec3d vec = arguments.nextPrimitive(this);
        Vec3d to = arguments.nextPrimitive(this);
        double delta = arguments.nextPrimitive(NumberDef.class);
        return new Vec3d(MathHelper.lerp(delta, vec.x, to.x), MathHelper.lerp(delta, vec.y, to.y), MathHelper.lerp(delta, vec.z, to.z));
    }


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

    public Vec3d floor(Arguments arguments) {
        Vec3d vec = arguments.nextPrimitive(this);
        double d = Math.floor(vec.x);
        double e = Math.floor(vec.y);
        double f = Math.floor(vec.z);
        return new Vec3d(d, e, f);
    }

    public final double getX(Arguments arguments) {
        return arguments.nextPrimitive(this).x;
    }

    public final double getY(Arguments arguments) {
        return arguments.nextPrimitive(this).y;
    }

    public final double getZ(Arguments arguments) {
        return arguments.nextPrimitive(this).z;
    }

    /**
     * static functions
     */

    public Vec3d fromPolar(Arguments arguments) {
        double yaw = arguments.nextPrimitive(NumberDef.class);
        double pitch = arguments.nextPrimitive(NumberDef.class);
        double f = Math.cos(-yaw * 0.017453292D - 3.1415927D);
        double g = Math.sin(-yaw * 0.017453292F - 3.1415927F);
        double h = -Math.cos(-pitch * 0.017453292F);
        double i = Math.sin(-pitch * 0.017453292F);
        return new Vec3d(g * h, i, f * h);
    }
    public Vec3d zero(Arguments arguments) {
        return new Vec3d(0,0,0);
    }
    public Vec3d fromScalar(Arguments arguments) {
        double s = arguments.nextPrimitive(NumberDef.class);
        return new Vec3d(s,s,s);
    }
}
