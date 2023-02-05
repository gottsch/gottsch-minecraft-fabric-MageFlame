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
import mod.gottsch.fabric.mageflame.core.entity.creature.ISummonFlameEntity;
import mod.gottsch.fabric.mageflame.core.entity.creature.SummonFlameBaseEntity;
import mod.gottsch.fabric.mageflame.core.registry.SummonFlameRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

import java.util.Objects;
import java.util.UUID;

/**
 *
 * @author Mark Gottschling on Nov 6, 2022
 *
 */
public class SummonFlameServerEntityLoadHandler implements ServerEntityEvents.Load {

    @Override
    public void onLoad(Entity entity, ServerWorld world) {
        // this is probaby moot, but just being cautious
        if (world.isClient()) {
            return;
        }

        if (entity instanceof ISummonFlameEntity) {
            // MageFlame.LOGGER.debug("entity is joing the level -> {}", entity.getClass().getSimpleName());

            SummonFlameBaseEntity flameEntity = (SummonFlameBaseEntity)entity;
            // register the entity
            if (flameEntity.getOwnerUUID() != null) {
                // MageFlame.LOGGER.debug("entity -> {} has owner -> {}", entity.getUuidAsString(), flameEntity.getOwnerUUID().toString());
                if (!SummonFlameRegistry.isRegistered(flameEntity.getOwnerUUID())) {
                    // MageFlame.LOGGER.debug("owner is NOT registered -> {}", flameEntity.getOwnerUUID().toString());
                    SummonFlameRegistry.register(flameEntity.getOwnerUUID(), entity.getUuid());
                }
                /*
                 * NOTE this is an edge-case scenario where the entity was not unregistered and killed
                 *  and it attempts to reunite with owner.
                 */
                else if (!Objects.equals(SummonFlameRegistry.get(flameEntity.getOwnerUUID()), entity.getUuid())) {
                    // MageFlame.LOGGER.debug("event entity -> {} has a previously registered owner -> {} and not the existing entity", entity.getUuidAsString(), flameEntity.getOwnerUUID());
                    /*
                     *  registered to another entity, check who the younger is
                     */
                    Entity existingEntity = world.getEntity(SummonFlameRegistry.get(flameEntity.getOwnerUUID()));
                    if (existingEntity != null) {
                        // MageFlame.LOGGER.debug("found existing entity -> {}", existingEntity.getUuidAsString());
                        // MageFlame.LOGGER.debug("existing birth -> {}, entity birth -> {}", ((ISummonFlameEntity)existingEntity).getBirthTime(), flameEntity.getBirthTime());
                        SummonFlameBaseEntity existingFlameEntity = (SummonFlameBaseEntity)existingEntity;
                        // check if this entity is younger than the existing entity
                        if (flameEntity.getBirthTime() > ((ISummonFlameEntity)existingEntity).getBirthTime()) {
                            // MageFlame.LOGGER.debug("killing existing -> {}", existingEntity.getUuidAsString());
                            // kill the existing registered entity
                            existingFlameEntity.setOwner(null);
                            SummonFlameRegistry.register(flameEntity.getOwnerUUID(), entity.getUuid());
                            // MageFlame.LOGGER.debug("registering entity -> {} to owner -> {}", entity.getUuid(), flameEntity.getOwnerUUID());
                        } else {
                            // MageFlame.LOGGER.debug("killing myself -> {}", entity.getUuidAsString());
                            flameEntity.setOwner(null);
                        }
                    }
                }
            }

            // update position and light blocks
            flameEntity.updateLightBlocks();
        }
    }
}
