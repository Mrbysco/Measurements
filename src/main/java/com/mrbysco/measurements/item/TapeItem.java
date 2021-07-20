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
	public ActionResultType onItemUse(ItemUseContext context) {
		PlayerEntity player = context.getPlayer();

		if(player != null && !(player instanceof FakePlayer) && player.world.isRemote) {
			return com.mrbysco.measurements.client.ClientHandler.addBox(player, context.func_242401_i());
		}

		return super.onItemUse(context);
	}
}
