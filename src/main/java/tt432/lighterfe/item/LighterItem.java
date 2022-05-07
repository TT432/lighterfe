package tt432.lighterfe.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.world.BlockEvent;
import org.jetbrains.annotations.Nullable;
import tt432.lighterfe.capability.ItemEnergyCapabilityProvider;
import tt432.lighterfe.config.LighterConfig;
import tt432.lighterfe.config.LighterfeConfig;
import tt432.lighterfe.entity.LighterEntity;

import java.util.List;

/**
 * @author DustW
 **/
public class LighterItem extends Item {
    public LighterItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Nullable
    @Override
    public Entity createEntity(Level level, Entity location, ItemStack stack) {
        if (level.isClientSide) {
            return null;
        }

        var serverLevel = (ServerLevel) level;

        if (location instanceof ItemEntity itemEntity &&
                itemEntity.getThrower() != null &&
                serverLevel.getEntity(itemEntity.getThrower()) instanceof Player player) {

            return drop(player, stack);
        }

        return null;
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        var player = pContext.getPlayer();
        var level = pContext.getLevel();
        var clickedPos = pContext.getClickedPos();
        var blockState = level.getBlockState(clickedPos);
        var itemInHand = pContext.getItemInHand();
        var capability = itemInHand.getCapability(CapabilityEnergy.ENERGY).resolve();
        var litEnergy = LighterfeConfig.getLighterConfig().getLitEnergy();
        var facing = pContext.getClickedFace();

        if (capability.isEmpty() || capability.get().getEnergyStored() < litEnergy && !getCreativeTag(itemInHand)) {
            return InteractionResult.PASS;
        }

        if (!CampfireBlock.canLight(blockState) && !CandleBlock.canLight(blockState) && !CandleCakeBlock.canLight(blockState)) {
            var facingPos = clickedPos.relative(facing);

            if (BaseFireBlock.canBePlacedAt(level, facingPos, pContext.getHorizontalDirection())) {
                var cancelled = ForgeEventFactory.onBlockPlace(player,
                        BlockSnapshot.create(level.dimension(), level, clickedPos, 11), facing);

                if (cancelled) {
                    return InteractionResult.PASS;
                }

                level.playSound(player, facingPos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS,
                        1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                var fire = BaseFireBlock.getState(level, facingPos);
                level.setBlock(facingPos, fire, 11);
                level.gameEvent(player, GameEvent.BLOCK_PLACE, clickedPos);

                if (player instanceof ServerPlayer) {
                    CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, facingPos, itemInHand);
                    capability.get().extractEnergy(litEnergy, false);
                }

                return InteractionResult.sidedSuccess(level.isClientSide());
            } else {
                return InteractionResult.FAIL;
            }
        } else {
            var placedAgainst = blockState.setValue(BlockStateProperties.LIT, Boolean.TRUE);

            var cancelled = MinecraftForge.EVENT_BUS.post(new BlockEvent.EntityPlaceEvent(
                    BlockSnapshot.create(level.dimension(), level, clickedPos, 11), placedAgainst, player));

            if (cancelled) {
                return InteractionResult.PASS;
            }

            level.playSound(player, clickedPos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS,
                    1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            level.setBlock(clickedPos, placedAgainst, 11);
            level.gameEvent(player, GameEvent.BLOCK_PLACE, clickedPos);

            if (player != null) {
                capability.get().extractEnergy(litEnergy, false);
            }

            return InteractionResult.sidedSuccess(level.isClientSide());
        }
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        var level = player.level;
        var look = player.getLookAngle();
        var pos = player.getEyePosition().add(look.normalize().scale(1.5));
        var fireballEnergy = LighterfeConfig.getLighterConfig().getFireballEnergy();

        if (!level.isClientSide) {
            item.getCapability(CapabilityEnergy.ENERGY).ifPresent(cap -> {
                if (getCreativeTag(item) || cap.getEnergyStored() >= fireballEnergy) {
                    cap.extractEnergy(fireballEnergy, false);

                    var fireBall = new LargeFireball(level, player, look.x, look.y, look.z, 0);
                    fireBall.setNoGravity(true);
                    fireBall.setPos(pos);
                    level.addFreshEntity(fireBall);
                }
            });
        }

        return true;
    }

    public LighterEntity drop(Player player, ItemStack pDroppedItem) {
        if (pDroppedItem.isEmpty()) {
            return null;
        } else {
            if (player.level.isClientSide) {
                player.swing(InteractionHand.MAIN_HAND);
            }

            var level = player.level;
            var lighterEntity = new LighterEntity(level,
                    player.getX(), player.getEyeY() - (double)0.3F, player.getZ(), pDroppedItem);

            lighterEntity.setPickUpDelay(20);
            lighterEntity.setThrower(player.getUUID());

            var sinX = Mth.sin(player.getXRot() * ((float)Math.PI / 180F));
            var cosX = Mth.cos(player.getXRot() * ((float)Math.PI / 180F));
            var sinY = Mth.sin(player.getYRot() * ((float)Math.PI / 180F));
            var cosY = Mth.cos(player.getYRot() * ((float)Math.PI / 180F));
            var f5 = level.random.nextFloat() * ((float)Math.PI * 2F);
            var f6 = 0.02F * level.random.nextFloat();

            lighterEntity.setDeltaMovement(
                    (double)(-sinY * cosX * 0.3F) + Math.cos(f5) * (double)f6,
                    (double)(-sinX * 0.3F + 0.1F + (level.random.nextFloat() - level.random.nextFloat()) * 0.1F),
                    (double)(cosY * cosX * 0.3F) + Math.sin(f5) * (double)f6);

            return lighterEntity;
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TranslatableComponent("item.lighter.tooltip.fireball"));

        var config = LighterfeConfig.getLighterConfig();
        var energyConfig = LighterfeConfig.getEnergyConfig("lighter_item");

        pTooltipComponents.add(new TranslatableComponent("item.lighter.tooltip.lit_energy", config.getLitEnergy()));
        pTooltipComponents.add(new TranslatableComponent("item.lighter.tooltip.fireball_energy", config.getFireballEnergy()));

        var capability  = pStack.getCapability(CapabilityEnergy.ENERGY).resolve();

        if (getCreativeTag(pStack)) {
            pTooltipComponents.add(new TranslatableComponent("item.lighter.tooltip.creative"));
            return;
        }

        if (capability.isEmpty()) {return;}

        var storedEnergy = capability.get().getEnergyStored();

        var count = (int) (storedEnergy * 20. / energyConfig.getCapacity());

        pTooltipComponents.add(new TranslatableComponent("item.lighter.tooltip.energy",
                "§3" + "|".repeat(count), "§4" + "|".repeat(20 - count)));
    }

    public void setCreativeTag(ItemStack stack) {
        stack.getOrCreateTag().putBoolean("creative", true);
        stack.enchant(Enchantments.INFINITY_ARROWS, 1);
    }

    public boolean getCreativeTag(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("creative");
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ItemEnergyCapabilityProvider();
    }

    @Override
    public void fillItemCategory(CreativeModeTab pCategory, NonNullList<ItemStack> pItems) {
        if (this.allowdedIn(pCategory)) {
            pItems.add(new ItemStack(this));
            var creativeStack = new ItemStack(this);
            this.setCreativeTag(creativeStack);
            pItems.add(creativeStack);
        }
    }
}
