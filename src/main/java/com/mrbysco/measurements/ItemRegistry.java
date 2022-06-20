package com.mrbysco.measurements;

import com.mrbysco.measurements.item.TapeItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Measurements.MOD_ID);

	public static final RegistryObject<Item> TAPE_MEASURE_ITEM = ITEMS.register("tape_measure", () -> new TapeItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
}
