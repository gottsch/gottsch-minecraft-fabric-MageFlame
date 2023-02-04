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
package mod.gottsch.fabric.mageflame.core.entity.creature;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 *
 */
public interface ISummonFlameEntity {
    public void doLivingEffects();
    void doDeathEffects();

    @Nullable
    LivingEntity getOwner();
    void setOwner(LivingEntity entity);

    UUID getOwnerUUID();
    void setOwnerUUID(UUID uuid);

    BlockPos getCurrentLightCoords();
    BlockPos getLastLightCoords();
    long getBirthTime();
    long getLifespan();

    void setCurrentLightCoords(BlockPos currentLightCoords);
    void setLastLightCoords(BlockPos lastLightCoords);
    void setBirthTime(long birthTime);
    void setLifespan(long lifespan);

    public boolean updateLightCoords();
    public void updateLightBlocks();

    default public boolean canLiveInFluid() {
        return false;
    }

    public @NotNull Block getFlameBlock();

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
        Vec3d spawnPos = coords;
        if (!level.getBlockState(new BlockPos(coords)).isAir()) {
            // test to the left
            switch (direction) {
                default:
                case NORTH:
                    if (level.getBlockState(new BlockPos(coords.add(-1, 0, 0))).isAir()) return coords.add(-1, 0, 0);
                case SOUTH:
                    if (level.getBlockState(new BlockPos(coords.add(1, 0, 0))).isAir()) return coords.add(1, 0, 0);
                case EAST :
                    if (level.getBlockState(new BlockPos(coords.add(0, 0, -1))).isAir()) return coords.add(0, 0, -1);
                case WEST:
                    if (level.getBlockState(new BlockPos(coords.add(0, 0, 1))).isAir()) return coords.add(0, 0, 1);
            };

            // test to the left+down
            switch (direction) {
                default:
                case NORTH:
                    if (level.getBlockState(new BlockPos(coords.add(-1, -1, 0))).isAir()) return coords.add(-1, -1, 0);
                case SOUTH:
                    if (level.getBlockState(new BlockPos(coords.add(1, -1, 0))).isAir()) return coords.add(1, -1, 0);
                case EAST :
                    if (level.getBlockState(new BlockPos(coords.add(0, -1, -1))).isAir()) return coords.add(0, -1, -1);
                case WEST:
                    if (level.getBlockState(new BlockPos(coords.add(0, -1, 1))).isAir()) return coords.add(0, -1, 1);
            };

            // test behind
            switch (direction) {
                default:
                case NORTH:
                    if (level.getBlockState(new BlockPos(coords.add(0, 0, 1))).isAir()) return coords.add(0, 0, 1);
                case SOUTH:
                    if (level.getBlockState(new BlockPos(coords.add(0, 0, -1))).isAir()) return coords.add(0, 0, -1);
                case EAST :
                    if (level.getBlockState(new BlockPos(coords.add(-1, 0, 0))).isAir()) return coords.add(-1, 0, 0);
                case WEST:
                    if (level.getBlockState(new BlockPos(coords.add(1, 0, 0))).isAir()) return coords.add(1, 0, 0);
            };

            // test down
            if (level.getBlockState(new BlockPos(coords.add(0, 1, 0))).isAir()) return coords.add(0, 1, 0);

            // test right
            switch (direction) {
                default:
                case NORTH:
                    if (level.getBlockState(new BlockPos(coords.add(1, 0, 0))).isAir()) return coords.add(1, 0, 0);
                case SOUTH:
                    if (level.getBlockState(new BlockPos(coords.add(-1, 0, 0))).isAir()) return coords.add(-1, 0, 0);
                case EAST :
                    if (level.getBlockState(new BlockPos(coords.add(0, 0, 1))).isAir()) return coords.add(0, 0, 1);
                case WEST:
                    if (level.getBlockState(new BlockPos(coords.add(0, 0, -1))).isAir()) return coords.add(0, 0, -1);
            };

            // test right+down
            switch (direction) {
                default:
                case NORTH:
                    if (level.getBlockState(new BlockPos(coords.add(1, -1, 0))).isAir()) return coords.add(1, -1, 0);
                case SOUTH:
                    if (level.getBlockState(new BlockPos(coords.add(-1, -1, 0))).isAir()) return coords.add(-1, -1, 0);
                case EAST :
                    if (level.getBlockState(new BlockPos(coords.add(0, -1, 1))).isAir()) return coords.add(0, -1, 1);
                case WEST:
                    if (level.getBlockState(new BlockPos(coords.add(0, -1, -1))).isAir()) return coords.add(0, -1, -1);
            };
        }
        return spawnPos;
    }
}
