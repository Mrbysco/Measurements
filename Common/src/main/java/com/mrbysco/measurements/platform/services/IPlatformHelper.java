package com.mrbysco.measurements.platform.services;

import com.mrbysco.measurements.config.LineColor;
import com.mrbysco.measurements.config.TextColor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;

public interface IPlatformHelper {

	/**
	 * if the user trying to right-click with the tape is allowed to use the tape
	 *
	 * @return If it's a valid user
	 */
	boolean isValidUser(Player player);

	/**
	 * Get's the BlockHitResult from the context because it's private on common
	 *
	 * @return the hitResult
	 */
	BlockHitResult getHitResult(UseOnContext context);

	/**
	 * Gets the linecolor specified in the config
	 *
	 * @return Configured LineColor
	 */
	LineColor getLineColor();

	/**
	 * Gets the textColor specified in the config
	 *
	 * @return Configured TextColor
	 */
	TextColor getTextColor();

	/**
	 * Gets the lineWidth specified in the config
	 *
	 * @return Configured lineWidth
	 */
	float getLineWidth();

	/**
	 * Gets the lineWidthMax specified in the config
	 *
	 * @return Configured lineWidthMax
	 */
	float getLineWidthMax();

	/**
	 * Gets the textSize specified in the config
	 *
	 * @return Configured textSize
	 */
	float getTextSize();
}
