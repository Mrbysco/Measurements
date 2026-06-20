package com.mrbysco.measurements;

import com.mrbysco.measurements.callback.PlayerTickCallback;
import com.mrbysco.measurements.client.ClientClass;
import com.mrbysco.measurements.config.MeasurementConfig;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.InteractionResult;
import net.neoforged.fml.config.ModConfig;
import org.joml.Matrix4fc;

public class ClientMeasurementsFabric implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ConfigRegistry.INSTANCE.register(Constants.MOD_ID, ModConfig.Type.CLIENT, MeasurementConfig.clientSpec);

		PlayerTickCallback.EVENT.register((player) -> {
			ClientClass.onPlayerTick(player);
			return InteractionResult.PASS;
		});
		LevelRenderEvents.AFTER_TRANSLUCENT_TERRAIN.register(context -> {
			Minecraft mc = Minecraft.getInstance();
			GameRenderer gameRenderer = context.gameRenderer();
			Matrix4fc projectionMatrix = gameRenderer.gameRenderState().levelRenderState.cameraRenderState.viewRotationMatrix;
			ClientClass.onRenderWorldLast(
					mc.player,
					projectionMatrix,
					context.poseStack(),
					context.submitNodeCollector(),
					gameRenderer.mainCamera()
			);
		});
	}
}
