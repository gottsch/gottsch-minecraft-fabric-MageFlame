package mod.gottsch.fabric.mageflame.core.entity.creature;

import mod.gottsch.fabric.mageflame.MageFlame;
import mod.gottsch.fabric.mageflame.core.setup.Registration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class GreaterRevelationEntity extends SummonFlameBaseEntity {

    public GreaterRevelationEntity(EntityType<? extends FlyingEntity> entityType, World level) {
        super(entityType, level, MageFlame.CONFIG.greaterRevelationLifespan());
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK);
    }

    @Override
    public @NotNull Block getFlameBlock() {
        return Registration.GREATER_REVELATION_BLOCK;
    }

    @Override
    public void doLivingEffects() {
        double d1 = this.getY() + 0.2;
        for (int i=0; i < 2; i++) {
            double d0 = this.getRandomX(0.5);
            double d2 = this.getRandomZ(0.5);
            this.getWorld().addParticle(Registration.REVELATION_PARTICLE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
        double d0 = this.getX(0.5);
        double d2 = this.getZ(0.5);
        this.getWorld().addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        this.getWorld().addParticle(ParticleTypes.SPORE_BLOSSOM_AIR, d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }

    /**
     * not sure if there exists an equivalent method of these in fabric
     */
    public double getX(double factor) {
        return this.getPos().x + (double)this.getWidth() * factor;
    }

    public double getRandomX(double factor) {
        return this.getX((2.0D * this.random.nextDouble() - 1.0D) * factor);
    }

    public double getZ(double factor) {
        return this.getPos().z + (double)this.getWidth() * factor;
    }

    public double getRandomZ(double factor) {
        return this.getZ((2.0D * this.random.nextDouble() - 1.0D) * factor);
    }

    /**
     * Greater Revelation is powerful enough to destroy replaceable blocks
     */
    @Override
    protected boolean testPlacement(BlockPos pos) {
        BlockState state = this.getWorld().getBlockState(pos);
        // check block
        if (state.isAir() || (state.isReplaceable()) && state.getFluidState().isEmpty()) {
            return true;
        }
        return false;
    }
}