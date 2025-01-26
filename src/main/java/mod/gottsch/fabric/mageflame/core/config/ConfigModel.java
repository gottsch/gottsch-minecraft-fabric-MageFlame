package mod.gottsch.fabric.mageflame.core.config;

import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.*;

/**
 * Created by Mark Gottschling on 3/7/2023
 */
@Modmenu(modId = "mageflame")
@Config(name = "mageflame", wrapperName = "MyConfig")
public class ConfigModel {
    @SectionHeader("clientProperties")
    public boolean enableLifespanDisplay = true;

    @SectionHeader("flameProperties")

    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    @RangeConstraint(min = 1, max = 10)
    public int maxSummonedEntitiesPerPlayer = 1;

    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    @RangeConstraint(min = 1200, max = 72000)
    public int mageFlameLifespan = 12000;

    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    @RangeConstraint(min = 1200, max = 72000)
    public int lesserRevelationLifespan = 18000;

    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    @RangeConstraint(min = 1200, max = 72000)
    public int greaterRevelationLifespan = 36000;

    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    public boolean isEmberHoundLifespanInfinite = true;

    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    @RangeConstraint(min = 1200, max = 72000)
    public int emberHoundLifespan = 72000;

    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    @RangeConstraint(min = 1200, max = 72000)
    public int bubbleFlameLifespan = 54000;

    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    @RangeConstraint(min = 1200, max = 72000)
    public int glowglobLifespan = 12000;
}

