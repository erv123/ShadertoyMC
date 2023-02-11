package net.erv123.shadertoymc.mixins;

import net.erv123.shadertoymc.util.MinecraftServerTicker;
import net.erv123.shadertoymc.util.ShaderUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements MinecraftServerTicker {
	@Shadow
	private long nextTickTimestamp;
	@Shadow
	private long lastTimeReference;

	@Shadow
	public abstract void tick(BooleanSupplier shouldKeepTicking);

	@Shadow
	public abstract boolean runTask();

	@Inject(method = "loadWorld", at = @At("HEAD"))
	private void serverLoaded(CallbackInfo ci) {
		ShaderUtils.SERVER = (MinecraftServer) (Object) this;
	}

	@Override
	public void forceTick(BooleanSupplier bool) {
		this.nextTickTimestamp = this.lastTimeReference = Util.getMeasuringTimeMs();
		this.tick(bool);
		while (this.runTask()) {
			Thread.yield();
		}
	}
}
