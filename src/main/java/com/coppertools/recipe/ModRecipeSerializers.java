package com.coppertools.recipe;

import com.coppertools.CopperToolsMod;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CopperToolsMod.MOD_ID);

    public static final RegistryObject<RecipeSerializer<WaxingRecipe>> WAXING =
            RECIPE_SERIALIZERS.register("waxing",
                    () -> new SimpleCraftingRecipeSerializer<>(WaxingRecipe::new));

    public static final RegistryObject<RecipeSerializer<AxeScrapingRecipe>> AXE_SCRAPING =
            RECIPE_SERIALIZERS.register("axe_scraping",
                    () -> new SimpleCraftingRecipeSerializer<>(AxeScrapingRecipe::new));

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
