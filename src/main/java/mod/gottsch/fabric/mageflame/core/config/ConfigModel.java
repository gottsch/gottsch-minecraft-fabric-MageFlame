package mod.gottsch.fabric.mageflame.core.config;

import blue.endless.jankson.Comment;
import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.RangeConstraint;
import io.wispforest.owo.config.annotation.SectionHeader;

/**
 * Created by Mark Gottschling on 3/7/2023
 */
@Modmenu(modId = "mageflame")
@Config(name = "mageflame", wrapperName = "MyConfig")
public class ConfigModel {
    @SectionHeader("flameProperties")
//    @RestartRequired
//    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    @RangeConstraint(min = 1200, max = 72000)
    @Comment("Mage Flame's Lifespan.")
    public int mageFlameLifespan = 12000;

//    @RestartRequired
//    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    @RangeConstraint(min = 1200, max = 72000)
    public int lesserRevelationLifespan = 18000;

//    @RestartRequired
//    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    @RangeConstraint(min = 1200, max = 72000)
    public int greaterRevelationLifespan = 36000;

    // client side (make separate config?
    @RangeConstraint(min = 1, max = 20)
    public int updateLightTicks = 2;
}

