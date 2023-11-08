package com.mrbysco.measurements.mixin;

import com.mrbysco.measurements.client.ClientClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Inject(method = "disconnect(Lnet/minecraft/client/gui/screens/Screen;)V", at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/GameRenderer;resetData()V",
			shift = At.Shift.AFTER,
			ordinal = 0))
	public void measurements$disconnect(Screen screen, CallbackInfo ci) {
		ClientClass.onLogOut();
	}
}