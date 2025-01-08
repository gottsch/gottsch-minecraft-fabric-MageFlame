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
package mod.gottsch.fabric.mageflame.core.client.renderer.entity;

import mod.gottsch.fabric.mageflame.core.client.model.entity.FlameBallModel;
import mod.gottsch.fabric.mageflame.core.client.renderer.entity.layer.GreaterRevelationGlowFeatureRenderer;
import mod.gottsch.fabric.mageflame.core.client.renderer.entity.layer.MageFlameGlowFeatureRenderer;
import mod.gottsch.fabric.mageflame.core.entity.creature.MageFlameEntity;
import mod.gottsch.fabric.mageflame.core.setup.ClientSetup;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;

/*
 * A renderer is used to provide an entity model, shadow size, and texture.
 */
public class MageFlameRenderer<T extends MageFlameEntity> extends MobEntityRenderer<T, LivingEntityRenderState, FlameBallModel> {

    public MageFlameRenderer(EntityRendererFactory.Context context) {
        super(context, new FlameBallModel(context.getPart(ClientSetup.FLAME_BALL_LAYER)), 0.5f);
        this.addFeature(new MageFlameGlowFeatureRenderer<>(this));
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public Identifier getTexture(LivingEntityRenderState state) {
        return Identifier.of("mageflame", "textures/entity/mage_flame.png");
    }
}
