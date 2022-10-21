package com.mrbysco.measurements;

import com.mojang.logging.LogUtils;
import com.mrbysco.measurements.client.ClientHandler;
import com.mrbysco.measurements.client.LoginHandler;
import com.mrbysco.measurements.config.MeasurementConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Measurements.MOD_ID)
public class Measurements {
	public static final String MOD_ID = "measurements";
	public static final Logger LOGGER = LogUtils.getLogger();

	public Measurements() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, MeasurementConfig.clientSpec);
		eventBus.register(MeasurementConfig.class);

		ItemRegistry.ITEMS.register(eventBus);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			MinecraftForge.EVENT_BUS.register(new ClientHandler());
			MinecraftForge.EVENT_BUS.register(new LoginHandler());
		});
	}
}
