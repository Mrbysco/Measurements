package com.mrbysco.measurements.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderBuffers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.TickEvent.PlayerTickEvent;
import org.joml.Matrix4f;

public class ClientHandler {
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END && event.side == LogicalSide.CLIENT) {
			ClientClass.onPlayerTick(event.player);
		}
	}

	@SubscribeEvent
	public void onRenderWorldLast(RenderLevelStageEvent event) {
		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
			final Minecraft minecraft = Minecraft.getInstance();
			LocalPlayer player = minecraft.player;
			Matrix4f projectionMatrix = event.getProjectionMatrix();
			PoseStack poseStack = event.getPoseStack();
			RenderBuffers renderBuffers = minecraft.renderBuffers();
			Camera camera = minecraft.gameRenderer.getMainCamera();

			ClientClass.onRenderWorldLast(player, projectionMatrix, poseStack, renderBuffers, camera);
		}
	}
}
