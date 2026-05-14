
package me.pajic.oxidizable_copper_gear.mixin;

import it.unimi.dsi.fastutil.objects.ObjectBooleanPair;
import me.pajic.oxidizable_copper_gear.Main;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ItemEntity.class})
public abstract class ItemEntityMixin
extends Entity {
    public ItemEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    public abstract ItemStack getItem();

    @Shadow
    public abstract void setItem(ItemStack var1);

    @Inject(method={"tick"}, at={@At(value="TAIL")})
    private void oxidizeDroppedCopperGear(CallbackInfo ci) {
        ObjectBooleanPair<ItemStack> updatedStack = Main.tryOxidize(this.getItem(), this.level().getDayTime(), this.level().getRandom(), true);
        if (updatedStack.rightBoolean()) {
            this.setItem((ItemStack)updatedStack.left());
        }
    }
}
