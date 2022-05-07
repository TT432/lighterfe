package tt432.lighterfe.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tt432.lighterfe.config.EnergyConfig;

/**
 * @author DustW
 **/
public abstract class ModBaseBlockEntity extends BlockEntity {
    protected LazyOptional<EnergyStorage> energy;
    protected LazyOptional<ItemStackHandler> item;

    public ModBaseBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);

        initCapabilities();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        var result = CapabilityEnergy.ENERGY.orEmpty(cap, energy.cast());

        if (!result.isPresent()) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, item.cast());
        }
        else {
            return result;
        }
    }

    protected void initCapabilities() {
        if (energy == null) {
            var config = getConfig();
            this.energy = LazyOptional.of(() -> new EnergyStorage(config.getCapacity(), config.getMaxReceive(), config.getMaxExtract()));
        }

        if (item == null) {
            this.item = LazyOptional.of(() -> new ItemStackHandler(1));
        }
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        var result = new CompoundTag();

        this.energy.ifPresent(cap -> result.putInt("energy", cap.getEnergyStored()));
        this.item.ifPresent(cap -> result.put("item", cap.serializeNBT()));

        return result;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);

        this.energy.ifPresent(cap -> tag.putInt("energy", cap.getEnergyStored()));
        this.item.ifPresent(cap -> tag.put("item", cap.serializeNBT()));
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        this.energy.ifPresent(cap -> cap.deserializeNBT(tag.get("energy")));
        this.item.ifPresent(cap -> cap.deserializeNBT(tag.getCompound("item")));
    }

    @Override
    public void setChanged() {
        if (!level.isClientSide) {
            var packet = ClientboundBlockEntityDataPacket.create(this);
            ((ServerLevel)this.level).getChunkSource().chunkMap.getPlayers(new ChunkPos(getBlockPos()), false)
                    .forEach(k -> k.connection.send(packet));
        }

        super.setChanged();
    }

    public abstract EnergyConfig getConfig();
}
