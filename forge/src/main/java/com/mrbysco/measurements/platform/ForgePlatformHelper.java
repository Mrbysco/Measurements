package com.mrbysco.measurements.platform;

import com.mrbysco.measurements.config.LineColor;
import com.mrbysco.measurements.config.MeasurementConfigForge;
import com.mrbysco.measurements.config.TextColor;
import com.mrbysco.measurements.platform.services.IPlatformHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.FakePlayer;

public class ForgePlatformHelper implements IPlatformHelper {

	@Override
	public boolean isValidUser(Player player) {
		return player != null && !(player instanceof FakePlayer) && player.level().isClientSide;
	}

	@Override
	public LineColor getLineColor() {
		return MeasurementConfigForge.CLIENT.lineColor.get();
	}

	@Override
	public TextColor getTextColor() {
		return MeasurementConfigForge.CLIENT.textColor.get();
	}

	@Override
	public float getLineWidth() {
		return MeasurementConfigForge.CLIENT.lineWidth.get().floatValue();
	}

	@Override
	public float getLineWidthMax() {
		return MeasurementConfigForge.CLIENT.lineWidthMax.get().floatValue();
	}

	@Override
	public float getTextSize() {
		return MeasurementConfigForge.CLIENT.textSize.get().floatValue();
	}
}
