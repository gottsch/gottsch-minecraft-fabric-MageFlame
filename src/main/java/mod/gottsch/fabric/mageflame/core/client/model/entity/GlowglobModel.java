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
package mod.gottsch.fabric.mageflame.core.client.model.entity;

import mod.gottsch.fabric.mageflame.core.entity.creature.SummonedFlyingEntity;
import mod.gottsch.fabric.mageflame.core.entity.creature.SummonedPathAwareEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.MathHelper;

/**
 * @author Mark Gottschling on 1/21/2025
 */
public class GlowglobModel<T extends MobEntity> extends EntityModel<T> {
	private final ModelPart main;
	private final float bodyY;
	private final float scale;

	/**
	 *
	 * @param root
	 */
	public GlowglobModel(ModelPart root) {
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
		ModelPartData main = modelPartData.addChild("main", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -30.0F, -1.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F))
				.uv(0, 9).cuboid(-3.0F, -26.0F, -1.0F, 4.0F, 2.0F, 4.0F, new Dilation(0.0F))
				.uv(0, 16).cuboid(-1.5F, -26.0F, 0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(1.0F, 50.0F, -1.0F));
		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public void setAngles(MobEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		bob(this.main, 0.15F, ageInTicks);
		pulse(this.main, 0.05F, ageInTicks);
	}

	public void bob(ModelPart part, float bobAmount, float age) {
		part.pivotY = this.bodyY + (MathHelper.cos(age * 0.25F) * bobAmount + 0.05F);
	}

	public void pulse(ModelPart part, float scale, float age) {
		float changeScale = MathHelper.cos(age * 0.1F) * scale + 0.05F;
		part.xScale = this.scale + changeScale;
		part.zScale = this.scale + changeScale;
		part.yScale = this.scale + changeScale;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
		main.render(matrices, vertexConsumer, light, overlay, color);
	}
}