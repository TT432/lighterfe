package tt432.lighterfe.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tt432.lighterfe.config.EnergyConfig;
import tt432.lighterfe.config.LighterfeConfig;

/**
 * @author DustW
 **/
public class ItemEnergyCapabilityProvider extends CapabilityProvider<ItemEnergyCapabilityProvider> implements INBTSerializable<CompoundTag> {
    protected LazyOptional<EnergyStorage> energy;

    public ItemEnergyCapabilityProvider() {
        super(ItemEnergyCapabilityProvider.class);

        var config = LighterfeConfig.getEnergyConfig("lighter_item");
        energy = LazyOptional.of(() -> new EnergyStorage(config.getCapacity(), config.getMaxReceive(), config.getMaxExtract()));
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return CapabilityEnergy.ENERGY.orEmpty(cap, energy.cast());
    }

    @Override
    public CompoundTag serializeNBT() {
        var result = new CompoundTag();
        result.put("energy", energy.resolve().get().serializeNBT());
        return result;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        energy.resolve().get().deserializeNBT(nbt.get("energy"));
    }
}
