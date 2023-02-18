package net.erv123.shadertoymc.arucas.extension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.senseiwells.arucas.api.ArucasExtension;
import me.senseiwells.arucas.api.docs.annotations.ExtensionDoc;
import me.senseiwells.arucas.builtin.FunctionDef;
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
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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
		int originX = arguments.nextPrimitive(NumberDef.class).intValue();
		int originY = arguments.nextPrimitive(NumberDef.class).intValue();
		int originZ = arguments.nextPrimitive(NumberDef.class).intValue();
		int sizeX = arguments.nextPrimitive(NumberDef.class).intValue();
		int sizeY = arguments.nextPrimitive(NumberDef.class).intValue();
		int sizeZ = arguments.nextPrimitive(NumberDef.class).intValue();

		if (sizeX < 1 || sizeY < 1 || sizeZ < 1) {
			throw new RuntimeError("Size cannot be less than 1");
		}

		ClassInstance callback = arguments.nextFunction();
		Interpreter interpreter = arguments.getInterpreter();

		int volume = sizeX * sizeY * sizeZ;
		int completed = 0;

		int parameters = callback.asPrimitive(FunctionDef.class).getCount();
		Function<Vec3i, List<Object>> generator = switch (parameters) {
			case 1 -> List::of;
			case 2 -> (position) -> {
				Vec3d normal = new Vec3d(
					MathHelper.lerp(-1, 1, position.getX() / (double) sizeX),
					MathHelper.lerp(-1, 1, position.getY() / (double) sizeY),
					MathHelper.lerp(-1, 1, position.getZ() / (double) sizeZ)
				);
				return List.of(position, normal);
			};
			case 3 -> (position) -> {
				Vec3d normal = new Vec3d(
					MathHelper.lerp(-1, 1, position.getX() / (double) sizeX),
					MathHelper.lerp(-1, 1, position.getY() / (double) sizeY),
					MathHelper.lerp(-1, 1, position.getZ() / (double) sizeZ)
				);
				Vec3i absolute = position.add(originX, originY, originX);
				return List.of(interpreter.convertValue(position), interpreter.convertValue(normal), interpreter.convertValue(absolute));
			};
			default -> throw new RuntimeError("Callback function needs to have 1, 2, or 3 parameters");
		};

		ScriptUtils.showProgressBar(interpreter);
		for (int x = originX; x <= originX + sizeX; x++) {
			for (int y = originY; y <= originY + sizeY; y++) {
				for (int z = originZ; z <= originZ + sizeZ; z++, completed++) {
					List<ClassInstance> args = new ArrayList<>();
					for (Object vec : generator.apply(new Vec3i(x, y, z))) {
						args.add(interpreter.convertValue(vec));
					}
					interpreter.call(callback, args, Trace.getINTERNAL());
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
