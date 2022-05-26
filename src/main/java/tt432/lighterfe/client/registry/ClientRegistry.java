package tt432.lighterfe.client.registry;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import tt432.lighterfe.block.ModBlocks;
import tt432.lighterfe.blockentity.CampfireGeneratorBlockEntity;
import tt432.lighterfe.blockentity.ModBlockEntities;
import tt432.lighterfe.client.renderer.blockentity.CampfireGeneratorBlockEntityRenderer;
import tt432.lighterfe.client.renderer.blockentity.ChargingPileBlockEntityRenderer;

/**
 * @author DustW
 **/
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistry {
    @SubscribeEvent
    public static void clientSetUp(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // register BER
            BlockEntityRenderers.register(ModBlockEntities.CHARGING_PILE.get(), ChargingPileBlockEntityRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.CAMPFIRE_GENERATOR.get(), CampfireGeneratorBlockEntityRenderer::new);

            // register BlockRenderType
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CAMPFIRE_GENERATOR.get(), RenderType.cutout());
        });
    }
}