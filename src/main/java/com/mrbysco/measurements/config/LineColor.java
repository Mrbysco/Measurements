package com.mrbysco.measurements.config;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.Nullable;

public enum LineColor {
	RANDOM(null),
	WHITE(DyeColor.WHITE),
	ORANGE(DyeColor.ORANGE),
	MAGENTA(DyeColor.MAGENTA),
	LIGHT_BLUE(DyeColor.LIGHT_BLUE),
	YELLOW(DyeColor.YELLOW),
	LIME(DyeColor.LIME),
	PINK(DyeColor.PINK),
	GRAY(DyeColor.GRAY),
	LIGHT_GRAY(DyeColor.LIGHT_GRAY),
	CYAN(DyeColor.CYAN),
	PURPLE(DyeColor.PURPLE),
	BLUE(DyeColor.BLUE),
	BROWN(DyeColor.BROWN),
	GREEN(DyeColor.GREEN),
	RED(DyeColor.RED),
	BLACK(DyeColor.BLACK);

	private final DyeColor color;

	LineColor(@Nullable DyeColor color) {
		this.color = color;
	}

	public DyeColor getColor(RandomSource random) {
		if (this.color == null) {
			int pick = random.nextInt(DyeColor.values().length);
			return DyeColor.values()[pick];
		}
		return color;
	}
}