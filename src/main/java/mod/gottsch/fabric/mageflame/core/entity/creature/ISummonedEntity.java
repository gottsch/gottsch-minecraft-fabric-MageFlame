/*
 * This file is part of Mage Flame.
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
package mod.gottsch.fabric.mageflame.core.entity.creature;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 *
 */
public interface ISummonedEntity {
    static final String OWNER = "owner";
    static final String BIRTH_TIME = "birthTime";
    static final String LIFESPAN = "lifespan";

    void doLivingEffects();
    void doDeathEffects();
    double updateLifespan();
    default boolean canLiveInFluid() {
        return false;
    }

    void kill(DamageSource damageSource);

    LivingEntity getOwner();
//    void setOwner(LivingEntity entity);
    default void setOwner(LivingEntity entity) {
        if (entity == null) {
            setOwnerUUID(null);
        }
        else {
            setOwnerUUID(entity.getUuid());
        }
    }

    UUID getOwnerUUID();
    void setOwnerUUID(UUID uuid);

    long getBirthTime();
    int getLifespan();

    void setBirthTime(long birthTime);
    void setLifespan(int lifespan);

    /**
     *
     * @return
     */
    default public Vec3d selectSummonOffsetPos(LivingEntity entity) {
        Vec3d eyePos = entity.getEyePos();
        Direction direction = entity.getMovementDirection();
        Vec3d offsetPos = switch (direction) {
            case NORTH -> eyePos.add(new Vec3d(0.5, 0, 0.35));
            case SOUTH -> eyePos.add(new Vec3d(-0.5, 0, -0.35));
            case EAST -> eyePos.add(new Vec3d(-0.35, 0, 0.5));
            case WEST -> eyePos.add(new Vec3d(0.35, 0, -0.5));
            default -> eyePos.add(new Vec3d(1, 0, 1));
        };
        return offsetPos;
    }

    /**
     * TODO this might need to move to a Util - this is currently duplicated in ISummonFlameItem
     * @param level
     * @param coords
     * @param direction
     * @return
     */
    default public Vec3d selectSpawnPos(World level, Vec3d coords, Direction direction) {

        if (!level.getBlockState(new BlockPos(vec3ToBlockPos(coords))).isAir()) {
            // test to the left
            switch (direction) {
                default:
                case NORTH:
                    if (level.getBlockState(vec3ToBlockPos(coords.add(-1, 0, 0))).isAir()) coords.add(-1, 0, 0);
                case SOUTH:
                    if (level.getBlockState(vec3ToBlockPos(coords.add(1, 0, 0))).isAir()) coords.add(1, 0, 0);
                case EAST :
                    if (level.getBlockState(vec3ToBlockPos(coords.add(0, 0, -1))).isAir()) coords.add(0, 0, -1);
                case WEST:
                    if (level.getBlockState(vec3ToBlockPos(coords.add(0, 0, 1))).isAir()) coords.add(0, 0, 1);
            };

            // test to the left+down
            switch (direction) {
                default:
                case NORTH:
                    if (level.getBlockState(vec3ToBlockPos(coords.add(-1, -1, 0))).isAir()) coords.add(-1, -1, 0);
                case SOUTH:
                    if (level.getBlockState(vec3ToBlockPos(coords.add(1, -1, 0))).isAir()) coords.add(1, -1, 0);
                case EAST :
                    if (level.getBlockState(vec3ToBlockPos(coords.add(0, -1, -1))).isAir()) coords.add(0, -1, -1);
                case WEST:
                    if (level.getBlockState(vec3ToBlockPos(coords.add(0, -1, 1))).isAir()) coords.add(0, -1, 1);
            };

            // test behind
            switch (direction) {
                default:
                case NORTH:
                    if (level.getBlockState(vec3ToBlockPos(coords.add(0, 0, 1))).isAir()) coords.add(0, 0, 1);
                case SOUTH:
                    if (level.getBlockState(vec3ToBlockPos(coords.add(0, 0, -1))).isAir()) coords.add(0, 0, -1);
                case EAST :
                    if (level.getBlockState(vec3ToBlockPos(coords.add(-1, 0, 0))).isAir()) coords.add(-1, 0, 0);
                case WEST:
                    if (level.getBlockState(vec3ToBlockPos(coords.add(1, 0, 0))).isAir()) coords.add(1, 0, 0);
            };

            // test down
            if (level.getBlockState(vec3ToBlockPos(coords.add(0, 1, 0))).isAir()) coords.add(0, 1, 0);

            // test right
            switch (direction) {
                default:
                case NORTH:
                    if (level.getBlockState(vec3ToBlockPos(coords.add(1, 0, 0))).isAir()) coords.add(1, 0, 0);
                case SOUTH:
                    if (level.getBlockState(vec3ToBlockPos(coords.add(-1, 0, 0))).isAir()) coords.add(-1, 0, 0);
                case EAST :
                    if (level.getBlockState(vec3ToBlockPos(coords.add(0, 0, 1))).isAir()) coords.add(0, 0, 1);
                case WEST:
                    if (level.getBlockState(vec3ToBlockPos(coords.add(0, 0, -1))).isAir()) coords.add(0, 0, -1);
            };

            // test right+down
            switch (direction) {
                default:
                case NORTH:
                    if (level.getBlockState(vec3ToBlockPos(coords.add(1, -1, 0))).isAir()) coords.add(1, -1, 0);
                case SOUTH:
                    if (level.getBlockState(vec3ToBlockPos(coords.add(-1, -1, 0))).isAir()) coords.add(-1, -1, 0);
                case EAST :
                    if (level.getBlockState(vec3ToBlockPos(coords.add(0, -1, 1))).isAir()) coords.add(0, -1, 1);
                case WEST:
                    if (level.getBlockState(vec3ToBlockPos(coords.add(0, -1, -1))).isAir()) coords.add(0, -1, -1);
            };
        }
        return coords;
    }

    @Deprecated
    // TODO move to a util class
    private BlockPos vec3ToBlockPos(Vec3d vec3) {
        return new BlockPos((int)vec3.x, (int)vec3.y, (int)vec3.z);
    }
}
