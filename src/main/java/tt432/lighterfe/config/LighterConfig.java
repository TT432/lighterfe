package tt432.lighterfe.config;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * @author DustW
 **/
public class LighterConfig {
    ForgeConfigSpec.IntValue litEnergy;
    ForgeConfigSpec.IntValue fireballEnergy;

    public LighterConfig(ForgeConfigSpec.Builder builder) {
        litEnergy = builder.comment("单次点火耗能")
                .defineInRange("litEnergy", 10, 0, Integer.MAX_VALUE);
        fireballEnergy = builder.comment("火球耗能")
                .defineInRange("fireballEnergy", 100, 0, Integer.MAX_VALUE);
    }

    public int getLitEnergy() {
        return litEnergy.get();
    }

    public int getFireballEnergy() {
        return fireballEnergy.get();
    }
}
