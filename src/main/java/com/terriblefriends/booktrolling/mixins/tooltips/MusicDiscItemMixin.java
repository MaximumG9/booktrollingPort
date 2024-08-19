package com.terriblefriends.booktrolling.mixins.tooltips;

import net.minecraft.component.type.JukeboxPlayableComponent;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(JukeboxPlayableComponent.class)
public class MusicDiscItemMixin {
    @Inject(at=@At("HEAD"),method="appendTooltip", cancellable = true)
    public void booktrolling$appendSizeTooltip(Item.TooltipContext context, Consumer<Text> tooltip, TooltipType type, CallbackInfo ci) {
        ci.cancel();
    }
}
