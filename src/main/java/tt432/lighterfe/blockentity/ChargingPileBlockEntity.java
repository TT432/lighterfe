package tt432.lighterfe.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.energy.CapabilityEnergy;
import tt432.lighterfe.config.EnergyConfig;
import tt432.lighterfe.config.LighterfeConfig;

/**
 * @author DustW
 **/
public class ChargingPileBlockEntity extends ModBaseBlockEntity {
    public ChargingPileBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.CHARGING_PILE.get(), pWorldPosition, pBlockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ChargingPileBlockEntity blockEntity) {
        if (level.isClientSide) {
            return;
        }

        var config = blockEntity.getConfig();

        blockEntity.energy.ifPresent(energyStorage -> {
            blockEntity.item.ifPresent(itemHandler ->
                    itemHandler.getStackInSlot(0).getCapability(CapabilityEnergy.ENERGY).ifPresent(energy -> {
                        var simulate = energyStorage.extractEnergy(config.getMaxExtract(), true);
                        var energyCanAdd = energy.receiveEnergy(simulate, false);
                        energyStorage.extractEnergy(energyCanAdd, false);

                        blockEntity.setChanged();
                    }));

            var belowBlockEntity = level.getBlockEntity(pos.below());

            if (belowBlockEntity == null) {
                return;
            }

            belowBlockEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(energy -> {
                var simulate = energy.extractEnergy(config.getMaxReceive(), true);
                var energyCanAdd = energyStorage.receiveEnergy(simulate, false);
                energy.extractEnergy(energyCanAdd, false);

                blockEntity.setChanged();
                belowBlockEntity.setChanged();
            });
        });
    }

    @Override
    public EnergyConfig getConfig() {
        return LighterfeConfig.getEnergyConfig("charging_pile");
    }
}
