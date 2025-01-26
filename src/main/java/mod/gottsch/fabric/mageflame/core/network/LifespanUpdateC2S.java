/*
 * This file is part of Mage Flame.
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
package mod.gottsch.fabric.mageflame.core.network;

import mod.gottsch.fabric.mageflame.MageFlame;
import mod.gottsch.fabric.mageflame.core.entity.creature.ISummonedEntity;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

/**
 * @author Mark Gottschling on 1/25/2025
 */
public record LifespanUpdateC2S(String uuid, int id) implements CustomPayload {
    public static final CustomPayload.Id<LifespanUpdateC2S> ID = new CustomPayload.Id<>(ModNetwork.LIFESPAN_UPDATE_C2S_ID);
    public static final PacketCodec<RegistryByteBuf, LifespanUpdateC2S> CODEC = PacketCodec.tuple(PacketCodecs.STRING, LifespanUpdateC2S::uuid,
            PacketCodecs.INTEGER, LifespanUpdateC2S::id,
            LifespanUpdateC2S::new);

    public static void receive(ServerPlayerEntity player, String uuid, int id) {
//        MageFlame.LOGGER.debug("server received packet: uuid ->{}, id -> {}", uuid, id);

        // get the entity by uuid
        UUID entityUuId = UUID.fromString(uuid);
        Entity entity = player.getServerWorld().getEntity(entityUuId);
        if (entity == null) {
            entity = player.getServerWorld().getEntityById(id);
        }
        if (entity instanceof ISummonedEntity) {
//                EEchelons.LOGGER.debug("setting entity -> {} to level -> {}", entity.getDisplayName().asString(), level);
//            ((ISummonedEntity)entity).setLifespan(lifespan);

            // send a message back to the client with the lifespan
            LifespanUpdateS2C payload = new LifespanUpdateS2C(id, ((ISummonedEntity)entity).getLifespan());
            ServerPlayNetworking.send(player, payload);
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
