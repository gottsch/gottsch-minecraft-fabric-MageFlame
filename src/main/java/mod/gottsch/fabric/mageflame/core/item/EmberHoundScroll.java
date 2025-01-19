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
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;


/**
 *
 * @author Mark Gottschling Jan 11, 2025
 *
 */
public class EmberHoundScroll extends SummonPathAwareScrollItem {

	public EmberHoundScroll(Settings properties) {

		super(properties);
	}

	@Override
	public EntityType<? extends MobEntity> getSummonFlameEntity() {

		return Registration.EMBER_HOUND_ENTITY;
	}

//	@Override
//	public Vec3d selectSpawnPos(World level, Vec3d coords, Direction direction) {
//		// TODO get the ground pos - look at EggItem
//		BlockPos spawnPos = new BlockPos((int)coords.x, (int)coords.y, (int)coords.z);
//		if (level.getBlockState(spawnPos.up()).isAir()) {
//			coords = coords.add(0, 1, 0);
//		} else {
//			spawnPos.offset(direction.getOpposite());
//			coords = new Vec3d(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
//		}
//
//		return coords;
//	}

	@Override
	public void appendBaseText(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {

		tooltip.add(Text.translatable(LangUtil.tooltip("ember_hound.desc")).formatted(Formatting.YELLOW));
		tooltip.add(Text.literal(" "));
		tooltip.add(Text.translatable(LangUtil.tooltip("light_level"), ticksToTime(MageFlame.CONFIG.emberHoundLifespan())));
	}

	@Override
	public void appendAdvancedText(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		appendLore(stack, context, tooltip, "ember_hound.lore");
	}
}
