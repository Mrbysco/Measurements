package com.mrbysco.measurements.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraftforge.common.util.FakePlayer;

public class TapeItem extends Item {
	public TapeItem(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		PlayerEntity player = context.getPlayer();

		if(player != null && !(player instanceof FakePlayer) && player.level.isClientSide) {
			return com.mrbysco.measurements.client.ClientHandler.addBox(player, context.getHitResult());
		}

		return super.useOn(context);
	}
}
