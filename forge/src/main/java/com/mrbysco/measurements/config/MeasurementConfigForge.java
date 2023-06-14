package com.mrbysco.measurements.config;

import com.mrbysco.measurements.Constants;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

public class MeasurementConfigForge {
	public static class Client {

		public final ForgeConfigSpec.DoubleValue lineWidth;
		public final ForgeConfigSpec.IntValue lineWidthMax;
		public final ForgeConfigSpec.DoubleValue textSize;
		public final ForgeConfigSpec.EnumValue<LineColor> lineColor;
		public final ForgeConfigSpec.EnumValue<TextColor> textColor;

		Client(ForgeConfigSpec.Builder builder) {
			builder.comment("Client settings")
					.push("client");

			lineColor = builder
					.comment("Set line color. [Default: YELLOW]")
					.defineEnum("lineColor", LineColor.YELLOW);
			textColor = builder
					.comment("Set text color. [Default: YELLOW]")
					.defineEnum("textColor", TextColor.YELLOW);
			textSize = builder
					.comment("Set text size [Default: 0.02]")
					.defineInRange("textSize", 0.02D, 0.01, 0.10);
			lineWidth = builder
					.comment("Set line width (thickness). [Default: 2]")
					.defineInRange("lineWidth", 2.0D, 1, 16);
			lineWidthMax = builder
					.comment("Set line width when further away (thickness). [Default: 2]")
					.defineInRange("lineWidthMax", 2, 1, 16);

			builder.pop();
		}
	}

	public static final ForgeConfigSpec clientSpec;
	public static final Client CLIENT;

	static {
		final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
		clientSpec = specPair.getRight();
		CLIENT = specPair.getLeft();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfigEvent.Loading configEvent) {
		Constants.LOGGER.debug("Loaded Measurements' config file {}", configEvent.getConfig().getFileName());
	}

	@SubscribeEvent
	public static void onFileChange(final ModConfigEvent.Reloading configEvent) {
		Constants.LOGGER.debug("Measurements' config just got changed on the file system!");
	}
}
