package com.coppertools.oxidation;

import net.minecraftforge.common.ForgeConfigSpec;

public class OxidationConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.IntValue WAX_MAX_USES;

    public static final ForgeConfigSpec.IntValue USES_PER_OXIDATION_STAGE;
    public static final ForgeConfigSpec.IntValue ARMOR_USES_PER_OXIDATION_STAGE;

    public static final ForgeConfigSpec.DoubleValue EXPOSED_SPEED_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue WEATHERED_SPEED_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue OXIDIZED_SPEED_MULTIPLIER;
    public static final ForgeConfigSpec.IntValue WEATHERED_ARMOR_REDUCTION;
    public static final ForgeConfigSpec.IntValue OXIDIZED_ARMOR_REDUCTION;
    public static final ForgeConfigSpec.DoubleValue WEATHERED_DURABILITY_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue OXIDIZED_DURABILITY_MULTIPLIER;

    static {
        BUILDER.comment("Copper Tools - Oxidation Configuration")
                .push("wax");

        WAX_MAX_USES = BUILDER
                .comment("How many uses before wax wears off. Default: 200")
                .defineInRange("waxMaxUses", 200, 10, 10000);

        BUILDER.pop().push("oxidation");

        USES_PER_OXIDATION_STAGE = BUILDER
                .comment("Number of block-breaks/attacks before tool oxidation advances one stage. Default: 44 (~1/3 of stone durability)")
                .defineInRange("usesPerOxidationStage", 44, 5, 1000);

        ARMOR_USES_PER_OXIDATION_STAGE = BUILDER
                .comment("Number of 'uses' before armor oxidation advances one stage. Each minute worn = 1 use; each hit taken = 1 use. Default: 20 (~20 minutes idle, faster in combat)")
                .defineInRange("armorUsesPerOxidationStage", 20, 1, 500);

        BUILDER.pop().push("penalties");

        EXPOSED_SPEED_MULTIPLIER = BUILDER
                .comment("Mining speed multiplier at Exposed stage (1.0 = no penalty). Default: 0.90")
                .defineInRange("exposedSpeedMultiplier", 0.90, 0.1, 1.0);

        WEATHERED_SPEED_MULTIPLIER = BUILDER
                .comment("Mining speed multiplier at Weathered stage. Default: 0.80")
                .defineInRange("weatheredSpeedMultiplier", 0.80, 0.1, 1.0);

        OXIDIZED_SPEED_MULTIPLIER = BUILDER
                .comment("Mining speed multiplier at Oxidized stage. Default: 0.70")
                .defineInRange("oxidizedSpeedMultiplier", 0.70, 0.1, 1.0);

        WEATHERED_ARMOR_REDUCTION = BUILDER
                .comment("Armor value reduction at Weathered stage. Default: 1")
                .defineInRange("weatheredArmorReduction", 1, 0, 5);

        OXIDIZED_ARMOR_REDUCTION = BUILDER
                .comment("Armor value reduction at Oxidized stage. Default: 2")
                .defineInRange("oxidizedArmorReduction", 2, 0, 5);

        WEATHERED_DURABILITY_MULTIPLIER = BUILDER
                .comment("Extra durability consumption multiplier at Weathered stage. Default: 1.25 (25% more damage)")
                .defineInRange("weatheredDurabilityMultiplier", 1.25, 1.0, 5.0);

        OXIDIZED_DURABILITY_MULTIPLIER = BUILDER
                .comment("Extra durability consumption multiplier at Oxidized stage. Default: 1.50 (50% more damage)")
                .defineInRange("oxidizedDurabilityMultiplier", 1.50, 1.0, 5.0);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
