package com.mrbysco.measurements.mixin;

import com.mrbysco.measurements.client.ClientClass;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
	@Inject(method = "handleLogin(Lnet/minecraft/network/protocol/game/ClientboundLoginPacket;)V", at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/player/LocalPlayer;resetPos()V",
			shift = At.Shift.AFTER,
			ordinal = 0))
	public void measurements$handleLogin(ClientboundLoginPacket loginPacket, CallbackInfo ci) {
		ClientClass.onLogIn();
	}
}
