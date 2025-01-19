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
import mod.gottsch.fabric.mageflame.core.entity.ai.goal.SummonedLightSourceAttackWithOwnerGoal;
import mod.gottsch.fabric.mageflame.core.entity.ai.goal.SummonedLightSourceTrackOwnerAttackerGoal;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Mark Gottschling on 1/9/2025
 */
public class EmberHoundEntity extends SummonedPathAwareEntity {

    public EmberHoundEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world, MageFlame.CONFIG.emberHoundLifespan());
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(4, new PounceAtTargetGoal(this, 0.4F));
        this.goalSelector.add(5, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(10, new LookAroundGoal(this));
        this.targetSelector.add(1, new SummonedLightSourceTrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new SummonedLightSourceAttackWithOwnerGoal(this));
        this.targetSelector.add(3, new RevengeGoal(this, PlayerEntity.class).setGroupRevenge());
        this.targetSelector.add(7, new ActiveTargetGoal(this, AbstractSkeletonEntity.class, false));
    }

    public static DefaultAttributeContainer.Builder createWolfAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3F)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0);
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
    }

    protected SoundEvent getAmbientSound() {
        int i = this.random.nextInt(10);
        if (i < 2) {
            return SoundEvents.ENTITY_WOLF_GROWL;
        } else if (i < 5) {
            return SoundEvents.ENTITY_WOLF_PANT;
        } else if (i < 7) {
            return SoundEvents.ENTITY_WOLF_AMBIENT;
        } else {
            return super.getAmbientSound();
        }
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_WOLF_HURT;
    }

    @Override
    public void doLivingEffects() {
        double d1 = this.getRandomBodyY();
        for (int i=0; i < 3; i++) {
            double d0 = this.getRandomX(0.5);
            double d2 = this.getRandomZ(0.75);
            this.getWorld().addParticle(ParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
        if (this.getWorld().getTime() % 4 == 0) {
            double d0 = this.getX(0.5);
            double d2 = this.getZ(0.75);
            this.getWorld().addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }

    // TODO need to move this to parent class
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

//    @Override
//    protected boolean testPlacement(BlockPos pos) {
//        BlockState state = this.getWorld().getBlockState(pos);
//        // check block
//        if (state.isAir() || (state.isReplaceable()) && state.getFluidState().isEmpty()) {
//            return true;
//        }
//        return false;
//    }
}
