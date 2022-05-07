package tt432.lighterfe.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;
import java.util.Map;

/**
 * @author DustW
 **/
public class LighterfeConfig {
    public static ForgeConfigSpec COMMON_CONFIG;

    private static final Map<String, EnergyConfig> CONFIG_MAP = new HashMap<>();
    private static final LighterConfig LIGHTER_CONFIG;
    private static final CampfireGeneratorConfig CAMPFIRE_GENERATOR_CONFIG;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("lighter (Item)");

        CONFIG_MAP.put("lighter_item", new EnergyConfig(builder));
        LIGHTER_CONFIG = new LighterConfig(builder);

        builder.pop();

        builder.push("charging pile");

        CONFIG_MAP.put("charging_pile", new EnergyConfig(builder));

        builder.pop();

        builder.push("campfire generator");

        CONFIG_MAP.put("campfire_generator", new EnergyConfig(builder));
        CAMPFIRE_GENERATOR_CONFIG = new CampfireGeneratorConfig(builder);

        builder.pop();

        COMMON_CONFIG = builder.build();
    }

    public static EnergyConfig getEnergyConfig(String name) {
        return CONFIG_MAP.get(name);
    }

    public static LighterConfig getLighterConfig() {
        return LIGHTER_CONFIG;
    }

    public static CampfireGeneratorConfig getCampfireGeneratorConfig() {
        return CAMPFIRE_GENERATOR_CONFIG;
    }
}
