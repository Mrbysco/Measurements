package com.mrbysco.measurements.platform;

import com.mrbysco.measurements.config.LineColor;
import com.mrbysco.measurements.config.MeasurementConfigNeoForge;
import com.mrbysco.measurements.config.TextColor;
import com.mrbysco.measurements.platform.services.IPlatformHelper;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.FakePlayer;

public class NeoForgePlatformHelper implements IPlatformHelper {

	@Override
	public boolean isValidUser(Player player) {
		return player != null && !(player instanceof FakePlayer) && player.level().isClientSide;
	}

	@Override
	public LineColor getLineColor() {
		return MeasurementConfigNeoForge.CLIENT.lineColor.get();
	}

	@Override
	public TextColor getTextColor() {
		return MeasurementConfigNeoForge.CLIENT.textColor.get();
	}

	@Override
	public float getLineWidth() {
		return MeasurementConfigNeoForge.CLIENT.lineWidth.get().floatValue();
	}

	@Override
	public float getLineWidthMax() {
		return MeasurementConfigNeoForge.CLIENT.lineWidthMax.get().floatValue();
	}

	@Override
	public float getTextSize() {
		return MeasurementConfigNeoForge.CLIENT.textSize.get().floatValue();
	}
}
