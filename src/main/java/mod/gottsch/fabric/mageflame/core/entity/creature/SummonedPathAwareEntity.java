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
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by Mark Gottschling on 1/9/2025
 */
public abstract class SummonedPathAwareEntity extends PathAwareEntity implements ISummonedEntity {
    private static final TrackedData<Optional<UUID>> DATA_OWNER_UUID;

    // entity for composite inheritance
    private final SummonedEntityBaseHandler<SummonedPathAwareEntity> summonedEntityBaseHandler;

    static {
        DATA_OWNER_UUID = DataTracker.registerData(SummonedPathAwareEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    }

    protected SummonedPathAwareEntity(EntityType<? extends PathAwareEntity> entityType, World world, int lifespan) {
        super(entityType, world);
        this.summonedEntityBaseHandler = new SummonedEntityBaseHandler<>(world.getTime(), lifespan);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(6, new FollowOwnerGoal(this, (double)1.0F, 5.0F, 2.0F));
    }

    // TODO why isn't his used?!
    public static DefaultAttributeContainer.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 0.5)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25F);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(DATA_OWNER_UUID, Optional.empty());
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        // do not play a sound
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.BLOCK_CAMPFIRE_CRACKLE;
    }

    @Override
    public double updateLifespan() {
        return this.summonedEntityBaseHandler.updateLifespan();
    }

    @Override
    public void doDeathEffects() {
        this.summonedEntityBaseHandler.doDeathEffects(this);
    }

    @Override
    public void tick() {
        super.tick();
        this.summonedEntityBaseHandler.tick(this, getOwner());
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        this.summonedEntityBaseHandler.tickMovement(this);
    }

//    protected boolean testPlacement(BlockPos pos) {
//        BlockState state = this.getWorld().getBlockState(pos);
//        // check block
//        return state.isAir();
//    }

    @Override
    public LivingEntity getOwner() {
        try {
            UUID uuid = this.getOwnerUUID();
            return (uuid == null) ? null : this.getWorld().getPlayerByUuid(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    /**
     * override vanilla entity.kill()
     */
    @Override
    public void kill() {
        this.summonedEntityBaseHandler.kill(this);
    }

    /**
     * override MageFlame#ISummonedEntity.kill(DamageSource)
     * @param damageSource
     */
    @Override
    public void kill(DamageSource damageSource) {
        this.summonedEntityBaseHandler.kill(this, damageSource);

        // set dead
        this.dead = true;
//
//        // MageFlame.LOGGER.info("kill - current light coords -> {}, last light coords -> {}", getCurrentLightCoords(), getLastLightCoords());
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        if (this.getOwnerUUID() != null) {
            nbt.putUuid(OWNER, this.getOwnerUUID());
        }

        nbt.putLong(BIRTH_TIME, getBirthTime());
        nbt.putInt(LIFESPAN, getLifespan());
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains(OWNER)) {
            UUID uuid = nbt.getUuid(OWNER);
            try {
                this.setOwnerUUID(uuid);
            } catch (Throwable throwable) {
                MageFlame.LOGGER.warn("Unable to set owner of flame ball to -> {}", uuid);
            }
        }

        if (nbt.contains(BIRTH_TIME)) {
            setBirthTime(nbt.getLong(BIRTH_TIME));
        }
        if (nbt.contains(LIFESPAN)) {
            setLifespan(nbt.getInt(LIFESPAN));
        }
    }

    @Override
    public void checkDespawn() {
        // does NOT despawn
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    public UUID getOwnerUUID() {
        return this.getDataTracker().get(DATA_OWNER_UUID).orElse(null);
    }

    @Override
    public void setOwnerUUID(UUID uuid) {
        this.dataTracker.set(DATA_OWNER_UUID, Optional.ofNullable(uuid));
    }

    @Override
    public long getBirthTime() {
//        return birthTime;
        return this.summonedEntityBaseHandler.getBirthTime();
    }

    @Override
    public void setBirthTime(long birthTime) {
//        this.birthTime = birthTime;
        this.summonedEntityBaseHandler.setBirthTime(birthTime);
    }

    @Override
    public int getLifespan() {
//        return lifespan;
        return this.summonedEntityBaseHandler.getLifespan();
    }

    @Override
    public void setLifespan(int lifespan) {
//        this.lifespan =lifespan;
        this.summonedEntityBaseHandler.setLifespan(lifespan);
    }

    ///// from TameableEntity /////
    public final boolean cannotFollowOwner() {
        return this.hasVehicle() || this.getOwner() != null && this.getOwner().isSpectator();
    }

    protected boolean canTeleportOntoLeaves() {
        return true;
    }

    public void tryTeleportToOwner() {
        LivingEntity livingEntity = this.getOwner();
        if (livingEntity != null) {
            this.tryTeleportNear(livingEntity.getBlockPos());
        }
    }

    public boolean shouldTryTeleportToOwner() {
        LivingEntity livingEntity = this.getOwner();
        return livingEntity != null && this.squaredDistanceTo(this.getOwner()) >= 144.0;
    }

    private void tryTeleportNear(BlockPos pos) {
        for (int i = 0; i < 10; i++) {
            int j = this.random.nextBetween(-3, 3);
            int k = this.random.nextBetween(-3, 3);
            if (Math.abs(j) >= 2 || Math.abs(k) >= 2) {
                int l = this.random.nextBetween(-1, 1);
                if (this.tryTeleportTo(pos.getX() + j, pos.getY() + l, pos.getZ() + k)) {
                    return;
                }
            }
        }
    }

    private boolean tryTeleportTo(int x, int y, int z) {
        if (!this.canTeleportTo(new BlockPos(x, y, z))) {
            return false;
        } else {
            this.refreshPositionAndAngles((double)x + 0.5, (double)y, (double)z + 0.5, this.getYaw(), this.getPitch());
            this.navigation.stop();
            return true;
        }
    }

    private boolean canTeleportTo(BlockPos pos) {
        PathNodeType pathNodeType = LandPathNodeMaker.getLandNodeType(this, pos);
        if (pathNodeType != PathNodeType.WALKABLE) {
            return false;
        } else {
            BlockState blockState = this.getWorld().getBlockState(pos.down());
            if (!this.canTeleportOntoLeaves() && blockState.getBlock() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos blockPos = pos.subtract(this.getBlockPos());
                return this.getWorld().isSpaceEmpty(this, this.getBoundingBox().offset(blockPos));
            }
        }
    }

    public boolean canAttackWithOwner(LivingEntity target, LivingEntity owner) {
        return true;
    }

    ///// end from Tameable /////

    public static class FollowOwnerGoal extends Goal {
        private final SummonedPathAwareEntity lightSourceEntity;
        private LivingEntity owner;
        private final double speed;
        private final EntityNavigation navigation;
        private int updateCountdownTicks;
        private final float maxDistance;
        private final float minDistance;
        private float oldWaterPathfindingPenalty;

        public FollowOwnerGoal(SummonedPathAwareEntity lightSourceEntity, double speed, float minDistance, float maxDistance) {
            this.lightSourceEntity = lightSourceEntity;
            this.speed = speed;
            this.navigation = lightSourceEntity.getNavigation();
            this.minDistance = minDistance;
            this.maxDistance = maxDistance;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
            if (!(lightSourceEntity.getNavigation() instanceof MobNavigation) && !(lightSourceEntity.getNavigation() instanceof BirdNavigation)) {
                throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
            }
        }

        public boolean canStart() {
            LivingEntity ownerEntity = this.lightSourceEntity.getOwner();
            if (ownerEntity == null) {
                return false;
            } else if (this.lightSourceEntity.cannotFollowOwner()) {
                return false;
            } else if (this.lightSourceEntity.squaredDistanceTo(ownerEntity) < (double)(this.minDistance * this.minDistance)) {
                return false;
            } else {
                this.owner = ownerEntity;
                return true;
            }
        }

        public boolean shouldContinue() {
            if (this.navigation.isIdle()) {
                return false;
            } else if (this.lightSourceEntity.cannotFollowOwner()) {
                return false;
            } else {
                return !(this.lightSourceEntity.squaredDistanceTo(this.owner) <= (double)(this.maxDistance * this.maxDistance));
            }
        }

        public void start() {
            this.updateCountdownTicks = 0;
            this.oldWaterPathfindingPenalty = this.lightSourceEntity.getPathfindingPenalty(PathNodeType.WATER);
            this.lightSourceEntity.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
        }

        public void stop() {
            this.owner = null;
            this.navigation.stop();
            this.lightSourceEntity.setPathfindingPenalty(PathNodeType.WATER, this.oldWaterPathfindingPenalty);
        }

        public void tick() {
            boolean bl = this.lightSourceEntity.shouldTryTeleportToOwner();
            if (!bl) {
                this.lightSourceEntity.getLookControl().lookAt(this.owner, 10.0F, (float)this.lightSourceEntity.getMaxLookPitchChange());
            }

            if (--this.updateCountdownTicks <= 0) {
                this.updateCountdownTicks = this.getTickCount(10);
                if (bl) {
                    this.lightSourceEntity.tryTeleportToOwner();
                } else {
                    this.navigation.startMovingTo(this.owner, this.speed);
                }
            }
        }
    }
}
