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
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

/**
 * 
 * @author Mark Gottschling on Jan 9, 2025
 *
 */
public class ModModelProvider extends FabricModelProvider {

	public ModModelProvider(FabricDataOutput output) {
		super(output);
	}


	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
	}

	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
		// scrolls
		itemModelGenerator.register(Registration.MAGE_FLAME_SCROLL, Models.GENERATED);
		itemModelGenerator.register(Registration.LESSER_REVELATION_SCROLL, Models.GENERATED);
		itemModelGenerator.register(Registration.GREATER_REVELATION_SCROLL, Models.GENERATED);
		itemModelGenerator.register(Registration.WINGED_TORCH_SCROLL, Models.GENERATED);
		itemModelGenerator.register(Registration.EMBER_HOUND_SCROLL, Models.GENERATED);
		itemModelGenerator.register(Registration.BUBBLE_FLAME_SCROLL, Models.GENERATED);

	}
}
