package mod.gottsch.fabric.mageflame.core.client.renderer.entity;

import mod.gottsch.fabric.mageflame.core.client.model.entity.FlameBallModel;
import mod.gottsch.fabric.mageflame.core.client.renderer.entity.layer.MageFlameGlowFeatureRenderer;
import mod.gottsch.fabric.mageflame.core.entity.creature.MageFlameEntity;
import mod.gottsch.fabric.mageflame.core.setup.ClientSetup;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

/*
 * A renderer is used to provide an entity model, shadow size, and texture.
 */
public class MageFlameRenderer<T extends MageFlameEntity> extends MobEntityRenderer<T, FlameBallModel<T>> {

    public MageFlameRenderer(EntityRendererFactory.Context context) {
        super(context, new FlameBallModel<>(context.getPart(ClientSetup.FLAME_BALL_LAYER)), 0.5f);
        addFeature(new MageFlameGlowFeatureRenderer<>(this));
    }

    @Override
    public Identifier getTexture(MageFlameEntity entity) {
        return new Identifier("mageflame", "textures/entity/mage_flame.png");
    }
}
