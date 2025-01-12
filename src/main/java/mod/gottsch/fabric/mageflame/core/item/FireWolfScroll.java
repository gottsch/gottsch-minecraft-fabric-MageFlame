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

import mod.gottsch.fabric.mageflame.core.entity.creature.FireWolfEntity;
import mod.gottsch.fabric.mageflame.core.entity.creature.ISummonedLightSourceEntity;
import mod.gottsch.fabric.mageflame.core.registry.SummonFlameRegistry;
import mod.gottsch.fabric.mageflame.core.setup.Registration;
import mod.gottsch.fabric.mageflame.core.util.LangUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.Spawner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;
import java.util.Objects;
import java.util.UUID;


/**
 * 
 * @author Mark Gottschling Jan 11, 2025
 *
 */
public class FireWolfScroll extends SummonFlameBaseItem {

	public FireWolfScroll(Settings properties) {

		super(properties);
	}
	
	public EntityType<? extends MobEntity> getSummonFlameEntity() {

		return Registration.FIRE_WOLF_ENTITY;
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		if (!(world instanceof ServerWorld)) {
			return ActionResult.SUCCESS;
		} else {
			ItemStack itemStack = context.getStack();
			BlockPos blockPos = context.getBlockPos();
			Direction direction = context.getSide();
			BlockState blockState = world.getBlockState(blockPos);

				BlockPos blockPos2;
				if (blockState.getCollisionShape(world, blockPos).isEmpty()) {
					blockPos2 = blockPos;
				} else {
					blockPos2 = blockPos.offset(direction);
				}

				EntityType<?> entityType = Registration.FIRE_WOLF_ENTITY;
				Entity mob = entityType.spawnFromItemStack((ServerWorld)world, itemStack, context.getPlayer(), blockPos2, SpawnReason.MOB_SUMMONED, true, !Objects.equals(blockPos, blockPos2) && direction == Direction.UP);
				if (mob != null) {
					itemStack.decrement(1);
					world.emitGameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockPos);

					PlayerEntity owner = context.getPlayer();
					((ISummonedLightSourceEntity)mob).setOwner(owner);
					if (SummonFlameRegistry.isRegistered(owner.getUuid())) {
						// unregister existing entity for player
						UUID existingUuid = SummonFlameRegistry.unregister(owner.getUuid());
						// MageFlame.LOGGER.debug("owner is registered to entity -> {}", existingUuid.toString());
						Entity existingMob = ((ServerWorld)world).getEntity(existingUuid);
						if (existingMob != null) {
							// MageFlame.LOGGER.debug("located and killing exisiting entity -> {}", existingUuid.toString());
							((ISummonedLightSourceEntity)existingMob).kill();
						}
					}

					// cast effects
					for (int p = 0; p < 20; p++) {
						double xSpeed = world.random.nextGaussian() * 0.02D;
						double ySpeed = world.random.nextGaussian() * 0.02D;
						double zSpeed = world.random.nextGaussian() * 0.02D;

						world.addParticle(ParticleTypes.POOF, owner.getX(), owner.getY() + 0.5, owner.getZ(), xSpeed, ySpeed, zSpeed);
					}
				}

				return ActionResult.CONSUME;

		}
	}

	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		BlockHitResult blockHitResult = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
		if (blockHitResult.getType() != HitResult.Type.BLOCK) {
			return TypedActionResult.pass(itemStack);
		} else if (!(world instanceof ServerWorld)) {
			return TypedActionResult.success(itemStack);
		} else {
			BlockPos blockPos = blockHitResult.getBlockPos();
			if (!(world.getBlockState(blockPos).getBlock() instanceof FluidBlock)) {
				return TypedActionResult.pass(itemStack);
			} else if (world.canPlayerModifyAt(user, blockPos) && user.canPlaceOn(blockPos, blockHitResult.getSide(), itemStack)) {
				EntityType<?> entityType = Registration.FIRE_WOLF_ENTITY;
				Entity entity = entityType.spawnFromItemStack((ServerWorld)world, itemStack, user, blockPos, SpawnReason.SPAWN_EGG, false, false);
				if (entity == null) {
					return TypedActionResult.pass(itemStack);
				} else {
					itemStack.decrementUnlessCreative(1, user);
					user.incrementStat(Stats.USED.getOrCreateStat(this));
					world.emitGameEvent(user, GameEvent.ENTITY_PLACE, entity.getPos());
					return TypedActionResult.consume(itemStack);
					// TODO add registration etc
				}
			} else {
				return TypedActionResult.fail(itemStack);
			}
		}
	}

		@Override
	public Vec3d selectSpawnPos(World level, Vec3d coords, Direction direction) {
		// TODO get the ground pos - look at EggItem
		BlockPos spawnPos = new BlockPos((int)coords.x, (int)coords.y, (int)coords.z);
		if (level.getBlockState(spawnPos.up()).isAir()) {
			coords = coords.add(0, 1, 0);
		} else {
			spawnPos.offset(direction.getOpposite());
			coords = new Vec3d(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
		}

		return coords;
	}

	@Override
	public void appendBaseText(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		
		tooltip.add(Text.translatable(LangUtil.tooltip("fire_wolf.desc")).formatted(Formatting.YELLOW));
		tooltip.add(Text.literal(" "));
		tooltip.add(Text.translatable(LangUtil.tooltip("light_level"), 14));
	}

	@Override
	public void appendAdvancedText(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		appendLore(stack, context, tooltip, "fire_wolf.lore");
	}
}
