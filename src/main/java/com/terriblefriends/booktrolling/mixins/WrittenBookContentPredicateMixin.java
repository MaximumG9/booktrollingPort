package com.terriblefriends.booktrolling.mixins;

import net.minecraft.component.type.WrittenBookContentComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WritableBookItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.item.WrittenBookContentPredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WrittenBookContentPredicate.class)
public class WrittenBookContentPredicateMixin {
    private final WrittenBookContentPredicate THIS = (WrittenBookContentPredicate) ((Object) this);

    @Inject(at=@At("HEAD"),method="test(Lnet/minecraft/item/ItemStack;Lnet/minecraft/component/type/WrittenBookContentComponent;)Z",cancellable = true)
    private void booktrolling$allowLongTitlesToBeParsed(ItemStack itemStack, WrittenBookContentComponent writtenBookContentComponent, CallbackInfoReturnable<Boolean> cir) {
        if (!THIS.test(itemStack,writtenBookContentComponent)) {
            cir.setReturnValue(false);
        } else if (THIS.title().isEmpty()) {
            cir.setReturnValue(false);
        } else {
            cir.setReturnValue(THIS.author().isPresent());
        }
        cir.cancel();
    }
}
