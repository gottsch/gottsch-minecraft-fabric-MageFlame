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
package mod.gottsch.fabric.mageflame.core.config;

/**
 *
 */
public class MageFlameConfigs {
    private static SimpleConfig CONFIG;
    public static long MAGE_FLAME_LIFESPAN;
    public static long LESSER_REVELATION_LIFESPAN;
    public static long GREATER_REVELATION_LIFESPAN;

    /**
     *
     */
    public static void register() {
        SimpleConfig.DefaultConfig configs = new SimpleConfig.DefaultConfig() {
            @Override
            public String get(String namespace) {
                return
                        "###############################\n" +
                                "# Flame / Torch Entity Properties\n" +
                                "###############################\n" +
                                "# The lifespan of a Mage Flame spell/entity in ticks.\n" +
                                "# Ex. 20 ticks * 60 seconds * 5 = 6000 = 5 minutes.\n" +
                                "# Default: 12000\n" +
                                "mageFlameLifespan=12000\n" +
                                "# The lifespan of a Lesser Revelation spell/entity in ticks.\n" +
                                "#Default: 18000\n" +
                                "lesserRevelationLifespan=18000\n" +
                                "# The lifespan of a Greater Revelation spell/entity in ticks.\n" +
                                "# Default: 1200 ~ 72000\n" +
                                "greaterRevelationLifespan=36000";
            }
        };

        CONFIG = SimpleConfig.of("mageflame-config").provider(configs).request();
        MAGE_FLAME_LIFESPAN = CONFIG.getOrDefault( "mageFlameLifespan", 12000 );
        LESSER_REVELATION_LIFESPAN = CONFIG.getOrDefault( "lesserRevelationLifespan", 18000 );
        GREATER_REVELATION_LIFESPAN = CONFIG.getOrDefault( "greaterRevelationLifespan", 36000 );
    }
}
