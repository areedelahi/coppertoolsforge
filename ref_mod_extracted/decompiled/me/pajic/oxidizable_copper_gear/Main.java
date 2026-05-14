
package me.pajic.oxidizable_copper_gear;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.ObjectBooleanImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectBooleanPair;
import me.pajic.oxidizable_copper_gear.recipe.ItemAxingRecipe;
import me.pajic.oxidizable_copper_gear.recipe.ItemWaxingRecipe;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(value="oxidizable_copper_gear")
public class Main {
    public static final String MOD_ID = "oxidizable_copper_gear";
    private static final Logger LOGGER = LoggerFactory.getLogger((String)"Oxidizable Copper Gear");
    private static final boolean DEBUG = !FMLLoader.getCurrent().isProduction();
    public static final DataComponentType<Integer> OXIDATION = DataComponentType.builder().persistent((Codec)Codec.INT).build();
    public static final DataComponentType<Boolean> WAXED = DataComponentType.builder().persistent((Codec)Codec.BOOL).build();
    public static final TagKey<Item> OXIDIZABLE = TagKey.create((ResourceKey)Registries.ITEM, (ResourceLocation)Main.withModNamespace("oxidizable"));
    public static RecipeSerializer<ItemWaxingRecipe> ITEM_WAXING = new CustomRecipe.Serializer(ItemWaxingRecipe::new);
    public static RecipeSerializer<ItemAxingRecipe> ITEM_AXING = new CustomRecipe.Serializer(ItemAxingRecipe::new);

    public Main(IEventBus modEventBus) {
        modEventBus.addListener(this::registerData);
        modEventBus.addListener(this::registerDefaultDatapack);
        modEventBus.addListener(this::addCreative);
    }

    private void registerDefaultDatapack(AddPackFindersEvent event) {
        event.addPackFinders(Main.withModNamespace("resourcepacks/default_dp"), PackType.SERVER_DATA, (Component)Component.literal((String)"Oxidizable Copper Gear Default Pack"), PackSource.BUILT_IN, false, Pack.Position.TOP);
    }

    private void registerData(RegisterEvent event) {
        event.register(Registries.DATA_COMPONENT_TYPE, registry -> {
            registry.register(Main.withModNamespace("oxidation"), OXIDATION);
            registry.register(Main.withModNamespace("waxed"), WAXED);
        });
        event.register(Registries.RECIPE_SERIALIZER, registry -> {
            registry.register(ResourceLocation.withDefaultNamespace((String)"crafting_special_item_waxing"), ITEM_WAXING);
            registry.register(ResourceLocation.withDefaultNamespace((String)"crafting_special_item_axing"), ITEM_AXING);
        });
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() != CreativeModeTabs.SEARCH) {
            BuiltInRegistries.ITEM.getTagOrEmpty(OXIDIZABLE).forEach(item -> {
                ItemStack originalItem = new ItemStack(item);
                if (event.getParentEntries().contains((Object)originalItem)) {
                    for (int i = 3; i > 0; --i) {
                        ItemStack oxidizedItem = new ItemStack(item);
                        oxidizedItem.set(OXIDATION, (Object)i);
                        ItemStack waxedOxidizedItem = oxidizedItem.copy();
                        waxedOxidizedItem.set(WAXED, (Object)true);
                        event.insertAfter(originalItem, waxedOxidizedItem, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                        event.insertAfter(originalItem, oxidizedItem, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                    }
                    ItemStack waxedItem = originalItem.copy();
                    waxedItem.set(WAXED, (Object)true);
                    event.insertAfter(originalItem, waxedItem, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                }
            });
        }
    }

    public static ObjectBooleanPair<ItemStack> tryOxidize(ItemStack stack, long worldTime, RandomSource random, boolean copy) {
        int oxidation;
        if (stack.is(OXIDIZABLE) && !stack.has(WAXED) && worldTime % 1200L == 0L && (oxidation = Main.getItemOxidation(stack)) < 3) {
            float chanceModifier;
            float f = chanceModifier = stack.isDamageableItem() ? Mth.clampedMap((float)(stack.getMaxDamage() - stack.getDamageValue()), (float)((float)stack.getMaxDamage() * ((float)(2 - oxidation) / 4.0f)), (float)((float)stack.getMaxDamage() * ((float)(4 - oxidation) / 4.0f)), (float)0.5f, (float)2.0f) : 1.0f;
            if (random.nextFloat() < 0.028444445f / chanceModifier) {
                Main.debugLog("Ticking oxidation for item {}, chance modifier was {}", stack.getHoverName().getString(), Float.valueOf(chanceModifier));
                if (copy) {
                    ItemStack updatedStack = stack.copy();
                    Main.incrementItemOxidation(updatedStack, 1);
                    return new ObjectBooleanImmutablePair((Object)updatedStack, true);
                }
                Main.incrementItemOxidation(stack, 1);
            }
        }
        return new ObjectBooleanImmutablePair((Object)stack, false);
    }

    public static int getItemOxidation(ItemStack stack) {
        return (Integer)stack.getOrDefault(OXIDATION, (Object)0);
    }

    public static void incrementItemOxidation(ItemStack stack, int increment) {
        int updatedOxidation = Main.getItemOxidation(stack) + increment;
        if (updatedOxidation <= 0) {
            stack.remove(OXIDATION);
        } else {
            stack.set(OXIDATION, (Object)Mth.clamp((int)updatedOxidation, (int)1, (int)3));
        }
    }

    public static ResourceLocation withModNamespace(String path) {
        return ResourceLocation.fromNamespaceAndPath((String)MOD_ID, (String)path);
    }

    public static void debugLog(String message, Object ... args) {
        if (DEBUG) {
            LOGGER.info(message, args);
        }
    }
}
