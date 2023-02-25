package net.erv123.shadertoymc.util;

import me.senseiwells.arucas.builtin.StringDef;
import me.senseiwells.arucas.core.Interpreter;
import me.senseiwells.arucas.utils.impl.ArucasMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;

import java.util.stream.Collectors;

public class BlockUtils {
	public static ArucasMap stateAsMap(BlockState state, Interpreter interpreter) {
		ArucasMap map = new ArucasMap();
		state.getEntries().forEach((property, comparable) -> {
			Object value = property instanceof EnumProperty<?> ? ((StringIdentifiable) comparable).asString() : comparable;
			map.put(interpreter, interpreter.create(StringDef.class, property.getName()), interpreter.convertValue(value));
		});
		return map;
	}

	public static ArucasMap nbtAsMap(BlockEntity entity, Interpreter interpreter) {
		return NbtUtils.nbtToMap(interpreter, entity.createNbt(), 10);
	}

	public static String blockData(ServerWorld world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		String data = blockId(state);

		String properties = state.getEntries().entrySet().stream().map(entry -> {
			if (entry != null) {
				Property<?> property = entry.getKey();
				Comparable<?> comparable = entry.getValue();
				String value = property instanceof EnumProperty<?> ? ((StringIdentifiable) comparable).asString() : comparable.toString();
				return property.getName() + "=" + value;
			}
			return "<NULL>";
		}).collect(Collectors.joining(",", "[", "]"));

		if (!properties.equals("[]")) {
			data += properties;
		}

		BlockEntity entity = world.getBlockEntity(pos);
		if (entity != null) {
			data += entity.createNbt().asString();
		}

		return data;
	}

	public static String blockId(BlockState state) {
		return Registries.BLOCK.getId(state.getBlock()).toString();
	}
}
