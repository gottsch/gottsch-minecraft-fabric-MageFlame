package mod.gottsch.fabric.mageflame.core.setup;

import mod.gottsch.fabric.mageflame.MageFlame;
import mod.gottsch.fabric.mageflame.core.client.model.entity.FlameBallModel;
import mod.gottsch.fabric.mageflame.core.client.model.entity.LargeFlameBallModel;
import mod.gottsch.fabric.mageflame.core.client.model.entity.WingedTorchModel;
import mod.gottsch.fabric.mageflame.core.client.renderer.entity.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ClientSetup implements ClientModInitializer {
     public static final EntityModelLayer FLAME_BALL_LAYER = new EntityModelLayer(new Identifier("mageflame", "flame_ball"), "main");
    public static final EntityModelLayer LARGE_FLAME_BALL_LAYER = new EntityModelLayer(new Identifier("mageflame", "large_flame_ball"), "main");
    public static final EntityModelLayer WINGED_TORCH_LAYER = new EntityModelLayer(new Identifier("mageflame", "winged_torch"), "main");

    @Override
    public void onInitializeClient() {

        EntityRendererRegistry.register(Registration.MAGE_FLAME_ENTITY, MageFlameRenderer::new);
        EntityRendererRegistry.register(Registration.LESSER_REVELATION_ENTITY, LesserRevelationRenderer::new);
        EntityRendererRegistry.register(Registration.GREATER_REVELATION_ENTITY, GreaterRevelationRenderer::new);
        EntityRendererRegistry.register(Registration.WINGED_TORCH_ENTITY, WingedTorchRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(FLAME_BALL_LAYER, FlameBallModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(LARGE_FLAME_BALL_LAYER, LargeFlameBallModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WINGED_TORCH_LAYER, WingedTorchModel::getTexturedModelData);

        /* Adds our particle textures to vanilla's Texture Atlas so it can be shown properly.
         * Modify the namespace and particle id accordingly.
         *
         * This is only used if you plan to add your own textures for the particle. Otherwise, remove  this.*/
//        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(((atlasTexture, registry) -> {
//            registry.register(new Identifier(MageFlame.MOD_ID, "particle/revelation_particle"));
//        }));

        /* Registers our particle client-side.
         * First argument is our particle's instance, created previously on ExampleMod.
         * Second argument is the particle's factory. The factory controls how the particle behaves.
         * In this example, we'll use FlameParticle's Factory.*/
        ParticleFactoryRegistry.getInstance().register(Registration.REVELATION_PARTICLE, FlameParticle.Factory::new);
    }
}
