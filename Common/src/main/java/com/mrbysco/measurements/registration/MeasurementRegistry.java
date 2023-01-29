package com.mrbysco.measurements.registration;

import com.mrbysco.measurements.Constants;
import com.mrbysco.measurements.item.TapeItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

/**
 * Example class for item registration
 */
public class MeasurementRegistry {

	/**
	 * The provider for items
	 */
	public static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(BuiltInRegistries.ITEM, Constants.MOD_ID);

	public static final RegistryObject<Item> TAPE_MEASURE_ITEM = ITEMS.register("tape_measure", () -> new TapeItem(new Item.Properties()));


	// Called in the mod initializer / constructor in order to make sure that items are registered
	public static void loadClass() {
	}
}
