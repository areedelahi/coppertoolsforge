package com.coppertools.oxidation;

import com.coppertools.CopperToolsMod;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class OxidationHelper {

    private static final String TAG_ROOT = CopperToolsMod.MOD_ID;
    private static final String TAG_OXIDATION = "oxidation";
    private static final String TAG_USE_COUNT = "use_count";
    private static final String TAG_WAXED = "waxed";
    private static final String TAG_WAX_USES = "wax_uses_remaining";

    public static final int STAGE_UNAFFECTED = 0;
    public static final int STAGE_EXPOSED = 1;
    public static final int STAGE_WEATHERED = 2;
    public static final int STAGE_OXIDIZED = 3;
    public static final int MAX_STAGE = 3;

    private static CompoundTag getOrCreateModTag(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(TAG_ROOT)) {
            tag.put(TAG_ROOT, new CompoundTag());
        }
        return tag.getCompound(TAG_ROOT);
    }

    private static CompoundTag getModTag(ItemStack stack) {
        if (!stack.hasTag() || !stack.getTag().contains(TAG_ROOT)) {
            return null;
        }
        return stack.getTag().getCompound(TAG_ROOT);
    }

    public static int getOxidationLevel(ItemStack stack) {
        CompoundTag modTag = getModTag(stack);
        if (modTag == null)
            return STAGE_UNAFFECTED;
        return modTag.getInt(TAG_OXIDATION);
    }

    public static void setOxidationLevel(ItemStack stack, int level) {
        int clamped = Math.max(STAGE_UNAFFECTED, Math.min(MAX_STAGE, level));
        CompoundTag modTag = getOrCreateModTag(stack);
        modTag.putInt(TAG_OXIDATION, clamped);
    }

    public static void incrementOxidation(ItemStack stack, int amount) {
        setOxidationLevel(stack, getOxidationLevel(stack) + amount);
    }

    public static int getUseCount(ItemStack stack) {
        CompoundTag modTag = getModTag(stack);
        if (modTag == null)
            return 0;
        return modTag.getInt(TAG_USE_COUNT);
    }

    public static void setUseCount(ItemStack stack, int count) {
        CompoundTag modTag = getOrCreateModTag(stack);
        modTag.putInt(TAG_USE_COUNT, count);
    }

    public static boolean onItemUsed(ItemStack stack, Player player) {
        return onItemUsedWithThreshold(stack, player, OxidationConfig.USES_PER_OXIDATION_STAGE.get());
    }

    public static boolean onArmorUsed(ItemStack stack, Player player) {
        return onItemUsedWithThreshold(stack, player, OxidationConfig.ARMOR_USES_PER_OXIDATION_STAGE.get());
    }

    private static boolean onItemUsedWithThreshold(ItemStack stack, Player player, int threshold) {
        if (isWaxed(stack)) {
            decrementWaxUse(stack, player);
            return false;
        }

        int currentStage = getOxidationLevel(stack);
        if (currentStage >= MAX_STAGE) {
            return false; 
        }

        int useCount = getUseCount(stack) + 1;
        setUseCount(stack, useCount);

        if (useCount >= threshold) {
            setUseCount(stack, 0); 
            incrementOxidation(stack, 1);
            return true;
        }
        return false;
    }

    public static boolean isWaxed(ItemStack stack) {
        CompoundTag modTag = getModTag(stack);
        if (modTag == null)
            return false;
        return modTag.getBoolean(TAG_WAXED);
    }

    public static int getWaxUsesRemaining(ItemStack stack) {
        CompoundTag modTag = getModTag(stack);
        if (modTag == null)
            return 0;
        return modTag.getInt(TAG_WAX_USES);
    }

    public static void applyWax(ItemStack stack) {
        CompoundTag modTag = getOrCreateModTag(stack);
        modTag.putBoolean(TAG_WAXED, true);
        modTag.putInt(TAG_WAX_USES, OxidationConfig.WAX_MAX_USES.get());
    }

    public static void removeWax(ItemStack stack) {
        CompoundTag modTag = getOrCreateModTag(stack);
        modTag.putBoolean(TAG_WAXED, false);
        modTag.remove(TAG_WAX_USES);
    }

    public static void decrementWaxUse(ItemStack stack, Player player) {
        if (!isWaxed(stack))
            return;

        CompoundTag modTag = getOrCreateModTag(stack);
        int remaining = modTag.getInt(TAG_WAX_USES) - 1;

        if (remaining <= 0) {
            removeWax(stack);
            if (player != null && !player.level().isClientSide()) {
                player.displayClientMessage(
                        Component.translatable("message.coppertools.wax_expired",
                                stack.getHoverName()).withStyle(ChatFormatting.GOLD),
                        true 
                );
            }
        } else {
            modTag.putInt(TAG_WAX_USES, remaining);
        }
    }

    public static <T extends LivingEntity> int consumeWaxDurability(ItemStack stack, int amount, T entity) {
        if (!isWaxed(stack)) {
            return amount; 
        }
        return 0; 

    }

    public static float getSpeedMultiplier(ItemStack stack) {
        if (isWaxed(stack))
            return 1.0f;
        return switch (getOxidationLevel(stack)) {
            case STAGE_EXPOSED -> OxidationConfig.EXPOSED_SPEED_MULTIPLIER.get().floatValue();
            case STAGE_WEATHERED -> OxidationConfig.WEATHERED_SPEED_MULTIPLIER.get().floatValue();
            case STAGE_OXIDIZED -> OxidationConfig.OXIDIZED_SPEED_MULTIPLIER.get().floatValue();
            default -> 1.0f;
        };
    }

    public static int getArmorReduction(ItemStack stack) {
        if (isWaxed(stack))
            return 0;
        return switch (getOxidationLevel(stack)) {
            case STAGE_WEATHERED -> OxidationConfig.WEATHERED_ARMOR_REDUCTION.get();
            case STAGE_OXIDIZED -> OxidationConfig.OXIDIZED_ARMOR_REDUCTION.get();
            default -> 0;
        };
    }

    public static float getDurabilityMultiplier(ItemStack stack) {
        if (isWaxed(stack))
            return 1.0f;
        return switch (getOxidationLevel(stack)) {
            case STAGE_WEATHERED -> OxidationConfig.WEATHERED_DURABILITY_MULTIPLIER.get().floatValue();
            case STAGE_OXIDIZED -> OxidationConfig.OXIDIZED_DURABILITY_MULTIPLIER.get().floatValue();
            default -> 1.0f;
        };
    }

    public static String getOxidationPrefix(ItemStack stack) {
        String prefix = switch (getOxidationLevel(stack)) {
            case STAGE_EXPOSED -> "Exposed ";
            case STAGE_WEATHERED -> "Weathered ";
            case STAGE_OXIDIZED -> "Oxidized ";
            default -> "";
        };
        if (isWaxed(stack)) {
            prefix = "Waxed " + prefix;
        }
        return prefix;
    }

    public static String getOxidationStageName(int stage) {
        return switch (stage) {
            case STAGE_EXPOSED -> "Exposed";
            case STAGE_WEATHERED -> "Weathered";
            case STAGE_OXIDIZED -> "Oxidized";
            default -> "Unaffected";
        };
    }

    public static boolean isCopperItem(ItemStack stack) {
        if (stack.isEmpty())
            return false;

        var regName = net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(stack.getItem());
        return regName != null && regName.getNamespace().equals(CopperToolsMod.MOD_ID);
    }
}
