package mod.gottsch.fabric.mageflame.core.setup;

import com.google.common.eventbus.Subscribe;
import dev.lambdaurora.lambdynlights.api.DynamicLightHandler;
import dev.lambdaurora.lambdynlights.api.DynamicLightsInitializer;
import dev.lambdaurora.lambdynlights.api.item.ItemLightSourceManager;

import static dev.lambdaurora.lambdynlights.api.DynamicLightHandlers.registerDynamicLightHandler;


/**
 * Created by Mark Gottschling on 1/11/2025
 */
public class DynamicLights implements DynamicLightsInitializer {

    @Override
    public void onInitializeDynamicLights(ItemLightSourceManager itemLightSourceManager) {
        registerDynamicLightHandler(Registration.FIRE_WOLF_ENTITY, DynamicLightHandler.makeHandler(blaze -> 14, blaze -> true));
    }
}
