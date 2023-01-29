package com.mrbysco.measurements.item;

import com.mrbysco.measurements.platform.Services;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class TapeItem extends Item {
	public TapeItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();

		if (Services.PLATFORM.isValidUser(player)) {
			return com.mrbysco.measurements.client.BoxHandler.addBox(player, Services.PLATFORM.getHitResult(context));
		}

		return super.useOn(context);
	}
}
