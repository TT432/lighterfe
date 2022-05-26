package tt432.lighterfe.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tt432.lighterfe.Lighterfe;

/**
 * @author DustW
 **/
public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Lighterfe.MOD_ID);

    public static final RegistryObject<Block> CHARGING_PILE = BLOCKS.register("charging_pile",
            () -> new ChargingPileBlock(BlockBehaviour.Properties.of(Material.STONE).strength(3.5F)));

    public static final RegistryObject<Block> CAMPFIRE_GENERATOR = BLOCKS.register("campfire_generator",
            () -> new CampfireGeneratorBlock(BlockBehaviour.Properties.of(Material.STONE).noOcclusion().strength(3.5F)));
}
