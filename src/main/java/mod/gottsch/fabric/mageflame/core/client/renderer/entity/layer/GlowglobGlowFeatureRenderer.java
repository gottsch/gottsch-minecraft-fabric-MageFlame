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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Mage Flame.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.fabric.mageflame.core.client.renderer.entity.layer;

import mod.gottsch.fabric.mageflame.core.client.model.entity.GlowglobModel;
import mod.gottsch.fabric.mageflame.core.client.model.entity.LargeFlameBallModel;
import mod.gottsch.fabric.mageflame.core.entity.creature.GlowglobEntity;
import mod.gottsch.fabric.mageflame.core.entity.creature.SummonedFlyingEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.util.Identifier;

/**
 * 
 * @author Mark Gottschling Jan 23, 2025
 *
 */
public class GlowglobGlowFeatureRenderer<T extends GlowglobEntity, M extends GlowglobModel<T>> extends EyesFeatureRenderer<T, M> {
	private static final RenderLayer SKIN = RenderLayer.getEyes(Identifier.of("mageflame","textures/entity/glowglob_glow.png"));
	public GlowglobGlowFeatureRenderer(FeatureRendererContext<T, M> featureRendererContext) {
		super(featureRendererContext);
	}

	@Override
	public RenderLayer getEyesTexture() {
		return SKIN;
	}
}
