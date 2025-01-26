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

import mod.gottsch.fabric.mageflame.core.item.ModItems;
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

        // items
        translationBuilder.add(ModItems.MAGE_FLAME_SCROLL, "Mage Flame Scroll");
        translationBuilder.add(ModItems.LESSER_REVELATION_SCROLL, "Lesser Revelation Scroll");
        translationBuilder.add(ModItems.GREATER_REVELATION_SCROLL, "Greater Revelation Scroll");
        translationBuilder.add(ModItems.WINGED_TORCH_SCROLL, "Winged Torch Scroll");
        translationBuilder.add(ModItems.EMBER_HOUND_SCROLL, "Ember Hound Scroll");
        translationBuilder.add(ModItems.BUBBLE_FLAME_SCROLL, "Bubble Flame Scroll");
        translationBuilder.add(ModItems.GLOWGLOB_BALL, "Glowglob Ball");

        // entities
        translationBuilder.add(Registration.MAGE_FLAME_ENTITY, "Mage Flame");
        translationBuilder.add(Registration.LESSER_REVELATION_ENTITY, "Lesser Revelation");
        translationBuilder.add(Registration.GREATER_REVELATION_ENTITY, "Greater Revelation");
        translationBuilder.add(Registration.WINGED_TORCH_ENTITY, "Winged Torch");
        translationBuilder.add(Registration.EMBER_HOUND_ENTITY, "Ember Hound");
        translationBuilder.add(Registration.BUBBLE_FLAME_ENTITY, "Bubble Flame");
        translationBuilder.add(Registration.GLOWGLOB_ENTITY, "Glowglob");
        translationBuilder.add(Registration.GLOWGLOB_BALL_ENTITY, "Glowglob Ball");

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
        translationBuilder.add(LangUtil.tooltip("mage_flame.lore"), "The weakest of the summoned flames,~well-suited for the apprentice spellcaster.~It will allow you to see, but not as bright~as a regular torch.");

        translationBuilder.add(LangUtil.tooltip("lesser_revelation.desc"), "A more powerful version of Mage Flame.");
        translationBuilder.add(LangUtil.tooltip("lesser_revelation.lore"), "A ball of magic-green fire. It has a more~powerful light and increased lifespan than Mage Flame.");

        translationBuilder.add(LangUtil.tooltip("greater_revelation.desc"), "The most powerful of the magical flames.");
        translationBuilder.add(LangUtil.tooltip("greater_revelation.lore"), "The spellcaster is able to channel a great~amount of power to generate a large ball~of magic-blue fire. Brighter than a torch~and has staying power.");

        translationBuilder.add(LangUtil.tooltip("winged_torch.desc"), "Allows the spellcaster to summon a Winged Torch.");
        translationBuilder.add(LangUtil.tooltip("winged_torch.lore"), "The spellcaster is able reach into the astral~plane and summon a Winged Torch. The torch~will remain under your charge until you~release it or it perishes.");

        translationBuilder.add(LangUtil.tooltip("ember_hound.desc"), "Allows the spellcaster to summon an Ember Hound.");
        translationBuilder.add(LangUtil.tooltip("ember_hound.lore"), "The spellcaster is able reach into the nether~plane and summon an Ember Hound.~The Ember Hound will remain under your charge until~you release it or it perishes (default).");

        translationBuilder.add(LangUtil.tooltip("bubble_flame.desc"), "Allows the spellcaster to conjure a Bubble Flame.");
        translationBuilder.add(LangUtil.tooltip("bubble_flame.lore"), "A bright flame within its own air bubble.~Able to travel underwater.");

        translationBuilder.add(LangUtil.tooltip("glowglob.desc"), "Can be thrown to create a glowglob.");
        translationBuilder.add(LangUtil.tooltip("glowglob.lore"), "Creates a weak stationary source of light.~A glowglob ball can be thrown to cast light in~hard to reach places.");

        /*
         * modmenu integration
         */
        translationBuilder.add("text.config.mageflame.title",  "Mage Flame Configuration");
        translationBuilder.add("text.config.mageflame.section.clientProperties", "Client Entity Properties");
        translationBuilder.add("text.config.mageflame.section.flameProperties", "Entity Properties");
        translationBuilder.add("text.config.mageflame.option.mageFlameLifespan", "Mage Flame Lifespan");
        translationBuilder.add("text.config.mageflame.option.lesserRevelationLifespan", "Lesser Revelation Lifespan");
        translationBuilder.add("text.config.mageflame.option.greaterRevelationLifespan", "Greater Revelation Lifespan");
        translationBuilder.add("text.config.mageflame.option.bubbleFlameLifespan", "Bubble Flame Lifespan");
        translationBuilder.add("text.config.mageflame.option.isEmberHoundLifespanInfinite", "Is Ember Hound Lifespan Infinite?");
        translationBuilder.add("text.config.mageflame.option.emberHoundLifespan", "Ember Hound Lifespan");
        translationBuilder.add("text.config.mageflame.option.glowglobLifespan", "Glowglob Lifespan");
        translationBuilder.add("text.config.mageflame.option.maxSummonedEntitiesPerPlayer", "Max Entities Per Player");
        translationBuilder.add("text.config.mageflame.option.enableLifespanDisplay", "Enable display of entity Lifespan");

    }

    public final String name(Item item) {
        return Registries.ITEM.getId(item).getPath();
    }
}
