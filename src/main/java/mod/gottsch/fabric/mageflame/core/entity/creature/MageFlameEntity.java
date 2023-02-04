package mod.gottsch.fabric.mageflame.core.entity.creature;

import mod.gottsch.fabric.mageflame.core.config.MageFlameConfigs;
import mod.gottsch.fabric.mageflame.core.setup.Registration;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class MageFlameEntity extends SummonFlameBaseEntity {

    public MageFlameEntity(EntityType<? extends FlyingEntity> entityType, World level) {
        super(entityType, level,  MageFlameConfigs.MAGE_FLAME_LIFESPAN); //12000);//Config.SERVER.mageFlameLifespan.get());
    }

    @Override
    public @NotNull Block getFlameBlock() {
        return Registration.MAGE_FLAME_BLOCK;
    }

    @Override
    public void doLivingEffects() {
        double d0 = this.getX();
        double d1 = this.getY() + 0.2;
        double d2 = this.getZ();
        this.world.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        this.world.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }
}