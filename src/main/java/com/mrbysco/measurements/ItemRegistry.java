package com.mrbysco.measurements;

import com.mrbysco.measurements.item.TapeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Measurements.MOD_ID);

	public static final RegistryObject<Item> TAPE_MEASURE_ITEM  = ITEMS.register("tape_measure", () -> new TapeItem(new Item.Properties().group(ItemGroup.MISC)));
}
