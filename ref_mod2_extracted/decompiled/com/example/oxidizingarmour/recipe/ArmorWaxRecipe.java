
package com.example.oxidizingarmour.recipe;

import com.example.oxidizingarmour.OxidizingArmour;
import com.example.oxidizingarmour.item.CopperArmorItem;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1852;
import net.minecraft.class_1865;
import net.minecraft.class_1937;
import net.minecraft.class_7225;
import net.minecraft.class_7710;
import net.minecraft.class_9694;

public class ArmorWaxRecipe
extends class_1852 {
    public ArmorWaxRecipe(class_7710 category) {
        super(category);
    }

    public boolean matches(class_9694 input, class_1937 level) {
        boolean hasArmor = false;
        boolean hasHoneycomb = false;
        for (int i = 0; i < input.method_59983(); ++i) {
            class_1799 stack = input.method_59984(i);
            if (stack.method_7960()) continue;
            if (stack.method_7909() instanceof CopperArmorItem) {
                if (hasArmor || Boolean.TRUE.equals(stack.method_58694(OxidizingArmour.WAXED))) {
                    return false;
                }
                hasArmor = true;
                continue;
            }
            if (stack.method_31574(class_1802.field_20414)) {
                if (hasHoneycomb) {
                    return false;
                }
                hasHoneycomb = true;
                continue;
            }
            return false;
        }
        return hasArmor && hasHoneycomb;
    }

    public class_1799 assemble(class_9694 input, class_7225.class_7874 provider) {
        class_1799 armorStack = class_1799.field_8037;
        for (int i = 0; i < input.method_59983(); ++i) {
            class_1799 stack = input.method_59984(i);
            if (stack.method_7960() || !(stack.method_7909() instanceof CopperArmorItem)) continue;
            armorStack = stack;
            break;
        }
        if (!armorStack.method_7960()) {
            class_1799 result = armorStack.method_7972();
            result.method_7939(1);
            result.method_57379(OxidizingArmour.WAXED, (Object)true);
            return result;
        }
        return class_1799.field_8037;
    }

    public class_1865<? extends class_1852> method_8119() {
        return OxidizingArmour.WAX_RECIPE_SERIALIZER;
    }
}
