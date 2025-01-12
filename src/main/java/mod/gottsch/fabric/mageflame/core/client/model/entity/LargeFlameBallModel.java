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
package mod.gottsch.fabric.mageflame.core.client.model.entity;

import mod.gottsch.fabric.mageflame.core.entity.creature.SummonedLightSourceFlyingEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

/**
 *
 */
public class LargeFlameBallModel<T extends SummonedLightSourceFlyingEntity> extends EntityModel<T> {
	private final ModelPart main;
	private final float bodyY;
	private final float scale;

	/**
	 *
	 * @param root
	 */
	public LargeFlameBallModel(ModelPart root) {
		this.main = root.getChild("main");
		this.bodyY = main.pivotY;
		this.scale = main.xScale;
	}

	/**
	 *
	 * @return
	 */
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData bb_main = modelPartData.addChild("main", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -3.0F, -1.0F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		return TexturedModelData.of(modelData, 16, 16);
	}

	@Override
	public void setAngles(SummonedLightSourceFlyingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		bob(this.main, bodyY, ageInTicks);
		pulse(this.main, scale, ageInTicks);
	}

	public static void bob(ModelPart part, float originY, float age) {
		part.pivotY = originY + (MathHelper.cos(age * 0.25F) * 0.5F + 0.05F);
	}

	public static void pulse(ModelPart part, float scale, float age) {
		float changeScale = MathHelper.cos(age * 0.1F) * 0.25F + 0.05F;
		part.xScale = scale + changeScale;
		part.zScale = scale + changeScale;
		part.yScale = scale + changeScale;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
		main.render(matrices, vertexConsumer, light, overlay, color);
	}
}