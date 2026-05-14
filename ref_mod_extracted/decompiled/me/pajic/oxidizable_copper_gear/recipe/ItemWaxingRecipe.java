
package me.pajic.oxidizable_copper_gear.recipe;

import me.pajic.oxidizable_copper_gear.Main;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemWaxingRecipe
extends CustomRecipe {
    public ItemWaxingRecipe(CraftingBookCategory category) {
        super(category);
    }

    public boolean matches(CraftingInput input, @NotNull Level level) {
        if (input.size() != 2) {
            return false;
        }
        boolean hasWaxableItem = false;
        boolean hasHoneycomb = false;
        for (int i = 0; i < input.size(); ++i) {
            ItemStack itemStack = input.getItem(i);
            if (itemStack.isEmpty()) continue;
            if (itemStack.is(Items.HONEYCOMB)) {
                if (hasHoneycomb) {
                    return false;
                }
                hasHoneycomb = true;
                continue;
            }
            if (!itemStack.is(Main.OXIDIZABLE) || itemStack.has(Main.WAXED)) {
                return false;
            }
            if (hasWaxableItem) {
                return false;
            }
            hasWaxableItem = true;
        }
        return hasWaxableItem && hasHoneycomb;
    }

    @NotNull
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack itemStack = ItemStack.EMPTY;
        for (int i = 0; i < input.size(); ++i) {
            ItemStack itemStack2 = input.getItem(i);
            if (!itemStack2.is(Main.OXIDIZABLE)) continue;
            itemStack = itemStack2.copy();
            itemStack.set(Main.WAXED, (Object)true);
        }
        return itemStack;
    }

    @NotNull
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return Main.ITEM_WAXING;
    }
}
