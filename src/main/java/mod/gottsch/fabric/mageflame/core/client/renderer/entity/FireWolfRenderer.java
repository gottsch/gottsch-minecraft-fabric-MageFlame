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
package mod.gottsch.fabric.mageflame.core.client.renderer.entity;

import mod.gottsch.fabric.mageflame.core.client.model.entity.FireWolfModel;
import mod.gottsch.fabric.mageflame.core.client.renderer.entity.layer.FireWolfFlameFeatureRenderer;
import mod.gottsch.fabric.mageflame.core.entity.creature.FireWolfEntity;
import mod.gottsch.fabric.mageflame.core.setup.ClientSetup;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

/*
 * A renderer is used to provide an entity model, shadow size, and texture.
 */
public class FireWolfRenderer<T extends FireWolfEntity> extends MobEntityRenderer<T, FireWolfModel<T>> {

    public FireWolfRenderer(EntityRendererFactory.Context context) {
        super(context, new FireWolfModel<>(context.getPart(ClientSetup.FIRE_WOLF_LAYER)), 0);
//        this.addFeature(new FireWolfFlameFeatureRenderer<>(this));
    }

    @Override
    public Identifier getTexture(FireWolfEntity entity) {
        return Identifier.of("mageflame", "textures/entity/fire_wolf.png");
    }
}
