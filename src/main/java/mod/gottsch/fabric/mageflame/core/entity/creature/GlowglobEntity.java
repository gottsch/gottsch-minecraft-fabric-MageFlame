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
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.EnumSet;

/**
 * @author Mark Gottschling on 1/21/2025
 */
public class GlowglobEntity extends FlyingEntity implements ILifespanEntity {
    private int lifespan;

    public GlowglobEntity(EntityType<? extends FlyingEntity> entityType, World world) {
        super(entityType, world);
        setLifespan(MageFlame.CONFIG.glowglobLifespan());
        this.moveControl = new GlowglobMoveControl(this);
    }

    // TODO add goal for movement, to go to 1 block above ground
    // TODO in movement control, if -y movement (down) then travel slowly
    // TODO travel slow in general
    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(5, new FloatGoal(this));

    }

    public static DefaultAttributeContainer.Builder createGlobAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1.0);
    }

    @Override
    public double updateLifespan() {
        return --this.lifespan;
    }

    @Override
    public void doLivingEffects() {
        double d0 = this.getRandomX(0.5);
        double d1 = this.getY() + 0.25;
        double d2 = this.getRandomZ(0.5);
        this.getWorld().addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        this.getWorld().addParticle(ParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
//        this.getWorld().addParticle(ParticleTypes.LAVA, d0, d1, d2, 0.0, 0.0, 0.0);
    }

    @Override
    public void doDeathEffects() {
    }

    @Override
    public void tick() {
        super.tick();
        if (!getWorld().isClient) {
            if (updateLifespan() < 0) {
                kill();
            }
        }
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (getWorld().isClient) {
            if (getWorld().getTime() % 10 == 0) {
                BlockState state = getWorld().getBlockState(getBlockPos());
                if (state.getFluidState().isEmpty() || canLiveInFluid()) {
                    doLivingEffects();
                }
            }
        } else {
            if (getWorld().getTime() % 10 == 0) {
                BlockState state = getWorld().getBlockState(getBlockPos());
                if (!state.getFluidState().isEmpty() && !canLiveInFluid()) {
                    // kill self
                    kill();
                }
            }
        }
    }

    @Override
    public void kill() {
        super.kill();
        doDeathEffects();
        // hide the entity
        setInvisible(true);
        // set dead
        this.dead = true;
    }

    @Override
    protected void playHurtSound(DamageSource source) {
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putInt(LIFESPAN, getLifespan());
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

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

    @Override
    public int getLifespan() {
        return lifespan;
    }

    @Override
    public void setLifespan(int lifespan) {
        this.lifespan = lifespan;
    }


    static class GlowglobMoveControl extends MoveControl {
        private final GlowglobEntity glowglob;
        private int collisionCheckCooldown;

        public GlowglobMoveControl(final GlowglobEntity glowglob) {
            super(glowglob);
            this.glowglob = glowglob;
        }

        @Override
        public void tick() {
            if (this.state == MoveControl.State.MOVE_TO) {
                // get the vector between the glowglob and the target
                Vec3d vec3d = new Vec3d(this.targetX - glowglob.getX(), this.targetY - glowglob.getY(), this.targetZ - glowglob.getZ());

                double distance = vec3d.length();
                vec3d = vec3d.normalize();
                if (this.willCollide(vec3d, MathHelper.ceil(distance))) {
                    this.glowglob.setVelocity(this.glowglob.getVelocity().add(vec3d.multiply(0.025)));
                }

                // NOTE this is the important part
                // when within body length of target, then start the stopping process
                if (distance < glowglob.getBoundingBox().getAverageSideLength()) {
                    glowglob.setVelocity(glowglob.getVelocity().multiply(0.25));
                    if (glowglob.getVelocity().length() < 0.0125) {
                        glowglob.setVelocity(glowglob.getVelocity().multiply(0));
                        this.state = MoveControl.State.WAIT;
                    }
                }
            }
        }

        private boolean willCollide(Vec3d direction, int steps) {
            Box box = this.glowglob.getBoundingBox();
            for (int i = 1; i < steps; i++) {
                box = box.offset(direction);
                if (!this.glowglob.getWorld().isSpaceEmpty(this.glowglob, box)) {
                    return false;
                }
            }
            return true;
        }
    }

    /*
     *
     */
    static class FloatGoal extends Goal {
        private final GlowglobEntity glowglob;

        public FloatGoal(GlowglobEntity glowglob) {
            this.glowglob = glowglob;
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        @Override
        public boolean canStart() {
            MoveControl moveControl = this.glowglob.getMoveControl();
            if (!moveControl.isMoving()) {
                return true;
            }
            return false;
        }

        @Override
        public boolean shouldContinue() {
            return false;
        }

        @Override
        public void start() {

            /*
             * calculate the target position
             */
            int count = 0;
            BlockPos pos = glowglob.getBlockPos();
            while (glowglob.getWorld().getBlockState(pos).isReplaceable() && count < 32) {
                pos = pos.down();
                count++;
            }

            // calculate target y
            double y = pos.getY() + 2.6;

            // calculate the vector between glowglob and floor on y-axis
            Vec3d vec3d = new Vec3d(0, y - glowglob.getY(), 0);

            // if the distance is greater than the average side size of the glowglob's
            // bounding box then update the target position
            if (vec3d.length() > glowglob.getBoundingBox().getAverageSideLength()) {
                this.glowglob.getMoveControl().moveTo(glowglob.getX(), y, glowglob.getZ(), 0.05);
            }
        }
    }
}
