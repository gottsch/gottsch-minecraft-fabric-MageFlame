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
import mod.gottsch.fabric.mageflame.core.config.MageFlameConfigs;
import mod.gottsch.fabric.mageflame.core.config.SimpleConfig;
import mod.gottsch.fabric.mageflame.core.entity.creature.*;
import mod.gottsch.fabric.mageflame.core.event.SummonFlameServerEntityLoadHandler;
import mod.gottsch.fabric.mageflame.core.item.GreaterFlameScroll;
import mod.gottsch.fabric.mageflame.core.item.LesserFlameScroll;
import mod.gottsch.fabric.mageflame.core.item.MageFlameScroll;
import mod.gottsch.fabric.mageflame.core.item.WingedTorchScroll;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.particle.DefaultParticleType;
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
    public static final Block MAGE_FLAME_BLOCK = new SummonFlameBlock(FabricBlockSettings.of(Material.AIR).noCollision().lightLevel((state) -> {
        return 11;
    }).dropsNothing().air());

    public static final Block LESSER_REVELATION_BLOCK = new SummonFlameBlock(FabricBlockSettings.of(Material.AIR).noCollision().lightLevel((state) -> {
        return 13;
    }).dropsNothing().air());

    public static final Block GREATER_REVELATION_BLOCK = new SummonFlameBlock(FabricBlockSettings.of(Material.AIR).noCollision().lightLevel((state) -> {
        return 15;
    }).dropsNothing().air());

    // items
    private static final Item MAGE_FLAME_SCROLL = new MageFlameScroll(new FabricItemSettings().group(ItemGroup.MISC));
    private static final Item LESSER_REVELATION_SCROLL = new LesserFlameScroll(new FabricItemSettings().group(ItemGroup.MISC));
    private static final Item GREATER_REVELATION_SCROLL = new GreaterFlameScroll(new FabricItemSettings().group(ItemGroup.MISC));
    private static final Item WINGED_TORCH_SCROLL = new WingedTorchScroll(new FabricItemSettings().group(ItemGroup.MISC));

    // entities
    public static final EntityType<MageFlameEntity> MAGE_FLAME_ENTITY = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier("mageflame", "mage_flame"),
            FabricEntityTypeBuilder.create(
                            SpawnGroup.CREATURE, MageFlameEntity::new)
                    .dimensions(EntityDimensions.fixed(0.125f, 0.125f))
                    .build()
    );

    public static final EntityType<LesserRevelationEntity> LESSER_REVELATION_ENTITY = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier("mageflame", "lesser_revelation"),
            FabricEntityTypeBuilder.create(
                            SpawnGroup.CREATURE, LesserRevelationEntity::new)
                    .dimensions(EntityDimensions.fixed(0.125f, 0.125f))
                    .build()
    );

    public static final EntityType<GreaterRevelationEntity> GREATER_REVELATION_ENTITY = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier("mageflame", "greater_revelation"),
            FabricEntityTypeBuilder.create(
                            SpawnGroup.CREATURE, GreaterRevelationEntity::new)
                    .dimensions(EntityDimensions.fixed(0.1875f, 0.1875f))
                    .build()
    );

    public static final EntityType<WingedTorchEntity> WINGED_TORCH_ENTITY = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier("mageflame", "winged_torch"),
            FabricEntityTypeBuilder.create(
                            SpawnGroup.CREATURE, WingedTorchEntity::new)
                    .dimensions(EntityDimensions.fixed(0.375F, 0.25F))
                    .build()
    );

    // particles
    public static final DefaultParticleType REVELATION_PARTICLE = FabricParticleTypes.simple();

    /**
     *
     */
    public static void register() {
        // register configs
        // NOTE ensure that these properties are only used server-side and not for registration properties.
        MageFlameConfigs.register();

        // register blocks
        Registry.register(Registry.BLOCK, new Identifier(MageFlame.MOD_ID, "mage_flame_block"), MAGE_FLAME_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(MageFlame.MOD_ID, "lesser_revelation_block"), LESSER_REVELATION_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(MageFlame.MOD_ID, "greater_revelation_block"), GREATER_REVELATION_BLOCK);

        // register items
        Registry.register(Registry.ITEM, new Identifier(MageFlame.MOD_ID, "mage_flame_scroll"), MAGE_FLAME_SCROLL);
        Registry.register(Registry.ITEM, new Identifier(MageFlame.MOD_ID, "lesser_revelation_scroll"), LESSER_REVELATION_SCROLL);
        Registry.register(Registry.ITEM, new Identifier(MageFlame.MOD_ID, "greater_revelation_scroll"), GREATER_REVELATION_SCROLL);
        Registry.register(Registry.ITEM, new Identifier(MageFlame.MOD_ID, "winged_torch_scroll"), WINGED_TORCH_SCROLL);

        // register entity attributes
         FabricDefaultAttributeRegistry.register(MAGE_FLAME_ENTITY, MageFlameEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(LESSER_REVELATION_ENTITY, LesserRevelationEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(GREATER_REVELATION_ENTITY, GreaterRevelationEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(WINGED_TORCH_ENTITY, WingedTorchEntity.createMobAttributes());

        // particles
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(MageFlame.MOD_ID, "revelation_particle"), REVELATION_PARTICLE);

        /*
         * Don't need nature spawns for this mod, but see https://www.youtube.com/watch?v=7gbmJGZvQks when you do.
         *
         *  BiomeModifications.addSpawn(BiomeSelectors.categories(Biome.Category.PLAINS), SpawnGroup.CREATURE)
         */

        ServerEntityEvents.ENTITY_LOAD.register(new SummonFlameServerEntityLoadHandler());

        MageFlame.LOGGER.info("Hello Fabric world!");
    }
}
