package com.codebyseth.mixin.client;

import com.codebyseth.BetonModClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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
        if (BetonModClient.config.betonMovement.getValue()) {
            cir.setReturnValue(self.isSubmergedInWater() ? self.input.hasForwardMovement() : hasImpulse);
        } else {
            cir.cancel();
        }
    }

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/input/Input;tick(ZF)V", shift = At.Shift.AFTER))
    private void tickMovementOnJump(CallbackInfo ci) {
        ClientPlayerEntity self = (ClientPlayerEntity) (Object) this;
        if (BetonModClient.config.scrollJump.getValue()) {
            if (BetonModClient.mouseScroll != 0) {
                self.input.jumping = true;
            }
        }
    }

    @Inject(method = "tickMovement", at = @At(value = "TAIL"))
    private void tickMovementResetScroll(CallbackInfo ci) {
        BetonModClient.mouseScroll = 0;
    }
}
