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
package mod.gottsch.fabric.mageflame.core.network;

import mod.gottsch.fabric.mageflame.MageFlame;
import net.minecraft.util.Identifier;

/**
 * @author Mark Gottschling on 1/24/2025
 */
public class ModNetwork {
    public static final Identifier LIFESPAN_UPDATE_C2S_ID = Identifier.of(MageFlame.MOD_ID, "lifespan_c2s");
    public static final Identifier LIFESPAN_UPDATE_S2C_ID = Identifier.of(MageFlame.MOD_ID, "lifespan_s2c");

}
