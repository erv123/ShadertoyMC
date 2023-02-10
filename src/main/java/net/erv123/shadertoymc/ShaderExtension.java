package net.erv123.shadertoymc;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.senseiwells.arucas.api.ArucasExtension;
import me.senseiwells.arucas.builtin.NumberDef;
import me.senseiwells.arucas.builtin.StringDef;
import me.senseiwells.arucas.core.Interpreter;
import me.senseiwells.arucas.utils.Arguments;
import me.senseiwells.arucas.utils.BuiltInFunction;
import me.senseiwells.arucas.utils.impl.ArucasList;
import net.minecraft.block.Block;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ShaderExtension implements ArucasExtension {

    @NotNull
    @Override
    public List<BuiltInFunction> getBuiltInFunctions() {
        return List.of(
                BuiltInFunction.of("getWorld", this::getWorld),
                BuiltInFunction.of("place", 5, this::place),
                BuiltInFunction.of("place", 4, this::placeNoWorldArg),
                BuiltInFunction.of("getArea", this::getArea)
        );
    }

    @NotNull
    @Override
    public String getName() {
        return "shaderExtension";
    }

    public String getWorld(Arguments arguments) {
        ServerPlayerEntity serverPlayerEntity = ShaderUtils.getPlayerForInterpreter(arguments.getInterpreter());
        return serverPlayerEntity.getWorld().getRegistryKey().getValue().getPath();
    }

    public Void place(Arguments arguments) {
        ServerPlayerEntity entity = ShaderUtils.getPlayerForInterpreter(arguments.getInterpreter());
        String block = arguments.nextPrimitive(StringDef.class);
        int x = arguments.nextPrimitive(NumberDef.class).intValue();
        int y = arguments.nextPrimitive(NumberDef.class).intValue();
        int z = arguments.nextPrimitive(NumberDef.class).intValue();
        String worldString = arguments.nextPrimitive(StringDef.class);
        BlockPos pos = new BlockPos(x, y, z);
        try {
            DynamicRegistryManager manager = entity.server.getRegistryManager();
            RegistryWrapper<Block> registryWrapper = manager.getWrapperOrThrow(RegistryKeys.BLOCK);
            BlockArgumentParser.BlockResult blockResult = BlockArgumentParser.block(registryWrapper, block, true);
            RegistryKey<World> worldRegistry = RegistryKey.of(RegistryKeys.WORLD, Identifier.tryParse(worldString));
            World world = entity.server.getWorld(worldRegistry);
            SharedConstants.canBlocksFall = false;
            world.setBlockState(pos, blockResult.blockState(), Block.NOTIFY_LISTENERS, 0);
            SharedConstants.canBlocksFall = true;
        } catch (CommandSyntaxException e) {
            ShadertoyMC.LOGGER.error("Failed to place block", e);
        }
        return null;
    }

    public Void placeNoWorldArg(Arguments arguments) {
        ServerPlayerEntity entity = ShaderUtils.getPlayerForInterpreter(arguments.getInterpreter());
        String block = arguments.nextPrimitive(StringDef.class);
        int x = arguments.nextPrimitive(NumberDef.class).intValue();
        int y = arguments.nextPrimitive(NumberDef.class).intValue();
        int z = arguments.nextPrimitive(NumberDef.class).intValue();
        ServerPlayerEntity serverPlayerEntity = ShaderUtils.getPlayerForInterpreter(arguments.getInterpreter());
        World world = serverPlayerEntity.getWorld();
        BlockPos pos = new BlockPos(x, y, z);
        try {
            DynamicRegistryManager manager = entity.server.getRegistryManager();
            RegistryWrapper<Block> registryWrapper = manager.getWrapperOrThrow(RegistryKeys.BLOCK);
            BlockArgumentParser.BlockResult blockResult = BlockArgumentParser.block(registryWrapper, block, true);
            SharedConstants.canBlocksFall = false;
            world.setBlockState(pos, blockResult.blockState(),Block.NOTIFY_LISTENERS, 0);
            SharedConstants.canBlocksFall = true;
        } catch (CommandSyntaxException e) {
            ShadertoyMC.LOGGER.error("Failed to place block");
        }
        return null;
    }

    public ArucasList getArea(Arguments arguments) {
        Interpreter interpreter = arguments.getInterpreter();
        List<Integer> area = ShaderArea.read().getParams();
        ArucasList list = new ArucasList();
        area.forEach(e -> list.add(interpreter.convertValue(e)));
        return list;
    }
}
