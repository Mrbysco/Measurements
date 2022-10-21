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
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

import java.util.ArrayList;
import java.util.List;

public class ClientHandler {
	protected static final RandomSource random = RandomSource.create();
	private static final List<MeasurementBox> boxList = new ArrayList<>();

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END && event.side == LogicalSide.CLIENT) {
			Player player = event.player;
			if (Minecraft.getInstance().player == player) {
				if (!player.isHolding(ItemRegistry.TAPE_MEASURE_ITEM.get())) {
					clear();
					return;
				}

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
	}

	@SubscribeEvent
	public void onRenderWorldLast(RenderLevelStageEvent event) {
		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
			final Minecraft minecraft = Minecraft.getInstance();
			LocalPlayer player = minecraft.player;
			if (player == null || !player.isHolding(ItemRegistry.TAPE_MEASURE_ITEM.get())) return;

			final ResourceKey<Level> currentDimension = player.level.dimension();
			Matrix4f projectionMatrix = event.getProjectionMatrix();
			PoseStack poseStack = event.getPoseStack();
			RenderBuffers renderBuffers = minecraft.renderBuffers();
			Camera camera = minecraft.gameRenderer.getMainCamera();
			poseStack.pushPose();
			boxList.forEach(box -> box.render(currentDimension, poseStack, renderBuffers, camera, projectionMatrix));
			poseStack.popPose();
		}
	}

	public static InteractionResult addBox(Player playerEntity, BlockPos pos) {
		if (playerEntity.isShiftKeyDown()) {
			undo();
			return InteractionResult.SUCCESS;
		}

		if (boxList.size() > 0) {
			MeasurementBox lastBox = boxList.get(boxList.size() - 1);

			if (lastBox.isFinished()) {
				final MeasurementBox box = new MeasurementBox(pos, playerEntity.level.dimension());
				boxList.add(box);
			} else {
				lastBox.setBlockEnd(pos);
				lastBox.setFinished();
			}
		} else {
			final MeasurementBox box = new MeasurementBox(pos, playerEntity.level.dimension());
			boxList.add(box);
		}

		return InteractionResult.PASS;
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
