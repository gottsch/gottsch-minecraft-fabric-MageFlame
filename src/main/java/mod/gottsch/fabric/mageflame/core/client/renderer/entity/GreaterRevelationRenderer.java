/*
 * This file is part of Mage Flame.
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
package mod.gottsch.fabric.mageflame.core.client.renderer.entity;

import mod.gottsch.fabric.mageflame.core.client.model.entity.LargeFlameBallModel;
import mod.gottsch.fabric.mageflame.core.client.renderer.entity.layer.GreaterRevelationGlowFeatureRenderer;
import mod.gottsch.fabric.mageflame.core.entity.creature.GreaterRevelationEntity;
import mod.gottsch.fabric.mageflame.core.setup.ClientSetup;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

/*
 * A renderer is used to provide an entity model, shadow size, and texture.
 */
public class GreaterRevelationRenderer<T extends GreaterRevelationEntity> extends MobEntityRenderer<T, LargeFlameBallModel<T>> {

    public GreaterRevelationRenderer(EntityRendererFactory.Context context) {
        super(context, new LargeFlameBallModel<>(context.getPart(ClientSetup.LARGE_FLAME_BALL_LAYER)), 0);
        this.addFeature(new GreaterRevelationGlowFeatureRenderer<>(this));
    }

    @Override
    public Identifier getTexture(GreaterRevelationEntity entity) {
        return Identifier.of("mageflame", "textures/entity/greater_revelation.png");
    }
}
