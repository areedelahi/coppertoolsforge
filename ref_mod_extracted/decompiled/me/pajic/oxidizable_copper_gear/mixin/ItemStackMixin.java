
package me.pajic.oxidizable_copper_gear.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.pajic.oxidizable_copper_gear.Main;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value={ItemStack.class})
public abstract class ItemStackMixin
implements DataComponentHolder {
    @ModifyReturnValue(method={"getItemName"}, at={@At(value="RETURN")})
    private Component addOxidationPrefix(Component original) {
        Component name = switch (Main.getItemOxidation((ItemStack)this)) {
            case 1 -> Component.translatable((String)"text.oxidizable_copper_gear.exposed", (Object[])new Object[]{original});
            case 2 -> Component.translatable((String)"text.oxidizable_copper_gear.weathered", (Object[])new Object[]{original});
            case 3 -> Component.translatable((String)"text.oxidizable_copper_gear.oxidized", (Object[])new Object[]{original});
            default -> original;
        };
        return this.has(Main.WAXED) ? Component.translatable((String)"text.oxidizable_copper_gear.waxed", (Object[])new Object[]{name}) : name;
    }
}
