package com.mrbysco.measurements.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mrbysco.measurements.ItemRegistry;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class ClientHandler {
	private static final List<MeasurementBox> boxList = new ArrayList<>();

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		if(event.phase == TickEvent.Phase.END && event.player.level.isClientSide) {
			Player player = event.player;
			if(player.getMainHandItem().getItem() != ItemRegistry.TAPE_MEASURE_ITEM.get()) {
				clear();
				return;
			}

			if(boxList.size() > 0) {
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

	@SubscribeEvent
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		LocalPlayer player = Minecraft.getInstance().player;
		if(player == null || player.getMainHandItem().getItem() != ItemRegistry.TAPE_MEASURE_ITEM.get()) return;

		final ResourceKey<Level> currentDimension = player.level.dimension();
		Minecraft minecraft = Minecraft.getInstance();
		Matrix4f projectionMatrix = event.getProjectionMatrix();
		PoseStack poseStack = event.getMatrixStack();
		RenderBuffers renderBuffers = minecraft.renderBuffers();
		Camera camera = minecraft.gameRenderer.getMainCamera();
		boxList.forEach(box -> box.render(currentDimension, poseStack, renderBuffers, camera, projectionMatrix));
	}

	public static InteractionResult addBox(Player playerEntity, BlockHitResult hitResult) {
		if (playerEntity.isShiftKeyDown()) {
			undo();
			return InteractionResult.FAIL;
		}

		BlockPos block = hitResult.getBlockPos();

		if (boxList.size() > 0) {
			MeasurementBox lastBox = boxList.get(boxList.size() - 1);

			if (lastBox.isFinished()) {
				final MeasurementBox box = new MeasurementBox(block, playerEntity.level.dimension());
				boxList.add(box);
			} else {
				lastBox.setBlockEnd(block);
				lastBox.setFinished();
			}
		} else {
			final MeasurementBox box = new MeasurementBox(block, playerEntity.level.dimension());
			boxList.add(box);
		}

		return InteractionResult.FAIL;
	}

	public static void undo() {
		if (boxList.size() > 0) {
			boxList.remove(boxList.size() - 1);
		}
	}

	public static void clear() {
		boxList.clear();
	}
}
