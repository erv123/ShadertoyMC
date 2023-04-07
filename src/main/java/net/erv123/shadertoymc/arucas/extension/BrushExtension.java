package net.erv123.shadertoymc.arucas.extension;

import me.senseiwells.arucas.api.ArucasExtension;
import me.senseiwells.arucas.builtin.BooleanDef;
import me.senseiwells.arucas.builtin.NumberDef;
import me.senseiwells.arucas.builtin.StringDef;
import me.senseiwells.arucas.classes.instance.ClassInstance;
import me.senseiwells.arucas.functions.builtin.Arguments;
import me.senseiwells.arucas.functions.builtin.BuiltInFunction;
import me.senseiwells.arucas.interpreter.Interpreter;
import net.erv123.shadertoymc.arucas.impl.Brush;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BrushExtension implements ArucasExtension {
	public static HashMap<Interpreter,List<Brush>> BRUSHES = new HashMap<>();
	@NotNull
	@Override
	public List<BuiltInFunction> getBuiltInFunctions() {
		return List.of(
			BuiltInFunction.of("addBrush",2,this::addBrush2),
			BuiltInFunction.of("addBrush",3,this::addBrush3),
			BuiltInFunction.of("addBrush",4,this::addBrush4)
		);
	}

	@NotNull
	@Override
	public String getName() {
		return "BrushExtension";
	}
	private Void addBrush2(Arguments arguments){
		Interpreter interpreter = arguments.getInterpreter();
		String item = arguments.nextPrimitive(StringDef.class);
		int maxDistance = 128;
		boolean includeFluids = true;
		ClassInstance callback = arguments.nextFunction();
		internalAddBrush(maxDistance, includeFluids, item, interpreter, callback);
		return null;
	}
	private Void addBrush3(Arguments arguments){
		Interpreter interpreter = arguments.getInterpreter();
		String item = arguments.nextPrimitive(StringDef.class);
		int maxDistance = arguments.nextPrimitive(NumberDef.class).intValue();
		boolean includeFluids = true;
		ClassInstance callback = arguments.nextFunction();
		internalAddBrush(maxDistance, includeFluids, item, interpreter, callback);
		return null;
	}

	private Void addBrush4(Arguments arguments){
		Interpreter interpreter = arguments.getInterpreter();
		String item = arguments.nextPrimitive(StringDef.class);
		int maxDistance = arguments.nextPrimitive(NumberDef.class).intValue();
		boolean includeFluids = arguments.nextPrimitive(BooleanDef.class);
		ClassInstance callback = arguments.nextFunction();
		internalAddBrush(maxDistance, includeFluids, item, interpreter, callback);
		return null;
	}
	private void internalAddBrush(int maxDistance, boolean includeFluids, String item, Interpreter interpreter, ClassInstance callback){
		Brush brush = new Brush(maxDistance,includeFluids,item,interpreter,callback);
		BRUSHES.putIfAbsent(interpreter,new ArrayList<>());
		BRUSHES.get(interpreter).add(brush);
	}
}
