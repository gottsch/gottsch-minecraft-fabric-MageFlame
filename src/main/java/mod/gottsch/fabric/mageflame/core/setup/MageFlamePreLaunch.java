package mod.gottsch.fabric.mageflame.core.setup;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.impl.FormattedException;

/**
 * @author Mark Gottschling on 4/10/2026
 */

public class MageFlamePreLaunch implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) {
            return;
        }
        if (!FabricLoader.getInstance().isModLoaded("lambdynlights")) {
            throw new FormattedException(
                    "Missing required mod",
                    "Mage Flame requires LambDynamicLights on the client.\n\n" +
                            "Download it from:\nhttps://modrinth.com/mod/lambdynamiclights"
            );
        }
    }
}