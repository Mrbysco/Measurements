package com.mrbysco.measurements.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.joml.Matrix4fc;

@EventBusSubscriber(Dist.CLIENT)
public class ClientHandler {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		if (event.getEntity().level().isClientSide()) {
			ClientClass.onPlayerTick(event.getEntity());
		}
	}

	@SubscribeEvent
	public static void onSubmitCustomGeometry(SubmitCustomGeometryEvent event) {
		final Minecraft minecraft = Minecraft.getInstance();
		LocalPlayer player = minecraft.player;
		Matrix4fc projectionMatrix = minecraft.gameRenderer.gameRenderState().levelRenderState.cameraRenderState.viewRotationMatrix;
		PoseStack poseStack = event.getPoseStack();
		Camera camera = minecraft.gameRenderer.mainCamera();
		SubmitNodeCollector nodeCollector = event.getSubmitNodeCollector();

		ClientClass.onRenderWorldLast(player, projectionMatrix, poseStack, nodeCollector, camera);
	}
}
