package com.mrbysco.measurements.registration;

import com.mrbysco.measurements.Constants;
import com.mrbysco.measurements.item.TapeItem;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

/**
 * Example class for item registration
 */
public class MeasurementRegistry {

    /**
     * The provider for items
     */
    public static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registry.ITEM_REGISTRY, Constants.MOD_ID);

    public static final RegistryObject<Item> TAPE_MEASURE_ITEM = ITEMS.register("tape_measure", () -> new TapeItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));


    // Called in the mod initializer / constructor in order to make sure that items are registered
    public static void loadClass() {}
}
