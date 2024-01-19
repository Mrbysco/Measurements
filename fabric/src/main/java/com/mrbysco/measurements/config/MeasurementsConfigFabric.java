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
		public double lineWidth = 2.0d;
		@Comment("Set line width when further away (thickness). [Default: 2]")
		public int lineWidthMax = 2;
	}

	@Override
	public void validatePostLoad() throws ValidationException {
		if (client.lineWidth < 1 || client.lineWidth > 16) {
			client.lineWidth = 2;
		}
		if (client.lineWidthMax < 1 || client.lineWidthMax > 16) {
			client.lineWidthMax = 2;
		}
	}
}
