package com.mrbysco.measurements.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;

public class TapeItem extends Item {
	public TapeItem(Properties properties) {
		super(properties);
	}

	@Override
	public @NotNull InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();

		if (player != null && !(player instanceof FakePlayer) && player.level.isClientSide) {
			return com.mrbysco.measurements.client.ClientHandler.addBox(player, context.getClickedPos());
		}

		return super.useOn(context);
	}
}
