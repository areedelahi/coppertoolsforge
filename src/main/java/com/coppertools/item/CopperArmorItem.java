package com.coppertools.item;

import com.coppertools.CopperToolsMod;
import com.coppertools.oxidation.OxidationHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class CopperArmorItem extends ArmorItem {

    public CopperArmorItem(Type type, Properties properties) {
        super(CopperArmorMaterial.COPPER, type, properties);
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
    public <T extends net.minecraft.world.entity.LivingEntity> int damageItem(
            ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return com.coppertools.oxidation.OxidationHelper.consumeWaxDurability(stack, amount, entity);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (!level.isClientSide() && entity instanceof Player player) {
            EquipmentSlot slot = getEquipmentSlot();
            ItemStack equippedStack = player.getItemBySlot(slot);

            if (equippedStack == stack) {

                if (level.getGameTime() % 1200 == 0 && !OxidationHelper.isWaxed(stack)) {
                    OxidationHelper.onArmorUsed(stack, player);
                }
            }
        }
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        int oxidation = OxidationHelper.getOxidationLevel(stack);
        String prefix = switch (oxidation) {
            case OxidationHelper.STAGE_EXPOSED -> "exposed_";
            case OxidationHelper.STAGE_WEATHERED -> "weathered_";
            case OxidationHelper.STAGE_OXIDIZED -> "oxidized_";
            default -> "";
        };

        int layer = (slot == EquipmentSlot.LEGS) ? 2 : 1;
        return CopperToolsMod.MOD_ID + ":textures/models/armor/" + prefix + "copper_layer_" + layer + ".png";
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

    public int getEffectiveDefense(ItemStack stack) {
        int baseDefense = this.getMaterial().getDefenseForType(this.getType());
        int reduction = OxidationHelper.getArmorReduction(stack);
        return Math.max(0, baseDefense - reduction);
    }
}
