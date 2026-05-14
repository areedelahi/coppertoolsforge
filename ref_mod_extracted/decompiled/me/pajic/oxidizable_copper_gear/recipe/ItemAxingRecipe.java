
package me.pajic.oxidizable_copper_gear.recipe;

import me.pajic.oxidizable_copper_gear.Main;
import net.minecraft.core.NonNullList;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemAxingRecipe
extends CustomRecipe {
    public ItemAxingRecipe(CraftingBookCategory category) {
        super(category);
    }

    public boolean matches(CraftingInput input, @NotNull Level level) {
        if (input.size() != 2) {
            return false;
        }
        boolean hasDewaxableItem = false;
        boolean hasOxidizableItem = false;
        boolean hasAxe = false;
        for (int i = 0; i < input.size(); ++i) {
            ItemStack itemStack = input.getItem(i);
            if (itemStack.isEmpty()) continue;
            if (itemStack.is(ItemTags.AXES) && !itemStack.has(Main.WAXED)) {
                if (hasAxe) {
                    return false;
                }
                hasAxe = true;
                continue;
            }
            if (!itemStack.is(Main.OXIDIZABLE)) {
                return false;
            }
            if (hasDewaxableItem || hasOxidizableItem) {
                return false;
            }
            if (itemStack.has(Main.WAXED)) {
                hasDewaxableItem = true;
                continue;
            }
            hasOxidizableItem = true;
        }
        Main.debugLog("hasDewaxableItem {} hasOxidizableItem {} hasAxe {}", hasDewaxableItem, hasOxidizableItem, hasAxe);
        return hasAxe && (hasDewaxableItem || hasOxidizableItem);
    }

    @NotNull
    public ItemStack assemble(CraftingInput input, 
     @NotNull HolderLookup.Provider registries) {
        ItemStack itemStack = ItemStack.EMPTY;
        for (int i = 0; i < input.size(); ++i) {
            ItemStack itemStack2 = input.getItem(i);
            Main.debugLog("itemStack {} oxidizable {} waxed {}", itemStack2.getHoverName().getString(), itemStack2.is(Main.OXIDIZABLE), itemStack2.has(Main.WAXED));
            if (!itemStack2.is(Main.OXIDIZABLE)) continue;
            if (itemStack2.has(Main.WAXED)) {
                itemStack = itemStack2.copy();
                itemStack.remove(Main.WAXED);
                Main.debugLog("unwaxing {}", itemStack.getHoverName().getString());
                continue;
            }
            if (Main.getItemOxidation(itemStack2) <= 0) continue;
            itemStack = itemStack2.copy();
            Main.incrementItemOxidation(itemStack, -1);
            Main.debugLog("reducing oxidation on {}", itemStack.getHoverName().getString());
        }
        Main.debugLog("returning {}", itemStack.getHoverName().getString());
        return itemStack;
    }

    @NotNull
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList items = NonNullList.withSize((int)input.size(), (Object)ItemStack.EMPTY);
        for (int i = 0; i < input.size(); ++i) {
            ItemStack itemStack = input.getItem(i);
            if (!itemStack.is(ItemTags.AXES) || itemStack.has(Main.WAXED)) continue;
            ItemStack itemStack2 = itemStack.copy();
            itemStack2.setDamageValue(itemStack2.getDamageValue() + 1);
            items.set(i, (Object)itemStack2);
        }
        return items;
    }

    @NotNull
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return Main.ITEM_AXING;
    }
}
