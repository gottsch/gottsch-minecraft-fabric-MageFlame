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
package mod.gottsch.fabric.mageflame.core.client.renderer.entity.layer;

import mod.gottsch.fabric.mageflame.core.client.model.entity.WingedTorchModel;
import mod.gottsch.fabric.mageflame.core.entity.creature.SummonFlameBaseEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.util.Identifier;

/**
 * 
 * @author Mark Gottschling May 8, 2023
 *
 */
public class WingedTorchFlameFeatureRenderer<T extends SummonFlameBaseEntity, M extends WingedTorchModel<T>> extends EyesFeatureRenderer<T, M> {
	private static final RenderLayer SKIN = RenderLayer.getEyes(new Identifier("mageflame","textures/entity/winged_torch_flame.png"));

	public WingedTorchFlameFeatureRenderer(FeatureRendererContext<T, M> featureRendererContext) {
		super(featureRendererContext);
	}

	@Override
	public RenderLayer getEyesTexture() {
		return SKIN;
	}
}
