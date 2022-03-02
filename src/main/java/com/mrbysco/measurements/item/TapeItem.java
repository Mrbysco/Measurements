package com.mrbysco.measurements.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraftforge.common.util.FakePlayer;

public class TapeItem extends Item {
	public TapeItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();

		if (player != null && !(player instanceof FakePlayer) && player.level.isClientSide) {
			return com.mrbysco.measurements.client.ClientHandler.addBox(player, context.getHitResult());
		}

		return super.useOn(context);
	}
}
