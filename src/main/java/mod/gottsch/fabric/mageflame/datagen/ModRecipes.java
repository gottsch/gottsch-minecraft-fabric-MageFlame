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

import mod.gottsch.fabric.mageflame.MageFlame;
import mod.gottsch.fabric.mageflame.core.setup.Registration;
import net.minecraft.data.DataOutput;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

/**
 * 
 * @author Mark Gottschling on Jan 9, 2025
 *
 */
public class ModRecipes extends RecipeProvider {

		public ModRecipes(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registry) {
			super(output, registry);
		}

		@Override
		public void generate(RecipeExporter exporter) {
			ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Registration.MAGE_FLAME_SCROLL, 1)
					.input(Items.TORCH)
					.input(Items.PAPER)
					.criterion(hasItem(Items.TORCH), conditionsFromItem(Items.TORCH))
					.criterion(hasItem(Items.PAPER), conditionsFromItem(Items.PAPER))
					.offerTo(exporter, Identifier.of(MageFlame.MOD_ID, getRecipeName(Registration.MAGE_FLAME_SCROLL)));

			ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Registration.LESSER_REVELATION_SCROLL, 1)
					.input(Items.TORCH)
					.input(Items.PAPER)
					.input(Items.FLINT_AND_STEEL)
					.criterion(hasItem(Items.TORCH), conditionsFromItem(Items.TORCH))
					.criterion(hasItem(Items.PAPER), conditionsFromItem(Items.PAPER))
					.criterion(hasItem(Items.FLINT_AND_STEEL), conditionsFromItem(Items.FLINT_AND_STEEL))
					.offerTo(exporter, Identifier.of(MageFlame.MOD_ID, getRecipeName(Registration.LESSER_REVELATION_SCROLL)));

			ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Registration.GREATER_REVELATION_SCROLL, 1)
					.input(Items.TORCH)
					.input(Items.PAPER)
					.input(Items.FLINT_AND_STEEL)
					.input(Items.GLOWSTONE_DUST)
					.criterion(hasItem(Items.TORCH), conditionsFromItem(Items.TORCH))
					.criterion(hasItem(Items.PAPER), conditionsFromItem(Items.PAPER))
					.criterion(hasItem(Items.FLINT_AND_STEEL), conditionsFromItem(Items.FLINT_AND_STEEL))
					.criterion(hasItem(Items.GLOWSTONE_DUST), conditionsFromItem(Items.GLOWSTONE_DUST))
					.offerTo(exporter, Identifier.of(MageFlame.MOD_ID, getRecipeName(Registration.GREATER_REVELATION_SCROLL)));

			ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, Registration.WINGED_TORCH_SCROLL)
					.pattern(" e ")
					.pattern("ftf")
					.pattern("spb")
					.input('e', Items.SPIDER_EYE)
					.input('f', Items.FEATHER)
					.input('t', Items.TORCH)
					.input('s', Items.FLINT_AND_STEEL)
					.input('p', Items.PAPER)
					.input('b', Items.BLAZE_POWDER)
					.criterion(hasItem(Items.TORCH), conditionsFromItem(Items.TORCH))
					.criterion(hasItem(Items.PAPER), conditionsFromItem(Items.PAPER))
					.criterion(hasItem(Items.FLINT_AND_STEEL), conditionsFromItem(Items.FLINT_AND_STEEL))
					.criterion(hasItem(Items.SPIDER_EYE), conditionsFromItem(Items.SPIDER_EYE))
					.criterion(hasItem(Items.FEATHER), conditionsFromItem(Items.FEATHER))
					.criterion(hasItem(Items.BLAZE_POWDER), conditionsFromItem(Items.BLAZE_POWDER))
					.offerTo(exporter, Identifier.of(MageFlame.MOD_ID, getRecipeName(Registration.WINGED_TORCH_SCROLL)));

			ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, Registration.EMBER_HOUND_SCROLL)
					.pattern(" x ")
					.pattern("ltl")
					.pattern("spb")
					.input('x', Items.LAVA_BUCKET)
					.input('l', Items.LEATHER)
					.input('t', Items.TORCH)
					.input('s', Items.FLINT_AND_STEEL)
					.input('p', Items.PAPER)
					.input('b', Items.BLAZE_POWDER)
					.criterion(hasItem(Items.TORCH), conditionsFromItem(Items.TORCH))
					.criterion(hasItem(Items.PAPER), conditionsFromItem(Items.PAPER))
					.criterion(hasItem(Items.FLINT_AND_STEEL), conditionsFromItem(Items.FLINT_AND_STEEL))
					.criterion(hasItem(Items.LAVA_BUCKET), conditionsFromItem(Items.LAVA_BUCKET))
					.criterion(hasItem(Items.LEATHER), conditionsFromItem(Items.LEATHER))
					.criterion(hasItem(Items.BLAZE_POWDER), conditionsFromItem(Items.BLAZE_POWDER))
					.offerTo(exporter, Identifier.of(MageFlame.MOD_ID, getRecipeName(Registration.EMBER_HOUND_SCROLL)));

		}

}
