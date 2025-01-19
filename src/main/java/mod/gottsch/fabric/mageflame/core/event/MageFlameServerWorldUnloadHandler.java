/*
 * This file is part of  Mage Flame.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
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

import mod.gottsch.fabric.gottschcore.spatial.Coords;
import mod.gottsch.fabric.mageflame.core.entity.creature.ISummonedEntity;
import mod.gottsch.fabric.mageflame.core.peristence.PlayerData;
import mod.gottsch.fabric.mageflame.core.peristence.StateSaverAndLoader;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

/**
 * Created by Mark Gottschling on 1/14/2025
 */
public class MageFlameServerWorldUnloadHandler implements ServerEntityEvents.Unload {

    @Override
    public void onUnload(Entity entity, ServerWorld world) {
        if (world.isClient()) {
            return;
        }

        if (entity instanceof PlayerEntity player) {
            PlayerData playerData = StateSaverAndLoader.getPlayerState((LivingEntity) entity);
            playerData.getKeys().forEach(modId -> {
                // get the entity from the world
                Entity mob = world.getEntity(modId);
                if (mob instanceof ISummonedEntity lightSourceEntity) {
                    // update player's entities
                    playerData.get(modId).ifPresent(data -> {
                        data.setLifespan(lightSourceEntity.getLifespan());
                        data.setCoords(Coords.of(mob.getBlockPos()));

                    });
                    // kill mob
                    mob.kill();
                } else {
                    // can't find mob so unregister
                    playerData.unregister(modId);
                }
            });
            StateSaverAndLoader.getServerState(world.getServer()).markDirty();
        }
    }
}
