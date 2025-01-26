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
package mod.gottsch.fabric.mageflame.core.peristence;

import com.google.common.collect.Maps;
import mod.gottsch.fabric.gottschcore.spatial.Coords;
import mod.gottsch.fabric.gottschcore.spatial.ICoords;
import mod.gottsch.fabric.mageflame.MageFlame;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.*;

/**
 * Created by Mark Gottschling on 1/14/2025
 */
public class PlayerData {
    /*
     * map of summoned entity data
     * key = entity.uuid
     */
    private final Map<UUID, SummonedEntityData> entityRegistry = Maps.newHashMap();

    public Optional<SummonedEntityData> register(UUID key, SummonedEntityData value) {
        if (key == null || value == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(entityRegistry.put(key, value));
    }

    public boolean isRegistered(UUID key) {
        if (entityRegistry.containsKey(key)) {
            return true;
        }
        return false;
    }

    public Optional<SummonedEntityData> unregister(UUID key) {
        if (key != null && isRegistered(key)) {
            return Optional.of(entityRegistry.remove(key));
        }
        return Optional.empty();
    }

    public Optional<SummonedEntityData> get(UUID key) {
        if (entityRegistry.containsKey(key)) {
            return Optional.of(entityRegistry.get(key));
        }
        return Optional.empty();
    }

    public Optional<SummonedEntityData> get(int index) {
        if (!entityRegistry.isEmpty() && index < entityRegistry.size()) {
            Object o = entityRegistry.values().toArray()[index];
        }
        return Optional.empty();
    }


    public void clear() {
        entityRegistry.clear();
    }

    public Map<UUID, SummonedEntityData> getDetachedRegistry() {
        Map<UUID, SummonedEntityData> map = Maps.newHashMap();
        map.putAll(entityRegistry);
        return map;
    }

    public NbtCompound writeNbt(NbtCompound nbt) {
        entityRegistry.forEach((key, value) -> {
            NbtCompound data = new NbtCompound();
            if (value.getId() != null) {
                data.putUuid("id", value.getId());
            }
            data.putString("type", Registries.ENTITY_TYPE.getId(value.getEntityType()).toString());
            data.putInt("lifespan", value.getLifespan());
            data.putLong("createTime", value.getCreateTime());
            NbtCompound coords = new NbtCompound();
            if (value.getCoords() != null && value.getCoords() != Coords.EMPTY) {
                data.put("coords", value.getCoords().save(coords));
            }
            nbt.put(key.toString(), data);
        });
        return nbt;
    }

    public void loadNbt(NbtCompound nbt) {
        nbt.getKeys().forEach(key -> {
            SummonedEntityData data = new SummonedEntityData();
            NbtCompound summonedNbt = nbt.getCompound(key);

            if (summonedNbt.contains("id")) {
                UUID id = summonedNbt.getUuid("id");
                data.setId(id);
            }
            if (summonedNbt.contains("type")) {
                EntityType entityType = Registries.ENTITY_TYPE.get(Identifier.of(summonedNbt.getString("type")));
                data.setEntityType(entityType);
            }
            if (summonedNbt.contains("lifespan")) {
                int lifespan = summonedNbt.getInt("lifespan");
                data.setLifespan(lifespan);
            }
            if (summonedNbt.contains("createTime")) {
                long createTime = summonedNbt.getLong("createTime");
                data.setCreateTime(createTime);
            }
            if (summonedNbt.contains("coords")) {
                ICoords coords = Coords.EMPTY.load(summonedNbt.getCompound("coords"));
                data.setCoords(coords);
            }

            UUID uuid = UUID.fromString(key);
            register(uuid, data);
        });
    }

    public List<UUID> getKeys() {
        return new ArrayList<>(entityRegistry.keySet());
    }

    public List<SummonedEntityData> getValues() {
        return new ArrayList<>(entityRegistry.values());
    }

    @Override
    public String toString() {
        return "PlayerData{" +
                "entityRegistry=" + entityRegistry +
                '}';
    }
}
