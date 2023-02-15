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
public abstract class SummonFlameBaseEntity extends FlyingEntity implements ISummonFlameEntity {
    private static final TrackedData<Optional<UUID>> DATA_OWNER_UUID;

    public static final String OWNER = "owner";
    public static final String LAST_LIGHT_COORDS = "currentLightCoords";
    public static final String CURRENT_LIGHT_COORDS = "currentLightCoords";
    public static final String BIRTH_TIME = "birthTime";
    public static final String LIFESPAN = "lifespan";

    private BlockPos currentLightCoords;
    private BlockPos lastLightCoords;

    private long birthTime;
    private long lifespan;

    static {
        DATA_OWNER_UUID = DataTracker.registerData(SummonFlameBaseEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    }

    /**
     *
     * @param entityType
     * @param world
     */
    protected SummonFlameBaseEntity(EntityType<? extends FlyingEntity> entityType, World world, long lifespan) {
        super(entityType, world);
        this.birthTime = world.getTime();
        this.lifespan = lifespan;
        this.moveControl = new SummonFlameMoveControl(this);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new SummonFlameFollowOwnerGoal(this, 3F));
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
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(DATA_OWNER_UUID, Optional.empty());
    }

    @Override
    public void doDeathEffects() {
        if (world.isClient) {
            double d0 = this.getX();
            double d1 = this.getY() + 0.2;
            double d2 = this.getZ();
//            ((ServerWorld)world).sendParticles(ParticleTypes.SMOKE, d0, d1, d2, 1, 0D, 0D, 0D, (double)0);
            this.world.addParticle(ParticleTypes.SMOKE, this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5), (this.random.nextDouble() - 0.5) * 2.0, -this.random.nextDouble(), (this.random.nextDouble() - 0.5) * 2.0);

        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.world.isClient) {
            if (updateLifespan() < 0) {
                kill(DamageSource.GENERIC);
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

        if (this.world.isClient) {
            if (this.world.getTime() % 10 == 0) {
                BlockState state = this.world.getBlockState(this.getBlockPos());
                if (state.getFluidState().isEmpty() || canLiveInFluid()) {
                    doLivingEffects();
                }
            }
        }
        else {
            // check for death scenarios ie no owner, if in water
            if (this.world.getTime() % 10 == 0) {
                BlockState state = this.world.getBlockState(this.getBlockPos());
                if (this.getOwner() == null || (!state.getFluidState().isEmpty() && !canLiveInFluid())) {
                    // kill self
                    kill();
                    return;
                }
            }
            updateLightBlocks();
        }
    }

    /**
     *
     */
    @Override
    public void updateLightBlocks() {
        if (this.dead) {
            return;
        }
//        // MageFlame.LOGGER.debug("entity updating light blocks -> {}", this.getUuidAsString());
        // initial setup
        if (getCurrentLightCoords() == null) {
            if (!updateLightCoords()) {
                kill();
                return;
            }
            // set last = current as they are in the same place
            setLastLightCoords(getCurrentLightCoords());
            world.setBlockState(getCurrentLightCoords(), getFlameBlock().getDefaultState());
        } else {
            if (!getBlockPos().equals(getCurrentLightCoords())) {
                // test location if fluids
                BlockState currentState = world.getBlockState(getBlockPos());
                if (!currentState.getFluidState().isEmpty() && !canLiveInFluid()) {
                    world.setBlockState(getCurrentLightCoords(), Blocks.AIR.getDefaultState());
                }
                else {
                    if (!updateLightCoords()) {
                        kill();
                        return;
                    }

                    // update block with flame
                    world.setBlockState(getCurrentLightCoords(), getFlameBlock().getDefaultState());

                    // delete old
                    world.setBlockState(getLastLightCoords(), Blocks.AIR.getDefaultState());
                }
            }
        }
    }

    /**
     *
     * @return whether the update was successful.
     */
    @Override
    public boolean updateLightCoords() {
        BlockPos pos = this.getBlockPos();
        BlockPos newPos = pos;

        /*
         *  want to short-circuit as quickly as possible here,
         *  with the fewest object created
         */
        // check in place
        if (testPlacement(pos)) {
        }
        // up
        else if (testPlacement(newPos = pos.up())) {
        }
        // check west
        else if (testPlacement(newPos = pos.west())) {
        }
        // check east
        else if (testPlacement(newPos = pos.east()))	{
        }
        // check north
        else if (testPlacement(newPos = pos.north())) {
        }
        // check south
        else if (testPlacement(newPos = pos.south())) {
        }
        // check down
        else if (testPlacement(newPos = pos.down())) {
        }
        else {
            return false;
        }

        // update positions
        setLastLightCoords(getCurrentLightCoords());
        setCurrentLightCoords(new BlockPos(newPos));

        return true;
    }

    /**
     *
     * @param pos
     * @return
     */
    protected boolean testPlacement(BlockPos pos) {
        BlockState state = this.world.getBlockState(pos);
        // check block
        return state.isAir();
    }

    @Override
    public void kill() {
        kill(DamageSource.GENERIC);
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

        // MageFlame.LOGGER.debug("kill - current light coords -> {}, last light coords -> {}", getCurrentLightCoords(), getLastLightCoords());

        // remove light blocks
        if (getCurrentLightCoords() != null && world.getBlockState(getCurrentLightCoords()).getBlock() == getFlameBlock()) {
            world.setBlockState(getCurrentLightCoords(), Blocks.AIR.getDefaultState());
        }
        if (getLastLightCoords() != null && world.getBlockState(getLastLightCoords()).getBlock() == getFlameBlock()) {
            world.setBlockState(getLastLightCoords(), Blocks.AIR.getDefaultState());
        }
    }

    @Override
    protected void updatePostDeath() {
        // remove light blocks
        if (getCurrentLightCoords() != null && world.getBlockState(getCurrentLightCoords()).getBlock() == getFlameBlock()) {
            world.setBlockState(getCurrentLightCoords(), Blocks.AIR.getDefaultState());
        }
        if (getLastLightCoords() != null && world.getBlockState(getLastLightCoords()).getBlock() == getFlameBlock()) {
            world.setBlockState(getLastLightCoords(), Blocks.AIR.getDefaultState());
        }

        super.updatePostDeath();
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

        if (this.getCurrentLightCoords() != null) {
            nbt.put(CURRENT_LIGHT_COORDS, saveCoords(getCurrentLightCoords()));
        }
        if (this.getLastLightCoords() != null) {
            nbt.put(LAST_LIGHT_COORDS, saveCoords(getLastLightCoords()));
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
        if (nbt.contains(CURRENT_LIGHT_COORDS)) {
            BlockPos coords = loadCoords(nbt.getCompound(CURRENT_LIGHT_COORDS));
            setCurrentLightCoords(coords);
        }
        if (nbt.contains(LAST_LIGHT_COORDS)) {
            BlockPos coords = loadCoords(nbt.getCompound(LAST_LIGHT_COORDS));
            setLastLightCoords(coords);
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
    public static class SummonFlameFollowOwnerGoal extends Goal {
        private SummonFlameBaseEntity flameBall;
        // the distance away at which the flame ball starts to follow
        private float startDistance;
        private LivingEntity owner;

        /**
         *
         * @param flameBall
         * @param startDistance
         */
        public SummonFlameFollowOwnerGoal(SummonFlameBaseEntity flameBall, float startDistance) {
            this.flameBall = flameBall;
            this.startDistance = startDistance;
        }

        @Override
        public boolean canStart() {
            if (this.flameBall.random.nextInt(toGoalTicks(7)) == 0) {
                return false;
            }

            LivingEntity entity = this.flameBall.getOwner();
            if (entity == null) {
                return false;
            } else if (entity.isSpectator()) {
                return false;
            }

            this.owner = entity;
            MoveControl moveControl = this.flameBall.getMoveControl();
            double distance = 0;
            if (this.flameBall.getTarget() != null) {
                double d0 = moveControl.getTargetX() - this.flameBall.getX();
                double d1 = moveControl.getTargetY() - this.flameBall.getY();
                double d2 = moveControl.getTargetZ() - this.flameBall.getZ();
                distance = d0 * d0 + d1 * d1 + d2 * d2;
            }
            boolean outsideProximity = this.flameBall.squaredDistanceTo(entity) > startDistance * startDistance;
            return (distance < 1.0D && outsideProximity) || distance > 3600.0D;
        }

        @Override
        public boolean shouldContinue() {
            double d0 = this.flameBall.getMoveControl().getTargetX() - this.flameBall.getX();
            double d1 = this.flameBall.getMoveControl().getTargetY() - this.flameBall.getY();
            double d2 = this.flameBall.getMoveControl().getTargetZ() - this.flameBall.getZ();
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;

            if (d3 >= 1D) {
                return true;
            }
            return false;
        }

        @Override
        public void start() {
            Vec3d initialPos = this.flameBall.selectSummonOffsetPos(this.owner);
            Vec3d wantedPos = this.flameBall.selectSpawnPos(this.flameBall.world, new Vec3d(initialPos.x, initialPos.y, initialPos.z), this.flameBall.getMovementDirection());
            this.flameBall.getMoveControl().moveTo(wantedPos.x, wantedPos.y, wantedPos.z, 1.0D);
        }

        @Override
        public void stop() {
            this.owner = null;
        }

        @Override
        public void tick() {
            if (this.flameBall.random.nextInt(toGoalTicks(5)) == 0) {
                if (this.flameBall.squaredDistanceTo(this.owner) >= 36.0D) {
                    // teleport to owner
                    Vec3d offsetPos = this.flameBall.selectSummonOffsetPos(this.owner);
                    Vec3d wantedPos = this.flameBall.selectSpawnPos(this.flameBall.world, new Vec3d(offsetPos.x, offsetPos.y, offsetPos.z), this.flameBall.getMovementDirection());
                    this.flameBall.getMoveControl().moveTo(wantedPos.x, wantedPos.y, wantedPos.z, 1.0D);
//                    this.flameBall.moveTo(wantedPos.x, wantedPos.y, wantedPos.z, this.flameBall.getYRot(), this.flameBall.getXRot());
                }
            }
        }
    }

    /*
     * This uses the Vex MoveControl tick() algorithm.
     */
    class SummonFlameMoveControl extends MoveControl {
        public SummonFlameMoveControl(SummonFlameBaseEntity entity) {
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
            return uuid == null ? null : this.world.getPlayerByUuid(uuid);
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
    public BlockPos getCurrentLightCoords() {
        return currentLightCoords;
    }

    @Override
    public BlockPos getLastLightCoords() {
        return lastLightCoords;
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
    public void setCurrentLightCoords(BlockPos currentLightCoords) {
        this.currentLightCoords = currentLightCoords;
    }

    @Override
    public void setLastLightCoords(BlockPos lastLightCoords) {
        this.lastLightCoords = lastLightCoords;
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