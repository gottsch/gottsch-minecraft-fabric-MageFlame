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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURCoordsE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Mage Flame.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.fabric.mageflame.core.entity.creature;

import mod.gottsch.fabric.mageflame.MageFlame;
import mod.gottsch.fabric.mageflame.core.peristence.PlayerData;
import mod.gottsch.fabric.mageflame.core.peristence.StateSaverAndLoader;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.particle.ParticleTypes;

/**
 * Created by Mark Gottschling on 1/15/2025
 */
public class SummonedBaseHandler<T extends MobEntity & ISummonedEntity> {
    private static final int MAX_BUFFER_TIME = 1200;

    private int lifespan;
    private long birthTime;
    private int bufferTime;

    public SummonedBaseHandler() {}

    public SummonedBaseHandler(long birthTime, int lifespan) {
        this.birthTime = birthTime;
        this.lifespan = lifespan;
    }

    public void tick(T entity, LivingEntity owner) {
        if (!entity.getWorld().isClient) {
            if (entity.updateLifespan() < 0) {
                MageFlame.LOGGER.info("kill and unregister summoned with expired lifespan");
                killAndUnregister(entity, owner, entity.getWorld().getDamageSources().generic());
            }
        }
    }

    public void tickMovement(T entity) {
        if (entity.getWorld().isClient) {
            if (entity.getWorld().getTime() % 10 == 0) {
                BlockState state = entity.getWorld().getBlockState(entity.getBlockPos());
                if (state.getFluidState().isEmpty() || entity.canLiveInFluid()) {
                    entity.doLivingEffects();
                }
            }
        }
        else {
            // check for death scenarios ie no owner, if in water
            // NOTE the entity will join the world BEFORE the player
            // in single player and therefor will have no owner
            // and will call kill(). use bufferTime to delay this action.
            if (entity.getWorld().getTime() % 10 == 0) {
                BlockState state = entity.getWorld().getBlockState(entity.getBlockPos());
                if (entity.getOwner() == null) {
                    bufferTime += 10;
                    if (bufferTime > MAX_BUFFER_TIME) {
                        MageFlame.LOGGER.info("killing summoned entity that doesn't have an owner -> {}", entity.getUuid());
                        entity.kill();
                    }
                    return;
                } else if (!state.getFluidState().isEmpty() && !entity.canLiveInFluid()) {
                    // kill self
                    MageFlame.LOGGER.info("killing summoned entity that drowned -> {}", entity.getUuid());

                    killAndUnregister(entity, entity.getOwner());

                    return;
                }
                if (bufferTime > 0) bufferTime = 0;
            }
        }
    }

    public void killAndUnregister(T entity, LivingEntity owner) {
        unregister(entity, owner);
        entity.kill();
    }

    public void killAndUnregister(T entity, LivingEntity owner, DamageSource damageSource) {
        MageFlame.LOGGER.info("killAndUnregister called.");
        unregister(entity, owner);
        entity.kill(damageSource);
    }

    public void unregister(T entity, LivingEntity owner) {
        MageFlame.LOGGER.info("unregister called.");
        if (owner != null) {
            PlayerData playerData = StateSaverAndLoader.getPlayerState(owner);
            playerData.unregister(entity.getUuid());
        }
    }

    public void kill(T entity) {
        MageFlame.LOGGER.info("kill called");
        entity.kill(entity.getWorld().getDamageSources().generic());
    }

    public void kill(T entity, DamageSource damageSource) {
        MageFlame.LOGGER.info("killing with damagesource -> {}", entity.getUuid(), damageSource);
        entity.damage(damageSource, Float.MAX_VALUE);

        entity.doDeathEffects();

        // hide the entity
        entity.setInvisible(true);

//        entity.dead = true;
        // MageFlame.LOGGER.info("kill - current light coords -> {}, last light coords -> {}", getCurrentLightCoords(), getLastLightCoords());
    }

    /**
     *
     */
    protected double updateLifespan() {
        return --this.lifespan;
    }

    public void doDeathEffects(T entity) {
        if (entity.getWorld().isClient) {
            double d0 = entity.getX();
            double d1 = entity.getY() + 0.2;
            double d2 = entity.getZ();
            entity.getWorld().addParticle(ParticleTypes.SMOKE, entity.getParticleX(0.5), entity.getRandomBodyY(), entity.getParticleZ(0.5), (entity.getRandom().nextDouble() - 0.5) * 2.0, -entity.getRandom().nextDouble(), (entity.getRandom().nextDouble() - 0.5) * 2.0);
        }
    }

    public int getLifespan() {
        return lifespan;
    }

    public void setLifespan(int lifespan) {
        this.lifespan =lifespan;
    }

    public long getBirthTime() {
        return birthTime;
    }

    public void setBirthTime(long birthTime) {
        this.birthTime = birthTime;
    }
}
