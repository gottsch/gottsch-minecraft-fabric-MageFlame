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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURCoordsE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Mage Flame.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.fabric.mageflame.core.entity.creature;

import mod.gottsch.fabric.mageflame.MageFlame;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

/**
 *
 * @author Mark Gottschling Jan 21, 2023
 *
 */
public abstract class SummonedLightSourceFlyingEntity extends FlyingEntity implements ISummonedLightSourceEntity {
    private static final TrackedData<Optional<UUID>> DATA_OWNER_UUID;

    private BlockPos currentLightCoords;
    private BlockPos lastLightCoords;

    private long birthTime;
    private long lifespan;

    static {
        DATA_OWNER_UUID = DataTracker.registerData(SummonedLightSourceFlyingEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    }

    /**
     *
     * @param entityType
     * @param world
     */
    protected SummonedLightSourceFlyingEntity(EntityType<? extends FlyingEntity> entityType, World world, long lifespan) {
        super(entityType, world);
        this.birthTime = getWorld().getTime();
        this.lifespan = lifespan;
        this.moveControl = new SummonedLightSourceFlyingMoveControl(this);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new SummonedLightSourceFlyingFollowOwnerGoal(this, 3F));
    }

    /**
     *
     */
    public static DefaultAttributeContainer.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 0.5)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3F);
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
    public void doDeathEffects() {
        if (getWorld().isClient) {
            double d0 = this.getX();
            double d1 = this.getY() + 0.2;
            double d2 = this.getZ();
//            ((ServerWorld)world).sendParticles(ParticleTypes.SMOKE, d0, d1, d2, 1, 0D, 0D, 0D, (double)0);
            this.getWorld().addParticle(ParticleTypes.SMOKE, this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5), (this.random.nextDouble() - 0.5) * 2.0, -this.random.nextDouble(), (this.random.nextDouble() - 0.5) * 2.0);

        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient) {
            if (updateLifespan() < 0) {
                kill(getWorld().getDamageSources().generic());
            }
        }
    }

    /**
     *
     */
    protected double updateLifespan() {

        return --this.lifespan;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        if (this.getWorld().isClient) {
            if (this.getWorld().getTime() % 10 == 0) {
                BlockState state = this.getWorld().getBlockState(this.getBlockPos());
                if (state.getFluidState().isEmpty() || canLiveInFluid()) {
                    doLivingEffects();
                }
            }
        }
        else {
            // check for death scenarios ie no owner, if in water
            if (this.getWorld().getTime() % 10 == 0) {
                BlockState state = this.getWorld().getBlockState(this.getBlockPos());
                if (this.getOwner() == null || (!state.getFluidState().isEmpty() && !canLiveInFluid())) {
                    // kill self
                    kill();
                    return;
                }
            }
        }
    }

    /**
     *
     * @param pos
     * @return
     */
    protected boolean testPlacement(BlockPos pos) {
        BlockState state = this.getWorld().getBlockState(pos);
        // check block
        return state.isAir();
    }

    @Override
    public void kill() {
        kill(getWorld().getDamageSources().generic());
    }

    /**
     *
     * @param damageSource the source of the damage
     */
    public void kill(DamageSource damageSource) {
        this.damage(damageSource, Float.MAX_VALUE);

        doDeathEffects();

        // hide the entity
        setInvisible(true);

        // set dead
        this.dead = true;
    }

    /**
     *
     * @param nbt
     */
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        if (this.getOwnerUUID() != null) {
            nbt.putUuid(OWNER, this.getOwnerUUID());
        }

        nbt.putLong(BIRTH_TIME, getBirthTime());
        nbt.putDouble(LIFESPAN, getLifespan());
    }

    /**
     *
     * @param nbt
     */
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
            setLifespan(nbt.getLong(LIFESPAN));
        }
    }

    // TEMP until GottschCore for Fabric exists
    public static NbtCompound saveCoords(BlockPos pos) {
        NbtCompound tag = new NbtCompound();
        tag.putInt("x", pos.getX());
        tag.putInt("y", pos.getY());
        tag.putInt("z", pos.getZ());
        return tag;
    }

    public static BlockPos loadCoords(NbtCompound tag) {
        if (tag.contains("x") && tag.contains("y") && tag.contains("z")) {
            return new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
        }
        return null;
    }

    @Override
    public void checkDespawn() {
        // does NOT despawn
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    public static boolean canSpawn(EntityType<GhastEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return false;
    }

    /**
     *
     */
    public static class SummonedLightSourceFlyingFollowOwnerGoal extends Goal {
        private SummonedLightSourceFlyingEntity lightSourceEntity;
        // the distance away at which the flame ball starts to follow
        private float startDistance;
        private LivingEntity owner;

        /**
         *
         * @param lightSourceEntity
         * @param startDistance
         */
        public SummonedLightSourceFlyingFollowOwnerGoal(SummonedLightSourceFlyingEntity lightSourceEntity, float startDistance) {
            this.lightSourceEntity = lightSourceEntity;
            this.startDistance = startDistance;
        }

        @Override
        public boolean canStart() {
            if (this.lightSourceEntity.random.nextInt(toGoalTicks(7)) == 0) {
                return false;
            }

            LivingEntity ownerEntity = this.lightSourceEntity.getOwner();
            if (ownerEntity == null) {
                return false;
            } else if (ownerEntity.isSpectator()) {
                return false;
            }

            this.owner = ownerEntity;
            MoveControl moveControl = this.lightSourceEntity.getMoveControl();
            double distance = 0;
            if (this.lightSourceEntity.getTarget() != null) {
                double d0 = moveControl.getTargetX() - this.lightSourceEntity.getX();
                double d1 = moveControl.getTargetY() - this.lightSourceEntity.getY();
                double d2 = moveControl.getTargetZ() - this.lightSourceEntity.getZ();
                distance = d0 * d0 + d1 * d1 + d2 * d2;
            }
            boolean outsideProximity = this.lightSourceEntity.squaredDistanceTo(ownerEntity) > startDistance * startDistance;
            return (distance < 1.0D && outsideProximity) || distance > 3600.0D;
        }

        @Override
        public boolean shouldContinue() {
            double d0 = this.lightSourceEntity.getMoveControl().getTargetX() - this.lightSourceEntity.getX();
            double d1 = this.lightSourceEntity.getMoveControl().getTargetY() - this.lightSourceEntity.getY();
            double d2 = this.lightSourceEntity.getMoveControl().getTargetZ() - this.lightSourceEntity.getZ();
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;

            if (d3 >= 1D) {
                return true;
            }
            return false;
        }

        @Override
        public void start() {
            Vec3d initialPos = this.lightSourceEntity.selectSummonOffsetPos(this.owner);
            Vec3d wantedPos = this.lightSourceEntity.selectSpawnPos(this.lightSourceEntity.getWorld(), new Vec3d(initialPos.x, initialPos.y, initialPos.z), this.lightSourceEntity.getMovementDirection());
            this.lightSourceEntity.getMoveControl().moveTo(wantedPos.x, wantedPos.y, wantedPos.z, 1.0D);
        }

        @Override
        public void stop() {
            this.owner = null;
        }

        @Override
        public void tick() {
            if (this.lightSourceEntity.random.nextInt(toGoalTicks(5)) == 0) {
                if (this.lightSourceEntity.squaredDistanceTo(this.owner) >= 36.0D) {
                    // teleport to owner
                    Vec3d offsetPos = this.lightSourceEntity.selectSummonOffsetPos(this.owner);
                    Vec3d wantedPos = this.lightSourceEntity.selectSpawnPos(this.lightSourceEntity.getWorld(), new Vec3d(offsetPos.x, offsetPos.y, offsetPos.z), this.lightSourceEntity.getMovementDirection());
                    this.lightSourceEntity.getMoveControl().moveTo(wantedPos.x, wantedPos.y, wantedPos.z, 1.0D);
//                    this.flameBall.moveTo(wantedPos.x, wantedPos.y, wantedPos.z, this.flameBall.getYRot(), this.flameBall.getXRot());
                }
            }
        }
    }

    /*
     * This uses the Vex MoveControl tick() algorithm.
     */
    class SummonedLightSourceFlyingMoveControl extends MoveControl {
        public SummonedLightSourceFlyingMoveControl(SummonedLightSourceFlyingEntity entity) {
            super(entity);
        }

        public void tick() {
            if (this.state == State.MOVE_TO) {
                Vec3d vec3d = new Vec3d(this.targetX - this.entity.getX(), this.targetY - this.entity.getY(), this.targetZ - this.entity.getZ());
                double d = vec3d.length();
                if (d < this.entity.getBoundingBox().getAverageSideLength()) {
                    this.state = State.WAIT;
                    this.entity.setVelocity(this.entity.getVelocity().multiply(0.5));
                } else {
                    this.entity.setVelocity(this.entity.getVelocity().add(vec3d.multiply(this.speed * 0.05 / d)));
                    if (this.entity.getTarget() == null) {
                        Vec3d vec3d2 = this.entity.getVelocity();
                        this.entity.setYaw(-((float) MathHelper.atan2(vec3d2.x, vec3d2.z)) * 57.295776F);
                        this.entity.bodyYaw = this.entity.getYaw();
                    } else {
                        double e = this.entity.getTarget().getX() - this.entity.getX();
                        double f = this.entity.getTarget().getZ() - this.entity.getZ();
                        this.entity.setYaw(-((float)MathHelper.atan2(e, f)) * 57.295776F);
                        this.entity.bodyYaw = this.entity.getYaw();
                    }
                }

            }
        }
    }

    @Override
    @Nullable
    public LivingEntity getOwner() {
        try {
            UUID uuid = this.getOwnerUUID();
            return uuid == null ? null : this.getWorld().getPlayerByUuid(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    @Override
    public void setOwner(LivingEntity entity) {
        if (entity == null) {
            setOwnerUUID(null);
        }
        else {
            setOwnerUUID(entity.getUuid());
        }
    }

    @Override
    public UUID getOwnerUUID() {
        return this.dataTracker.get(DATA_OWNER_UUID).orElse(null);
    }

    @Override
    public void setOwnerUUID(UUID uuid) {
        this.dataTracker.set(DATA_OWNER_UUID, Optional.ofNullable(uuid));
    }

    @Override
    public long getBirthTime() {
        return birthTime;
    }

    @Override
    public long getLifespan() {
        return lifespan;
    }

    @Override
    public void setBirthTime(long birthTime) {
        this.birthTime = birthTime;
    }

    @Override
    public void setLifespan(long lifespan) {
        this.lifespan = lifespan;
    }
}