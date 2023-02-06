/*
 * This file is part of  Mage Flame.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
 *
 * Mage Flame is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Mage Flame is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Mage Flame.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.fabric.mageflame.core.item;

import mod.gottsch.fabric.mageflame.core.util.LangUtil;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * 
 * @author Mark Gottschling Jan 22, 2023
 *
 */
public abstract class SummonFlameBaseItem extends Item implements ISummonFlameItem {

	/**
	 * 
	 * @param properties
	 */
	public SummonFlameBaseItem(Settings properties) {

		super(properties);
	}

	@Override
	public Text getName() {
		return new LiteralText(this.getTranslationKey()).formatted(Formatting.AQUA);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
//	public void appendHoverText(ItemStack stack, World level, List<Component> tooltip, TooltipFlag flag) {
		appendBaseText(stack, world, tooltip, context);
		LangUtil.appendAdvancedHoverText(tooltip, tt -> {
			appendAdvancedText(stack, world, tooltip, context);
		});
	}

	public void appendBaseText(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

	}

	public void appendAdvancedText(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

	}
	
	public void appendLore(ItemStack stack, World level, List<Text> tooltip, TooltipContext flag, String key) {
		MutableText lore = new TranslatableText(LangUtil.tooltip(key));
		tooltip.add(new LiteralText(" "));
		for (String s : lore.getString().split("~")) {	
			tooltip.add(new TranslatableText(LangUtil.INDENT2)
					.append(new LiteralText(s).formatted(Formatting.GOLD, Formatting.ITALIC)));
		}
	}

	public String ticksToTime(int ticks) {
		int secs = ticks / 20;   
		int hours = secs / 3600;
		int remainder = secs % 3600;
		int minutes = remainder / 60;
		int seconds = remainder % 60;
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}

	@Override
	public TypedActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
		ItemStack heldStack = player.getStackInHand(hand);
		if (level.isClient) {
			return TypedActionResult.pass(heldStack);
		}
		Vec3d spawnPos = getByPlayerPos(player);

		// spawn entity
		// MageFlame.LOGGER.debug("using summon flame item...");
		Optional<MobEntity> mob = spawn((ServerWorld)level, new Random(), player, getSummonFlameEntity(), spawnPos);
		if (mob.isPresent()) {
			// MageFlame.LOGGER.debug("summon flame is present...");
			// reduce scroll stack size ie consume
			heldStack.decrement(1);
			return TypedActionResult.consume(heldStack);
		}
		return super.use(level, player, hand);
	}
}
