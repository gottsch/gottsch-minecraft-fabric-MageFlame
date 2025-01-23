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
package mod.gottsch.fabric.mageflame.core.item;

import mod.gottsch.fabric.mageflame.MageFlame;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * @author Mark Gottschling on 1/23/2025
 */
public class ModItems {
    public static final Item MAGE_FLAME_SCROLL = new MageFlameScroll(new Item.Settings());
    public static final Item LESSER_REVELATION_SCROLL = new LesserFlameScroll(new Item.Settings());
    public static final Item GREATER_REVELATION_SCROLL = new GreaterFlameScroll(new Item.Settings());
    public static final Item WINGED_TORCH_SCROLL = new WingedTorchScroll(new Item.Settings());
    public static final Item EMBER_HOUND_SCROLL = new EmberHoundScroll(new Item.Settings());
    public static final Item BUBBLE_FLAME_SCROLL = new BubbleFlameScroll(new Item.Settings());
//    public static final Item GLOWGLOB_SCROLL = new GlowglobScroll(new Item.Settings());
    public static final Item GLOWGLOB_BALL = new GlowglobBall(new Item.Settings());

    public static void register() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> {
            content.add(MAGE_FLAME_SCROLL);
            content.add(LESSER_REVELATION_SCROLL);
            content.add(GREATER_REVELATION_SCROLL);
            content.add(WINGED_TORCH_SCROLL);
            content.add(EMBER_HOUND_SCROLL);
            content.add(BUBBLE_FLAME_SCROLL);
//            content.add(GLOWGLOB_SCROLL);
            content.add(GLOWGLOB_BALL);
        });

            // register items
        Registry.register(Registries.ITEM, Identifier.of(MageFlame.MOD_ID, "mage_flame_scroll"), MAGE_FLAME_SCROLL);
        Registry.register(Registries.ITEM, Identifier.of(MageFlame.MOD_ID, "lesser_revelation_scroll"), LESSER_REVELATION_SCROLL);
        Registry.register(Registries.ITEM, Identifier.of(MageFlame.MOD_ID, "greater_revelation_scroll"), GREATER_REVELATION_SCROLL);
        Registry.register(Registries.ITEM, Identifier.of(MageFlame.MOD_ID, "winged_torch_scroll"), WINGED_TORCH_SCROLL);
        Registry.register(Registries.ITEM, Identifier.of(MageFlame.MOD_ID, "ember_hound_scroll"), EMBER_HOUND_SCROLL);
        Registry.register(Registries.ITEM, Identifier.of(MageFlame.MOD_ID, "bubble_flame_scroll"), BUBBLE_FLAME_SCROLL);
//        Registry.register(Registries.ITEM, Identifier.of(MageFlame.MOD_ID, "glowglob_scroll"), GLOWGLOB_SCROLL);
        register("glowglob_ball", GLOWGLOB_BALL);

    }

    private static void register(String name, Item item) {
        Registry.register(Registries.ITEM, Identifier.of(MageFlame.MOD_ID, name), item);

    }
}
