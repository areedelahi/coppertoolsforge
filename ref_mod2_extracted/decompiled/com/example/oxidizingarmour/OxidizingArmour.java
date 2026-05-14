
package com.example.oxidizingarmour;

import com.example.oxidizingarmour.entity.HuskedVillager;
import com.example.oxidizingarmour.item.CopperArmorItem;
import com.example.oxidizingarmour.recipe.ArmorWaxRecipe;
import com.mojang.serialization.Codec;
import java.util.function.Function;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.class_10191;
import net.minecraft.class_10394;
import net.minecraft.class_1299;
import net.minecraft.class_1311;
import net.minecraft.class_1642;
import net.minecraft.class_1740;
import net.minecraft.class_1792;
import net.minecraft.class_1852;
import net.minecraft.class_1865;
import net.minecraft.class_1935;
import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_5132;
import net.minecraft.class_5321;
import net.minecraft.class_7706;
import net.minecraft.class_7923;
import net.minecraft.class_7924;
import net.minecraft.class_8051;
import net.minecraft.class_9331;

public class OxidizingArmour
implements ModInitializer {
    public static final String MOD_ID = "oxidizable_copper_gear";
    public static class_1792 COPPER_HELMET;
    public static class_1792 COPPER_CHESTPLATE;
    public static class_1792 COPPER_LEGGINGS;
    public static class_1792 COPPER_BOOTS;
    public static class_9331<Integer> OXIDATION;
    public static class_9331<Boolean> WAXED;
    public static class_1865<ArmorWaxRecipe> WAX_RECIPE_SERIALIZER;
    public static final class_5321<class_10394> EXPOSED_ASSET;
    public static final class_5321<class_10394> WEATHERED_ASSET;
    public static final class_5321<class_10394> OXIDIZED_ASSET;
    public static class_1299<HuskedVillager> HUSKED_VILLAGER;

    private static class_5321<class_1792> itemKey(String name) {
        return class_5321.method_29179((class_5321)class_7924.field_41197, (class_2960)class_2960.method_60655((String)MOD_ID, (String)name));
    }

    private static class_1792 registerItem(String name, Function<class_1792.class_1793, class_1792> factory) {
        class_5321<class_1792> key = OxidizingArmour.itemKey(name);
        class_1792 item = factory.apply(new class_1792.class_1793().method_63686(key));
        return (class_1792)class_2378.method_39197((class_2378)class_7923.field_41178, key, (Object)item);
    }

    public void onInitialize() {
        class_5321 HUSK_VILLAGER_KEY = class_5321.method_29179((class_5321)class_7924.field_41266, (class_2960)class_2960.method_60655((String)MOD_ID, (String)"husked_villager"));
        HUSKED_VILLAGER = (class_1299)class_2378.method_39197((class_2378)class_7923.field_41177, (class_5321)HUSK_VILLAGER_KEY, (Object)class_1299.class_1300.method_5903(HuskedVillager::new, (class_1311)class_1311.field_6302).method_17687(0.6f, 1.95f).method_27299(8).method_5905(HUSK_VILLAGER_KEY));
        FabricDefaultAttributeRegistry.register(HUSKED_VILLAGER, (class_5132.class_5133)class_1642.method_26940());
        OXIDATION = (class_9331)class_2378.method_10230((class_2378)class_7923.field_49658, (class_2960)class_2960.method_60655((String)MOD_ID, (String)"oxidation"), (Object)class_9331.method_57873().method_57881((Codec)Codec.INT).method_57880());
        WAXED = (class_9331)class_2378.method_10230((class_2378)class_7923.field_49658, (class_2960)class_2960.method_60655((String)MOD_ID, (String)"waxed"), (Object)class_9331.method_57873().method_57881((Codec)Codec.BOOL).method_57880());
        WAX_RECIPE_SERIALIZER = (class_1865)class_2378.method_10230((class_2378)class_7923.field_41189, (class_2960)class_2960.method_60655((String)MOD_ID, (String)"crafting_special_armorwax"), (Object)new class_1852.class_1866(ArmorWaxRecipe::new));
        COPPER_HELMET = OxidizingArmour.registerItem("copper_helmet", props -> new CopperArmorItem(props.method_66332(class_1740.field_61364, class_8051.field_41934).method_57349(OXIDATION, (Object)0)));
        COPPER_CHESTPLATE = OxidizingArmour.registerItem("copper_chestplate", props -> new CopperArmorItem(props.method_66332(class_1740.field_61364, class_8051.field_41935).method_57349(OXIDATION, (Object)0)));
        COPPER_LEGGINGS = OxidizingArmour.registerItem("copper_leggings", props -> new CopperArmorItem(props.method_66332(class_1740.field_61364, class_8051.field_41936).method_57349(OXIDATION, (Object)0)));
        COPPER_BOOTS = OxidizingArmour.registerItem("copper_boots", props -> new CopperArmorItem(props.method_66332(class_1740.field_61364, class_8051.field_41937).method_57349(OXIDATION, (Object)0)));
        ItemGroupEvents.modifyEntriesEvent((class_5321)class_7706.field_40202).register(content -> {
            content.method_45421((class_1935)COPPER_HELMET);
            content.method_45421((class_1935)COPPER_CHESTPLATE);
            content.method_45421((class_1935)COPPER_LEGGINGS);
            content.method_45421((class_1935)COPPER_BOOTS);
        });
    }

    static {
        EXPOSED_ASSET = class_5321.method_29179((class_5321)class_10191.field_55214, (class_2960)class_2960.method_60655((String)MOD_ID, (String)"exposed_copper"));
        WEATHERED_ASSET = class_5321.method_29179((class_5321)class_10191.field_55214, (class_2960)class_2960.method_60655((String)MOD_ID, (String)"weathered_copper"));
        OXIDIZED_ASSET = class_5321.method_29179((class_5321)class_10191.field_55214, (class_2960)class_2960.method_60655((String)MOD_ID, (String)"oxidized_copper"));
    }
}
