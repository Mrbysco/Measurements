package com.mrbysco.measurements;

import com.mrbysco.measurements.callback.PlayerTickCallback;
import com.mrbysco.measurements.client.ClientClass;
import com.mrbysco.measurements.config.MeasurementsConfigFabric;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;

public class ClientMeasurementsFabric implements ClientModInitializer {
	private Thread watchThread = null;
	public static ConfigHolder<MeasurementsConfigFabric> config;

	@Override
	public void onInitializeClient() {
		ConfigHolder<MeasurementsConfigFabric> holder = AutoConfig.register(MeasurementsConfigFabric.class, Toml4jConfigSerializer::new);
		config = holder;
		try {
			var watchService = FileSystems.getDefault().newWatchService();
			Paths.get("config").register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
			watchThread = new Thread(() -> {
				WatchKey key;
				try {
					while ((key = watchService.take()) != null) {
						if (Thread.currentThread().isInterrupted()) {
							watchService.close();
							break;
						}
						for (WatchEvent<?> event : key.pollEvents()) {
							if (event.kind() == StandardWatchEventKinds.OVERFLOW) {
								continue;
							}
							if (((Path) event.context()).endsWith("measurements.toml")) {
								Constants.LOGGER.info("Reloading Measurements config");
								if (holder.load()) {
									config = holder;
								}
							}
						}
						key.reset();
					}
				} catch (InterruptedException ignored) {
				} catch (IOException e) {
					Constants.LOGGER.error("Failed to close filesystem watcher", e);
				}
			}, "Measurements Config Watcher");
			watchThread.start();
		} catch (IOException e) {
			Constants.LOGGER.error("Failed to create filesystem watcher for configs", e);
		}

		PlayerTickCallback.EVENT.register((player) -> {
			ClientClass.onPlayerTick(player);
			return InteractionResult.PASS;
		});

		WorldRenderEvents.AFTER_TRANSLUCENT.register(e -> {
			Minecraft mc = Minecraft.getInstance();
			ClientClass.onRenderWorldLast(mc.player, e.projectionMatrix(), e.matrixStack(), mc.renderBuffers(), e.camera());
		});
	}
}
