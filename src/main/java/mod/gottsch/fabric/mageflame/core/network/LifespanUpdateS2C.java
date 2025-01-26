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

import com.mojang.authlib.minecraft.client.MinecraftClient;
import mod.gottsch.fabric.mageflame.MageFlame;
import mod.gottsch.fabric.mageflame.core.entity.creature.ISummonedEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

/**
 * @author Mark Gottschling on 1/24/2025
 */
public record LifespanUpdateS2C(int entityId, int lifespan) implements CustomPayload {

    public static final CustomPayload.Id<LifespanUpdateS2C> ID = new CustomPayload.Id<>(ModNetwork.LIFESPAN_UPDATE_S2C_ID);
    public static final PacketCodec<RegistryByteBuf, LifespanUpdateS2C> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, LifespanUpdateS2C::entityId,
            PacketCodecs.INTEGER, LifespanUpdateS2C::lifespan,
            LifespanUpdateS2C::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void receive(ClientPlayerEntity player, int entityId, int lifespan) {
        MageFlame.LOGGER.debug("client received packet");

            // get the entity by uuid
            Entity entity = player.getWorld().getEntityById(entityId);
            if (entity instanceof ISummonedEntity) {
//                EEchelons.LOGGER.debug("setting entity -> {} to level -> {}", entity.getDisplayName().asString(), level);
                ((ISummonedEntity)entity).setLifespan(lifespan);
            }
    }
}
