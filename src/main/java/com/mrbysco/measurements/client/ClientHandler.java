package com.mrbysco.measurements.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrbysco.measurements.ItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.RenderTypeBuffers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.world.World;
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
			PlayerEntity player = event.player;
			if(player.getMainHandItem().getItem() != ItemRegistry.TAPE_MEASURE_ITEM.get()) {
				clear();
				return;
			}

			if(boxList.size() > 0) {
				MeasurementBox lastBox = boxList.get(boxList.size() - 1);
				if (!lastBox.isFinished()) {
					RayTraceResult rayHit = Minecraft.getInstance().hitResult;

					if (rayHit != null && rayHit.getType() == RayTraceResult.Type.BLOCK) {
						BlockRayTraceResult blockHitResult = (BlockRayTraceResult) rayHit;
						lastBox.setBlockEnd(new BlockPos(blockHitResult.getBlockPos()));
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		ClientPlayerEntity player = Minecraft.getInstance().player;
		if(player == null || player.getMainHandItem().getItem() != ItemRegistry.TAPE_MEASURE_ITEM.get()) return;

		final RegistryKey<World> currentDimension = player.level.dimension();
		Minecraft minecraft = Minecraft.getInstance();
		Matrix4f projectionMatrix = event.getProjectionMatrix();
		MatrixStack matrixStack = event.getMatrixStack();
		RenderTypeBuffers renderTypeBuffer = minecraft.renderBuffers();
		ActiveRenderInfo camera = minecraft.gameRenderer.getMainCamera();
		boxList.forEach(box -> box.render(currentDimension, matrixStack, renderTypeBuffer, camera, projectionMatrix));
	}

	public static ActionResultType addBox(PlayerEntity playerEntity, BlockRayTraceResult hitResult) {
		if (playerEntity.isShiftKeyDown()) {
			undo();
			return ActionResultType.FAIL;
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

		return ActionResultType.FAIL;
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
