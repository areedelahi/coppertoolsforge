package com.coppertools.item;

import com.coppertools.oxidation.OxidationHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class CopperSwordItem extends SwordItem {

    public CopperSwordItem(Properties properties) {
        super(CopperToolTier.INSTANCE, 3, -2.4f, properties);
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
