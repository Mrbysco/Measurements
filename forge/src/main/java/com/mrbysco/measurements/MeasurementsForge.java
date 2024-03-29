package com.mrbysco.measurements;

import com.mrbysco.measurements.client.ClientClass;
import com.mrbysco.measurements.client.ClientHandler;
import com.mrbysco.measurements.config.MeasurementConfigForge;
import com.mrbysco.measurements.registration.MeasurementRegistry;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class MeasurementsForge {

	public MeasurementsForge() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, MeasurementConfigForge.clientSpec);
		eventBus.register(MeasurementConfigForge.class);

		CommonClass.init();

		eventBus.addListener(this::addTabContents);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			MinecraftForge.EVENT_BUS.register(new ClientHandler());
			MinecraftForge.EVENT_BUS.register(new ClientClass());
		});
	}

	@SubscribeEvent
	public void addTabContents(final BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
			event.accept(MeasurementRegistry.TAPE_MEASURE_ITEM.get());
		}
	}
}