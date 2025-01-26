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

import mod.gottsch.fabric.gottschcore.spatial.Coords;
import mod.gottsch.fabric.gottschcore.spatial.ICoords;
import mod.gottsch.fabric.mageflame.core.util.LangUtil;
import mod.gottsch.fabric.mageflame.core.util.SpawnUtil;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * 
 * @author Mark Gottschling Jan 22, 2023
 *
 */
public abstract class SummonFlyingScrollItem extends Item implements ISummonScrollItem {

	/**
	 * 
	 * @param properties
	 */
	public SummonFlyingScrollItem(Settings properties) {

		super(properties);
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
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		appendBaseText(stack, context, tooltip, type);
		LangUtil.appendAdvancedHoverText(tooltip, tt -> {
			appendAdvancedText(stack, context, tooltip, type);
		});
	}

	@Override
	public TypedActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
		ItemStack heldStack = player.getStackInHand(hand);
		if (level.isClient) {
			return TypedActionResult.pass(heldStack);
		}
		Direction direction = player.getMovementDirection();
		Vec3d playerPos = getByPlayerPos(player);
		Vec3d spawnVec3 = selectSpawnPos(level, playerPos, direction);
//		BlockPos spawnPos = new BlockPos((int)spawnVec3.x, (int)spawnVec3.y, (int)spawnVec3.z);
		ICoords coords = Coords.of(SpawnUtil.vec3ToBlockPos(spawnVec3));
		// spawn entity
		// MageFlame.LOGGER.info("using summon flame item...");
		Optional<?> mob = spawn((ServerWorld)level, level.random, player, getSummonFlameEntity(), coords);
		if (mob.isPresent()) {
			// MageFlame.LOGGER.info("summon flame is present...");
			// reduce scroll stack size ie consume
			heldStack.decrement(1);
			return TypedActionResult.consume(heldStack);
		}
		return super.use(level, player, hand);
	}
}
