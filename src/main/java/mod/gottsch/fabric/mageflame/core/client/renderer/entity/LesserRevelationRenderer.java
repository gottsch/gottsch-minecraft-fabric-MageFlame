package mod.gottsch.fabric.mageflame.core.client.renderer.entity;

import mod.gottsch.fabric.mageflame.core.client.model.entity.FlameBallModel;
import mod.gottsch.fabric.mageflame.core.entity.creature.LesserRevelationEntity;
import mod.gottsch.fabric.mageflame.core.entity.creature.MageFlameEntity;
import mod.gottsch.fabric.mageflame.core.setup.ClientSetup;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

/*
 * A renderer is used to provide an entity model, shadow size, and texture.
 */
public class LesserRevelationRenderer<T extends LesserRevelationEntity> extends MobEntityRenderer<T, FlameBallModel<T>> {

    public LesserRevelationRenderer(EntityRendererFactory.Context context) {
        super(context, new FlameBallModel(context.getPart(ClientSetup.FLAME_BALL_LAYER)), 0.5f);
    }

    @Override
    public Identifier getTexture(LesserRevelationEntity entity) {
        return new Identifier("mageflame", "textures/entity/lesser_revelation.png");
    }
}
