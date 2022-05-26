package tt432.lighterfe.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * @author DustW
 **/
public class LighterEntity extends ItemEntity {
    public LighterEntity(Level p_32001_, double p_32002_, double p_32003_, double p_32004_, ItemStack p_32005_) {
        super(p_32001_, p_32002_, p_32003_, p_32004_, p_32005_);
    }

    @Override
    public void tick() {
        super.tick();

        if (
                !hasPickUpDelay() &&
                getThrower() != null &&
                !level.isClientSide &&
                ((ServerLevel) level).getEntity(getThrower()) instanceof Player player
        ) {
            moveTo(player.position());
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }
}
