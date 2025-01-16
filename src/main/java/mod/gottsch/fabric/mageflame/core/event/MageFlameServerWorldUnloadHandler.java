package mod.gottsch.fabric.mageflame.core.event;

import mod.gottsch.fabric.gottschcore.spatial.Coords;
import mod.gottsch.fabric.mageflame.MageFlame;
import mod.gottsch.fabric.mageflame.core.entity.creature.ISummonedEntity;
import mod.gottsch.fabric.mageflame.core.peristence.PlayerData;
import mod.gottsch.fabric.mageflame.core.peristence.StateSaverAndLoader;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

/**
 * Created by Mark Gottschling on 1/14/2025
 */
public class MageFlameServerWorldUnloadHandler implements ServerEntityEvents.Unload {

    @Override
    public void onUnload(Entity entity, ServerWorld world) {
        if (world.isClient()) {
            return;
        }

        if (entity instanceof PlayerEntity player) {
            MageFlame.LOGGER.info("player entity leaving world -> {}", entity.getName().getString());
            PlayerData playerData = StateSaverAndLoader.getPlayerState((LivingEntity) entity);
            MageFlame.LOGGER.info("unload playerData -> {}", playerData);
            playerData.getKeys().forEach(modId -> {
                MageFlame.LOGGER.info("saving player -> {}", player.getUuid());
                // get the entity from the world
                Entity mob = world.getEntity(modId);
                if (mob instanceof ISummonedEntity lightSourceEntity) {
                    MageFlame.LOGGER.info("got world mob");
                    // update player's entities
                    playerData.get(modId).ifPresent(data -> {
                        data.setLifespan(lightSourceEntity.getLifespan());
                        data.setCoords(Coords.of(mob.getBlockPos()));
                        MageFlame.LOGGER.info("saving mob leaving world  -> {}", data);

                    });
                    // kill mob
                    mob.kill();
                } else {
                    MageFlame.LOGGER.info("unregistering mob -> {}", modId);
                    // can't find mob so unregister
                    playerData.unregister(modId);
                }
            });
            MageFlame.LOGGER.info("unload playerData end state -> {}", playerData);
            StateSaverAndLoader.getServerState(world.getServer()).markDirty();
        }
    }
}
