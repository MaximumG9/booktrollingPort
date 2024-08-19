package com.terriblefriends.booktrolling.mixins.tooltips;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(BundleItem.class)
public class BundleItemMixin extends Item {

    public BundleItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(at=@At("HEAD"),method="getTooltipData",cancellable = true)
    private void booktrolling$preventHugeBundleTooltipLag(ItemStack stack, CallbackInfoReturnable<Optional<TooltipData>> cir) {
        if (stack.getComponents().get(DataComponentTypes.BUNDLE_CONTENTS).size() > 1000) {
            cir.setReturnValue(Optional.empty());
            cir.cancel();
        }
    }
}
