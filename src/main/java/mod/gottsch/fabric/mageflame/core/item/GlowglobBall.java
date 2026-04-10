/*
 * This file is part of Mage Flame.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Mage Flame is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Mage Flame is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURCoordsE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Mage Flame.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.fabric.mageflame.core.item;

import mod.gottsch.fabric.mageflame.MageFlame;
import mod.gottsch.fabric.mageflame.core.config.MageFlameDynamicLightsConfig;
import mod.gottsch.fabric.mageflame.core.entity.projectile.thrown.GlowglobBallEntity;
import mod.gottsch.fabric.mageflame.core.setup.DynamicLights;
import mod.gottsch.fabric.mageflame.core.util.LangUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

import java.util.List;

/**
 * @author Mark Gottschling on 1/23/2025
 */
public class GlowglobBall extends Item implements ProjectileItem {

    public GlowglobBall(Item.Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(
                null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_LINGERING_POTION_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        if (!world.isClient) {
            GlowglobBallEntity entity = new GlowglobBallEntity(world, user);
            entity.setItem(itemStack);
            entity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
            world.spawnEntity(entity);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        itemStack.decrementUnlessCreative(1, user);
        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        GlowglobBallEntity entity = new GlowglobBallEntity(world, pos.getX(), pos.getY(), pos.getZ());
        entity.setItem(stack);
        return entity;
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
        // TODO need to add luminance
        tooltip.add(Text.translatable(LangUtil.tooltip("glowglob.desc")).formatted(Formatting.YELLOW));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.translatable(LangUtil.tooltip("light_level"), MageFlameDynamicLightsConfig.GLOWGLOB_LUMINANCE));
        tooltip.add(Text.translatable(LangUtil.tooltip("lifespan"), ticksToTime(MageFlame.CONFIG.glowglobLifespan())));
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
