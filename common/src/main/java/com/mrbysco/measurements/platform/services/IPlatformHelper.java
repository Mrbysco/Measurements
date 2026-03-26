package com.mrbysco.measurements.platform.services;

import net.minecraft.world.entity.player.Player;

public interface IPlatformHelper {

	/**
	 * if the user trying to right-click with the tape is allowed to use the tape
	 *
	 * @return If it's a valid user
	 */
	boolean isValidUser(Player player);
}
