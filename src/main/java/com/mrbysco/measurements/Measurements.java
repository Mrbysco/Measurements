package com.mrbysco.measurements;

import com.mrbysco.measurements.client.ClientHandler;
import com.mrbysco.measurements.client.LoginHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Measurements.MOD_ID)
public class Measurements {
	public static final String MOD_ID = "measurements";

	public Measurements() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

		ItemRegistry.ITEMS.register(eventBus);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			MinecraftForge.EVENT_BUS.register(new ClientHandler());
			MinecraftForge.EVENT_BUS.register(new LoginHandler());
		});
	}
}
