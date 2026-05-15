package com.coppertools.item;

import com.coppertools.oxidation.OxidationHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class CopperItemTooltip {

    public static void addTooltip(ItemStack stack, List<Component> tooltip) {
        int oxidation = OxidationHelper.getOxidationLevel(stack);
        boolean isArmor = stack.getItem() instanceof CopperArmorItem;

        ChatFormatting stageColor = switch (oxidation) {
            case OxidationHelper.STAGE_EXPOSED -> ChatFormatting.YELLOW;
            case OxidationHelper.STAGE_WEATHERED -> ChatFormatting.GREEN;
            case OxidationHelper.STAGE_OXIDIZED -> ChatFormatting.DARK_GREEN;
            default -> ChatFormatting.GOLD;
        };
        tooltip.add(Component.translatable("tooltip.coppertools.oxidation_stage",
                Component.literal(OxidationHelper.getOxidationStageName(oxidation)).withStyle(stageColor))
                .withStyle(ChatFormatting.GRAY));

        if (OxidationHelper.isWaxed(stack)) {
            int remaining = OxidationHelper.getWaxUsesRemaining(stack);
            tooltip.add(Component.translatable("tooltip.coppertools.waxed_remaining",
                    Component.literal(String.valueOf(remaining)).withStyle(ChatFormatting.AQUA))
                    .withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(Component.translatable("tooltip.coppertools.not_waxed")
                    .withStyle(ChatFormatting.DARK_GRAY));
        }

        if (!OxidationHelper.isWaxed(stack) && oxidation > OxidationHelper.STAGE_UNAFFECTED) {
            if (!isArmor) {

                float speedMult = OxidationHelper.getSpeedMultiplier(stack);
                if (speedMult < 1.0f) {
                    int penalty = Math.round((1.0f - speedMult) * 100);
                    tooltip.add(Component.translatable("tooltip.coppertools.efficiency_penalty",
                            Component.literal("-" + penalty + "%").withStyle(ChatFormatting.RED))
                            .withStyle(ChatFormatting.DARK_GRAY));
                }
            } else {

                int armorReduction = OxidationHelper.getArmorReduction(stack);
                if (armorReduction > 0) {
                    tooltip.add(Component.translatable("tooltip.coppertools.armor_penalty",
                            Component.literal("-" + armorReduction).withStyle(ChatFormatting.RED))
                            .withStyle(ChatFormatting.DARK_GRAY));
                }
            }
        }
    }
}
