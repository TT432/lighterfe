package tt432.lighterfe.config;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * @author DustW
 **/
public class EnergyConfig {
    ForgeConfigSpec.IntValue capacity;
    ForgeConfigSpec.IntValue maxReceive;
    ForgeConfigSpec.IntValue maxExtract;

    public EnergyConfig(ForgeConfigSpec.Builder builder) {
        capacity = builder.comment("最大容量（capability）")
                .defineInRange("capacity", 800, 0, Integer.MAX_VALUE);
        maxReceive = builder.comment("最大输入（capability）")
                .defineInRange("maxReceive", 100, 0, Integer.MAX_VALUE);
        maxExtract = builder.comment("最大输出（capability）")
                .defineInRange("maxExtract", 100, 0, Integer.MAX_VALUE);
    }

    /** 最大容量（capability） */
    public int getCapacity() {
        return capacity.get();
    }

    /** 最大输入（capability） */
    public int getMaxReceive() {
        return maxReceive.get();
    }

    /** 最大输出（capability） */
    public int getMaxExtract() {
        return maxExtract.get();
    }
}
