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

import mod.gottsch.fabric.mageflame.core.peristence.SummonedEntityData;
import mod.gottsch.fabric.mageflame.core.entity.creature.ISummonedEntity;
import mod.gottsch.fabric.mageflame.core.peristence.StateSaverAndLoader;
import mod.gottsch.fabric.mageflame.core.util.LangUtil;
import mod.gottsch.fabric.mageflame.core.util.SpawnUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
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

/**
 * Created by Mark Gottschling on 1/14/2025
 */
public abstract class SummonPathAwareScrollItem extends Item implements ISummonScrollItem {

    public SummonPathAwareScrollItem(Item.Settings properties) {

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

            EntityType entityType = getSummonFlameEntity();
            Entity mob = entityType.spawnFromItemStack((ServerWorld)world, itemStack, context.getPlayer(), blockPos2, SpawnReason.MOB_SUMMONED, true, !Objects.equals(blockPos, blockPos2) && direction == Direction.UP);
            if (mob != null) {
                itemStack.decrement(1);
                world.emitGameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockPos);

                PlayerEntity owner = context.getPlayer();
                postSpawn((ServerWorld)world, (MobEntity) mob, entityType, owner);
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

                EntityType entityType = getSummonFlameEntity();
                Entity mob = entityType.spawnFromItemStack((ServerWorld)world, itemStack, user, blockPos, SpawnReason.MOB_SUMMONED, false, false);
                if (mob == null) {
                    return TypedActionResult.pass(itemStack);
                } else {
                    itemStack.decrementUnlessCreative(1, user);
                    user.incrementStat(Stats.USED.getOrCreateStat(this));
                    world.emitGameEvent(user, GameEvent.ENTITY_PLACE, mob.getPos());

                    itemStack.decrement(1);
                    world.emitGameEvent(user, GameEvent.ENTITY_PLACE, blockPos);

                    postSpawn((ServerWorld)world, (MobEntity) mob, entityType, user);

                    return TypedActionResult.consume(itemStack);
                }
            } else {
                return TypedActionResult.fail(itemStack);
            }
        }
    }

    public <T extends MobEntity & ISummonedEntity>void postSpawn(ServerWorld world, MobEntity mob, EntityType<T> entityType, LivingEntity owner) {
        ((ISummonedEntity)mob).setOwner(owner);
        // TODO add limit and replace oldest

        // registry entity
        SpawnUtil.register(world, entityType, mob, owner);
//        // MageFlame.LOGGER.info("registering entity -> {} to owner -> {}", mob.getUuidAsString(), owner.getUuidAsString());
////        SummonedLightSourceRegistry.register(owner.getUuid(), mob.getUuid());
//        SummonedEntityData entityData = new SummonedEntityData();
//        entityData.setEntityType(entityType);
//        entityData.setLifespan(((ISummonedEntity) mob).getLifespan());
//        entityData.setCreateTime(world.getTime());
//        StateSaverAndLoader.getPlayerState(owner).register(mob.getUuid(), entityData);
////        StateSaverAndLoader.getServerState(world.getServer()).markDirty();

        // cast effects
        doCastEffects(world, owner);
    }

}
