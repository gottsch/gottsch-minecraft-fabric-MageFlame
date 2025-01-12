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

import mod.gottsch.fabric.mageflame.core.entity.creature.SummonedLightSourcePathAwareEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

/**
 *
 * @author Mark Gottschling Jan 11, 2025
 *
 */
public class FireWolfModel<T extends SummonedLightSourcePathAwareEntity> extends EntityModel<T> {

	private final ModelPart head;
	private final ModelPart torso;
	private final ModelPart neck;
	private final ModelPart rightHindLeg;
	private final ModelPart leftHindLeg;
	private final ModelPart rightFrontLeg;
	private final ModelPart leftFrontLeg;
	private final ModelPart tail;

	private float bodyY;

	/**
	 *
	 * @param root
	 */
	public FireWolfModel(ModelPart root) {
		this.head = root.getChild("head");
		this.torso = root.getChild("body");
		this.neck = root.getChild("upperBody");
		this.rightHindLeg = root.getChild("rightRearLeg");
		this.leftHindLeg = root.getChild("leftRearLeg");
		this.rightFrontLeg = root.getChild("rightFrontLeg");
		this.leftFrontLeg = root.getChild("leftFrontLeg");
		this.tail = root.getChild("tail");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -4.0F, -4.0F, 6.0F, 6.0F, 4.0F, new Dilation(0.0F))
				.uv(16, 14).cuboid(-3.0F, -6.0F, -2.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
				.uv(16, 14).cuboid(1.0F, -6.0F, -2.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
				.uv(0, 10).cuboid(-1.5F, -1.0156F, -7.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, 13.5F, -5.0F, 0.1745F, 0.0F, 0.0F));

		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(18, 14).cuboid(-4.0F, -2.0F, -3.0F, 6.0F, 9.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 14.0F, 2.0F, 1.5708F, 0.0F, 0.0F));
		ModelPartData upperBody = modelPartData.addChild("upperBody", ModelPartBuilder.create().uv(21, 0).cuboid(-4.0F, -8.0F, -3.0F, 8.0F, 6.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, 14.0F, 2.0F, 1.5708F, 0.0F, 0.0F));
		ModelPartData rightRearLeg = modelPartData.addChild("rightRearLeg", ModelPartBuilder.create().uv(0, 18).mirrored().cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-2.5F, 16.0F, 7.0F));
		ModelPartData leftRearLeg = modelPartData.addChild("leftRearLeg", ModelPartBuilder.create().uv(0, 18).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.5F, 16.0F, 7.0F));
		ModelPartData rightFrontLeg = modelPartData.addChild("rightFrontLeg", ModelPartBuilder.create().uv(0, 18).mirrored().cuboid(-2.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-2.5F, 16.0F, -4.0F));
		ModelPartData leftFrontLeg = modelPartData.addChild("leftFrontLeg", ModelPartBuilder.create().uv(0, 18).cuboid(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.5F, 16.0F, -4.0F));
		ModelPartData tail = modelPartData.addChild("tail", ModelPartBuilder.create().uv(9, 18).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, 12.0F, 8.0F, 0.9599F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 32);
	}

	@Override
	public void animateModel(T wolfEntity, float f, float g, float h) {
		this.tail.yaw = MathHelper.cos(f * 0.6662F) * 1.4F * g;

//		this.torso.setPivot(0.0F, 14.0F, 2.0F);
//		this.torso.pitch = ((float)Math.PI / 2F);
//		this.neck.setPivot(-1.0F, 14.0F, -3.0F);
//		this.neck.pitch = this.torso.pitch;
		this.tail.setPivot(-1.0F, 12.0F, 8.0F);
		this.rightHindLeg.setPivot(-2.5F, 16.0F, 7.0F);
		this.leftHindLeg.setPivot(0.5F, 16.0F, 7.0F);
		this.rightFrontLeg.setPivot(-2.5F, 16.0F, -4.0F);
		this.leftFrontLeg.setPivot(0.5F, 16.0F, -4.0F);
		this.rightHindLeg.pitch = MathHelper.cos(f * 0.6662F) * 1.4F * g;
		this.leftHindLeg.pitch = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * g;
		this.rightFrontLeg.pitch = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * g;
		this.leftFrontLeg.pitch = MathHelper.cos(f * 0.6662F) * 1.4F * g;

	}

	@Override
	public void setAngles(SummonedLightSourcePathAwareEntity entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch) {
		this.head.pitch = headPitch * ((float)Math.PI / 180F);
		this.head.yaw = headYaw * ((float)Math.PI / 180F);
//		this.tail.pitch = age;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
		head.render(matrices, vertexConsumer, light, overlay, color);
		torso.render(matrices, vertexConsumer, light, overlay, color);
		neck.render(matrices, vertexConsumer, light, overlay, color);
		rightHindLeg.render(matrices, vertexConsumer, light, overlay, color);
		leftHindLeg.render(matrices, vertexConsumer, light, overlay, color);
		rightFrontLeg.render(matrices, vertexConsumer, light, overlay, color);
		leftFrontLeg.render(matrices, vertexConsumer, light, overlay, color);
		tail.render(matrices, vertexConsumer, light, overlay, color);
	}

	public static void bob(ModelPart part, float originY, float age) {
		part.pivotY = originY + (MathHelper.cos(age * 0.25F) * 0.5F + 0.05F);
	}
}