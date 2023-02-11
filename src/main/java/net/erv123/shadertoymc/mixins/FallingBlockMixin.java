package net.erv123.shadertoymc.mixins;

import net.erv123.shadertoymc.ShaderUtils;
import net.erv123.shadertoymc.ShadertoyMC;
import net.minecraft.block.FallingBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlock.class)
public class FallingBlockMixin {
    @Inject(method = "onBlockAdded",at = @At("HEAD"),cancellable = true)
    private void onBlockAdded(CallbackInfo ci){
        if(!ShaderUtils.canBlocksFall){
            ci.cancel();
            ShadertoyMC.LOGGER.info("MIXIN");
        }
    }
}
