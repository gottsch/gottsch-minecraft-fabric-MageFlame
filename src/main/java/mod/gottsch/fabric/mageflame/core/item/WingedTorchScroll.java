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

import mod.gottsch.fabric.mageflame.core.setup.Registration;
import mod.gottsch.fabric.mageflame.core.util.LangUtil;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;


/**
 * 
 * @author Mark Gottschling Jan 19, 2023
 *
 */
public class WingedTorchScroll extends SummonFlameBaseItem {

	public WingedTorchScroll(Settings properties) {

		super(properties);
	}
	
	public EntityType<? extends MobEntity> getSummonFlameEntity() {

		return Registration.WINGED_TORCH_ENTITY;
	}

	@Override
	public void appendBaseText(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		
		tooltip.add(Text.translatable(LangUtil.tooltip("winged_torch.desc")).formatted(Formatting.YELLOW));
		tooltip.add(Text.literal(" "));
		tooltip.add(Text.translatable(LangUtil.tooltip("light_level"), Registration.GREATER_REVELATION_BLOCK.getDefaultState().getLuminance()));
	}

	@Override
	public void appendAdvancedText(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		appendLore(stack, world, tooltip, context, "winged_torch.lore");
	}
}
