package tt432.lighterfe.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.CapabilityItemHandler;

/**
 * @author DustW
 **/
public abstract class ModBaseEntityBlock extends BaseEntityBlock {
    protected ModBaseEntityBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        var blockEntity = pLevel.getBlockEntity(pPos);

        if (blockEntity == null) {
            return InteractionResult.FAIL;
        }

        blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(cap -> {
            var extracted = cap.extractItem(0, 64, false);
            cap.insertItem(0, pPlayer.getItemInHand(pHand), false);
            pPlayer.setItemInHand(pHand, ItemStack.EMPTY);

            if (!pPlayer.addItem(extracted)) {
                pPlayer.drop(extracted, false);
            }

            blockEntity.setChanged();
        });

        return InteractionResult.CONSUME;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
}
