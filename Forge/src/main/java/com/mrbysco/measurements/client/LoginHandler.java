package com.mrbysco.measurements.client;

import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
