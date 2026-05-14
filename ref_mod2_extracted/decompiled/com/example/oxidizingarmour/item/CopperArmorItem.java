
package com.example.oxidizingarmour.item;

import com.example.oxidizingarmour.OxidizingArmour;
import java.util.Optional;
import net.minecraft.class_10191;
import net.minecraft.class_10192;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_3218;
import net.minecraft.class_9334;

public class CopperArmorItem
extends class_1792 {
    public CopperArmorItem(class_1792.class_1793 properties) {
        super(properties);
    }

    public void method_7888(class_1799 stack, class_3218 level, class_1297 entity, class_1304 slot) {
        super.method_7888(stack, level, entity, slot);
        if (Boolean.TRUE.equals(stack.method_58694(OxidizingArmour.WAXED))) {
            return;
        }
        if (stack.method_7963()) {
            float damageRatio = (float)stack.method_7919() / (float)stack.method_7936();
            int targetOxid = damageRatio < 0.25f ? 0 : (damageRatio < 0.5f ? 1 : (damageRatio < 0.75f ? 2 : 3));
            Integer currentOxidation = (Integer)stack.method_58694(OxidizingArmour.OXIDATION);
            if (currentOxidation == null) {
                currentOxidation = 0;
            }
            if (currentOxidation != targetOxid) {
                stack.method_57379(OxidizingArmour.OXIDATION, (Object)targetOxid);
                class_10192 eq = (class_10192)stack.method_58694(class_9334.field_54196);
                if (eq != null) {
                    Object newAsset = targetOxid == 1 ? OxidizingArmour.EXPOSED_ASSET : (targetOxid == 2 ? OxidizingArmour.WEATHERED_ASSET : (targetOxid == 3 ? OxidizingArmour.OXIDIZED_ASSET : class_10191.field_61365));
                    stack.method_57379(class_9334.field_54196, (Object)new class_10192(eq.comp_3174(), eq.comp_3175(), Optional.of(newAsset), eq.comp_3306(), eq.comp_3177(), eq.comp_3178(), eq.comp_3213(), eq.comp_3214(), eq.comp_3523(), eq.comp_4362(), eq.comp_4363()));
                }
            }
        }
    }
}
