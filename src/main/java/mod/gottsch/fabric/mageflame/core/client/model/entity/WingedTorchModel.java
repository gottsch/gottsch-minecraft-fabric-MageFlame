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

import mod.gottsch.fabric.mageflame.core.entity.creature.SummonFlameBaseEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

/**
 *
 */
public class WingedTorchModel<T extends SummonFlameBaseEntity> extends EntityModel<T> {
	private static final String RIGHT_WING_TIP = "rightWingTip";

	private final ModelPart main;
	private final ModelPart wings;
	private final ModelPart rightWing;
	private final ModelPart rightTip;
	private final ModelPart leftWing;
	private final ModelPart leftTip;

	private float bodyY;

	/**
	 *
	 * @param root
	 */
	public WingedTorchModel(ModelPart root) {
		super(RenderLayer::getEntityCutout);
		this.main = root.getChild("main");
		this.wings = main.getChild("wings");
		this.rightWing = wings.getChild("rightWing");
		this.leftWing = wings.getChild("leftWing");
		this.rightTip = rightWing.getChild("rightTip");
		this.leftTip = leftWing.getChild("leftTip");

		bodyY = main.pivotY;
	}

	/**
	 *
	 */
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		ModelPartData main = modelPartData.addChild("main", ModelPartBuilder.create(), ModelTransform.of(0.0F, 24.0F, 0.0F, 0.0873F, 0.0F, 0.0F));
		ModelPartData torch = main.addChild("torch", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData wings = main.addChild("wings", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData rightWing = wings.addChild("rightWing", ModelPartBuilder.create().uv(10, 1).mirrored().cuboid(-5.0F, -0.5F, 0.0F, 5.0F, 5.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, -6.5F, 1.0F, 0.0F, 0.1745F, 0.0873F));
		ModelPartData rightTip = rightWing.addChild("rightTip", ModelPartBuilder.create().uv(1, 14).mirrored().cuboid(-3.0F, -2.0F, -0.5F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-5.0F, 1.5F, 0.5F, 0.0F, -0.2618F, 0.0F));
		ModelPartData leftWing = wings.addChild("leftWing", ModelPartBuilder.create().uv(10, 1).cuboid(0.0F, -0.5F, 0.0F, 5.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -6.5F, 1.0F, 0.0F, -0.1745F, -0.0873F));
		ModelPartData leftTip = leftWing.addChild("leftTip", ModelPartBuilder.create().uv(1, 14).cuboid(0.0F, -2.0F, 0.0F, 3.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, 1.5F, 0.0F, 0.0F, 0.2618F, 0.0F));
		return TexturedModelData.of(modelData, 32, 32);
	}

	/**
	 * pitch = x-axis;
	 * yaw = y-axis;
	 * roll = z-axis;
	 *
	 * @param entity
	 * @param limbSwing
	 * @param limbSwingAmount
	 * @param ageInTicks
	 * @param netHeadYaw
	 * @param headPitch
	 */
	@Override
	public void setAngles(SummonFlameBaseEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		// flag wings
		float armSpeed = 0.35F;
		this.rightWing.yaw = /*0.47123894F + */MathHelper.cos(ageInTicks * armSpeed) * (float)Math.PI * 0.05F;
		this.leftWing.yaw = -this.rightWing.yaw;
		this.leftWing.pitch = 0.47123894F;
		this.rightWing.pitch = 0.47123894F;

		bob(main, bodyY, ageInTicks);
	}

	public static void bob(ModelPart part, float originY, float age) {
		part.pivotY = originY + (MathHelper.cos(age * 0.25F) * 0.5F + 0.05F);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}