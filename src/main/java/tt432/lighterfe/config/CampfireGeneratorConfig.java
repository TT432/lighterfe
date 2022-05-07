package tt432.lighterfe.config;

/**
 * @author DustW
 **/
public class CampfireGeneratorConfig {
    public static CampfireGeneratorConfig getInstance() {
        return new CampfireGeneratorConfig();
    }

    int energyPerTick = 20;

    public int getEnergyPerTick() {
        return energyPerTick;
    }
}
