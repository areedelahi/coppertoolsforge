
package me.pajic.oxidizable_copper_gear.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.oxidizable_copper_gear.ClientMain;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.Equippable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value={HumanoidArmorLayer.class})
public class HumanoidArmorLayerMixin {
    @ModifyArg(method={"renderArmorPiece"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer;renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;II)V"), index=1)
    private ResourceKey<EquipmentAsset> handleCopperArmorOxidation(ResourceKey<EquipmentAsset> original, @Local(argsOnly=true) ItemStack stack, @Local Equippable equippable) {
        return ClientMain.getAssetId(stack, equippable, original);
    }
}
