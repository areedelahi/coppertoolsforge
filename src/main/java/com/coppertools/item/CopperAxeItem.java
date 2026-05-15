package com.coppertools.item;

import com.coppertools.oxidation.OxidationHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class CopperAxeItem extends AxeItem {

    public CopperAxeItem(Properties properties) {
        super(CopperToolTier.INSTANCE, 6.0f, -3.1f, properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return OxidationHelper.isWaxed(stack);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return OxidationHelper.consumeWaxDurability(stack, amount, entity);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        float baseSpeed = super.getDestroySpeed(stack, state);
        if (baseSpeed > 1.0f) {
            return baseSpeed * OxidationHelper.getSpeedMultiplier(stack);
        }
        return baseSpeed;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!level.isClientSide()) {
            if (miner instanceof Player player) {
                boolean wasWaxed = OxidationHelper.isWaxed(stack);
                OxidationHelper.onItemUsed(stack, player);
                if (!wasWaxed) {
                    float multiplier = OxidationHelper.getDurabilityMultiplier(stack);
                    if (multiplier > 1.0f) {
                        int extraDamage = Math.round(multiplier) - 1;
                        stack.hurtAndBreak(extraDamage, miner, (e) ->
                                e.broadcastBreakEvent(miner.getUsedItemHand()));
                    }
                }
            }
        }
        return super.mineBlock(stack, level, state, pos, miner);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player) {
            boolean wasWaxed = OxidationHelper.isWaxed(stack);
            OxidationHelper.onItemUsed(stack, player);
            if (!wasWaxed) {
                float multiplier = OxidationHelper.getDurabilityMultiplier(stack);
                if (multiplier > 1.0f) {
                    int extraDamage = Math.round(multiplier) - 1;
                    stack.hurtAndBreak(extraDamage, attacker, (e) ->
                            e.broadcastBreakEvent(attacker.getUsedItemHand()));
                }
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public Component getName(ItemStack stack) {
        String prefix = OxidationHelper.getOxidationPrefix(stack);
        if (!prefix.isEmpty()) {
            return Component.literal(prefix).append(super.getName(stack));
        }
        return super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        CopperItemTooltip.addTooltip(stack, tooltip);
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
