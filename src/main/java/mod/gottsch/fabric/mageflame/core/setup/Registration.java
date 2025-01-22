/*
 * This file is part of Mage Flame.
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
import mod.gottsch.fabric.mageflame.core.entity.creature.*;
import mod.gottsch.fabric.mageflame.core.event.MageFlameServerWorldLoadHandler;
import mod.gottsch.fabric.mageflame.core.event.MageFlameServerWorldUnloadHandler;
import mod.gottsch.fabric.mageflame.core.item.*;
import mod.gottsch.fabric.mageflame.core.loot.ModLootTableModifiers;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 *
 */
public class Registration {
    public static final String MAGE_FLAME = "mage_flame";
    public static final String LESSER_REVELATION = "lesser_revelation";
    public static final String GREATER_REVELATION = "greater_revelation";
    public static final String WINGED_TORCH = "winged_torch";
    public static final String EMBER_HOUND= "ember_hound";
    public static final String BUBBLE_FLAME = "bubble_flame";
    public static final String GLOWGLOB = "glowglob";

    // items
    public static final Item MAGE_FLAME_SCROLL = new MageFlameScroll(new Item.Settings());
    public static final Item LESSER_REVELATION_SCROLL = new LesserFlameScroll(new Item.Settings());
    public static final Item GREATER_REVELATION_SCROLL = new GreaterFlameScroll(new Item.Settings());
    public static final Item WINGED_TORCH_SCROLL = new WingedTorchScroll(new Item.Settings());
    public static final Item EMBER_HOUND_SCROLL = new EmberHoundScroll(new Item.Settings());
    public static final Item BUBBLE_FLAME_SCROLL = new BubbleFlameScroll(new Item.Settings());
    public static final Item GLOWGLOB_SCROLL = new GlowglobScroll(new Item.Settings());

    // entities
    public static final EntityType<MageFlameEntity> MAGE_FLAME_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(MageFlame.MOD_ID, MAGE_FLAME),
            FabricEntityTypeBuilder.create(
                            SpawnGroup.CREATURE, MageFlameEntity::new)
                    .dimensions(EntityDimensions.fixed(0.125f, 0.125f))
                    .fireImmune()
                    .build()
    );

    public static final EntityType<LesserRevelationEntity> LESSER_REVELATION_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(MageFlame.MOD_ID, LESSER_REVELATION),
            FabricEntityTypeBuilder.create(
                            SpawnGroup.CREATURE, LesserRevelationEntity::new)
                    .dimensions(EntityDimensions.fixed(0.125f, 0.125f))
                    .fireImmune()
                    .build()
    );

    public static final EntityType<GreaterRevelationEntity> GREATER_REVELATION_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(MageFlame.MOD_ID, GREATER_REVELATION),
            FabricEntityTypeBuilder.create(
                            SpawnGroup.CREATURE, GreaterRevelationEntity::new)
                    .dimensions(EntityDimensions.fixed(0.1875f, 0.1875f))
                    .fireImmune()
                    .build()
    );

    public static final EntityType<WingedTorchEntity> WINGED_TORCH_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(MageFlame.MOD_ID, WINGED_TORCH),
            FabricEntityTypeBuilder.create(
                            SpawnGroup.CREATURE, WingedTorchEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.625F))
                    .fireImmune()
                    .build()
    );

    public static final EntityType<EmberHoundEntity> EMBER_HOUND_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(MageFlame.MOD_ID, EMBER_HOUND),
            FabricEntityTypeBuilder.create(
                            SpawnGroup.CREATURE, EmberHoundEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6F, 0.85F))
                    .fireImmune()
                    .build()
    );

    public static final EntityType<BubbleFlameEntity> BUBBLE_FLAME_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(MageFlame.MOD_ID, BUBBLE_FLAME),
            FabricEntityTypeBuilder.create(
                            SpawnGroup.CREATURE, BubbleFlameEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
                    .fireImmune()
                    .build()
    );

    public static final EntityType<GlowglobEntity> GLOWGLOB_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(MageFlame.MOD_ID, GLOWGLOB),
            FabricEntityTypeBuilder.create(
                            SpawnGroup.CREATURE, GlowglobEntity::new)
                    .dimensions(EntityDimensions.fixed(0.55F, 0.55F))
                    .fireImmune()
                    .build()
    );

    // particles
    public static final SimpleParticleType REVELATION_PARTICLE = FabricParticleTypes.simple();
    public static final SimpleParticleType GREATER_REVELATION_PARTICLE = FabricParticleTypes.simple();
    public static final SimpleParticleType BUBBLE_FLAME_PARTICLE = FabricParticleTypes.simple();

    /**
     *
     */
    public static void register() {

        // register item groups
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> {
            content.add(Registration.MAGE_FLAME_SCROLL);
            content.add(Registration.LESSER_REVELATION_SCROLL);
            content.add(Registration.GREATER_REVELATION_SCROLL);
            content.add(Registration.WINGED_TORCH_SCROLL);
            content.add(Registration.EMBER_HOUND_SCROLL);
            content.add(Registration.BUBBLE_FLAME_SCROLL);
            content.add(Registration.GLOWGLOB_SCROLL);
        });

        // register items
        Registry.register(Registries.ITEM, Identifier.of(MageFlame.MOD_ID, "mage_flame_scroll"), MAGE_FLAME_SCROLL);
        Registry.register(Registries.ITEM, Identifier.of(MageFlame.MOD_ID, "lesser_revelation_scroll"), LESSER_REVELATION_SCROLL);
        Registry.register(Registries.ITEM, Identifier.of(MageFlame.MOD_ID, "greater_revelation_scroll"), GREATER_REVELATION_SCROLL);
        Registry.register(Registries.ITEM, Identifier.of(MageFlame.MOD_ID, "winged_torch_scroll"), WINGED_TORCH_SCROLL);
        Registry.register(Registries.ITEM, Identifier.of(MageFlame.MOD_ID, "ember_hound_scroll"), EMBER_HOUND_SCROLL);
        Registry.register(Registries.ITEM, Identifier.of(MageFlame.MOD_ID, "bubble_flame_scroll"), BUBBLE_FLAME_SCROLL);
        Registry.register(Registries.ITEM, Identifier.of(MageFlame.MOD_ID, "glowglob_scroll"), GLOWGLOB_SCROLL);

        // register entity attributes
        FabricDefaultAttributeRegistry.register(MAGE_FLAME_ENTITY, MageFlameEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(LESSER_REVELATION_ENTITY, LesserRevelationEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(GREATER_REVELATION_ENTITY, GreaterRevelationEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(WINGED_TORCH_ENTITY, WingedTorchEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EMBER_HOUND_ENTITY, EmberHoundEntity.createWolfAttributes());
        FabricDefaultAttributeRegistry.register(BUBBLE_FLAME_ENTITY, BubbleFlameEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(GLOWGLOB_ENTITY, GlowglobEntity.createGlobAttributes());

        // particles
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(MageFlame.MOD_ID, "revelation_particle"), REVELATION_PARTICLE);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(MageFlame.MOD_ID, "greater_revelation_particle"), GREATER_REVELATION_PARTICLE);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(MageFlame.MOD_ID, "bubble_flame_particle"), BUBBLE_FLAME_PARTICLE);

        /*
         * Don't need nature spawns for this mod, but see https://www.youtube.com/watch?v=7gbmJGZvQks when you do.
         *
         *  BiomeModifications.addSpawn(BiomeSelectors.categories(Biome.Category.PLAINS), SpawnGroup.CREATURE)
         */

        ServerEntityEvents.ENTITY_LOAD.register(new MageFlameServerWorldLoadHandler());
        ServerEntityEvents.ENTITY_UNLOAD.register(new MageFlameServerWorldUnloadHandler());

        // loot table modifiers
        ModLootTableModifiers.modifyLootTables();

        // MageFlame.LOGGER.info("Hello Fabric world!");
    }
}
