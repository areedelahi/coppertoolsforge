package com.coppertools;

import com.coppertools.init.ModCreativeTab;
import com.coppertools.init.ModItems;
import com.coppertools.network.ModNetwork;
import com.coppertools.oxidation.OxidationConfig;
import com.coppertools.recipe.ModRecipeSerializers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(CopperToolsMod.MOD_ID)
public class CopperToolsMod {
    public static final String MOD_ID = "coppertools";
    public static final Logger LOGGER = LoggerFactory.getLogger("CopperTools");

    public CopperToolsMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModCreativeTab.register(modEventBus);
        ModRecipeSerializers.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, OxidationConfig.SPEC, "coppertools-common.toml");

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModNetwork.register();
            LOGGER.info("Copper Tools mod initialized!");
        });
    }
}
