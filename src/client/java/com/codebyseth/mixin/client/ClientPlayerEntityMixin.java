package com.codebyseth.mixin.client;

import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/input/Input;hasForwardMovement()Z"))
    private boolean tickMovement(Input input) {
        return input.movementForward > 1.0E-5F || input.movementForward < -1.0E-5F;
    }

    @Inject(method = "isWalking", at = @At(value = "RETURN"), cancellable = true)
    private void isWalking(CallbackInfoReturnable<Boolean> cir) {
        ClientPlayerEntity self = (ClientPlayerEntity) (Object) this;
        //boolean hasImpulse = self.input.movementForward >= 0.8 || self.input.movementForward <= -0.8;
        boolean hasImpulse = true;
        cir.setReturnValue(self.isSubmergedInWater() ? self.input.hasForwardMovement() : hasImpulse);
    }

}
