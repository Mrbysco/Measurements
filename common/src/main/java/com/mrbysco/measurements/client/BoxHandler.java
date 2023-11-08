package com.mrbysco.measurements.client;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoxHandler {
	protected static final Random random = new Random();
	private static final List<MeasurementBox> boxList = new ArrayList<>();

	public static InteractionResult addBox(Player playerEntity, BlockPos blockPos) {
		if (playerEntity.isShiftKeyDown()) {
			undo();
			return InteractionResult.SUCCESS;
		}

		if (!boxList.isEmpty()) {
			MeasurementBox lastBox = boxList.get(boxList.size() - 1);

			if (lastBox.isFinished()) {
				final MeasurementBox box = new MeasurementBox(blockPos, playerEntity.level().dimension());
				boxList.add(box);
			} else {
				lastBox.setBlockEnd(blockPos);
				lastBox.setFinished();
			}
		} else {
			final MeasurementBox box = new MeasurementBox(blockPos, playerEntity.level().dimension());
			boxList.add(box);
		}

		return InteractionResult.FAIL;
	}

	public static List<MeasurementBox> getBoxList() {
		return boxList;
	}

	public static void undo() {
		if (!boxList.isEmpty()) {
			boxList.remove(boxList.size() - 1);
		}
	}

	public static void clear() {
		boxList.clear();
	}
}
