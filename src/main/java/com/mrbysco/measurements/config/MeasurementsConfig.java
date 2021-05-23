package com.mrbysco.measurements.config;

import com.mrbysco.measurements.Measurements;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class MeasurementsConfig {

    public static class Common {
        public final IntValue lineWidth;
        public final IntValue lineWidthMax;
        public final ConfigValue<String> lineColor;
        public final ConfigValue<Double> textSize;

        Common(ForgeConfigSpec.Builder builder) {
            builder.comment("General Settings").push("General");

            lineWidth = builder
                .comment("Set line width (thickness). [default(float): 2.0]")
                .defineInRange("lineWidth", 2, 1, 16);
            lineWidthMax = builder
                .comment("Set max line width (thickness). [default(float): 1, min:0, max:16]")
                .defineInRange("lineWidthMax", 1, 1, 16);
            lineColor = builder
                .comment("Set line color. [default: YELLOW] [Valid Options: Random or any valid DyeColor]")
                .define("lineColor", "YELLOW");
            textSize = builder
                .comment("Set text size [default(float): 0.02, min:0.01, max:0.1]")
                .defineInRange("textSize", 0.02, 0.01, 0.10);

            builder.pop();
        }
    }
    public static final ForgeConfigSpec commonSpec;
    public static final Common COMMON;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = specPair.getRight();
        COMMON = specPair.getLeft();
    }

	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent) {
		Measurements.LOGGER.debug("[measurements] Loaded Measurements' config file {}", configEvent.getConfig().getFileName());
	}

	@SubscribeEvent
	public static void onFileChange(final ModConfig.Reloading configEvent) {
		Measurements.LOGGER.debug("[measurements] Measurements' config just got changed on the file system!");
	}
}
