package com.mrbysco.measurements.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

@EventBusSubscriber(Dist.CLIENT)
public class LoginHandler {
	@SubscribeEvent
	public static void onLogIn(ClientPlayerNetworkEvent.LoggingIn event) {
		ClientClass.onLogIn();
	}

	@SubscribeEvent
	public static void onLogOut(ClientPlayerNetworkEvent.LoggingOut event) {
		ClientClass.onLogOut();
	}
}
