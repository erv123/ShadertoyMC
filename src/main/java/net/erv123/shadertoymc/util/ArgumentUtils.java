package net.erv123.shadertoymc.util;

import me.senseiwells.arucas.builtin.NumberDef;
import me.senseiwells.arucas.utils.Arguments;
import net.erv123.shadertoymc.arucas.definitions.Vector3Def;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class ArgumentUtils {
	public static Vec3d getVec3d(Arguments arguments) {
		if (arguments.isNext(NumberDef.class)) {
			double x = arguments.nextPrimitive(NumberDef.class);
			double y = arguments.nextPrimitive(NumberDef.class);
			double z = arguments.nextPrimitive(NumberDef.class);
			return new Vec3d(x, y, z);
		}
		return arguments.nextPrimitive(Vector3Def.class);
	}

	public static Vec3i getVec3i(Arguments arguments) {
		if (arguments.isNext(NumberDef.class)) {
			int x = arguments.nextPrimitive(NumberDef.class).intValue();
			int y = arguments.nextPrimitive(NumberDef.class).intValue();
			int z = arguments.nextPrimitive(NumberDef.class).intValue();
			return new Vec3i(x, y, z);
		}
		Vec3d vec3d = arguments.nextPrimitive(Vector3Def.class);
		return new Vec3i(vec3d.x, vec3d.y, vec3d.z);
	}
}
