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

import mod.gottsch.fabric.mageflame.core.MageFlame;
import mod.gottsch.fabric.mageflame.core.block.SummonFlameBlock;
import mod.gottsch.fabric.mageflame.core.entity.creature.CubeEntity;
import mod.gottsch.fabric.mageflame.core.entity.creature.GreaterRevelationEntity;
import mod.gottsch.fabric.mageflame.core.entity.creature.LesserRevelationEntity;
import mod.gottsch.fabric.mageflame.core.entity.creature.MageFlameEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 *
 */
public class Registration {
    public static final String MAGE_FLAME = "mage_flame";
    public static final String LESSER_REVELATION = "lesser_revelation";
    public static final String GREATER_REVELATION = "greater_revelation";
    public static final String WINGED_TORCH = "winged_torch";

    // blocks
    private static final Block MAGE_FLAME_BLOCK = new SummonFlameBlock(FabricBlockSettings.of(Material.AIR).noCollision().lightLevel((state) -> {
        return 11;
    }).dropsNothing().air());
    private static final Block LESSER_REVELATION_BLOCK = new SummonFlameBlock(FabricBlockSettings.of(Material.AIR).noCollision().lightLevel((state) -> {
        return 13;
    }).dropsNothing().air());
    private static final Block GREATER_REVELATION_BLOCK = new SummonFlameBlock(FabricBlockSettings.of(Material.AIR).noCollision().lightLevel((state) -> {
        return 15;
    }).dropsNothing().air());

    // items
    private static final Item MAGE_FLAME_SCROLL = new Item(new FabricItemSettings());

    /*
     * Registers our Cube Entity under the ID "entitytesting:cube".
     *
     * The entity is registered under the SpawnGroup#CREATURE category, which is what most animals and passive/neutral mobs use.
     * It has a hitbox size of .75x.75, or 12 "pixels" wide (3/4ths of a block).
     */
    public static final EntityType<CubeEntity> CUBE = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier("mageflame", "cube"),
            FabricEntityTypeBuilder.create(
                    SpawnGroup.CREATURE, CubeEntity::new)
                    .dimensions(EntityDimensions.fixed(0.75f, 0.75f))
                    .build()
    );

    public static final EntityType<MageFlameEntity> MAGE_FLAME_ENTITY = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier("mageflame", "mage_flame"),
            FabricEntityTypeBuilder.create(
                            SpawnGroup.CREATURE, MageFlameEntity::new)
                    .dimensions(EntityDimensions.fixed(0.75f, 0.75f))
                    .build()
    );

    public static final EntityType<LesserRevelationEntity> LESSER_REVELATION_ENTITY = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier("mageflame", "lesser_revelation"),
            FabricEntityTypeBuilder.create(
                            SpawnGroup.CREATURE, LesserRevelationEntity::new)
                    .dimensions(EntityDimensions.fixed(0.75f, 0.75f))
                    .build()
    );

    public static final EntityType<GreaterRevelationEntity> GREATER_REVELATION_ENTITY = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier("mageflame", "greater_revelation"),
            FabricEntityTypeBuilder.create(
                            SpawnGroup.CREATURE, GreaterRevelationEntity::new)
                    .dimensions(EntityDimensions.fixed(0.75f, 0.75f))
                    .build()
    );

    /**
     *
     */
    public static void register() {
        // register blocks
        Registry.register(Registry.BLOCK, new Identifier(MageFlame.MOD_ID, "mage_flame_block"), MAGE_FLAME_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(MageFlame.MOD_ID, "lesser_revelation_block"), LESSER_REVELATION_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(MageFlame.MOD_ID, "greater_revelation_block"), GREATER_REVELATION_BLOCK);

        // register items
        Registry.register(Registry.ITEM, new Identifier(MageFlame.MOD_ID, "mage_flame_scroll"), MAGE_FLAME_SCROLL);

        // register entity attributes
        /*
         * Register our Cube Entity's default attributes.
         * Attributes are properties or stats of the mobs, including things like attack damage and health.
         * The game will crash if the entity doesn't have the proper attributes registered in time.
         *
         * In 1.15, this was done by a method override inside the entity class.
         * Most vanilla entities have a static method (eg. ZombieEntity#createZombieAttributes) for initializing their attributes.
         */
        FabricDefaultAttributeRegistry.register(CUBE, CubeEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(MAGE_FLAME_ENTITY, MageFlameEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(LESSER_REVELATION_ENTITY, LesserRevelationEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(GREATER_REVELATION_ENTITY, GreaterRevelationEntity.createMobAttributes());

        MageFlame.LOGGER.info("Hello Fabric world!");
    }
}
