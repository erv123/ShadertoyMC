package net.erv123.shadertoymc.mixins;


import net.erv123.shadertoymc.arucas.extension.BrushExtension;
import net.erv123.shadertoymc.arucas.impl.Brush;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;


@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow public abstract Item getItem();
	@Inject(
		method = "use",
		at = @At("HEAD")
	)
	private void onUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
		if(user instanceof ServerPlayerEntity player) {
			String item = this.getItem().toString();
			if (BrushExtension.BRUSHES != null) {
				for (List<Brush> brushList : BrushExtension.BRUSHES.values()) {
					for (Brush brush : brushList) {
						brush.execute(item, player);
					}
				}
			}
		}
	}
}