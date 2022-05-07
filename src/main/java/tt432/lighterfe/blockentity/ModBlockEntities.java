package tt432.lighterfe.blockentity;

import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tt432.lighterfe.Lighterfe;
import tt432.lighterfe.block.ModBlocks;

import java.util.function.Supplier;

/**
 * @author DustW
 **/
public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Lighterfe.MOD_ID);

    public static final RegistryObject<BlockEntityType<ChargingPileBlockEntity>> CHARGING_PILE = register("charging_pile",
            () -> BlockEntityType.Builder.of(ChargingPileBlockEntity::new, ModBlocks.CHARGING_PILE.get()));

    public static final RegistryObject<BlockEntityType<CampfireGeneratorBlockEntity>> CAMPFIRE_GENERATOR = register("campfire_generator",
            () -> BlockEntityType.Builder.of(CampfireGeneratorBlockEntity::new, ModBlocks.CAMPFIRE_GENERATOR.get()));

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String key, Supplier<BlockEntityType.Builder<T>> builder) {
        var type = Util.fetchChoiceType(References.BLOCK_ENTITY, key);
        return BLOCK_ENTITIES.register(key, () -> builder.get().build(type));
    }
}
