package mod.gottsch.fabric.mageflame.core.event;

import mod.gottsch.fabric.mageflame.core.entity.creature.ISummonFlameBaseEntity;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

import java.util.UUID;

public class SummonFlameServerEntityLoadHandler implements ServerEntityEvents.Load {

    @Override
    public void onLoad(Entity entity, ServerWorld world) {
        // this is probaby moot, but just being cautious
        if (!world.isClient()) {
            if (entity instanceof ISummonFlameBaseEntity) {
                entity.sendSystemMessage(Text.of("Hello"), UUID.randomUUID());
            }
        }
    }
}
