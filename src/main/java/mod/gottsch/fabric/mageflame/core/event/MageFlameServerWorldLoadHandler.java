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
package mod.gottsch.fabric.mageflame.core.event;

import mod.gottsch.fabric.mageflame.MageFlame;
import mod.gottsch.fabric.mageflame.core.peristence.PlayerData;
import mod.gottsch.fabric.mageflame.core.peristence.StateSaverAndLoader;
import mod.gottsch.fabric.mageflame.core.peristence.SummonedEntityData;
import mod.gottsch.fabric.mageflame.core.util.SpawnUtil;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.Map;
import java.util.UUID;

/**
 *
 * @author Mark Gottschling on Nov 6, 2022
 *
 */
public class MageFlameServerWorldLoadHandler implements ServerEntityEvents.Load {


    @Override
    public void onLoad(Entity entity, ServerWorld world) {
        // this is probaby moot, but just being cautious
        if (world.isClient()) {
            return;
        }

        if (entity instanceof PlayerEntity) {
            // load registry
//            StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(world.getServer());
            MageFlame.LOGGER.debug("player entity joining world -> {}", entity.getName().getString());

            PlayerData playerData = StateSaverAndLoader.getPlayerState((LivingEntity) entity);
            Map<UUID, SummonedEntityData> entityDataMap = playerData.getDetachedRegistry();
            playerData.clear();
            entityDataMap.forEach((id, entityData) -> {
                SpawnUtil.spawnAtPos(world, world.random, (LivingEntity) entity, entityData.getEntityType(), entityData.getCoords());
            });
        }
    }
}
