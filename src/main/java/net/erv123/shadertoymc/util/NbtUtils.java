package net.erv123.shadertoymc.util;

import me.senseiwells.arucas.builtin.ListDef;
import me.senseiwells.arucas.builtin.MapDef;
import me.senseiwells.arucas.builtin.NumberDef;
import me.senseiwells.arucas.builtin.StringDef;
import me.senseiwells.arucas.classes.instance.ClassInstance;
import me.senseiwells.arucas.interpreter.Interpreter;
import me.senseiwells.arucas.utils.impl.ArucasList;
import me.senseiwells.arucas.utils.impl.ArucasMap;
import net.minecraft.nbt.*;

public class NbtUtils {
	public static ArucasMap nbtToMap(Interpreter interpreter, NbtCompound compound, int depth) {
		ArucasMap nbtMap = new ArucasMap();
		depth--;
		if (compound == null || depth < 0) {
			return nbtMap;
		}
		for (String tagName : compound.getKeys()) {
			NbtElement element = compound.get(tagName);
			if (element == null) {
				continue;
			}
			nbtMap.put(interpreter, interpreter.create(StringDef.class, tagName), nbtToValue(interpreter, element, depth));
		}
		return nbtMap;
	}

	public static ArucasList nbtToList(Interpreter interpreter, AbstractNbtList<?> list, int depth) {
		ArucasList nbtList = new ArucasList();
		depth--;
		if (list == null || depth < 0) {
			return nbtList;
		}
		for (NbtElement element : list) {
			nbtList.add(nbtToValue(interpreter, element, depth));
		}
		return nbtList;
	}

	public static ClassInstance nbtToValue(Interpreter interpreter, NbtElement element, int depth) {
		if (element instanceof NbtCompound inCompound) {
			return interpreter.create(MapDef.class, nbtToMap(interpreter, inCompound, depth));
		}
		if (element instanceof AbstractNbtList<?> nbtList) {
			return interpreter.create(ListDef.class, nbtToList(interpreter, nbtList, depth));
		}
		if (element instanceof AbstractNbtNumber nbtNumber) {
			return interpreter.create(NumberDef.class, nbtNumber.doubleValue());
		}
		if (element == NbtEnd.INSTANCE) {
			return interpreter.getNull();
		}
		return interpreter.create(StringDef.class, element.asString());
	}
}
