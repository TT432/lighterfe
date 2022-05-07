package tt432.lighterfe;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tt432.lighterfe.block.ModBlocks;
import tt432.lighterfe.blockentity.ModBlockEntities;
import tt432.lighterfe.config.LighterfeConfig;
import tt432.lighterfe.item.ModItems;

/**
 * @author DustW
 */
@Mod(Lighterfe.MOD_ID)
public class Lighterfe {
    public static final String MOD_ID = "lighterfe";

    public static final CreativeModeTab TAB = new CreativeModeTab(MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.LIGHTER.get());
        }
    };

    public Lighterfe() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.ITEMS.register(bus);
        ModBlockEntities.BLOCK_ENTITIES.register(bus);
        ModBlocks.BLOCKS.register(bus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, LighterfeConfig.COMMON_CONFIG);
    }
}
