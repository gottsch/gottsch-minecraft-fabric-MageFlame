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
package mod.gottsch.fabric.mageflame.core.client.renderer.entity;

import mod.gottsch.fabric.mageflame.core.client.model.entity.GlowglobModel;
import mod.gottsch.fabric.mageflame.core.entity.creature.GlowglobEntity;
import mod.gottsch.fabric.mageflame.core.setup.ClientSetup;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

/**
 * @author Mark Gottschling on 1/21/2025
 */
public class GlowglobRenderer extends MobEntityRenderer<GlowglobEntity, GlowglobModel<GlowglobEntity>> {

    public GlowglobRenderer(EntityRendererFactory.Context context) {
        super(context, new GlowglobModel<>(context.getPart(ClientSetup.GLOWGLOB_LAYER)), 0);
    }

    @Override
    public Identifier getTexture(GlowglobEntity entity) {
        return Identifier.of("mageflame", "textures/entity/glowglob.png");
    }
}
