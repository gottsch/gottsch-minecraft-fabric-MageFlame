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

import mod.gottsch.fabric.mageflame.MageFlame;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Mark Gottschling on 1/12/2025
 */
public class StateSaverAndLoader extends PersistentState {
    private static final String REGISTRY_TAG = "mageFlameRegistry";

    public Map<UUID, PlayerData> players = new HashMap<>();

    public static PlayerData getPlayerState(LivingEntity player) {
        StateSaverAndLoader serverState = getServerState(player.getWorld().getServer());

        // Either get the player by the uuid, or we don't have data for him yet, make a new player state
        return serverState.players.computeIfAbsent(player.getUuid(), uuid -> new PlayerData());
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        MageFlame.LOGGER.info("saving persistent data...");
MageFlame.LOGGER.info("player data to be persisted -> {}", players);
        NbtCompound playersNbt = new NbtCompound();
        players.forEach((key, value) -> {
            NbtCompound playerNbt = new NbtCompound();

            value.writeNbt(playerNbt);

            playersNbt.put(key.toString(), playerNbt);
        });
        nbt.put("players", playersNbt);
        MageFlame.LOGGER.info("players NBT data -> {}", playersNbt);
//        // write registry to persistent state
//        NbtList tag = new NbtList();
//        SummonedLightSourceRegistry.REGISTRY.forEach((k,v) -> {
//            MageFlame.LOGGER.info("saving persistent entity -> {}, mob -> {}", k.toString(), v.toString());
//            NbtCompound entry = new NbtCompound();
//            entry.put("id", NbtString.of(k.toString()));
//            entry.put("value", NbtString.of(v.toString()));
//
//            tag.add(entry);
//        });
//        nbt.put(REGISTRY_TAG, tag);

        return nbt;
    }

    public static StateSaverAndLoader load(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        StateSaverAndLoader state = new StateSaverAndLoader();

        MageFlame.LOGGER.info("loading persistent data ie players");
        NbtCompound playersNbt = nbt.getCompound("players");
        playersNbt.getKeys().forEach(playerUuid -> {
            MageFlame.LOGGER.info("loading player -> {}", playerUuid);
            PlayerData playerData = new PlayerData();
            NbtCompound data = playersNbt.getCompound(playerUuid);
            MageFlame.LOGGER.info("player compound nbt -> {}", data);
            playerData.loadNbt(data);
            MageFlame.LOGGER.info("player data -> {}", playerData);
            state.players.put(UUID.fromString(playerUuid), playerData);
        });
//        if (nbt.contains(REGISTRY_TAG)) {
//            try {
//                NbtList tag = nbt.getList(REGISTRY_TAG, 10);
//                for (int i = 0; i < tag.size(); i++) {
//                    NbtCompound entry = tag.getCompound(i);
//                    MageFlame.LOGGER.info("loading persistent entity -> {}, mob -> {}", UUID.fromString(entry.getString("id")), UUID.fromString(entry.getString("value")));
//                    SummonedLightSourceRegistry.register(UUID.fromString(entry.getString("id")),
//                            UUID.fromString(entry.getString("value")));
//                }
//            }
//            catch(Exception e) {
//                MageFlame.LOGGER.error("error", e);
//            }
//        }
        return state;
    }

    private static Type<StateSaverAndLoader> type = new Type<>(
            StateSaverAndLoader::new, // If there's no 'StateSaverAndLoader' yet create one
            StateSaverAndLoader::load, // If there is a 'StateSaverAndLoader' NBT, parse it with 'createFromNbt'
            null // Supposed to be an 'DataFixTypes' enum, but we can just pass null
    );

    public static StateSaverAndLoader getServerState(MinecraftServer server) {
        // (Note: arbitrary choice to use 'World.OVERWORLD' instead of 'World.END' or 'World.NETHER'.  Any work)
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();

        // The first time the following 'getOrCreate' function is called, it creates a brand new 'StateSaverAndLoader' and
        // stores it inside the 'PersistentStateManager'. The subsequent calls to 'getOrCreate' pass in the saved
        // 'StateSaverAndLoader' NBT on disk to our function 'StateSaverAndLoader::createFromNbt'.
        StateSaverAndLoader state = persistentStateManager.getOrCreate(type, MageFlame.MOD_ID);

        // If state is not marked dirty, when Minecraft closes, 'writeNbt' won't be called and therefore nothing will be saved.
        // Technically it's 'cleaner' if you only mark state as dirty when there was actually a change, but the vast majority
        // of mod writers are just going to be confused when their data isn't being saved, and so it's best just to 'markDirty' for them.
        // Besides, it's literally just setting a bool to true, and the only time there's a 'cost' is when the file is written to disk when
        // there were no actual change to any of the mods state (INCREDIBLY RARE).
        state.markDirty();

        return state;
    }
}
