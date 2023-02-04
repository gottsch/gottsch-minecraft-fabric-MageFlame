package mod.gottsch.fabric.mageflame.core.client.renderer.entity;

import mod.gottsch.fabric.mageflame.core.client.model.entity.FlameBallModel;
import mod.gottsch.fabric.mageflame.core.client.model.entity.WingedTorchModel;
import mod.gottsch.fabric.mageflame.core.entity.creature.MageFlameEntity;
import mod.gottsch.fabric.mageflame.core.entity.creature.WingedTorchEntity;
import mod.gottsch.fabric.mageflame.core.setup.ClientSetup;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

/*
 * A renderer is used to provide an entity model, shadow size, and texture.
 */
public class WingedTorchRenderer<T extends WingedTorchEntity> extends MobEntityRenderer<T, WingedTorchModel<T>> {

    public WingedTorchRenderer(EntityRendererFactory.Context context) {
        super(context, new WingedTorchModel<>(context.getPart(ClientSetup.WINGED_TORCH_LAYER)), 0.5f);
    }

    @Override
    public Identifier getTexture(WingedTorchEntity entity) {
        return new Identifier("mageflame", "textures/entity/winged_torch.png");
    }
}
