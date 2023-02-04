package mod.gottsch.fabric.mageflame.core.client.renderer.entity;

import mod.gottsch.fabric.mageflame.core.client.model.entity.FlameBallModel;
import mod.gottsch.fabric.mageflame.core.client.model.entity.LargeFlameBallModel;
import mod.gottsch.fabric.mageflame.core.entity.creature.GreaterRevelationEntity;
import mod.gottsch.fabric.mageflame.core.entity.creature.LesserRevelationEntity;
import mod.gottsch.fabric.mageflame.core.setup.ClientSetup;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

/*
 * A renderer is used to provide an entity model, shadow size, and texture.
 */
public class GreaterRevelationRenderer<T extends GreaterRevelationEntity> extends MobEntityRenderer<T, LargeFlameBallModel<T>> {

    public GreaterRevelationRenderer(EntityRendererFactory.Context context) {
        super(context, new LargeFlameBallModel(context.getPart(ClientSetup.LARGE_FLAME_BALL_LAYER)), 0.5f);
    }

    @Override
    public Identifier getTexture(GreaterRevelationEntity entity) {
        return new Identifier("mageflame", "textures/entity/greater_revelation.png");
    }
}
