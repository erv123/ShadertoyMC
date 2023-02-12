package net.erv123.shadertoymc.arucas.extension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.senseiwells.arucas.api.ArucasExtension;
import me.senseiwells.arucas.api.docs.annotations.ExtensionDoc;
import me.senseiwells.arucas.builtin.NumberDef;
import me.senseiwells.arucas.builtin.StringDef;
import me.senseiwells.arucas.classes.instance.ClassInstance;
import me.senseiwells.arucas.core.Interpreter;
import me.senseiwells.arucas.exceptions.RuntimeError;
import me.senseiwells.arucas.utils.Arguments;
import me.senseiwells.arucas.utils.BuiltInFunction;
import me.senseiwells.arucas.utils.Trace;
import me.senseiwells.arucas.utils.Util;
import net.erv123.shadertoymc.util.ScriptUtils;
import net.erv123.shadertoymc.util.ShaderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.entity.boss.CommandBossBar;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@ExtensionDoc(
	name = "ShaderExtension",
	desc = "Extension with some functions for interacting with the Minecraft world.",
	language = Util.Language.Java
)
public class ShaderExtension implements ArucasExtension {
	@NotNull
	@Override
	public List<BuiltInFunction> getBuiltInFunctions() {
		return List.of(
			BuiltInFunction.of("getWorld", this::getWorld),
			BuiltInFunction.of("place", 4, this::placeDefault),
			BuiltInFunction.of("place", 5, this::placeWithWorld),
			BuiltInFunction.of("area", 7, this::area)
		);
	}

	@NotNull
	@Override
	public String getName() {
		return "ShaderExtension";
	}

	private String getWorld(Arguments arguments) {
		return ScriptUtils.getScriptHolder(arguments.getInterpreter()).getWorld().getRegistryKey().getValue().getPath();
	}

	private Void placeDefault(Arguments arguments) {
		String block = arguments.nextPrimitive(StringDef.class);
		int x = arguments.nextPrimitive(NumberDef.class).intValue();
		int y = arguments.nextPrimitive(NumberDef.class).intValue();
		int z = arguments.nextPrimitive(NumberDef.class).intValue();
		ServerCommandSource source = ScriptUtils.getScriptHolder(arguments.getInterpreter());
		this.place(arguments.getInterpreter(), source.getWorld(), block, x, y, z);
		return null;
	}

	private Void placeWithWorld(Arguments arguments) {
		String block = arguments.nextPrimitive(StringDef.class);
		int x = arguments.nextPrimitive(NumberDef.class).intValue();
		int y = arguments.nextPrimitive(NumberDef.class).intValue();
		int z = arguments.nextPrimitive(NumberDef.class).intValue();
		String worldString = arguments.nextPrimitive(StringDef.class);

		RegistryKey<World> registry = RuntimeError.wrap(() -> RegistryKey.of(RegistryKeys.WORLD, Identifier.tryParse(worldString)));
		ServerCommandSource source = ScriptUtils.getScriptHolder(arguments.getInterpreter());
		ServerWorld world = source.getServer().getWorld(registry);

		if (world == null) {
			throw new RuntimeError("Failed to get world for: " + worldString);
		}
		this.place(arguments.getInterpreter(), world, block, x, y, z);
		return null;
	}

	private Void area(Arguments arguments) {
		int ax = arguments.nextPrimitive(NumberDef.class).intValue();
		int ay = arguments.nextPrimitive(NumberDef.class).intValue();
		int az = arguments.nextPrimitive(NumberDef.class).intValue();
		int bx = arguments.nextPrimitive(NumberDef.class).intValue();
		int by = arguments.nextPrimitive(NumberDef.class).intValue();
		int bz = arguments.nextPrimitive(NumberDef.class).intValue();
		ClassInstance callback = arguments.nextFunction();
		Interpreter interpreter = arguments.getInterpreter();
		int startX = Math.min(ax, bx);
		int endX = Math.max(ax, bx);
		int startY = Math.min(ay, by);
		int endY = Math.max(ay, by);
		int startZ = Math.min(az, bz);
		int endZ = Math.max(az, bz);

		int volume = (endX - startX) * (endY - startY) * (endZ - startZ);
		int completed = 0;

		ScriptUtils.showProgressBar(interpreter);
		for (int x = startX; x <= endX; x++) {
			ClassInstance xInstance = interpreter.create(NumberDef.class, (double) x);
			for (int y = startY; y <= endY; y++) {
				ClassInstance yInstance = interpreter.create(NumberDef.class, (double) y);
				for (int z = startZ; z <= endZ; z++, completed++) {
					ClassInstance zInstance = interpreter.create(NumberDef.class, (double) z);
					interpreter.call(callback, List.of(xInstance, yInstance, zInstance), Trace.getINTERNAL());
				}
			}
			ScriptUtils.getBossBar(interpreter).setPercent(completed / (float) volume);
		}

		ScriptUtils.hideProgressBar(interpreter);
		return null;
	}

	private void place(Interpreter interpreter, ServerWorld world, String block, int x, int y, int z) {
		ServerCommandSource source = ScriptUtils.getScriptHolder(interpreter);
		DynamicRegistryManager manager = source.getRegistryManager();
		RegistryWrapper<Block> registryWrapper = manager.getWrapperOrThrow(RegistryKeys.BLOCK);
		try {
			BlockState state = BlockArgumentParser.block(registryWrapper, block, true).blockState();
			ShaderUtils.canBlocksFall = false;
			world.setBlockState(new BlockPos(x, y, z), state, Block.NOTIFY_LISTENERS, 0);
			ShaderUtils.canBlocksFall = true;
		} catch (CommandSyntaxException e) {
			throw new RuntimeError("Invalid block: " + block, e);
		}
	}
}
