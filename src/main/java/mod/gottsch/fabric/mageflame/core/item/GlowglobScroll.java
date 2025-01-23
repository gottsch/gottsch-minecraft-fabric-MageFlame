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

import mod.gottsch.fabric.mageflame.MageFlame;
import mod.gottsch.fabric.mageflame.core.setup.Registration;
import mod.gottsch.fabric.mageflame.core.util.LangUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;
import java.util.Objects;

// TODO GlowglobItem and ISummonScrollItem share most methods
// TODO create a new interface IScrollItem and ISummonScrollItem extends it
/**
 *
 * @author Mark Gottschling Jan 21, 2025
 *
 */
@Deprecated
public class GlowglobScroll extends Item {

	public GlowglobScroll(Settings properties) {

		super(properties);
	}

//	@Override
	public EntityType<? extends MobEntity> getEntityType() {

		return Registration.GLOWGLOB_ENTITY;
	}

	@Override
	public Text getName(ItemStack stack) {
		return Text.translatable(this.getTranslationKey(stack)).formatted(Formatting.AQUA);
	}

	@Override
	public Text getName() {
		return Text.translatable(this.getTranslationKey()).formatted(Formatting.AQUA);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		if (!(world instanceof ServerWorld)) {
			return ActionResult.SUCCESS;
		} else {
			ItemStack itemStack = context.getStack();
			BlockPos blockPos = context.getBlockPos();
			Direction direction = context.getSide();
			BlockState blockState = world.getBlockState(blockPos);

			BlockPos blockPos2;
			if (blockState.getCollisionShape(world, blockPos).isEmpty()) {
				blockPos2 = blockPos;
			} else {
				blockPos2 = blockPos.offset(direction);
			}

			EntityType entityType = getEntityType();
			Entity mob = entityType.spawnFromItemStack((ServerWorld)world, itemStack, context.getPlayer(), blockPos2, SpawnReason.MOB_SUMMONED, true, !Objects.equals(blockPos, blockPos2) && direction == Direction.UP);
			if (mob != null) {
				itemStack.decrement(1);
				world.emitGameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockPos);

				PlayerEntity owner = context.getPlayer();
			}

			return ActionResult.CONSUME;
		}
	}

	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		BlockHitResult blockHitResult = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
		if (blockHitResult.getType() != HitResult.Type.BLOCK) {
			return TypedActionResult.pass(itemStack);
		} else if (!(world instanceof ServerWorld)) {
			return TypedActionResult.success(itemStack);
		} else {
			BlockPos blockPos = blockHitResult.getBlockPos();
			if (!(world.getBlockState(blockPos).getBlock() instanceof FluidBlock)) {
				return TypedActionResult.pass(itemStack);
			} else if (world.canPlayerModifyAt(user, blockPos) && user.canPlaceOn(blockPos, blockHitResult.getSide(), itemStack)) {

				EntityType entityType = getEntityType();
				Entity mob = entityType.spawnFromItemStack((ServerWorld)world, itemStack, user, blockPos, SpawnReason.MOB_SUMMONED, false, false);
				if (mob == null) {
					return TypedActionResult.pass(itemStack);
				} else {
					itemStack.decrementUnlessCreative(1, user);
					user.incrementStat(Stats.USED.getOrCreateStat(this));
					world.emitGameEvent(user, GameEvent.ENTITY_PLACE, mob.getPos());

					itemStack.decrement(1);
					world.emitGameEvent(user, GameEvent.ENTITY_PLACE, blockPos);

					return TypedActionResult.consume(itemStack);
				}
			} else {
				return TypedActionResult.fail(itemStack);
			}
		}
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		appendBaseText(stack, context, tooltip, type);
		LangUtil.appendAdvancedHoverText(tooltip, tt -> {
			appendAdvancedText(stack, context, tooltip, type);
		});
	}

//	@Override
	public void appendBaseText(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {

		tooltip.add(Text.translatable(LangUtil.tooltip("glowglob.desc")).formatted(Formatting.YELLOW));
		tooltip.add(Text.literal(" "));
		tooltip.add(Text.translatable(LangUtil.tooltip("light_level"), ticksToTime(MageFlame.CONFIG.glowglobLifespan())));
	}

//	@Override
	public void appendAdvancedText(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		appendLore(stack, context, tooltip, "glowglob.lore");
	}

	public void appendLore(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, String key) {
		MutableText lore = Text.translatable(LangUtil.tooltip(key));
		tooltip.add(Text.literal(" "));
		for (String s : lore.getString().split("~")) {
			tooltip.add(Text.translatable(LangUtil.INDENT2)
					.append(Text.literal(s).formatted(Formatting.GOLD, Formatting.ITALIC)));
		}
	}

	// TODO probably could be static and create method for formatting
	public String ticksToTime(int ticks) {
		int secs = ticks / 20;
		int hours = secs / 3600;
		int remainder = secs % 3600;
		int minutes = remainder / 60;
		int seconds = remainder % 60;
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}
}
