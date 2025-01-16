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
import mod.gottsch.fabric.mageflame.MageFlame;

import static dev.lambdaurora.lambdynlights.api.DynamicLightHandlers.registerDynamicLightHandler;


/**
 * Created by Mark Gottschling on 1/11/2025
 */
public class DynamicLights implements DynamicLightsInitializer {

    @Override
    public void onInitializeDynamicLights(ItemLightSourceManager itemLightSourceManager) {
        registerDynamicLightHandler(Registration.MAGE_FLAME_ENTITY,
                DynamicLightHandler.makeHandler(firewolf -> 11, firewolf -> true)
        );
        registerDynamicLightHandler(Registration.LESSER_REVELATION_ENTITY,
                DynamicLightHandler.makeHandler(firewolf -> 13, firewolf -> true)
        );
        registerDynamicLightHandler(Registration.GREATER_REVELATION_ENTITY,
                DynamicLightHandler.makeHandler(firewolf -> 15, firewolf -> true)
        );
        registerDynamicLightHandler(Registration.WINGED_TORCH_ENTITY,
                DynamicLightHandler.makeHandler(firewolf -> 15, firewolf -> true)
        );

        registerDynamicLightHandler(Registration.FIRE_WOLF_ENTITY,
                entity -> {
                    int luminance = 15;
                    if(entity.getLifespan() < 1500F) {
                        luminance = (int) (entity.getLifespan() / MageFlame.CONFIG.emberHoundLifespan());
                        if (luminance < 1) luminance = 1;
                    }
                    return luminance;
                }
        );
    }
}
