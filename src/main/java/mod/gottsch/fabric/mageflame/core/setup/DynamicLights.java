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
package mod.gottsch.fabric.mageflame.core.setup;

import dev.lambdaurora.lambdynlights.api.DynamicLightHandler;
import dev.lambdaurora.lambdynlights.api.DynamicLightsInitializer;
import dev.lambdaurora.lambdynlights.api.item.ItemLightSourceManager;
import mod.gottsch.fabric.mageflame.core.config.MageFlameDynamicLightsConfig;

import static dev.lambdaurora.lambdynlights.api.DynamicLightHandlers.registerDynamicLightHandler;


/**
 * Created by Mark Gottschling on 1/11/2025
 */
public class DynamicLights implements DynamicLightsInitializer {

    @Override
    public void onInitializeDynamicLights(ItemLightSourceManager itemLightSourceManager) {
        registerDynamicLightHandler(Registration.MAGE_FLAME_ENTITY,
                entity -> {
                    int luminance = MageFlameDynamicLightsConfig.MAGE_FLAME_LUMINANCE;
                    if(entity.getLifespan() <= 1200F) {
                        luminance = (entity.getLifespan() / 1200) * MageFlameDynamicLightsConfig.MAGE_FLAME_LUMINANCE;
                        if (luminance < 1) luminance = 1;
                    }
                    return luminance;
                }
        );

        registerDynamicLightHandler(Registration.LESSER_REVELATION_ENTITY,
                entity -> {
                    int luminance = MageFlameDynamicLightsConfig.LESSER_REVELATION_LUMINANCE;
                    if(entity.getLifespan() <= 1200F) {
                        luminance = (entity.getLifespan() / 1200) * MageFlameDynamicLightsConfig.LESSER_REVELATION_LUMINANCE;
                        if (luminance < 1) luminance = 1;
                    }
                    return luminance;
                }
        );

        registerDynamicLightHandler(Registration.GREATER_REVELATION_ENTITY,
                entity -> {
                    int luminance = MageFlameDynamicLightsConfig.GREATER_REVELATION_LUMINANCE;
                    if(entity.getLifespan() <= 1200F) {
                        luminance = (entity.getLifespan() / 1200) * MageFlameDynamicLightsConfig.GREATER_REVELATION_LUMINANCE;
                        if (luminance < 1) luminance = 1;
                    }
                    return luminance;
                }
        );
        registerDynamicLightHandler(Registration.WINGED_TORCH_ENTITY,
                DynamicLightHandler.makeHandler(entity -> MageFlameDynamicLightsConfig.WINGED_TORCH_LUMINANCE, entity -> true)
        );

        registerDynamicLightHandler(Registration.EMBER_HOUND_ENTITY,
                entity -> {
                    int luminance = MageFlameDynamicLightsConfig.EMBER_HOUND_LUMINANCE;
                    luminance = (int) (entity.getHealth() / entity.getMaxHealth()) * MageFlameDynamicLightsConfig.EMBER_HOUND_LUMINANCE;
                    if (luminance < 1) luminance = 1;
                    return luminance;
                }
        );

        registerDynamicLightHandler(Registration.BUBBLE_FLAME_ENTITY,
                entity -> {
                    int luminance = MageFlameDynamicLightsConfig.BUBBLE_FLAME_LUMINANCE;
                    if(entity.getLifespan() <= 1200F) {
                        luminance = (entity.getLifespan() / 1200) * MageFlameDynamicLightsConfig.BUBBLE_FLAME_LUMINANCE;
                        if (luminance < 1) luminance = 1;
                    }
                    return luminance;
                }
        );

        registerDynamicLightHandler(Registration.GLOWGLOB_ENTITY,
                entity -> {
                    int luminance = MageFlameDynamicLightsConfig.GLOWGLOB_LUMINANCE;
                    if(entity.getLifespan() <= 1200F) {
                        luminance = (entity.getLifespan() / 1200) * MageFlameDynamicLightsConfig.GLOWGLOB_LUMINANCE;
                        if (luminance < 1) luminance = 1;
                    }
                    return luminance;
                }
        );
    }
}
