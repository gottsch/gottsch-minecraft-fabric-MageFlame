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
package mod.gottsch.fabric.mageflame.core.peristence;

import mod.gottsch.fabric.gottschcore.spatial.ICoords;
import mod.gottsch.fabric.mageflame.core.entity.creature.ISummonedEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;

import java.util.Comparator;
import java.util.UUID;

/**
 * Created by Mark Gottschling on 1/14/2025
 */
public class SummonedEntityData {
    private UUID id;
    private EntityType entityType;
    private int lifespan; // in ticks
    private long createTime; // in ticks
    private ICoords coords;

    public static Comparator<SummonedEntityData> lifespanComparator = new Comparator<SummonedEntityData>() {
        @Override
        public int compare(SummonedEntityData p1, SummonedEntityData p2) {
            return Integer.compare(p1.getLifespan(), p2.getLifespan());
        }
    };

    /**
     *
     */
    public SummonedEntityData() {}

    public <T extends MobEntity & ISummonedEntity> SummonedEntityData(UUID id, EntityType<T> entityType, int lifespan, long createTime) {
        this.id = id;
        this.entityType = entityType;
        this.lifespan = lifespan;
        this.createTime = createTime;
    }

    public <T extends MobEntity & ISummonedEntity> SummonedEntityData(UUID id, EntityType<T> entityType, int lifespan, long createTime, ICoords coords) {
        this(id, entityType, lifespan, createTime);
        this.coords = coords;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getLifespan() {
        return lifespan;
    }

    public void setLifespan(int lifespan) {
        this.lifespan = lifespan;
    }

    public <T extends MobEntity & ISummonedEntity> EntityType<T> getEntityType() {
        return entityType;
    }

    public <T extends MobEntity & ISummonedEntity> void setEntityType(EntityType<T> entityType) {
        this.entityType = entityType;
    }

    public ICoords getCoords() {
        return coords;
    }

    public void setCoords(ICoords coords) {
        this.coords = coords;
    }

    @Override
    public String toString() {
        return "SummonedEntityData{" +
                "id=" + id +
                ", entityType=" + entityType +
                ", lifespan=" + lifespan +
                ", createTime=" + createTime +
                ", coords=" + coords +
                '}';
    }
}
