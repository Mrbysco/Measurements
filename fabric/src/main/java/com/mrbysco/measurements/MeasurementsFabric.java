package com.mrbysco.measurements;

import com.mrbysco.measurements.registration.MeasurementRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.world.item.CreativeModeTabs;

public class MeasurementsFabric implements ModInitializer {

	@Override
	public void onInitialize() {
		CommonClass.init();

		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(content -> {
			content.accept(MeasurementRegistry.TAPE_MEASURE_ITEM.get());
		});
	}
}
