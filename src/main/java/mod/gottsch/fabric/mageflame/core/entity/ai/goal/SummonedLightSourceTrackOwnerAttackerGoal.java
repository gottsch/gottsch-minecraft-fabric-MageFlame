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

import java.util.EnumSet;

import mod.gottsch.fabric.mageflame.core.entity.creature.SummonedLightSourcePathAwareEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.passive.TameableEntity;

/**
 * Created by Mark Gottschling on 1/10/2025
 */
public class SummonedLightSourceTrackOwnerAttackerGoal extends TrackTargetGoal {
    private final SummonedLightSourcePathAwareEntity lightSourceEntity;
    private LivingEntity attacker;
    private int lastAttackedTime;

    public SummonedLightSourceTrackOwnerAttackerGoal(SummonedLightSourcePathAwareEntity lightSourceEntity) {
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
            this.attacker = livingEntity.getAttacker();
            int i = livingEntity.getLastAttackedTime();
            return i != this.lastAttackedTime && this.canTrack(this.attacker, TargetPredicate.DEFAULT) && this.lightSourceEntity.canAttackWithOwner(this.attacker, livingEntity);
        }
    }

    @Override
    public void start() {
        this.mob.setTarget(this.attacker);
        LivingEntity livingEntity = this.lightSourceEntity.getOwner();
        if (livingEntity != null) {
            this.lastAttackedTime = livingEntity.getLastAttackedTime();
        }

        super.start();
    }
}
