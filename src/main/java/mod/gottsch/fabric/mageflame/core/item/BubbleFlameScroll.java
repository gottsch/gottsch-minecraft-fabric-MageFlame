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
import mod.gottsch.fabric.mageflame.core.setup.DynamicLights;
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
 * @author Mark Gottschling Jan 18, 2025
 *
 */
public class BubbleFlameScroll extends SummonFlyingScrollItem {

	public BubbleFlameScroll(Settings properties) {

		super(properties);
	}
	
	public EntityType<? extends MobEntity> getSummonFlameEntity() {

		return Registration.BUBBLE_FLAME_ENTITY;
	}

	@Override
	public void appendBaseText(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.translatable(LangUtil.tooltip("bubble_flame.desc")).formatted(Formatting.YELLOW));
		tooltip.add(Text.literal(" "));
		tooltip.add(Text.translatable(LangUtil.tooltip("light_level"), DynamicLights.BUBBLE_FLAME_LUMINANCE));
		tooltip.add(Text.translatable(LangUtil.tooltip("lifespan"), ticksToTime(MageFlame.CONFIG.mageFlameLifespan())));
	}

	@Override
	public void appendAdvancedText(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		appendLore(stack, context, tooltip, "bubble_flame.lore");
	}
}
