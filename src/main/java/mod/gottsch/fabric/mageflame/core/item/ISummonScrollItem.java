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

import mod.gottsch.fabric.gottschcore.spatial.ICoords;
import mod.gottsch.fabric.mageflame.core.entity.creature.ISummonedEntity;
import mod.gottsch.fabric.mageflame.core.util.LangUtil;
import mod.gottsch.fabric.mageflame.core.util.SpawnUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

/**
 * 
 * @author Mark Gottschling Jan 20, 2023
 *
 */
public interface ISummonScrollItem {

	<T extends MobEntity & ISummonedEntity> EntityType<T> getSummonFlameEntity();

	default public String ticksToTime(int ticks) {
		int secs = ticks / 20;
		int hours = secs / 3600;
		int remainder = secs % 3600;
		int minutes = remainder / 60;
		int seconds = remainder % 60;
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}

	default public void appendBaseText(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
	}

	default public void appendAdvancedText(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
	}

	default public void appendLore(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, String key) {
		MutableText lore = Text.translatable(LangUtil.tooltip(key));
		tooltip.add(Text.literal(" "));
		for (String s : lore.getString().split("~")) {
			tooltip.add(Text.translatable(LangUtil.INDENT2)
					.append(Text.literal(s).formatted(Formatting.GOLD, Formatting.ITALIC)));
		}
	}

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

	default public <T extends MobEntity & ISummonedEntity> Optional<?> spawn(ServerWorld level, Random random, LivingEntity owner, EntityType<T> entityType, ICoords coords) {
		return SpawnUtil.spawnAtPos(level, random, owner, entityType, coords);
	}

	default public void doCastEffects(World world, LivingEntity owner) {
		for (int p = 0; p < 20; p++) {
			double xSpeed = world.random.nextGaussian() * 0.02D;
			double ySpeed = world.random.nextGaussian() * 0.02D;
			double zSpeed = world.random.nextGaussian() * 0.02D;

			world.addParticle(ParticleTypes.POOF, owner.getX(), owner.getY() + 0.5, owner.getZ(), xSpeed, ySpeed, zSpeed);
		}
	}

	/**
	 * TODO this might need to move to a Util 
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

	private BlockPos vec3ToBlockPos(Vec3d vec3) {
		return new BlockPos((int)vec3.x, (int)vec3.y, (int)vec3.z);
	}
}
