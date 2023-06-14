package com.mrbysco.measurements.config;

import com.mrbysco.measurements.Constants;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = Constants.MOD_ID)
public class MeasurementsConfigFabric implements ConfigData {
	@ConfigEntry.Gui.CollapsibleObject
	public Client client = new Client();

	public static class Client {
		@Comment("Set line color. [Default: YELLOW]")
		public LineColor lineColor = LineColor.YELLOW;
		@Comment("Set text color. [Default: YELLOW]")
		public TextColor textColor = TextColor.YELLOW;

		@Comment("Set text size [Default: 0.02]")
		public double textSize = 0.02D;
		@Comment("Set line width (thickness). [Default: 2]")
		@ConfigEntry.BoundedDiscrete(min = 1, max = 16)
		public double lineWidth = 2.0d;
		@Comment("Set line width when further away (thickness). [Default: 2]")
		@ConfigEntry.BoundedDiscrete(min = 1, max = 16)
		public int lineWidthMax = 2;
	}
}
