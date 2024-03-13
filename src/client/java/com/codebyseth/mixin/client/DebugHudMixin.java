package com.codebyseth.mixin.client;

import com.codebyseth.BetonModClient;
import com.codebyseth.client.player.BetonPlayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Locale;

@Mixin(DebugHud.class)
public class DebugHudMixin {

    @Inject(method = "drawLeftText",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", shift = At.Shift.AFTER, ordinal = 0),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    private void drawLeftText(DrawContext context, CallbackInfo info, List<String> list) {
        if (BetonModClient.config.betonMovement.getValue()) {
            BetonPlayer betonPlayer = (BetonPlayer) MinecraftClient.getInstance().player;

            if (betonPlayer == null)
                return;

            Vec3d velocity = betonPlayer.getVelocity();
            list.add(String.format(Locale.ROOT, "Velocity XYZ: %.3f / %.3f / %.3f", velocity.x, velocity.y, velocity.z));
            list.add(String.format(Locale.ROOT, "Slide Boost: %.3f", betonPlayer.slideBoost));
            list.add(String.format(Locale.ROOT, "Honey Sliding: %b", betonPlayer.honeySliding));
            list.add(String.format(Locale.ROOT, "Sprinting: %b", betonPlayer.isSprinting()));
            list.add(String.format(Locale.ROOT, "Jumping: %b", betonPlayer.input.jumping));
            list.add(String.format(Locale.ROOT, "H: %b, V: %b, G: %b", betonPlayer.horizontalCollision, betonPlayer.verticalCollision, betonPlayer.groundCollision));
            //list.add(String.format(Locale.ROOT, "Scroll: %.3f", minecraft.mouseHandler.mouseScroll));

            list.add("");
        }
    }
}