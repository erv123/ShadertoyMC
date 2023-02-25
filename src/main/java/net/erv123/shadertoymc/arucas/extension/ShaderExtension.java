package net.erv123.shadertoymc.arucas.extension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.senseiwells.arucas.api.ArucasExtension;
import me.senseiwells.arucas.api.docs.annotations.ExtensionDoc;
import me.senseiwells.arucas.api.docs.annotations.FunctionDoc;
import me.senseiwells.arucas.api.docs.annotations.ParameterDoc;
import me.senseiwells.arucas.api.docs.annotations.ReturnDoc;
import me.senseiwells.arucas.builtin.FunctionDef;
import me.senseiwells.arucas.builtin.NumberDef;
import me.senseiwells.arucas.builtin.ObjectDef;
import me.senseiwells.arucas.builtin.StringDef;
import me.senseiwells.arucas.classes.instance.ClassInstance;
import me.senseiwells.arucas.core.Interpreter;
import me.senseiwells.arucas.exceptions.RuntimeError;
import me.senseiwells.arucas.functions.builtin.BuiltInFunction;
import me.senseiwells.arucas.utils.Arguments;
import me.senseiwells.arucas.utils.Trace;
import me.senseiwells.arucas.utils.Util;
import me.senseiwells.arucas.utils.impl.ArucasMap;
import net.erv123.shadertoymc.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
			BuiltInFunction.of("dimension", this::dimension),
			BuiltInFunction.arb("query", this::query),
			BuiltInFunction.arb("place", this::place),
			BuiltInFunction.of("area", 1, this::area),
			BuiltInFunction.of("area", 7, this::customArea)
		);
	}

	@NotNull
	@Override
	public String getName() {
		return "ShaderExtension";
	}

	@FunctionDoc(
		name = "dimension",
		desc = {
			"Returns string for your current dimension.",
			"For example 'minecraft:the_nether'"
		},
		returns = @ReturnDoc(type = StringDef.class, desc = "String representing your current dimension."),
		examples = "world = dimension();"
	)
	private String dimension(Arguments arguments) {
		return ScriptUtils.getScriptHolder(arguments.getInterpreter()).getWorld().getRegistryKey().getValue().toString();
	}

	@FunctionDoc(
		name = "query",
		desc = {
			"This queries a the data for a block at a given position in a given dimension.",
			"The parameters for this function are as follows:",
			"position - this can either be as 3 numbers (x, y, z) or as a single Vector3,",
			"type - this is optional, this is the type of query, this can be one of the following: 'default', 'block', 'state', 'nbt', see examples,",
			"dimension - this is an optional argument defining the dimension in which to place the block,",
			"by default this is the dimension of the player that executed the script."
		},
		params = @ParameterDoc(type = ObjectDef.class, name = "args", desc = "The query arguments, see function description.", isVarargs = true),
		examples = {
			"query(0, 0, 0); // -> 'minecraft:oak_leaves[distance=7,persistent=true,waterlogged=false]'",
			"query(10, 0, 10); // -> 'minecraft:chest[facing=west,type=single,waterlogged=false]{Items:[{Count:64b,Slot:11b,id:\"minecraft:spruce_fence_gate\"},{Count:1b,Slot:14b,id:\"minecraft:diamond_chestplate\",tag:{Damage:0,Enchantments:[{id:\"minecraft:protection\",lvl:1s}],RepairCost:1,display:{Name:'{\"text\":\"Why Are You Reading This?\"}'}}}]}'"
		}
	)
	private Object query(Arguments arguments) {
		BlockPos pos = new BlockPos(ArgumentUtils.getVec3i(arguments));
		ServerCommandSource source = ScriptUtils.getScriptHolder(arguments.getInterpreter());

		String type = null;
		ServerWorld world = null;
		while (arguments.hasNext()) {
			String next = arguments.nextPrimitive(StringDef.class);
			String lower = next.toLowerCase(Locale.ROOT);
			switch (lower) {
				case "block", "id", "state", "properties", "nbt", "tags", "default" -> {
					if (type != null) {
						// Type was already set
						throw new RuntimeError("Cannot have multiple query types");
					}
					type = lower;
				}
				default -> {
					if (world != null) {
						// World was already set
						throw new RuntimeError("Invalid argument '" + next + "'");
					}

					world = WorldUtils.worldFromString(next);
					if (world == null) {
						throw new RuntimeError("Invalid world '" + next + "'");
					}
 				}
			}
		}

		if (type == null) {
			type = "default";
		}
		if (world == null) {
			world = source.getWorld();
		}

		return switch (type) {
			case "default" -> {
				yield BlockUtils.blockData(world, pos);
			}
			case "block", "id" -> {
				yield BlockUtils.blockId(world.getBlockState(pos));
			}
			case "state", "properties" -> {
				yield BlockUtils.stateAsMap(world.getBlockState(pos), arguments.getInterpreter());
			}
			case "nbt", "tags" -> {
				BlockEntity entity =  world.getBlockEntity(pos);
				if (entity == null) {
					yield new ArucasMap();
				}
				yield BlockUtils.nbtAsMap(entity, arguments.getInterpreter());
			}
			default -> throw new IllegalStateException();
 		};
	}

	@FunctionDoc(
		name = "place",
		desc = {
			"This function allows you to place a block in a given world.",
			"The parameters for this function are as follows:",
			"position - this can either be as 3 numbers (x, y, z) or as a single Vector3,",
			"block - this is the same format you would use for a setblock command,",
			"dimension - this is an optional argument defining the dimension in which to place the block,",
			"by default this is the dimension of the player that executed the script."
		},
		params = @ParameterDoc(type = ObjectDef.class, name = "args", desc = "The placement arguments, see the function description.", isVarargs = true),
		examples = {
			"place(0, 0, 0, 'minecraft:oak_planks');",
			"place(new Vector3(0, 0, 0), 'oak_planks', 'minecraft:the_nether');",
			"place(100, 64, 10, 'oak_sign[rotation = 4]{Text1: \"Example\"}', 'overworld');"
		}
	)
	private Void place(Arguments arguments) {
		ServerCommandSource source = ScriptUtils.getScriptHolder(arguments.getInterpreter());
		BlockPos pos = new BlockPos(ArgumentUtils.getVec3i(arguments));
		String block = arguments.nextPrimitive(StringDef.class);
		ServerWorld world = source.getWorld();

		if (arguments.hasNext()) {
			String worldString = arguments.nextPrimitive(StringDef.class);
			world = WorldUtils.worldFromString(worldString);
			if (world == null) {
				throw new RuntimeError("Invalid world '" + worldString + "'");
			}
		}
		if (arguments.hasNext()) {
			throw new RuntimeError("Too many arguments");
		}

		DynamicRegistryManager manager = source.getRegistryManager();
		RegistryWrapper<Block> registryWrapper = manager.getWrapperOrThrow(RegistryKeys.BLOCK);
		try {
			BlockArgumentParser.BlockResult result = BlockArgumentParser.block(registryWrapper, block, true);
			WorldUtils.setBlockWithNoUpdates(world, pos, result.blockState());

			NbtCompound nbt = result.nbt();
			if (nbt != null) {
				BlockEntity entity = world.getBlockEntity(pos);
				if (entity != null) {
					entity.readNbt(nbt);
				}
			}
		} catch (CommandSyntaxException e) {
			throw new RuntimeError("Invalid block: " + block, e);
		}
		return null;
	}

	@FunctionDoc(
		name = "area",
		desc = {
			"Used to iterate over an area specified by the player.",
			"This can be done with `/shadertoy area pos1 (<x> <y> <z>)?` and `/shadertoy area pos2 (<x> <y> <z>)?`,",
			"or `/shadertoy area origin <x> <y> <z> size <sX> <sY> <sZ>`"
		},
		params = @ParameterDoc(
			type = FunctionDef.class,
			name = "consumer",
			desc = {
				"This is the lambda function that gets iterated over the specified area.",
				"It takes 1-3 Vector3 parameters:",
				"1. Absolute coordinates of the block in the world.",
				"2. Normalized coordinates within the area (from -1, -1, -1 to 1, 1, 1).",
				"3. Local area coordinates (0, 0, 0 at area origin and goes up to sizeX, sizeY, sizeZ)."
			}
		),
		examples = {
			"""
			area(fun (aPos, nPos, lPos) {
			    // Do something...
			});
			"""
		}
	)
	private Void area(Arguments arguments) {
		Interpreter interpreter = arguments.getInterpreter();
		Area area = ScriptUtils.getArea(interpreter);
		if (area == null) {
			throw new RuntimeError("User has not defined an area to use!");
		}

		ClassInstance callback = arguments.nextFunction();

		interpreter.logDebug("Iterating user-defined area: " + area);
		this.internalArea(area.getOrigin(), area.getSize(), callback, interpreter);
		return null;
	}

	@FunctionDoc(
		name = "area",
		desc = "Used to iterate over an area specified by an origin and size.",
		params = {
			@ParameterDoc(type = NumberDef.class, name = "originX", desc = "The origin x coordinate"),
			@ParameterDoc(type = NumberDef.class, name = "originY", desc = "The origin y coordinate"),
			@ParameterDoc(type = NumberDef.class, name = "originZ", desc = "The origin z coordinate"),
			@ParameterDoc(type = NumberDef.class, name = "sizeX", desc = "The size in x axis"),
			@ParameterDoc(type = NumberDef.class, name = "sizeY", desc = "The size in y axis"),
			@ParameterDoc(type = NumberDef.class, name = "sizeZ", desc = "The size in z axis"),
			@ParameterDoc(
				type = FunctionDef.class,
				name = "consumer",
				desc = {
					"This is the lambda function that gets iterated over the specified area.",
					"It takes 1-3 Vector3 parameters:",
					"1. Absolute coordinates of the block in the world.",
					"2. Normalized coordinates within the area (from -1, -1, -1 to 1, 1, 1).",
					"3. Local area coordinates (0, 0, 0 at area origin and goes up to sizeX, sizeY, sizeZ)."
				}
			)
		},
		examples = {
			"""
			area(100, 100, 100, 200, 1, 200, fun(aPos, nPos, lPos) {
			    // Do something...
			});
			"""
		}
	)
	private Void customArea(Arguments arguments) {
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

		this.internalArea(new BlockPos(originX, originY, originZ), new Vec3i(sizeX, sizeY, sizeZ), callback, interpreter);
		return null;
	}

	private void internalArea(BlockPos origin, Vec3i size, ClassInstance callback, Interpreter interpreter) {
		int originX = origin.getX(), originY = origin.getY(), originZ = origin.getZ();
		int sizeX = size.getX(), sizeY = size.getY(), sizeZ = size.getZ();

		int parameters = callback.asPrimitive(FunctionDef.class).getCount();
		Function<Vec3i, List<Object>> generator = switch (parameters) {
			case 1 -> List::of;
			case 2 -> (absolute) -> {
				Vec3i local = absolute.subtract(origin);
				Vec3d normal = new Vec3d(
					MathHelper.lerp(local.getX() / (double) sizeX, -1, 1),
					MathHelper.lerp(local.getY() / (double) sizeY, -1, 1),
					MathHelper.lerp(local.getZ() / (double) sizeZ, -1, 1)
				);
				return List.of(absolute, normal);

			};
			case 3 -> (absolute) -> {
				Vec3i local = absolute.subtract(origin);
				Vec3d normal = new Vec3d(
					MathHelper.lerp(local.getX() / (double) sizeX, -1, 1),
					MathHelper.lerp(local.getY() / (double) sizeY, -1, 1),
					MathHelper.lerp(local.getZ() / (double) sizeZ, -1, 1)
				);
				return List.of(absolute, normal, local);
			};
			default -> throw new RuntimeError("Callback function needs to have 1, 2, or 3 parameters");
		};

		int volume = sizeX * sizeY * sizeZ;
		int completed = 0;

		ScriptUtils.showProgressBar(interpreter);
		for (int x = originX; x < originX + sizeX; x++) {
			for (int y = originY; y < originY + sizeY; y++) {
				for (int z = originZ; z < originZ + sizeZ; z++, completed++) {
					List<ClassInstance> args = new ArrayList<>();
					for (Object vec : generator.apply(new Vec3i(x, y, z))) {
						args.add(interpreter.convertValue(vec));
					}
					interpreter.call(callback, args, Trace.INTERNAL);
				}
			}
			ScriptUtils.getBossBar(interpreter).setPercent(completed / (float) volume);
		}
		ScriptUtils.hideProgressBar(interpreter);
	}
}
