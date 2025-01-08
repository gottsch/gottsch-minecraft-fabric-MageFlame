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
package mod.gottsch.fabric.mageflame.core.setup;

import mod.gottsch.fabric.mageflame.MageFlame;
import mod.gottsch.fabric.mageflame.core.block.SummonFlameBlock;
import mod.gottsch.fabric.mageflame.core.entity.creature.GreaterRevelationEntity;
import mod.gottsch.fabric.mageflame.core.entity.creature.LesserRevelationEntity;
import mod.gottsch.fabric.mageflame.core.entity.creature.MageFlameEntity;
import mod.gottsch.fabric.mageflame.core.entity.creature.WingedTorchEntity;
import mod.gottsch.fabric.mageflame.core.event.SummonFlameServerEntityLoadHandler;
import mod.gottsch.fabric.mageflame.core.item.GreaterFlameScroll;
import mod.gottsch.fabric.mageflame.core.item.LesserFlameScroll;
import mod.gottsch.fabric.mageflame.core.item.MageFlameScroll;
import mod.gottsch.fabric.mageflame.core.item.WingedTorchScroll;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *
 */
public class Registration {
    public static final String MAGE_FLAME = "mage_flame";
    public static final String LESSER_REVELATION = "lesser_revelation";
    public static final String GREATER_REVELATION = "greater_revelation";
    public static final String WINGED_TORCH = "winged_torch";

    // blocks
    public static Block MAGE_FLAME_BLOCK;
//    = new SummonFlameBlock(
//            AbstractBlock.Settings.create()
//                    .mapColor(MapColor.CLEAR)
//                    .strength(-1.0F, 3600000.8F)
//                    .noCollision()
//                    .dropsNothing()
//                    .nonOpaque()
//                    .luminance((state) -> {
//                        return 11;
//                    }));

    public static Block LESSER_REVELATION_BLOCK;
//    = new SummonFlameBlock(
//            AbstractBlock.Settings.create()
//                    .mapColor(MapColor.CLEAR)
//                    .strength(-1.0F, 3600000.8F)
//                    .noCollision()
//                    .dropsNothing()
//                    .nonOpaque()
//                    .luminance((state) -> {
//                        return 13;
//                    }));

    public static Block GREATER_REVELATION_BLOCK;
//    = new SummonFlameBlock(
//            AbstractBlock.Settings.create()
//                    .mapColor(MapColor.CLEAR)
//                    .strength(-1.0F, 3600000.8F)
//                    .noCollision()
//                    .dropsNothing()
//                    .nonOpaque()
//                    .luminance((state) -> {
//                        return 15;
//                    }));

    // items
    public static Item MAGE_FLAME_SCROLL; // = new MageFlameScroll(new Item.Settings());
    public static Item LESSER_REVELATION_SCROLL; // = new LesserFlameScroll(new Item.Settings());
    public static Item GREATER_REVELATION_SCROLL; // = new GreaterFlameScroll(new Item.Settings());
    public static Item WINGED_TORCH_SCROLL; // = new WingedTorchScroll(new Item.Settings());

    // entities
    public static final EntityType<MageFlameEntity> MAGE_FLAME_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(MageFlame.MOD_ID, MAGE_FLAME),
            FabricEntityTypeBuilder.create(
                            SpawnGroup.CREATURE, MageFlameEntity::new)
                    .dimensions(EntityDimensions.fixed(0.125f, 0.125f))
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(MageFlame.MOD_ID, MAGE_FLAME)))
    );

    public static final EntityType<LesserRevelationEntity> LESSER_REVELATION_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(MageFlame.MOD_ID, LESSER_REVELATION),
            FabricEntityTypeBuilder.create(
                            SpawnGroup.CREATURE, LesserRevelationEntity::new)
                    .dimensions(EntityDimensions.fixed(0.125f, 0.125f))
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(MageFlame.MOD_ID, LESSER_REVELATION)))
    );

    public static final EntityType<GreaterRevelationEntity> GREATER_REVELATION_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(MageFlame.MOD_ID, GREATER_REVELATION),
            FabricEntityTypeBuilder.create(
                            SpawnGroup.CREATURE, GreaterRevelationEntity::new)
                    .dimensions(EntityDimensions.fixed(0.1875f, 0.1875f))
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(MageFlame.MOD_ID, GREATER_REVELATION)))
    );

    public static final EntityType<WingedTorchEntity> WINGED_TORCH_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(MageFlame.MOD_ID, WINGED_TORCH),
            EntityType.Builder.create(
                            WingedTorchEntity::new, SpawnGroup.CREATURE)
                    .dimensions(0.25F, 0.625F)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(MageFlame.MOD_ID, WINGED_TORCH)))
    );

    // particles
    public static final SimpleParticleType REVELATION_PARTICLE = FabricParticleTypes.simple();

    public static Block registerSummonFlameBlock(String name, Block.Settings settings) {
        RegistryKey<Block> key = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(MageFlame.MOD_ID, name));
        return Registry.register(Registries.BLOCK, key, new SummonFlameBlock(settings.registryKey(key)));
    }

    /**
     *
     */
    public static void register() {

        MAGE_FLAME_BLOCK = registerSummonFlameBlock("mage_flame_block",
                AbstractBlock.Settings.create()
                        .mapColor(MapColor.CLEAR)
                        .strength(-1.0F, 3600000.8F)
                        .noCollision()
                        .dropsNothing()
                        .nonOpaque()
                        .luminance((state) -> 11)
        );

//        LESSER_REVELATION_BLOCK = Registry.register(Registries.BLOCK, RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(MageFlame.MOD_ID, "lesser_revelation_block")), new SummonFlameBlock(
//                AbstractBlock.Settings.create()
//                        .mapColor(MapColor.CLEAR)
//                        .strength(-1.0F, 3600000.8F)
//                        .noCollision()
//                        .dropsNothing()
//                        .nonOpaque()
//                        .luminance((state) -> {
//                            return 13;
//                        })
//        ));
        LESSER_REVELATION_BLOCK = registerSummonFlameBlock("lesser_revelation_block",
                AbstractBlock.Settings.create()
                        .mapColor(MapColor.CLEAR)
                        .strength(-1.0F, 3600000.8F)
                        .noCollision()
                        .dropsNothing()
                        .nonOpaque()
                        .luminance((state) -> 13)
        );

//        GREATER_REVELATION_BLOCK = Registry.register(Registries.BLOCK, RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(MageFlame.MOD_ID, "greater_revelation_block")), new SummonFlameBlock(
//                AbstractBlock.Settings.create()
//                        .mapColor(MapColor.CLEAR)
//                        .strength(-1.0F, 3600000.8F)
//                        .noCollision()
//                        .dropsNothing()
//                        .nonOpaque()
//                        .luminance((state) -> {
//                            return 15;
//                        })
//        ));
        GREATER_REVELATION_BLOCK = registerSummonFlameBlock("greater_revelation_block",
                AbstractBlock.Settings.create()
                        .mapColor(MapColor.CLEAR)
                        .strength(-1.0F, 3600000.8F)
                        .noCollision()
                        .dropsNothing()
                        .nonOpaque()
                        .luminance((state) -> 15)
        );

        // register items
        Identifier itemIdentifier = Identifier.of(MageFlame.MOD_ID, "mage_flame_scroll");
        RegistryKey<Item> itemRegistryKey = RegistryKey.of(RegistryKeys.ITEM, itemIdentifier);
        MAGE_FLAME_SCROLL = Registry.register(Registries.ITEM, itemRegistryKey, new MageFlameScroll(new Item.Settings().registryKey(itemRegistryKey)));

        itemIdentifier = Identifier.of(MageFlame.MOD_ID, "lesser_revelation_scroll");
        itemRegistryKey = RegistryKey.of(RegistryKeys.ITEM, itemIdentifier);
        LESSER_REVELATION_SCROLL = Registry.register(Registries.ITEM, itemRegistryKey, new LesserFlameScroll(new Item.Settings().registryKey(itemRegistryKey)));

        itemIdentifier = Identifier.of(MageFlame.MOD_ID, "greater_revelation_scroll");
        itemRegistryKey = RegistryKey.of(RegistryKeys.ITEM, itemIdentifier);
        GREATER_REVELATION_SCROLL = Registry.register(Registries.ITEM, itemRegistryKey, new GreaterFlameScroll(new Item.Settings().registryKey(itemRegistryKey)));

        itemIdentifier = Identifier.of(MageFlame.MOD_ID, "winged_torch_scroll");
        itemRegistryKey = RegistryKey.of(RegistryKeys.ITEM, itemIdentifier);
        WINGED_TORCH_SCROLL = Registry.register(Registries.ITEM, itemRegistryKey, new WingedTorchScroll(new Item.Settings().registryKey(itemRegistryKey)));

        // register item groups
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> {
            content.add(Registration.MAGE_FLAME_SCROLL);
            content.add(Registration.LESSER_REVELATION_SCROLL);
            content.add(Registration.GREATER_REVELATION_SCROLL);
            content.add(Registration.WINGED_TORCH_SCROLL);

        });

        // register entity attributes
        FabricDefaultAttributeRegistry.register(MAGE_FLAME_ENTITY, MageFlameEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(LESSER_REVELATION_ENTITY, LesserRevelationEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(GREATER_REVELATION_ENTITY, GreaterRevelationEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(WINGED_TORCH_ENTITY, WingedTorchEntity.createMobAttributes());

        // particles
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(MageFlame.MOD_ID, "revelation_particle"), REVELATION_PARTICLE);

        /*
         * Don't need nature spawns for this mod, but see https://www.youtube.com/watch?v=7gbmJGZvQks when you do.
         *
         *  BiomeModifications.addSpawn(BiomeSelectors.categories(Biome.Category.PLAINS), SpawnGroup.CREATURE)
         */

        ServerEntityEvents.ENTITY_LOAD.register(new SummonFlameServerEntityLoadHandler());

        // MageFlame.LOGGER.debug("Hello Fabric world!");
    }
}
