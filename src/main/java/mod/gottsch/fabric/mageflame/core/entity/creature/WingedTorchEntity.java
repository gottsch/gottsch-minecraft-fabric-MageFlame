package mod.gottsch.fabric.mageflame.core.entity.creature;

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
public class WingedTorchEntity extends SummonFlameBaseEntity {

    public WingedTorchEntity(EntityType<? extends FlyingEntity> entityType, World level) {
        super(entityType, level, 12000);
    }

    /**
     * @return
     */
    public static DefaultAttributeContainer.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3F);
    }

    @Override
    protected double updateLifespan() {
        return getLifespan();
    }

    @Override
    public @NotNull Block getFlameBlock() {
        return Registration.GREATER_REVELATION_BLOCK;
    }

    @Override
    public void doLivingEffects() {
        double d0 = this.getX();
        double d1 = this.getY() + 0.6;
        double d2 = this.getZ();
        this.getWorld().addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        this.getWorld().addParticle(ParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }

    /**
     * Winged Torch is powerful enough to destroy replaceable blocks
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