package mod.gottsch.fabric.mageflame.core.util;

import mod.gottsch.fabric.gottschcore.spatial.Coords;
import mod.gottsch.fabric.gottschcore.spatial.ICoords;
import mod.gottsch.fabric.mageflame.MageFlame;
import mod.gottsch.fabric.mageflame.core.peristence.SummonedEntityData;
import mod.gottsch.fabric.mageflame.core.entity.creature.ISummonedEntity;
import mod.gottsch.fabric.mageflame.core.peristence.PlayerData;
import mod.gottsch.fabric.mageflame.core.peristence.StateSaverAndLoader;

import net.minecraft.entity.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.Optional;

/**
 * Created by Mark Gottschling on 1/14/2025
 */
public class SpawnUtil {


    public static <T extends MobEntity & ISummonedEntity>Optional<?> spawnAtPos(ServerWorld level, Random random, LivingEntity owner, EntityType<T> entityType, Vec3d coords) {
        return spawnAtPos(level, random, owner, entityType, Coords.of((int)coords.x, (int)coords.y, (int)coords.z));
    }

    /**
     * use this version when you know the exact location to spawn.
     * ex. when joining world, and loading entities from persistence.
     */
    public static <T extends MobEntity & ISummonedEntity>Optional<?> spawnAtPos(ServerWorld level, Random random, LivingEntity owner, EntityType<T> entityType, ICoords coords) {

        if (!level.isClient) {
            BlockPos spawnPos = coords.toPos();

            // determine if the entity can spawn
            if(SpawnRestriction.canSpawn(entityType, level, SpawnReason.SPAWNER, spawnPos, level.getRandom())) {
                // create entity
                MobEntity mob = entityType.create(level);
                if (mob != null) {
                    mob.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
                    ((ISummonedEntity)mob).setOwner(owner);

                    register(level, entityType, mob, owner);
                    // TODO check if # of entities is exceeded and remove oldest

//                    // this is for 1 entity rule
//                    // remove previous entities
//                    PlayerData playerData = StateSaverAndLoader.getPlayerState(owner);
//                    if (!playerData.getKeys().isEmpty()) {
//                        playerData.getKeys().forEach(key -> {
//                            MageFlame.LOGGER.info("found exisiting entity in player data -> {}", key.toString());
//
//                            playerData.get(key).ifPresent(entityData -> {
//                                Entity worldEntity = level.getEntity(key);
//                                worldEntity.kill();
//                            });
//                        });
//                        playerData.clear();
//                    }
//
//                    // register entity to player
//                    SummonedEntityData summonedEntityData = new SummonedEntityData();
//                    summonedEntityData.setEntityType(entityType);
//                    summonedEntityData.setLifespan(((ISummonedEntity)mob).getLifespan());
//                    summonedEntityData.setCreateTime(level.getTime());
//                    playerData.register(mob.getUuid(), summonedEntityData);
//
//                    // redundant
//                    StateSaverAndLoader.getServerState(level.getServer()).markDirty();

                    // add entity into the level (ie EntityJoinWorldEvent)
                    level.spawnEntityAndPassengers(mob);

                    return Optional.of(mob);
                }
            }
        }
        return Optional.empty();
    }

//    // TODO probably deprecated
//    @Deprecated
//    public static <T extends MobEntity & ISummonedEntity> Optional<MobEntity> spawnSummonedFlyingEntity(ServerWorld level, Random random, LivingEntity owner, EntityType<T> entityType, Vec3d coords) {
//        Direction direction = owner.getMovementDirection();
//
//        if (!level.isClient) {
//            // select the first available spawn pos from origin (coords)
//            Vec3d spawnVec3 = selectSpawnPos(level, coords, direction);
//            BlockPos spawnPos = new BlockPos((int)spawnVec3.x, (int)spawnVec3.y, (int)spawnVec3.z);
//            // MageFlame.LOGGER.info("attempting to spawn summon flame at -> {} ...", spawnPos);
//
//            // determine if the entity can spawn
//            if(SpawnRestriction.canSpawn(entityType, level, SpawnReason.SPAWNER, spawnPos, level.getRandom())) {
//                // MageFlame.LOGGER.info("placement is good");
//                // create entity
//                MobEntity mob = entityType.create(level);
//                if (mob != null) {
//                    // MageFlame.LOGGER.info("new entity is created -> {}", mob.getUuidAsString());
//                    mob.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
//                    ((ISummonedEntity)mob).setOwner(owner);
//
//                    // MageFlame.LOGGER.info("is owner registered -> {}", SummonFlameRegistry.isRegistered(owner.getUuid()));
//                    // check and remove existing owner's entity, regardless if existing entity is located
//
//                    register(level, entityType, mob, owner);
////                    // this is for 1 entity rule
////                    // remove previous entities
////                    PlayerData playerData = StateSaverAndLoader.getPlayerState(owner);
////                    if (!playerData.getKeys().isEmpty()) {
////                        playerData.getKeys().forEach(key -> {
////                            MageFlame.LOGGER.info("found exisiting entity in player data -> {}", key.toString());
////
////                            playerData.get(key).ifPresent(entityData -> {
////                                Entity worldEntity = level.getEntity(key);
////                                worldEntity.kill();
////                            });
////                        });
////                        playerData.clear();
////                    }
////
////                    // register entity to player
////                    SummonedEntityData summonedEntityData = new SummonedEntityData();
////                    summonedEntityData.setEntityType(entityType);
////                    summonedEntityData.setLifespan(((ISummonedEntity)mob).getLifespan());
////                    summonedEntityData.setCreateTime(level.getTime());
////                    playerData.register(mob.getUuid(), summonedEntityData);
////
////                    // redundant
////                    StateSaverAndLoader.getServerState(level.getServer()).markDirty();
//
//                    // add entity into the level (ie EntityJoinWorldEvent)
//                    level.spawnEntityAndPassengers(mob);
//
//                    return Optional.of(mob);
//                }
//            }
//        }
//        return Optional.empty();
//    }

    public static <T extends MobEntity & ISummonedEntity> void register(ServerWorld world, EntityType<T> entityType, MobEntity mob, LivingEntity owner) {
        // this is for 1 entity rule
        // remove previous entities
        PlayerData playerData = StateSaverAndLoader.getPlayerState(owner);
        if (!playerData.getKeys().isEmpty()) {
            playerData.getKeys().forEach(key -> {
                MageFlame.LOGGER.info("found exisiting entity in player data -> {}", key.toString());

                playerData.get(key).ifPresent(entityData -> {
                    Entity worldEntity = world.getEntity(key);
                    worldEntity.kill();
                });
            });
            playerData.clear();
        }

        // register entity to player
        SummonedEntityData summonedEntityData = new SummonedEntityData();
        summonedEntityData.setEntityType(entityType);
        summonedEntityData.setLifespan(((ISummonedEntity)mob).getLifespan());
        summonedEntityData.setCreateTime(world.getTime());
        playerData.register(mob.getUuid(), summonedEntityData);

        // redundant
        StateSaverAndLoader.getServerState(world.getServer()).markDirty();
    }

    public static Vec3d selectSpawnPos(World level, Vec3d coords, Direction direction) {

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
