package com.coppertools.item;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public class CopperToolTier implements Tier {
    public static final CopperToolTier INSTANCE = new CopperToolTier();

    private static final int DURABILITY = 131;

    private static final float SPEED = 6.0f;

    private static final float ATTACK_DAMAGE_BONUS = 2.0f;

    private static final int LEVEL = 2;

    private static final int ENCHANTABILITY = 0;

    private static final Supplier<Ingredient> REPAIR_INGREDIENT = () -> Ingredient.of(Items.COPPER_INGOT);

    @Override
    public int getUses() {
        return DURABILITY;
    }

    @Override
    public float getSpeed() {
        return SPEED;
    }

    @Override
    public float getAttackDamageBonus() {
        return ATTACK_DAMAGE_BONUS;
    }

    @Override
    public int getLevel() {
        return LEVEL;
    }

    @Override
    public int getEnchantmentValue() {
        return ENCHANTABILITY;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return REPAIR_INGREDIENT.get();
    }
}
