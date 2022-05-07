package tt432.lighterfe.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.NotNull;
import tt432.lighterfe.config.CampfireGeneratorConfig;
import tt432.lighterfe.config.EnergyConfig;
import tt432.lighterfe.config.LighterfeConfig;

/**
 * @author DustW
 **/
public class CampfireGeneratorBlockEntity extends ModBaseBlockEntity {
    public CampfireGeneratorBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.CAMPFIRE_GENERATOR.get(), pWorldPosition, pBlockState);
    }

    int progress = 0;
    int energyPerTick = CampfireGeneratorConfig.getInstance().getEnergyPerTick();

    public static void tick(Level level, BlockPos pos, BlockState state, CampfireGeneratorBlockEntity be) {
        var belowBlock = level.getBlockState(pos.below()).getBlock();

        if (!(belowBlock instanceof CampfireBlock)) {
            return;
        }

        be.getCapability(CapabilityEnergy.ENERGY).ifPresent(energy -> {
            if (be.progress <= 0) {
                be.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
                    var stack = handler.getStackInSlot(0);
                    var burnTime = ForgeHooks.getBurnTime(stack, null);

                    if (burnTime > 0) {
                        stack.shrink(1);
                        be.progress = burnTime;

                        be.setChanged();
                    }
                });
            }
            else if (energy.getEnergyStored() <= energy.getMaxEnergyStored() - be.energyPerTick) {
                be.progress--;
                energy.receiveEnergy(be.energyPerTick, false);

                be.setChanged();
            }
        });
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        var result = super.getUpdateTag();
        result.putInt("progress", this.progress);
        return result;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("progress", this.progress);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        this.progress = tag.getInt("progress");
    }

    @Override
    public EnergyConfig getConfig() {
        return LighterfeConfig.getEnergyConfig("campfire_generator");
    }
}
