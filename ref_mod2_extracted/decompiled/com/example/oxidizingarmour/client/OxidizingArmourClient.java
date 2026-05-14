
package com.example.oxidizingarmour.client;

import com.example.oxidizingarmour.OxidizingArmour;
import com.example.oxidizingarmour.client.HuskedVillagerRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

@Environment(value=EnvType.CLIENT)
public class OxidizingArmourClient
implements ClientModInitializer {
    public void onInitializeClient() {
        EntityRendererRegistry.register(OxidizingArmour.HUSKED_VILLAGER, HuskedVillagerRenderer::new);
    }
}
