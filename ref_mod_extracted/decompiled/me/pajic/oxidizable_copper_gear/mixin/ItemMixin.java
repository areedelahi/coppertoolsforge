
package me.pajic.oxidizable_copper_gear.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.oxidizable_copper_gear.Main;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value={Item.class})
public class ItemMixin {
    @WrapMethod(method={"inventoryTick"})
    private void oxidizePlayerCopperGear(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot, Operation<Void> original) {
        Main.tryOxidize(stack, level.getDayTime(), level.getRandom(), false);
        original.call(new Object[]{stack, level, entity, slot});
    }
}
