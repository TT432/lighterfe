package tt432.lighterfe.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import tt432.lighterfe.blockentity.CampfireGeneratorBlockEntity;
import tt432.lighterfe.utils.ClientTickHandler;

/**
 * @author DustW
 **/
public class CampfireGeneratorBlockEntityRenderer implements BlockEntityRenderer<CampfireGeneratorBlockEntity> {
    public CampfireGeneratorBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
    }

    @Override
    public void render(CampfireGeneratorBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(cap -> {
            var time = ClientTickHandler.ticksInGame + partialTick;

            poseStack.pushPose();

            poseStack.translate(0.5, .35, 0.5);

            poseStack.mulPose(Vector3f.YP.rotationDegrees(time % 360));

            var itemRenderer = Minecraft.getInstance().getItemRenderer();
            var stack = cap.getStackInSlot(0);

            itemRenderer.renderStatic(stack, ItemTransforms.TransformType.GROUND,
                    LightTexture.FULL_BRIGHT, packedOverlay,
                    poseStack, bufferSource, stack.getItem().getRegistryName().hashCode());

            poseStack.popPose();
        });
    }
}
