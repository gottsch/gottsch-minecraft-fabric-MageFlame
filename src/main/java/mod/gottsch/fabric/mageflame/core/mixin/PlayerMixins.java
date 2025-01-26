/*
 * This file is part of  Magic Treasures.
 * Copyright (c) 2024 Mark Gottschling (gottsch)
 *
 * Magic Treasures is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Magic Treasures is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Magic Treasures.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.fabric.mageflame.core.mixin;

import mod.gottsch.fabric.mageflame.core.entity.creature.ISummonedEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

/**
 * @author Mark Gottschling on 1/17/2025
 */
@Deprecated
@Mixin(PlayerEntity.class)
public abstract class PlayerMixins extends LivingEntity {

    protected PlayerMixins(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * a simple mixin at executes at the beginning of the Player's tick event.
     * the mixin processes any jewelry/charms the player may be using.
     * @param ci
     */
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (!getWorld().isClient) {
            // don't process anything on the server side
            // NOTE don't cancel the tick() call.
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        HitResult hit = client.crosshairTarget;

        //            case HitResult.Type.MISS:
        //                //nothing near enough
        //                break;
        //            case HitResult.Type.BLOCK:
        //                BlockHitResult blockHit = (BlockHitResult) hit;
        //                BlockPos blockPos = blockHit.getBlockPos();
        //                BlockState blockState = client.world.getBlockState(blockPos);
        //                Block block = blockState.getBlock();
        //                break;

        if (hit.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHit = (EntityHitResult) hit;
            Entity entity = entityHit.getEntity();
            if (entity instanceof ISummonedEntity summonedEntity) {
                // TODO will have to send patch to client every second to update lifespan of entity

                // TODO do draw of ui
                // TODO name
                // TODO remaining lifespan in seconds
            }

        }
    }

}
