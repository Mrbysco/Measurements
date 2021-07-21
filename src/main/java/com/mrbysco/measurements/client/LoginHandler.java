package com.mrbysco.measurements.client;

import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LoginHandler {
	@SubscribeEvent
	public void onLogIn(ClientPlayerNetworkEvent.LoggedInEvent event) {
		ClientHandler.clear();
	}

	@SubscribeEvent
	public void onLogOut(ClientPlayerNetworkEvent.LoggedOutEvent event) {
		ClientHandler.clear();
	}
}