package com.mrbysco.measurements.platform;

import com.mrbysco.measurements.ClientMeasurementsFabric;
import com.mrbysco.measurements.config.LineColor;
import com.mrbysco.measurements.config.TextColor;
import com.mrbysco.measurements.platform.services.IPlatformHelper;
import net.minecraft.world.entity.player.Player;

public class FabricPlatformHelper implements IPlatformHelper {
	@Override
	public boolean isValidUser(Player player) {
		return player != null && player.level.isClientSide;
	}

	@Override
	public LineColor getLineColor() {
		return ClientMeasurementsFabric.config.get().client.lineColor;
	}

	@Override
	public TextColor getTextColor() {
		return ClientMeasurementsFabric.config.get().client.textColor;
	}

	@Override
	public float getLineWidth() {
		return (float) ClientMeasurementsFabric.config.get().client.lineWidth;
	}

	@Override
	public float getLineWidthMax() {
		return (float) ClientMeasurementsFabric.config.get().client.lineWidthMax;
	}

	@Override
	public float getTextSize() {
		return (float) ClientMeasurementsFabric.config.get().client.textSize;
	}
}
