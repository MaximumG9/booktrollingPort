package com.terriblefriends.booktrolling.mixins;

import com.terriblefriends.booktrolling.Booktrolling;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(RegistryByteBuf.class)
public class RegistryByteBufMixin {
    @Inject(method="makeFactory",at=@At("HEAD"))
    private static void booktrolling$copyNewestRegistryManager(DynamicRegistryManager registryManager, CallbackInfoReturnable<Function<ByteBuf, RegistryByteBuf>> cir) {
        Booktrolling.registryManager = registryManager;
    }
}
