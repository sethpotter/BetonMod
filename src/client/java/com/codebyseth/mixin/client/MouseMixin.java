package com.codebyseth.mixin.client;

import com.codebyseth.BetonModClient;
import net.minecraft.client.Mouse;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {

    @Inject(method = "onMouseScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;getDiscreteMouseScroll()Lnet/minecraft/client/option/SimpleOption;"))
    private void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        BetonModClient.mouseScroll = vertical;
    }

    @Redirect(method = "onMouseScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;scrollInHotbar(D)V"))
    private void onMouseScrollHotbarSwap(PlayerInventory instance, double scrollAmount) {
        if (!BetonModClient.config.scrollJump.getValue()) {
            instance.scrollInHotbar(scrollAmount);
        }
    }
}
