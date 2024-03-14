package com.codebyseth.mixin.client;

import com.codebyseth.BetonModClient;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Shadow private int jumpingCooldown;

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;jump()V"))
    private void tickMovement(CallbackInfo ci) {
        if (BetonModClient.config.scrollJump.getValue()) {
            if (BetonModClient.mouseScroll != 0) {
                BetonModClient.mouseScroll = 0;
                jumpingCooldown = 0;
            }
        }
    }

}
