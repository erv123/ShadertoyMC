package net.erv123.shadertoymc.util;

import net.erv123.shadertoymc.ShadertoyMC;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

public class WorldUtils {
	public static ServerWorld worldFromString(String string) {
		Identifier id = Identifier.tryParse(string);
		return id == null ? null : ShadertoyMC.SERVER.getWorld(RegistryKey.of(RegistryKeys.WORLD, id));
	}
}
