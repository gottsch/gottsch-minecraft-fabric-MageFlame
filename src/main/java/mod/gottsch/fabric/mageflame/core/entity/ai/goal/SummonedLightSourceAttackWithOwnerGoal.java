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
package mod.gottsch.fabric.mageflame.core.entity.ai.goal;

import mod.gottsch.fabric.mageflame.core.entity.creature.SummonedPathAwareEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;

import java.util.EnumSet;

/**
 * Created by Mark Gottschling on 1/10/2025
 */
public class SummonedLightSourceAttackWithOwnerGoal extends TrackTargetGoal {
    private final SummonedPathAwareEntity lightSourceEntity;
    private LivingEntity attacking;
    private int lastAttackTime;

    public SummonedLightSourceAttackWithOwnerGoal(SummonedPathAwareEntity lightSourceEntity) {
        super(lightSourceEntity, false);
        this.lightSourceEntity = lightSourceEntity;
        this.setControls(EnumSet.of(Goal.Control.TARGET));
    }

    @Override
    public boolean canStart() {
        LivingEntity livingEntity = this.lightSourceEntity.getOwner();
        if (livingEntity == null) {
            return false;
        } else {
            this.attacking = livingEntity.getAttacking();
            int i = livingEntity.getLastAttackTime();
            return i != this.lastAttackTime && this.canTrack(this.attacking, TargetPredicate.DEFAULT) && this.lightSourceEntity.canAttackWithOwner(this.attacking, livingEntity);
        }
    }

    @Override
    public void start() {
        this.mob.setTarget(this.attacking);
        LivingEntity livingEntity = this.lightSourceEntity.getOwner();
        if (livingEntity != null) {
            this.lastAttackTime = livingEntity.getLastAttackTime();
        }
        super.start();
    }
}
