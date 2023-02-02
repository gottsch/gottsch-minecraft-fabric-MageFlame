package mod.gottsch.fabric.mageflame.core.setup;

import mod.gottsch.fabric.mageflame.core.client.model.entity.CubeEntityModel;
import mod.gottsch.fabric.mageflame.core.client.model.entity.FlameBallModel;
import mod.gottsch.fabric.mageflame.core.client.model.entity.LargeFlameBallModel;
import mod.gottsch.fabric.mageflame.core.client.model.entity.WingedTorchModel;
import mod.gottsch.fabric.mageflame.core.client.renderer.entity.CubeEntityRenderer;
import mod.gottsch.fabric.mageflame.core.client.renderer.entity.GreaterRevelationRenderer;
import mod.gottsch.fabric.mageflame.core.client.renderer.entity.LesserRevelationRenderer;
import mod.gottsch.fabric.mageflame.core.client.renderer.entity.MageFlameRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ClientSetup implements ClientModInitializer {
    public static final EntityModelLayer MODEL_CUBE_LAYER = new EntityModelLayer(new Identifier("mageflame", "cube"), "main");
    public static final EntityModelLayer FLAME_BALL_LAYER = new EntityModelLayer(new Identifier("mageflame", "flame_ball"), "main");
    public static final EntityModelLayer LARGE_FLAME_BALL_LAYER = new EntityModelLayer(new Identifier("mageflame", "large_flame_ball"), "main");
    public static final EntityModelLayer WINGED_TORCH_LAYER = new EntityModelLayer(new Identifier("mageflame", "winged_torch"), "main");

    @Override
    public void onInitializeClient() {
        /*
         * Registers our Cube Entity's renderer, which provides a model and texture for the entity.
         *
         * Entity Renderers can also manipulate the model before it renders based on entity context (EndermanEntityRenderer#render).
         */
        EntityRendererRegistry.register(Registration.CUBE, (context) -> {
            return new CubeEntityRenderer(context);
        });

        EntityRendererRegistry.register(Registration.MAGE_FLAME_ENTITY, (context) -> {
            return new MageFlameRenderer(context);
        });

        EntityRendererRegistry.register(Registration.LESSER_REVELATION_ENTITY, (context) -> {
            return new LesserRevelationRenderer(context);
        });

        EntityRendererRegistry.register(Registration.GREATER_REVELATION_ENTITY, (context) -> {
            return new GreaterRevelationRenderer(context);
        });


        EntityModelLayerRegistry.registerModelLayer(MODEL_CUBE_LAYER, CubeEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(FLAME_BALL_LAYER, FlameBallModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(LARGE_FLAME_BALL_LAYER, LargeFlameBallModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WINGED_TORCH_LAYER, WingedTorchModel::getTexturedModelData);
    }
}
