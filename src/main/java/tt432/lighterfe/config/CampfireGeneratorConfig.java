package tt432.lighterfe.config;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * @author DustW
 **/
public class CampfireGeneratorConfig {
    ForgeConfigSpec.IntValue energyPerTick;

    public CampfireGeneratorConfig(ForgeConfigSpec.Builder builder) {
        energyPerTick = builder.comment("每 tick 产生多少能量")
                .defineInRange("energyPerTick", 20, 1, Integer.MAX_VALUE);
    }

    public int getEnergyPerTick() {
        return energyPerTick.get();
    }
}
