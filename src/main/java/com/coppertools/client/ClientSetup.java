package com.coppertools.client;

import com.coppertools.CopperToolsMod;
import com.coppertools.init.ModItems;
import com.coppertools.oxidation.OxidationHelper;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.item.Item;

@Mod.EventBusSubscriber(modid = CopperToolsMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ResourceLocation oxidationProperty = new ResourceLocation(CopperToolsMod.MOD_ID, "oxidation");

            RegistryObject<Item>[] allItems = new RegistryObject[]{
                    ModItems.COPPER_SWORD,
                    ModItems.COPPER_PICKAXE,
                    ModItems.COPPER_AXE,
                    ModItems.COPPER_SHOVEL,
                    ModItems.COPPER_HOE,
                    ModItems.COPPER_HELMET,
                    ModItems.COPPER_CHESTPLATE,
                    ModItems.COPPER_LEGGINGS,
                    ModItems.COPPER_BOOTS
            };

            for (RegistryObject<Item> itemReg : allItems) {
                ItemProperties.register(itemReg.get(), oxidationProperty,
                        (stack, level, entity, seed) -> {

                            return OxidationHelper.getOxidationLevel(stack) * 0.25f;
                        });
            }

            CopperToolsMod.LOGGER.info("Copper Tools client setup complete - model properties registered");
        });
    }
}
