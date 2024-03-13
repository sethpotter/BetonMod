package com.codebyseth.mixin.client;

import com.codebyseth.BetonModClient;
import com.codebyseth.client.player.BetonPlayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.HoneyBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HoneyBlock.class)
public class HoneyBlockMixin {

    @Inject(method = "onEntityCollision", at = @At(value = "HEAD"))
    private void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (isSlidingNoFall(pos, entity)) {
            if (entity instanceof BetonPlayer betonPlayer) {
                betonPlayer.honeySliding = true;
            }
        }
    }

    @Unique
    private boolean isSlidingNoFall(BlockPos pos, Entity entity) {
        if (entity.isOnGround()) {
            return false;
        } else if (entity.getY() > (double)pos.getY() + 0.9375 - 1.0E-7) {
            return false;
        } else {
            double d = Math.abs((double)pos.getX() + 0.5 - entity.getX());
            double e = Math.abs((double)pos.getZ() + 0.5 - entity.getZ());
            double f = 0.4375 + (double)(entity.getWidth() / 2.0F);
            return d + 1.0E-7 > f || e + 1.0E-7 > f;
        }
    }


    @Redirect(method = "onEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/HoneyBlock;updateSlidingVelocity(Lnet/minecraft/entity/Entity;)V"))
    private void redirectUpdateSlidingVelocity(HoneyBlock instance, Entity entity) {
        this.updateSlidingVelocity(entity);
    }


    @Unique
    private void updateSlidingVelocity(Entity entity) {
        Vec3d vec3d = entity.getVelocity();
        double shift = BetonModClient.config.honeySlideSpeed.getValue();
        if (vec3d.y < 0) {
            double d = shift / vec3d.y;
            entity.setVelocity(new Vec3d(vec3d.x * d, vec3d.y, vec3d.z * d));
        }
        entity.onLanding();
    }
}
