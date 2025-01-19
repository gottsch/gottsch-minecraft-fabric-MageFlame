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
package mod.gottsch.fabric.mageflame.core.loot;


import com.google.common.collect.Lists;
import mod.gottsch.fabric.mageflame.core.setup.Registration;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.List;

/**
 * @author Mark Gottschling on 1/17/2025
 */
public class ModLootTableModifiers {

    // TODO get all structures ids
    private static final Identifier ABANDONED_MINESHAFT_ID =
            Identifier.of("minecraft", "chests/abandoned_mineshaft");
    private static final Identifier ANCIENT_CITY_ID =
            Identifier.of("minecraft", "chests/ancient_city");
    private static final Identifier ANCIENT_CITY_ICE_BOX_ID =
            Identifier.of("minecraft", "chests/ancient_city_ice_box");

    private static final Identifier BURIED_TREASURE_ID =
            Identifier.of("minecraft", "chests/buried_treasure");

    private static final Identifier DESERT_PYRAMID_ID =
            Identifier.of("minecraft", "chests/desert_pyramid");
    private static final Identifier IGLOO_CHEST_ID =
            Identifier.of("minecraft", "chests/igloo_chest");
    private static final Identifier JUNGLE_TEMPLE_ID =
            Identifier.of("minecraft", "chests/jungle_temple");
    private static final Identifier SHIPWRECK_TREASURE_ID =
            Identifier.of("minecraft", "chests/shipwreck_treasure");
    private static final Identifier SIMPLE_DUNGEON_ID =
            Identifier.of("minecraft", "chests/simple_dungeon");

    private static final Identifier BLAZE_ID =
            Identifier.of("minecraft", "entities/blaze");
    private static final Identifier WITCH_ID =
            Identifier.of("minecraft", "entities/witch");

    private static final List<Identifier> LIST = Lists.newArrayList();

    // add all identifiers to the list
    static {
        // chests
//        LIST.add(ABANDONED_MINESHAFT_ID);
//        LIST.add(ANCIENT_CITY_ID);
//        LIST.add(ANCIENT_CITY_ICE_BOX_ID);
//        LIST.add(BURIED_TREASURE_ID);
//        LIST.add(JUNGLE_TEMPLE_ID);

        // entities
        LIST.add(BLAZE_ID);
        LIST.add(WITCH_ID);
    }

    public static void modifyLootTables() {

        LootTableEvents.MODIFY.register((registryKey, builder, lootTableSource, wrapperLookup) -> {
            LootPool.Builder poolBuilder = LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1))
                    .conditionally(RandomChanceLootCondition.builder(1f)) // Drops 100% of the time

                    // mage flame scroll
                    .with(ItemEntry.builder(Registration.MAGE_FLAME_SCROLL)
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f)))
                            .conditionally(RandomChanceLootCondition.builder(0.75f))
                            .build())
                     // lesser revelation scroll
                    .with(ItemEntry.builder(Registration.LESSER_REVELATION_SCROLL)
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f)))
                            .conditionally(RandomChanceLootCondition.builder(0.55f))
                            .build())
                    // greater revelation scroll
                    .with(ItemEntry.builder(Registration.GREATER_REVELATION_SCROLL)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)))
                        .conditionally(RandomChanceLootCondition.builder(0.35f))
                        .build())
                    // winged torch scroll
                    .with(ItemEntry.builder(Registration.WINGED_TORCH_SCROLL)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)))
                        .conditionally(RandomChanceLootCondition.builder(0.15f))
                        .build())
                    // ember hound scroll
                    .with(ItemEntry.builder(Registration.EMBER_HOUND_SCROLL)
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)))
                            .conditionally(RandomChanceLootCondition.builder(0.15f))
                            .build())
                   // bubble flame scroll
                    .with(ItemEntry.builder(Registration.BUBBLE_FLAME_SCROLL)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)))
                        .conditionally(RandomChanceLootCondition.builder(0.25f))
                        .build());
                    // TODO add another other scrolls

            // inject into all vanilla chests and other selected loot tables
            if (LIST.contains(registryKey.getValue()) || registryKey.getValue().getPath().contains("chests")) {
                builder.pool(poolBuilder).build();
            }
        });
    }
}
