package com.mrbysco.measurements.platform;

import com.mrbysco.measurements.ClientMeasurementsFabric;
import com.mrbysco.measurements.config.LineColor;
import com.mrbysco.measurements.config.MeasurementsConfigFabric;
import com.mrbysco.measurements.config.TextColor;
import com.mrbysco.measurements.mixin.UseOnContextAccessor;
import com.mrbysco.measurements.platform.services.IPlatformHelper;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;

public class FabricPlatformHelper implements IPlatformHelper {
	@Override
	public boolean isValidUser(Player player) {
		return player != null && player.level.isClientSide;
	}

	@Override
	public BlockHitResult getHitResult(UseOnContext context) {
		return ((UseOnContextAccessor) context).measurements_getHitResult();
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
