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
package mod.gottsch.fabric.mageflame.core.entity.creature;

import mod.gottsch.fabric.mageflame.MageFlame;
import mod.gottsch.fabric.mageflame.core.setup.Registration;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;

/**
 *
 * @author Mark Gottschling Jan 18, 2025
 *
 */
public class BubbleFlameEntity extends SummonedFlyingEntity {

    public BubbleFlameEntity(EntityType<? extends FlyingEntity> entityType, World level) {
        super(entityType, level, MageFlame.CONFIG.bubbleFlameLifespan());
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK);
    }

    @Override
    public boolean canLiveInFluid() {
        return true;
    }

    @Override
    public void doLivingEffects() {
        double d1 = this.getY() + 0.2;
        if (getWorld().getTime() % 2 == 0) {
            double d0 = this.getRandomX(0.65);
            double d2 = this.getRandomZ(0.65);
            this.getWorld().addParticle(Registration.BUBBLE_FLAME_PARTICLE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
        if (getWorld().getTime() % 3 == 0) {
            double d0 = this.getRandomX(0.65);
            double d2 = this.getRandomZ(0.65);
            this.getWorld().addParticle(Registration.BUBBLE_FLAME_PARTICLE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }

        if (this.getWorld().getTime() % 4 == 0) {
            double d0 = this.getX(0.65);
            double d2 = this.getZ(0.65);
            this.getWorld().addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }

    /**
     * not sure if there exists an equivalent method of these in fabric
     */
    public double getX(double factor) {
        return this.getPos().x + (double)this.getWidth() * factor;
    }

    public double getRandomX(double factor) {
        return this.getX((2.0D * this.random.nextDouble() - 1.0D) * factor);
    }

    public double getZ(double factor) {
        return this.getPos().z + (double)this.getWidth() * factor;
    }

    public double getRandomZ(double factor) {
        return this.getZ((2.0D * this.random.nextDouble() - 1.0D) * factor);
    }

}