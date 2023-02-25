package net.erv123.shadertoymc.arucas.extension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.senseiwells.arucas.api.ArucasExtension;
import me.senseiwells.arucas.api.docs.annotations.ExtensionDoc;
import me.senseiwells.arucas.api.docs.annotations.FunctionDoc;
import me.senseiwells.arucas.api.docs.annotations.ParameterDoc;
import me.senseiwells.arucas.api.docs.annotations.ReturnDoc;
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
import net.erv123.shadertoymc.arucas.definitions.Vector3Def;
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
			BuiltInFunction.of("getBlock", 1, this::getBlockVector),
			BuiltInFunction.of("getBlock", 2, this::getBlockWithWorld),
			BuiltInFunction.of("getBlock", 3, this::getBlock),
			BuiltInFunction.of("getBlock", 4, this::getBlockVectorWithWorld),
			BuiltInFunction.of("place", 2, this::placeVector),
			BuiltInFunction.of("place", 3, this::placeVectorWithWorld),
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

	@FunctionDoc(
		name = "getWorld",
		desc = "Returns string for your current dimension",
		examples = "getWorld();",
		returns = @ReturnDoc(type = StringDef.class, desc = "String representing your current dimension")
	)
	private String getWorld(Arguments arguments) {
		return ScriptUtils.getScriptHolder(arguments.getInterpreter()).getWorld().getRegistryKey().getValue().getPath();
	}

	@FunctionDoc(
		name = "getBlock",
		desc = "This gets a string for a block in the specified dimension",
		params = {
			@ParameterDoc(type = NumberDef.class, name = "x", desc = "The x coordinate"),
			@ParameterDoc(type = NumberDef.class, name = "y", desc = "The y coordinate"),
			@ParameterDoc(type = NumberDef.class, name = "z", desc = "The z coordinate"),
			@ParameterDoc(type = StringDef.class, name = "block", desc = "A string representing the target dimension (overworld, the_nether, the_end)")
		},
		returns = @ReturnDoc(type = StringDef.class, desc = "A string representing the block state"),
		examples = "getBlock(0,0,0,\"the_end\");"
	)
	private String getBlockWithWorld(Arguments arguments) {
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
		String blockString = world.getBlockState(new BlockPos(x, y, z)).toString();
		return blockString.substring(blockString.indexOf("{") + 1, blockString.indexOf("}"));
	}

	@FunctionDoc(
		name = "getBlock",
		desc = "This gets a string for a block",
		params = {
			@ParameterDoc(type = Vector3Def.class, name = "vector", desc = "A vector containing the x, y and z coordinates"),
			@ParameterDoc(type = StringDef.class, name = "block", desc = "A string representing the target dimension (overworld, the_nether, the_end)")
		},
		returns = @ReturnDoc(type = StringDef.class, desc = "A string representing the block state"),
		examples = "getBlock(new Vector3(0,0,0),\"the_end\");"
	)
	private String getBlockVectorWithWorld(Arguments arguments) {
		Vec3d vec = arguments.nextPrimitive(Vector3Def.class);
		int x = (int) vec.getX();
		int y = (int) vec.getY();
		int z = (int) vec.getZ();
		String worldString = arguments.nextPrimitive(StringDef.class);

		RegistryKey<World> registry = RuntimeError.wrap(() -> RegistryKey.of(RegistryKeys.WORLD, Identifier.tryParse(worldString)));
		ServerCommandSource source = ScriptUtils.getScriptHolder(arguments.getInterpreter());
		ServerWorld world = source.getServer().getWorld(registry);
		if (world == null) {
			throw new RuntimeError("Failed to get world for: " + worldString);
		}
		String blockString = world.getBlockState(new BlockPos(x, y, z)).toString();
		return blockString.substring(blockString.indexOf("{") + 1, blockString.indexOf("}"));
	}

	@FunctionDoc(
		name = "getBlock",
		desc = "This gets a string for a block",
		params = {
			@ParameterDoc(type = NumberDef.class, name = "x", desc = "The x coordinate"),
			@ParameterDoc(type = NumberDef.class, name = "y", desc = "The y coordinate"),
			@ParameterDoc(type = NumberDef.class, name = "z", desc = "The z coordinate")
		},
		returns = @ReturnDoc(type = StringDef.class, desc = "A string representing the block state"),
		examples = "getBlock(0,0,0);"
	)
	private String getBlock(Arguments arguments) {
		int x = arguments.nextPrimitive(NumberDef.class).intValue();
		int y = arguments.nextPrimitive(NumberDef.class).intValue();
		int z = arguments.nextPrimitive(NumberDef.class).intValue();
		ServerCommandSource source = ScriptUtils.getScriptHolder(arguments.getInterpreter());
		String blockString = source.getWorld().getBlockState(new BlockPos(x, y, z)).toString();
		return blockString.substring(blockString.indexOf("{") + 1, blockString.indexOf("}"));
	}

	@FunctionDoc(
		name = "getBlock",
		desc = "This gets a string for a block",
		params = {
			@ParameterDoc(type = Vector3Def.class, name = "vector", desc = "A vector containing the x, y and z coordinates")
		},
		returns = @ReturnDoc(type = StringDef.class, desc = "A string representing the block state"),
		examples = "getBlock(new Vector3(0,0,0));"
	)
	private String getBlockVector(Arguments arguments) {
		Vec3d vec = arguments.nextPrimitive(Vector3Def.class);
		int x = (int) vec.getX();
		int y = (int) vec.getY();
		int z = (int) vec.getZ();
		ServerCommandSource source = ScriptUtils.getScriptHolder(arguments.getInterpreter());
		String blockString = source.getWorld().getBlockState(new BlockPos(x, y, z)).toString();
		return blockString.substring(blockString.indexOf("{") + 1, blockString.indexOf("}"));
	}

	@FunctionDoc(
		name = "place",
		desc = "This places a block at the specified coordinates in the players current dimension",
		params = {
			@ParameterDoc(type = StringDef.class, name = "block", desc = {"A string representing the block that will be placed", "the syntax is the same as when using /setblock or /fill commands"}),
			@ParameterDoc(type = NumberDef.class, name = "x", desc = "The x coordinate"),
			@ParameterDoc(type = NumberDef.class, name = "y", desc = "The y coordinate"),
			@ParameterDoc(type = NumberDef.class, name = "z", desc = "The z coordinate")
		},
		examples = "place(\"air\", 0,0,0);"
	)
	private Void placeDefault(Arguments arguments) {
		String block = arguments.nextPrimitive(StringDef.class);
		int x = arguments.nextPrimitive(NumberDef.class).intValue();
		int y = arguments.nextPrimitive(NumberDef.class).intValue();
		int z = arguments.nextPrimitive(NumberDef.class).intValue();
		ServerCommandSource source = ScriptUtils.getScriptHolder(arguments.getInterpreter());
		this.place(arguments.getInterpreter(), source.getWorld(), block, x, y, z);
		return null;
	}

	@FunctionDoc(
		name = "place",
		desc = "This places a block at the specified coordinates in the specified dimension",
		params = {
			@ParameterDoc(type = StringDef.class, name = "block", desc = {"A string representing the block that will be placed", "the syntax is the same as when using /setblock or /fill commands"}),
			@ParameterDoc(type = NumberDef.class, name = "x", desc = "The x coordinate"),
			@ParameterDoc(type = NumberDef.class, name = "y", desc = "The y coordinate"),
			@ParameterDoc(type = NumberDef.class, name = "z", desc = "The z coordinate"),
			@ParameterDoc(type = StringDef.class, name = "block", desc = "A string representing the target dimension (overworld, the_nether, the_end)"),
		},
		examples = "place(\"air\", 0,0,0, \"overworld\");"
	)
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

	@FunctionDoc(
		name = "place",
		desc = "This places a block at the specified coordinates in the players current dimension ",
		params = {
			@ParameterDoc(type = StringDef.class, name = "block", desc = {"A string representing the block that will be placed", "the syntax is the same as when using /setblock or /fill commands"}),
			@ParameterDoc(type = Vector3Def.class, name = "vector", desc = "A vector containing the x, y and z coordinates")
		},
		examples = "place(\"air\", new Vector3(0,0,0));"
	)
	private Void placeVector(Arguments arguments) {
		String block = arguments.nextPrimitive(StringDef.class);
		Vec3d vec = arguments.nextPrimitive(Vector3Def.class);
		int x = (int) vec.getX();
		int y = (int) vec.getY();
		int z = (int) vec.getZ();
		ServerCommandSource source = ScriptUtils.getScriptHolder(arguments.getInterpreter());
		this.place(arguments.getInterpreter(), source.getWorld(), block, x, y, z);
		return null;
	}

	@FunctionDoc(
		name = "place",
		desc = "This places a block at the specified coordinates in the specified dimension",
		params = {
			@ParameterDoc(type = StringDef.class, name = "block", desc = {"A string representing the block that will be placed", "the syntax is the same as when using /setblock or /fill commands"}),
			@ParameterDoc(type = Vector3Def.class, name = "vector", desc = "A vector containing the x, y and z coordinates"),
			@ParameterDoc(type = StringDef.class, name = "block", desc = "A string representing the target dimension (overworld, the_nether, the_end)"),
		},
		examples = "place(\"air\", new Vector3(0,0,0), \"the_nether\");"
	)
	private Void placeVectorWithWorld(Arguments arguments) {
		String block = arguments.nextPrimitive(StringDef.class);
		Vec3d vec = arguments.nextPrimitive(Vector3Def.class);
		int x = (int) vec.getX();
		int y = (int) vec.getY();
		int z = (int) vec.getZ();
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

	@FunctionDoc(
		name = "area",
		desc = "Used to iterate a lambda over an area specified by an origin and size",
		params = {
			@ParameterDoc(type = NumberDef.class, name = "originX", desc = "The origin x coordinate"),
			@ParameterDoc(type = NumberDef.class, name = "originY", desc = "The origin y coordinate"),
			@ParameterDoc(type = NumberDef.class, name = "originZ", desc = "The origin z coordinate"),
			@ParameterDoc(type = NumberDef.class, name = "sizeX", desc = "The size in x axis"),
			@ParameterDoc(type = NumberDef.class, name = "sizeY", desc = "The size in y axis"),
			@ParameterDoc(type = NumberDef.class, name = "sizeZ", desc = "The size in z axis"),
			@ParameterDoc(type = FunctionDef.class, name = "lambda", desc = {"This is the lambda function that gets iterated over the specified area",
				"It takes 1-3 Vector3 parameters",
				"1. Absolute coordinates of the block in the world",
				"2. Normalized coordinates within the area (from -1 -1 -1 to 1 1 1 no matter the area size)",
				"3. Local area coordinates (0, 0, 0 at area origin and goes up to sizeX, sizeY, sizeZ"})
		},
		examples = "area(100, 100, 100, 200, 1, 200, fun(aPos, nPos, lPos) {/*your code goes here*/});"
	)
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
			case 2 -> (absolute) -> {
				Vec3i local = absolute.subtract(new Vec3i(originX, originY, originZ));
				Vec3d normal = new Vec3d(
					MathHelper.lerp(local.getX() / (double) sizeX, -1, 1),
					MathHelper.lerp(local.getY() / (double) sizeY, -1, 1),
					MathHelper.lerp(local.getZ() / (double) sizeZ, -1, 1)
				);
				return List.of(absolute, normal);

			};
			case 3 -> (absolute) -> {
				Vec3i local = absolute.subtract(new Vec3i(originX, originY, originZ));
				Vec3d normal = new Vec3d(
					MathHelper.lerp(local.getX() / (double) sizeX, -1, 1),
					MathHelper.lerp(local.getY() / (double) sizeY, -1, 1),
					MathHelper.lerp(local.getZ() / (double) sizeZ, -1, 1)
				);
				return List.of(absolute, normal, local);
			};
			default -> throw new RuntimeError("Callback function needs to have 1, 2, or 3 parameters");
		};

		ScriptUtils.showProgressBar(interpreter);
		for (int x = originX; x < originX + sizeX; x++) {
			for (int y = originY; y < originY + sizeY; y++) {
				for (int z = originZ; z < originZ + sizeZ; z++, completed++) {
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
