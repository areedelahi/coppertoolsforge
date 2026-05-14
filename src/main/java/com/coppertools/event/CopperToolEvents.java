package com.coppertools.event;

import com.coppertools.CopperToolsMod;
import com.coppertools.item.CopperArmorItem;
import com.coppertools.oxidation.OxidationHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CopperToolsMod.MOD_ID)
public class CopperToolEvents {

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide()) return;

        for (ItemStack armorStack : player.getArmorSlots()) {
            if (armorStack.getItem() instanceof CopperArmorItem) {
                boolean wasWaxed = OxidationHelper.isWaxed(armorStack);

                OxidationHelper.onArmorUsed(armorStack, player);

                if (!wasWaxed) {
                    float multiplier = OxidationHelper.getDurabilityMultiplier(armorStack);
                    if (multiplier > 1.0f) {
                        int extraDamage = Math.round(multiplier) - 1;
                        armorStack.hurtAndBreak(extraDamage, player, (p) ->
                                p.broadcastBreakEvent(((CopperArmorItem) armorStack.getItem()).getEquipmentSlot()));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        if (OxidationHelper.isCopperItem(event.getLeft())) {

            event.setCanceled(true);
        }
    }
}
