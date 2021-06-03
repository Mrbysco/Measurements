package com.mrbysco.measurements.util;

import java.util.Random;

import net.minecraft.item.DyeColor;

public class Utils {
	public int randomNum;

	public static DyeColor getRandColor() {
		Random randomNumGen = new Random();
		int randomNum = randomNumGen.nextInt(15);
		DyeColor[] dyeColors = DyeColor.values();
		DyeColor randomColor = dyeColors[randomNum];

		return randomColor;
	}
}
