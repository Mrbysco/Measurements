package com.mrbysco.measurements.client;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

public class LoginHandler {
	@SubscribeEvent
	public void onLogIn(ClientPlayerNetworkEvent.LoggingIn event) {
		ClientClass.onLogIn();
	}

	@SubscribeEvent
	public void onLogOut(ClientPlayerNetworkEvent.LoggingOut event) {
		ClientClass.onLogOut();
	}
}
