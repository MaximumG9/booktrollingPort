package com.terriblefriends.booktrolling.mixins.tooltips;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(FireworkRocketItem.class)
public class FireworkRocketItemMixin extends Item {
    public FireworkRocketItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(at=@At("HEAD"),method="appendTooltip")
    public void booktrolling$appendSizeTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci) {
        super.appendTooltip(stack, world, tooltip, context);
    }
}
