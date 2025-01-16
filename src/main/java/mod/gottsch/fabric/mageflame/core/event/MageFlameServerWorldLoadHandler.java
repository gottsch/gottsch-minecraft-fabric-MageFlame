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
import mod.gottsch.fabric.mageflame.core.peristence.SummonedEntityData;
import mod.gottsch.fabric.mageflame.core.entity.creature.ISummonedEntity;
import mod.gottsch.fabric.mageflame.core.peristence.PlayerData;
import mod.gottsch.fabric.mageflame.core.peristence.StateSaverAndLoader;
import mod.gottsch.fabric.mageflame.core.util.SpawnUtil;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.Map;
import java.util.Random;
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
            MageFlame.LOGGER.info("player entity joining world -> {}", entity.getName().getString());

            PlayerData playerData = StateSaverAndLoader.getPlayerState((LivingEntity) entity);
            Map<UUID, SummonedEntityData> entityDataMap = playerData.getDetachedRegistry();
            MageFlame.LOGGER.info("player {} data -> {}", entity.getUuid(), entityDataMap);
            MageFlame.LOGGER.info("size of summoned data registry -> {}", entityDataMap.size());
            playerData.clear();
            entityDataMap.forEach((id, entityData) -> {
//                SpawnUtil.spawnSummonedFlyingEntity(world, new Random(), (LivingEntity) entity, entityData.getEntityType(), SpawnUtil.getByPlayerPos((PlayerEntity) entity))
//                        .ifPresent(mob -> {
//                    // update lifespan
//                    ((ISummonedEntity)mob).setLifespan(entityData.getLifespan());
//                });
                MageFlame.LOGGER.info("summoning entity -> {} joining world at pos -> {}", entityData, entityData.getCoords().toShortString());

                SpawnUtil.spawnAtPos(world, world.random, (LivingEntity) entity, entityData.getEntityType(), entityData.getCoords());
            });
        }
//        // load registry
//        StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(world.getServer());
//
//        if (entity instanceof ISummonedLightSourceEntity lightSourceEntity) {
//             MageFlame.LOGGER.info("entity is joining the level -> {}", entity.getClass().getSimpleName());
//
//            // register the entity
//            if (lightSourceEntity.getOwnerUUID() != null) {
//                // MageFlame.LOGGER.info("entity -> {} has owner -> {}", entity.getUuidAsString(), flameEntity.getOwnerUUID().toString());
//                if (!SummonedLightSourceRegistry.isRegistered(lightSourceEntity.getOwnerUUID())) {
//                    // MageFlame.LOGGER.info("owner is NOT registered -> {}", flameEntity.getOwnerUUID().toString());
//                    SummonedLightSourceRegistry.register(lightSourceEntity.getOwnerUUID(), entity.getUuid());
//                }
//                /*
//                 * NOTE this is an edge-case scenario where the entity was not unregistered and killed
//                 *  and it attempts to reunite with owner.
//                 */
//                else if (!Objects.equals(SummonedLightSourceRegistry.get(lightSourceEntity.getOwnerUUID()), entity.getUuid())) {
//                    // MageFlame.LOGGER.info("event entity -> {} has a previously registered owner -> {} and not the existing entity", entity.getUuidAsString(), flameEntity.getOwnerUUID());
//                    /*
//                     *  registered to another entity, check who the younger is
//                     */
//                    Entity existingEntity = world.getEntity(SummonedLightSourceRegistry.get(lightSourceEntity.getOwnerUUID()));
//                    if (existingEntity != null) {
//                        // MageFlame.LOGGER.info("found existing entity -> {}", existingEntity.getUuidAsString());
//                        // MageFlame.LOGGER.info("existing birth -> {}, entity birth -> {}", ((ISummonFlameEntity)existingEntity).getBirthTime(), flameEntity.getBirthTime());
//                        ISummonedLightSourceEntity existingFlameEntity = (ISummonedLightSourceEntity)existingEntity;
//                        // check if this entity is younger than the existing entity
//                        if (lightSourceEntity.getBirthTime() > ((ISummonedLightSourceEntity)existingEntity).getBirthTime()) {
//                            // MageFlame.LOGGER.info("killing existing -> {}", existingEntity.getUuidAsString());
//                            // kill the existing registered entity
//                            existingFlameEntity.setOwner(null);
//                            SummonedLightSourceRegistry.register(lightSourceEntity.getOwnerUUID(), entity.getUuid());
//                            // MageFlame.LOGGER.info("registering entity -> {} to owner -> {}", entity.getUuid(), flameEntity.getOwnerUUID());
//                        } else {
//                            // MageFlame.LOGGER.info("killing myself -> {}", entity.getUuidAsString());
//                            lightSourceEntity.setOwner(null);
//                        }
//                    }
//                }
//            }
//        }
    }
}
