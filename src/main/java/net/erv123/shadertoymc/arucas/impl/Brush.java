package net.erv123.shadertoymc.arucas.impl;

import me.senseiwells.arucas.classes.instance.ClassInstance;
import me.senseiwells.arucas.compiler.Trace;
import me.senseiwells.arucas.interpreter.Interpreter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class Brush {
	private final int maxDistance;
	private final boolean includeFluids;
	private final String item;
	private final Interpreter interpreter;
	private final ClassInstance callback;

	public Brush(int maxDistance, boolean includeFluids, String item, Interpreter interpreter, ClassInstance callback) {
		this.maxDistance = maxDistance;
		this.includeFluids = includeFluids;
		this.item = item;
		this.interpreter = interpreter;
		this.callback = callback;
	}

	public void execute(String item, PlayerEntity player){
		if(item.equals(this.item)){
			Vec3d pos = player.raycast(maxDistance,0,includeFluids).getPos();
			interpreter.call(callback, List.of(interpreter.convertValue(pos)), Trace.INTERNAL);
		}
	}
}
