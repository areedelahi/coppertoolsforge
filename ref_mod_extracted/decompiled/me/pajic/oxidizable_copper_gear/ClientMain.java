
package me.pajic.oxidizable_copper_gear;

import me.pajic.oxidizable_copper_gear.Main;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddPackFindersEvent;

@Mod(value="oxidizable_copper_gear", dist={Dist.CLIENT})
@EventBusSubscriber(modid="oxidizable_copper_gear", value={Dist.CLIENT})
public class ClientMain {
    public ClientMain(IEventBus modEventBus) {
        modEventBus.addListener(this::registerDefaultResourcePack);
    }

    private void registerDefaultResourcePack(AddPackFindersEvent event) {
        event.addPackFinders(Main.withModNamespace("resourcepacks/default_rp"), PackType.CLIENT_RESOURCES, (Component)Component.literal((String)"Oxidizable Copper Gear Default Pack"), PackSource.BUILT_IN, true, Pack.Position.TOP);
    }

    public static ResourceKey<EquipmentAsset> getAssetId(ItemStack stack, Equippable equippable, ResourceKey<EquipmentAsset> original) {
        if (stack.is(Main.OXIDIZABLE)) {
            String state;
            switch (Main.getItemOxidation(stack)) {
                case 1: {
                    String string = "exposed_";
                    break;
                }
                case 2: {
                    String string = "weathered_";
                    break;
                }
                case 3: {
                    String string = "oxidized_";
                    break;
                }
                default: {
                    String string = state = "";
                }
            }
            if (!state.isEmpty()) {
                return ResourceKey.create((ResourceKey)EquipmentAssets.ROOT_ID, (ResourceLocation)Main.withModNamespace(state + ((ResourceKey)equippable.assetId().get()).location().getPath()));
            }
        }
        return original;
    }
}
