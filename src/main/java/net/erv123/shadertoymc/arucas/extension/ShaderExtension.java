package net.erv123.shadertoymc.arucas.extension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.senseiwells.arucas.api.ArucasExtension;
import me.senseiwells.arucas.api.docs.annotations.ExtensionDoc;
import me.senseiwells.arucas.builtin.NumberDef;
import me.senseiwells.arucas.builtin.StringDef;
import me.senseiwells.arucas.core.Interpreter;
import me.senseiwells.arucas.exceptions.RuntimeError;
import me.senseiwells.arucas.utils.Arguments;
import me.senseiwells.arucas.utils.BuiltInFunction;
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
			BuiltInFunction.of("progress", 1, this::updateProgress)
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

	private Void updateProgress(Arguments arguments) {
		float progress = arguments.nextPrimitive(NumberDef.class).floatValue();
		ScriptUtils.showProgressBar(arguments.getInterpreter());
		CommandBossBar bar = ScriptUtils.getBossBar(arguments.getInterpreter());
		bar.setPercent(progress);
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