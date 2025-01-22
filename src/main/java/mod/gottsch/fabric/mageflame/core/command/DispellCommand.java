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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Mage Flame.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.fabric.mageflame.core.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import mod.gottsch.fabric.mageflame.core.util.SpawnUtil;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

/**
 * Created by Mark Gottschling on 1/19/2025
 */
public class DispellCommand {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher
                .register(literal("mageflame")
                        .then(literal("dispel")
                                .then(argument("entity", StringArgumentType.greedyString())
                                        .suggests(new SummonedEntitiesSuggestionProvider())
                                        .executes(context -> killSummonedEntities(context.getSource(), getString(context, "entity")))
                                )
                        )
                ));
    }

    public static int killSummonedEntities(ServerCommandSource source, String entityName) {

        try {
            if ("all".equalsIgnoreCase(entityName)) {
                SpawnUtil.killAllSummonedEntities(source.getWorld(), source.getPlayerOrThrow());
            } else {
                // cycle through each entity and on first match, remove
                SpawnUtil.killSummonedEntity(source.getWorld(), source.getPlayerOrThrow(), entityName);
            }
        } catch(Exception e) {
            source.getServer().getPlayerManager().broadcast(error("An error occurred."), false);
        }
        return Command.SINGLE_SUCCESS; // Success
    }

    private static Text error(String message) {
        return Text.literal(message).formatted(Formatting.RED);
    }
}
