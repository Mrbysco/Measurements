package com.mrbysco.measurements.platform;

import com.mrbysco.measurements.platform.services.IPlatformHelper;
import net.minecraft.world.entity.player.Player;

public class FabricPlatformHelper implements IPlatformHelper {
	@Override
	public boolean isValidUser(Player player) {
		return player != null && player.level().isClientSide();
	}
}
