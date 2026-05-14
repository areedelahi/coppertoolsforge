package com.coppertools.recipe;

import com.coppertools.oxidation.OxidationHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class AxeScrapingRecipe extends CustomRecipe {

    public AxeScrapingRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        boolean hasCopperItem = false;
        boolean hasAxe = false;
        int itemCount = 0;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) continue;
            itemCount++;

            if (stack.is(ItemTags.AXES) && !OxidationHelper.isCopperItem(stack)) {
                if (hasAxe) return false;
                hasAxe = true;
            } else if (OxidationHelper.isCopperItem(stack)) {
                if (hasCopperItem) return false;

                if (!OxidationHelper.isWaxed(stack) && OxidationHelper.getOxidationLevel(stack) <= 0) {
                    return false;
                }
                hasCopperItem = true;
            } else {
                return false;
            }
        }

        return hasCopperItem && hasAxe && itemCount == 2;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty() && OxidationHelper.isCopperItem(stack)) {
                ItemStack result = stack.copy();
                result.setCount(1);

                if (OxidationHelper.isWaxed(result)) {

                    OxidationHelper.removeWax(result);
                } else if (OxidationHelper.getOxidationLevel(result) > 0) {

                    OxidationHelper.incrementOxidation(result, -1);
                }

                return result;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.is(ItemTags.AXES) && !OxidationHelper.isCopperItem(stack)) {

                ItemStack damagedAxe = stack.copy();
                damagedAxe.setDamageValue(damagedAxe.getDamageValue() + 1);
                if (damagedAxe.getDamageValue() < damagedAxe.getMaxDamage()) {
                    remaining.set(i, damagedAxe);
                }

            }
        }

        return remaining;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.AXE_SCRAPING.get();
    }
}
