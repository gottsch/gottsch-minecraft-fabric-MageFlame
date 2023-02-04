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
package mod.gottsch.fabric.mageflame.core.item;

import mod.gottsch.fabric.mageflame.MageFlame;
import mod.gottsch.fabric.mageflame.core.entity.creature.ISummonFlameEntity;
import mod.gottsch.fabric.mageflame.core.entity.creature.SummonFlameBaseEntity;
import mod.gottsch.fabric.mageflame.core.registry.SummonFlameRegistry;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

/**
 * 
 * @author Mark Gottschling Jan 20, 2023
 *
 */
public interface ISummonFlameItem {

	EntityType<? extends MobEntity> getSummonFlameEntity();

	/**
	 * 
	 * @param player
	 * @return
	 */
	default public Vec3d getByPlayerPos(PlayerEntity player) {
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

	/**
	 * 
	 * @param level
	 * @param random
	 * @param owner
	 * @param entityType
	 * @param coords
	 * @return
	 */
	default public Optional<MobEntity> spawn(ServerWorld level, Random random, LivingEntity owner, EntityType<? extends MobEntity> entityType, Vec3d coords) {
		Direction direction = owner.getMovementDirection();

		if (!level.isClient) {
			// select the first available spawn pos from origin (coords)
			Vec3d spawnPos = selectSpawnPos(level, coords, direction);
			// MageFlame.LOGGER.debug("attempting to spawn summon flame at -> {} ...", spawnPos);


			// determine if the entity can spawn
			if(SpawnRestriction.canSpawn(entityType, level, SpawnReason.SPAWNER, new BlockPos(coords), level.getRandom())) {
				// MageFlame.LOGGER.debug("placement is good");
				// create entity
				MobEntity mob = entityType.create(level);
				if (mob != null) {
					// MageFlame.LOGGER.debug("new entity is created -> {}", mob.getUuidAsString());
					mob.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
					((ISummonFlameEntity)mob).setOwner(owner);
					
					// MageFlame.LOGGER.debug("is owner registered -> {}", SummonFlameRegistry.isRegistered(owner.getUuid()));
					// check and remove existing owner's entity, regardless if existing entity is located
					if (SummonFlameRegistry.isRegistered(owner.getUuid())) {
						// unregister existing entity for player
						UUID existingUuid = SummonFlameRegistry.unregister(owner.getUuid());
						// MageFlame.LOGGER.debug("owner is registered to entity -> {}", existingUuid.toString());
						Entity existingMob = level.getEntity(existingUuid);
						if (existingMob != null) {
							// MageFlame.LOGGER.debug("located and killing exisiting entity -> {}", existingUuid.toString());
							((SummonFlameBaseEntity)existingMob).kill();
						}
					}

					// registry entity
					// MageFlame.LOGGER.debug("registering entity -> {} to owner -> {}", mob.getUuidAsString(), owner.getUuidAsString());
					SummonFlameRegistry.register(owner.getUuid(), mob.getUuid());
					
					// add entity into the level (ie EntityJoinWorldEvent)
					level.spawnEntityAndPassengers(mob);

					// cast effects
					for (int p = 0; p < 20; p++) {
						double xSpeed = random.nextGaussian() * 0.02D;
						double ySpeed = random.nextGaussian() * 0.02D;
						double zSpeed = random.nextGaussian() * 0.02D;

						level.addParticle(ParticleTypes.POOF, owner.getX(), owner.getY() + 0.5, owner.getZ(), xSpeed, ySpeed, zSpeed);
					}
					
					return Optional.of(mob);
				}
			}
		}
		return Optional.empty();
	}

	/**
	 * TODO this might need to move to a Util 
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
