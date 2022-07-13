package com.mrbysco.measurements.client;

import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LoginHandler {
	@SubscribeEvent
	public void onLogIn(ClientPlayerNetworkEvent.LoggingIn event) {
		ClientHandler.clear();
	}

	@SubscribeEvent
	public void onLogOut(ClientPlayerNetworkEvent.LoggingOut event) {
		ClientHandler.clear();
	}
}
