package com.mrbysco.measurements.platform;

import com.mrbysco.measurements.platform.services.IPlatformHelper;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.FakePlayer;

public class NeoForgePlatformHelper implements IPlatformHelper {

	@Override
	public boolean isValidUser(Player player) {
		return player != null && !(player instanceof FakePlayer) && player.level().isClientSide();
	}
}
