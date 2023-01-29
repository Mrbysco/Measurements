package com.mrbysco.measurements;

import com.mrbysco.measurements.registration.MeasurementRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;

public class MeasurementsFabric implements ModInitializer {

	@Override
	public void onInitialize() {
		CommonClass.init();

		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(content -> {
			content.accept(MeasurementRegistry.TAPE_MEASURE_ITEM.get());
		});
	}
}
