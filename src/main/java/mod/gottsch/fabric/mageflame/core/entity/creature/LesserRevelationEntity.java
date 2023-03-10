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
public class LesserRevelationEntity extends SummonFlameBaseEntity {

    public LesserRevelationEntity(EntityType<? extends FlyingEntity> entityType, World level) {
        super(entityType, level, MageFlameConfigs.LESSER_REVELATION_LIFESPAN); //18000);//Config.SERVER.mageFlameLifespan.get());
    }

    @Override
    public @NotNull Block getFlameBlock() {
        return Registration.LESSER_REVELATION_BLOCK;
    }

    @Override
    public void doLivingEffects() {
        double d0 = this.getX();
        double d1 = this.getY() + 0.2;
        double d2 = this.getZ();

        this.world.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        this.world.addParticle(Registration.REVELATION_PARTICLE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        this.world.addParticle(ParticleTypes.SPORE_BLOSSOM_AIR, d0, d1, d2, 0.0D, 0.0D, 0.0D);

    }
}