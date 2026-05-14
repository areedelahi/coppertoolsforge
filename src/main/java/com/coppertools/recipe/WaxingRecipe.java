package com.coppertools.recipe;

import com.coppertools.oxidation.OxidationHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class WaxingRecipe extends CustomRecipe {

    public WaxingRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        boolean hasCopperItem = false;
        boolean hasHoneycomb = false;
        int itemCount = 0;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) continue;
            itemCount++;

            if (stack.is(Items.HONEYCOMB)) {
                if (hasHoneycomb) return false; 
                hasHoneycomb = true;
            } else if (OxidationHelper.isCopperItem(stack)) {
                if (hasCopperItem) return false; 
                if (OxidationHelper.isWaxed(stack)) return false; 
                hasCopperItem = true;
            } else {
                return false; 
            }
        }

        return hasCopperItem && hasHoneycomb && itemCount == 2;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty() && OxidationHelper.isCopperItem(stack)) {
                ItemStack result = stack.copy();
                result.setCount(1);
                OxidationHelper.applyWax(result);
                return result;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.WAXING.get();
    }
}
