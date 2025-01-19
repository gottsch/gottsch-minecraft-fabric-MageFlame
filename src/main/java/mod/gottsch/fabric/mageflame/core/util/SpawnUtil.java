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
package mod.gottsch.fabric.mageflame.core.util;

import mod.gottsch.fabric.gottschcore.spatial.Coords;
import mod.gottsch.fabric.gottschcore.spatial.ICoords;
import mod.gottsch.fabric.mageflame.MageFlame;
import mod.gottsch.fabric.mageflame.core.entity.creature.ISummonedEntity;
import mod.gottsch.fabric.mageflame.core.peristence.PlayerData;
import mod.gottsch.fabric.mageflame.core.peristence.StateSaverAndLoader;
import mod.gottsch.fabric.mageflame.core.peristence.SummonedEntityData;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

/**
 * Created by Mark Gottschling on 1/14/2025
 */
public class SpawnUtil {


    public static <T extends MobEntity & ISummonedEntity>Optional<?> spawnAtPos(ServerWorld world, Random random, LivingEntity owner, EntityType<T> entityType, Vec3d coords) {
        return spawnAtPos(world, random, owner, entityType, Coords.of((int)coords.x, (int)coords.y, (int)coords.z));
    }

    /**
     * use this version when you know the exact location to spawn.
     * ex. when joining world, and loading entities from persistence.
     */
    public static <T extends MobEntity & ISummonedEntity>Optional<?> spawnAtPos(ServerWorld world, Random random, LivingEntity owner, EntityType<T> entityType, ICoords coords) {

        if (!world.isClient) {
            BlockPos spawnPos = coords.toPos();

            // determine if the entity can spawn
            if(SpawnRestriction.canSpawn(entityType, world, SpawnReason.SPAWNER, spawnPos, world.getRandom())) {
                // create entity
                MobEntity mob = entityType.create(world);
                if (mob != null) {
                    mob.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
                    ((ISummonedEntity)mob).setOwner(owner);

                    register(world, entityType, mob, owner);

                    // add entity into the world (ie EntityJoinWorldEvent)
                    world.spawnEntityAndPassengers(mob);

                    return Optional.of(mob);
                }
            }
        }
        return Optional.empty();
    }

    /**
     *
     * @param world
     * @param entityType
     * @param mob
     * @param owner
     * @param <T>
     */
    public static <T extends MobEntity & ISummonedEntity> void register(ServerWorld world, EntityType<T> entityType, MobEntity mob, LivingEntity owner) {
        PlayerData playerData = StateSaverAndLoader.getPlayerState(owner);
        if (playerData.getKeys().size() >= MageFlame.CONFIG.maxSummonedEntitiesPerPlayer()) {
            cullSummonedEntities(world, playerData);
        }

        // register entity to player
        SummonedEntityData summonedEntityData = new SummonedEntityData();
        summonedEntityData.setId(mob.getUuid());
        summonedEntityData.setEntityType(entityType);
        summonedEntityData.setLifespan(((ISummonedEntity) mob).getLifespan());
        summonedEntityData.setCreateTime(world.getTime());
        playerData.register(mob.getUuid(), summonedEntityData);

        // redundant
        StateSaverAndLoader.getServerState(world.getServer()).markDirty();
    }

    public static void killAllSummonedEntities(ServerWorld world, PlayerEntity player) {
        killAllSummonedEntities(world, StateSaverAndLoader.getPlayerState(player));
    }

    public static void killAllSummonedEntities(ServerWorld world, PlayerData playerData) {
        playerData.getValues().forEach(summonedEntityData -> {
            Entity worldEntity = world.getEntity(summonedEntityData.getId());
            if (worldEntity != null) {
                worldEntity.kill();
            }
            playerData.unregister(summonedEntityData.getId());
        });
    }

    public static void cullSummonedEntities(ServerWorld world, PlayerData playerData) {
        /*
         * kill and unregister with least lifespan remaining entities for registry until number <= maxEntities
         */
        List<SummonedEntityData> summonedEntityDataList = playerData.getValues();
        // update data with lifespan of actual in game entities
        summonedEntityDataList.forEach(summonedEntityData -> {
            ISummonedEntity worldEntity = (ISummonedEntity) world.getEntity(summonedEntityData.getId());
            if (worldEntity != null) {
                summonedEntityData.setLifespan(worldEntity.getLifespan());
            }
        });
        // sort the data
        summonedEntityDataList.sort(SummonedEntityData.lifespanComparator);

        while (summonedEntityDataList.size() >= MageFlame.CONFIG.maxSummonedEntitiesPerPlayer()) {
            Entity worldEntity = world.getEntity(summonedEntityDataList.getFirst().getId());
            if (worldEntity != null) {
                worldEntity.kill();
            }
            summonedEntityDataList.removeFirst();
        }
    }


    public static Vec3d selectSpawnPos(World world, Vec3d coords, Direction direction) {

        if (!world.getBlockState(new BlockPos(vec3ToBlockPos(coords))).isAir()) {
            // test to the left
            switch (direction) {
                default:
                case NORTH:
                    if (world.getBlockState(vec3ToBlockPos(coords.add(-1, 0, 0))).isAir()) coords.add(-1, 0, 0);
                case SOUTH:
                    if (world.getBlockState(vec3ToBlockPos(coords.add(1, 0, 0))).isAir()) coords.add(1, 0, 0);
                case EAST :
                    if (world.getBlockState(vec3ToBlockPos(coords.add(0, 0, -1))).isAir()) coords.add(0, 0, -1);
                case WEST:
                    if (world.getBlockState(vec3ToBlockPos(coords.add(0, 0, 1))).isAir()) coords.add(0, 0, 1);
            };

            // test to the left+down
            switch (direction) {
                default:
                case NORTH:
                    if (world.getBlockState(vec3ToBlockPos(coords.add(-1, -1, 0))).isAir()) coords.add(-1, -1, 0);
                case SOUTH:
                    if (world.getBlockState(vec3ToBlockPos(coords.add(1, -1, 0))).isAir()) coords.add(1, -1, 0);
                case EAST :
                    if (world.getBlockState(vec3ToBlockPos(coords.add(0, -1, -1))).isAir()) coords.add(0, -1, -1);
                case WEST:
                    if (world.getBlockState(vec3ToBlockPos(coords.add(0, -1, 1))).isAir()) coords.add(0, -1, 1);
            };

            // test behind
            switch (direction) {
                default:
                case NORTH:
                    if (world.getBlockState(vec3ToBlockPos(coords.add(0, 0, 1))).isAir()) coords.add(0, 0, 1);
                case SOUTH:
                    if (world.getBlockState(vec3ToBlockPos(coords.add(0, 0, -1))).isAir()) coords.add(0, 0, -1);
                case EAST :
                    if (world.getBlockState(vec3ToBlockPos(coords.add(-1, 0, 0))).isAir()) coords.add(-1, 0, 0);
                case WEST:
                    if (world.getBlockState(vec3ToBlockPos(coords.add(1, 0, 0))).isAir()) coords.add(1, 0, 0);
            };

            // test down
            if (world.getBlockState(vec3ToBlockPos(coords.add(0, 1, 0))).isAir()) coords.add(0, 1, 0);

            // test right
            switch (direction) {
                default:
                case NORTH:
                    if (world.getBlockState(vec3ToBlockPos(coords.add(1, 0, 0))).isAir()) coords.add(1, 0, 0);
                case SOUTH:
                    if (world.getBlockState(vec3ToBlockPos(coords.add(-1, 0, 0))).isAir()) coords.add(-1, 0, 0);
                case EAST :
                    if (world.getBlockState(vec3ToBlockPos(coords.add(0, 0, 1))).isAir()) coords.add(0, 0, 1);
                case WEST:
                    if (world.getBlockState(vec3ToBlockPos(coords.add(0, 0, -1))).isAir()) coords.add(0, 0, -1);
            };

            // test right+down
            switch (direction) {
                default:
                case NORTH:
                    if (world.getBlockState(vec3ToBlockPos(coords.add(1, -1, 0))).isAir()) coords.add(1, -1, 0);
                case SOUTH:
                    if (world.getBlockState(vec3ToBlockPos(coords.add(-1, -1, 0))).isAir()) coords.add(-1, -1, 0);
                case EAST :
                    if (world.getBlockState(vec3ToBlockPos(coords.add(0, -1, 1))).isAir()) coords.add(0, -1, 1);
                case WEST:
                    if (world.getBlockState(vec3ToBlockPos(coords.add(0, -1, -1))).isAir()) coords.add(0, -1, -1);
            };
        }
        return coords;
    }

    public static BlockPos vec3ToBlockPos(Vec3d vec3) {
        return new BlockPos((int)vec3.x, (int)vec3.y, (int)vec3.z);
    }

    public static Vec3d getByPlayerPos(PlayerEntity player) {
        Vec3d eyePos = player.getEyePos();
        Direction direction = player.getMovementDirection();
        return switch (direction) {
            case NORTH -> eyePos.add(new Vec3d(0.5, 0, 0.35));
            case SOUTH -> eyePos.add(new Vec3d(-0.5, 0, -0.35));
            case EAST -> eyePos.add(new Vec3d(-0.35, 0, 0.5));
            case WEST -> eyePos.add(new Vec3d(0.35, 0, -0.5));
            default -> eyePos.add(new Vec3d(0.5, 0, 0.35));
        };
    }


}
