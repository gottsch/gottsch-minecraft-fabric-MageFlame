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
package mod.gottsch.fabric.mageflame.datagen;

import mod.gottsch.fabric.mageflame.core.setup.Registration;
import mod.gottsch.fabric.mageflame.core.util.LangUtil;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModLangProvider extends FabricLanguageProvider {

    public ModLangProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {

        // scrolls
        translationBuilder.add(Registration.MAGE_FLAME_SCROLL, "Mage Flame Scroll");
        translationBuilder.add(Registration.LESSER_REVELATION_SCROLL, "Lesser Revelation Scroll");
        translationBuilder.add(Registration.GREATER_REVELATION_SCROLL, "Greater Revelation Scroll");
        translationBuilder.add(Registration.WINGED_TORCH_SCROLL, "Winged Torch Scroll");
        translationBuilder.add(Registration.FIRE_WOLF_SCROLL, "Ember Hound Scroll");

        // entities
        translationBuilder.add(Registration.MAGE_FLAME_ENTITY, "Mage Flame");
        translationBuilder.add(Registration.LESSER_REVELATION_ENTITY, "Lesser Revelation");
        translationBuilder.add(Registration.GREATER_REVELATION_ENTITY, "Greater Revelation");
        translationBuilder.add(Registration.WINGED_TORCH_ENTITY, "Winged Torch");
        translationBuilder.add(Registration.FIRE_WOLF_ENTITY, "Ember Hound");

        /*
         *  Util.tooltips
         */
        // general
        translationBuilder.add(LangUtil.tooltip("boolean.yes"), "Yes");
        translationBuilder.add(LangUtil.tooltip("boolean.no"), "No");
        translationBuilder.add(LangUtil.tooltip("infinite"), "Infinite");
        translationBuilder.add(LangUtil.tooltip("hold_shift"), "Hold [SHIFT] to expand");
        translationBuilder.add(LangUtil.tooltip("divider"), "--------------------");

        translationBuilder.add(LangUtil.tooltip("lifespan"), "Lifespan: %s");
        translationBuilder.add(LangUtil.tooltip("light_level"), "Light Level: %s");

        translationBuilder.add(LangUtil.tooltip("mage_flame.desc"), "Allows the spellcaster to create a small ball of flames.");
        translationBuilder.add(LangUtil.tooltip("mage_flame.lore"), "The weakest of the summoned flames,~well-suited for the apprentice spellcaster.~It will allow you to see, but not as bright~as a regular torch. It is not strong enough~to navigate through vines and other~replaceable blocks, and will extinguish if~it is unable to follow its owner.");

        translationBuilder.add(LangUtil.tooltip("lesser_revelation.desc"), "A more powerful version of Mage Flame.");
        translationBuilder.add(LangUtil.tooltip("lesser_revelation.lore"), "A ball of magic-green fire. It has a more~powerful light and increased lifespan.");

        translationBuilder.add(LangUtil.tooltip("greater_revelation.desc"), "The most powerful of the magical flames.");
        translationBuilder.add(LangUtil.tooltip("greater_revelation.lore"), "The spellcaster is able to channel a great~amount of power to generate a large ball~of magic-green fire. Brighter than a torch~and has staying power. It also has enough~power to destroy replaceable blocks.");

        translationBuilder.add(LangUtil.tooltip("winged_torch.desc"), "Allows the spellcaster to summon a Winged Torch.");
        translationBuilder.add(LangUtil.tooltip("winged_torch.lore"), "The spellcaster is able reach into the nether~plane and summon a Winged Torch. The torch~will remain under your charge until you~release it or it perishes.");

        translationBuilder.add(LangUtil.tooltip("fire_wolf.desc"), "Allows the spellcaster to summon an Ember Hound.");
        translationBuilder.add(LangUtil.tooltip("fire_wolf.lore"), "The spellcaster is able reach into the nether~plane and summon an Ember Hound....");

        /*
         * modmenu integration
         */
        translationBuilder.add("text.config.mageflame.title",  "Mage Flame Configuration");
                translationBuilder.add("text.config.mageflame.section.flameProperties", "Flame / Torch Entity Properties");
                translationBuilder.add("text.config.mageflame.option.mageFlameLifespan", "Mage Flame Lifespan");
                translationBuilder.add("text.config.mageflame.option.lesserRevelationLifespan", "Lesser Revelation Lifespan");
                translationBuilder.add("text.config.mageflame.option.greaterRevelationLifespan", "Greater Revelation Lifespan");
                translationBuilder.add("text.config.mageflame.option.updateLightTicks", "Update Light Delay in Ticks");

    }

    public final String name(Item item) {
        return Registries.ITEM.getId(item).getPath();
    }
}
