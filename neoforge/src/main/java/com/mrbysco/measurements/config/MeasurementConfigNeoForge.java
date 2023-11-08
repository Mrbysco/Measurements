package com.mrbysco.measurements.config;

import com.mrbysco.measurements.Constants;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class MeasurementConfigNeoForge {
	public static class Client {

		public final ModConfigSpec.DoubleValue lineWidth;
		public final ModConfigSpec.IntValue lineWidthMax;
		public final ModConfigSpec.DoubleValue textSize;
		public final ModConfigSpec.EnumValue<LineColor> lineColor;
		public final ModConfigSpec.EnumValue<TextColor> textColor;

		Client(ModConfigSpec.Builder builder) {
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

	public static final ModConfigSpec clientSpec;
	public static final Client CLIENT;

	static {
		final Pair<Client, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Client::new);
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
