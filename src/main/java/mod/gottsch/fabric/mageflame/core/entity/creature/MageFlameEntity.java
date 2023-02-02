package mod.gottsch.fabric.mageflame.core.entity.creature;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;

/**
 *
 */
public class MageFlameEntity extends SummonFlameBaseEntity {

    public MageFlameEntity(EntityType<? extends FlyingEntity> entityType, World world) {

        super(entityType, world);
    }

}