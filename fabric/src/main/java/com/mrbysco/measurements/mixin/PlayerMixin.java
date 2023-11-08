package com.mrbysco.measurements.mixin;

import com.mrbysco.measurements.callback.PlayerTickCallback;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {
	@Inject(method = "tick", at = @At("RETURN"))
	private void measurements$tick(CallbackInfo ci) {
		PlayerTickCallback.EVENT.invoker().tick((Player) (Object) this);
	}
}
