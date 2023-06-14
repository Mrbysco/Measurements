package com.mrbysco.measurements.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.measurements.registration.MeasurementRegistry;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.joml.Matrix4f;

import java.util.List;

public class ClientClass {
	public static void onLogIn() {
		BoxHandler.clear();
	}

	public static void onLogOut() {
		BoxHandler.clear();
	}

	public static void onPlayerTick(Player player) {
		if (Minecraft.getInstance().player == player) {
			if (!player.isHolding(MeasurementRegistry.TAPE_MEASURE_ITEM.get())) {
				BoxHandler.clear();
				return;
			}

			List<MeasurementBox> boxList = BoxHandler.getBoxList();
			if (boxList.size() > 0) {
				MeasurementBox lastBox = boxList.get(boxList.size() - 1);
				if (!lastBox.isFinished()) {
					HitResult rayHit = Minecraft.getInstance().hitResult;

					if (rayHit != null && rayHit.getType() == HitResult.Type.BLOCK) {
						BlockHitResult blockHitResult = (BlockHitResult) rayHit;
						lastBox.setBlockEnd(new BlockPos(blockHitResult.getBlockPos()));
					}
				}
			}
		}
	}

	public static void onRenderWorldLast(Player player, Matrix4f projectionMatrix, PoseStack poseStack, RenderBuffers renderBuffers, Camera camera) {
		if (player == null || !player.isHolding(MeasurementRegistry.TAPE_MEASURE_ITEM.get())) return;

		final ResourceKey<Level> currentDimension = player.level().dimension();
		poseStack.pushPose();
		List<MeasurementBox> boxList = BoxHandler.getBoxList();
		boxList.forEach(box -> box.render(currentDimension, poseStack, renderBuffers, camera, projectionMatrix));
		poseStack.popPose();
	}
}
