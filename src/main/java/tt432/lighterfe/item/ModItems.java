package tt432.lighterfe.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tt432.lighterfe.Lighterfe;
import tt432.lighterfe.block.ModBlocks;

/**
 * @author DustW
 **/
public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Lighterfe.MOD_ID);

    public static final RegistryObject<Item> LIGHTER = ITEMS.register("lighter",
            () -> new LighterItem(new Item.Properties().tab(Lighterfe.TAB).stacksTo(1)));

    public static final RegistryObject<Item> FEPORT = ITEMS.register("feport",
            () -> new Item(new Item.Properties().tab(Lighterfe.TAB)));

    public static final RegistryObject<Item> FEBATTERY = ITEMS.register("febattery",
            () -> new Item(new Item.Properties().tab(Lighterfe.TAB)));

    public static final RegistryObject<Item> CHARGING_PILE_BLOCK = ITEMS.register("charging_pile",
            () -> new BlockItem(ModBlocks.CHARGING_PILE.get(), new Item.Properties().tab(Lighterfe.TAB)));

    public static final RegistryObject<Item> CAMPFIRE_GENERATOR_BLOCK = ITEMS.register("campfire_generator",
            () -> new BlockItem(ModBlocks.CAMPFIRE_GENERATOR.get(), new Item.Properties().tab(Lighterfe.TAB)));
}
