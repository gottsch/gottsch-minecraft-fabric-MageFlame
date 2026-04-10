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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURCoordsE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Mage Flame.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.fabric.mageflame.core.setup;

import mod.gottsch.fabric.mageflame.MageFlame;
import mod.gottsch.fabric.mageflame.core.client.model.entity.*;
import mod.gottsch.fabric.mageflame.core.client.renderer.entity.*;
import mod.gottsch.fabric.mageflame.core.event.ClientHudHandler;
import mod.gottsch.fabric.mageflame.core.network.LifespanUpdateC2S;
import mod.gottsch.fabric.mageflame.core.network.LifespanUpdateS2C;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ClientSetup implements ClientModInitializer {
    public static final EntityModelLayer FLAME_BALL_LAYER = new EntityModelLayer(Identifier.of("mageflame", "flame_ball"), "main");
    public static final EntityModelLayer LARGE_FLAME_BALL_LAYER = new EntityModelLayer(Identifier.of("mageflame", "large_flame_ball"), "main");
    public static final EntityModelLayer WINGED_TORCH_LAYER = new EntityModelLayer(Identifier.of("mageflame", "winged_torch"), "main");
    public static final EntityModelLayer EMBER_HOUND_LAYER = new EntityModelLayer(Identifier.of("mageflame", "ember_hound"), "main");
    public static final EntityModelLayer BUBBLE_FLAME_LAYER = new EntityModelLayer(Identifier.of("mageflame", "bubble_flame_layer"), "main");
    public static final EntityModelLayer GLOWGLOB_LAYER = new EntityModelLayer(Identifier.of(MageFlame.MOD_ID, "glowglob_layer"), "main");


    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(Registration.MAGE_FLAME_ENTITY, MageFlameRenderer::new);
        EntityRendererRegistry.register(Registration.LESSER_REVELATION_ENTITY, LesserRevelationRenderer::new);
        EntityRendererRegistry.register(Registration.GREATER_REVELATION_ENTITY, GreaterRevelationRenderer::new);
        EntityRendererRegistry.register(Registration.WINGED_TORCH_ENTITY, WingedTorchRenderer::new);
        EntityRendererRegistry.register(Registration.EMBER_HOUND_ENTITY, EmberHoundRenderer::new);
        EntityRendererRegistry.register(Registration.BUBBLE_FLAME_ENTITY, BubbleFlameRenderer::new);
        EntityRendererRegistry.register(Registration.GLOWGLOB_ENTITY, GlowglobRenderer::new);
        EntityRendererRegistry.register(Registration.GLOWGLOB_BALL_ENTITY, FlyingItemEntityRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(FLAME_BALL_LAYER, FlameBallModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(LARGE_FLAME_BALL_LAYER, LargeFlameBallModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WINGED_TORCH_LAYER, WingedTorchModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(EMBER_HOUND_LAYER, EmberHoundModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(BUBBLE_FLAME_LAYER, BubbleFlameModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(GLOWGLOB_LAYER, GlowglobModel::getTexturedModelData);
        /* Adds our particle textures to vanilla's Texture Atlas so it can be shown properly.
         * Modify the namespace and particle id accordingly.
         *
         * This is only used if you plan to add your own textures for the particle. Otherwise, remove  this.*/
//        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(((atlasTexture, registry) -> {
//            registry.register(new Identifier(MageFlame.MOD_ID, "particle/revelation_particle"));
//        }));

        // events
        HudRenderCallback.EVENT.register(new ClientHudHandler());

        /* Registers our particle client-side.
         * First argument is our particle's instance, created previously on ExampleMod.
         * Second argument is the particle's factory. The factory controls how the particle behaves.
         * In this example, we'll use FlameParticle's Factory.*/
        ParticleFactoryRegistry.getInstance().register(Registration.REVELATION_PARTICLE, FlameParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(Registration.GREATER_REVELATION_PARTICLE, FlameParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(Registration.BUBBLE_FLAME_PARTICLE, FlameParticle.Factory::new);

        // register receiver handling
        ClientPlayNetworking.registerGlobalReceiver(LifespanUpdateS2C.ID, (payload, context) -> {
            context.client().execute(() -> {
                LifespanUpdateS2C.receive(context.player(), payload.entityId(), payload.lifespan());
            });
        });
    }
}
