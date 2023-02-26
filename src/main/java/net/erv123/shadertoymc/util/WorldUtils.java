package net.erv123.shadertoymc.util;

import net.erv123.shadertoymc.ShadertoyMC;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldUtils {
	public static void setBlockWithNoUpdates(World world, BlockPos pos, BlockState state) {
		ShaderUtils.canBlocksFall = false;
		world.setBlockState(pos, state, Block.NOTIFY_LISTENERS, 0);
		ShaderUtils.canBlocksFall = true;
	}

	public static ServerWorld worldFromString(String string) {
		Identifier id = Identifier.tryParse(string);
		return id == null ? null : ShadertoyMC.SERVER.getWorld(RegistryKey.of(RegistryKeys.WORLD, id));
	}
}
