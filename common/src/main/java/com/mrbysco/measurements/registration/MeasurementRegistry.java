package com.mrbysco.measurements.registration;

import com.mrbysco.measurements.Constants;
import com.mrbysco.measurements.item.TapeItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

/**
 * Class that holds all the registry objects for the mod
 */
public class MeasurementRegistry {

	/**
	 * The provider for items
	 */
	public static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(BuiltInRegistries.ITEM, Constants.MOD_ID);

	/**
	 * The registry object for the tape measure item
	 */
	public static final RegistryObject<Item> TAPE_MEASURE_ITEM = ITEMS.register("tape_measure", () -> new TapeItem(new Item.Properties().setId(getKey("tape_measure"))));


	/**
	 * Creates a new ResourceKey for the item
	 * @param path the path of the item
	 * @return the ResourceKey for the item
	 */
	private static ResourceKey<Item> getKey(String path) {
		return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path));
	}

	// Called in the mod initializer / constructor in order to make sure that items are registered
	public static void loadClass() {
	}
}
