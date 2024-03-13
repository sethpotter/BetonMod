package com.codebyseth.mixin.client;

import com.codebyseth.client.gui.screens.ClientSettingsScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.function.Supplier;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin {

    @ModifyArgs(method = "init",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/GridWidget$Adder;add(Lnet/minecraft/client/gui/widget/Widget;I)Lnet/minecraft/client/gui/widget/Widget;"))
    private void init(Args args) {
        args.set(0, EmptyWidget.ofHeight(20));
        args.set(1, 1);
    }

    @Inject(method = "init",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/GridWidget$Adder;add(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;", ordinal = 2),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void init(CallbackInfo ci, GridWidget gridWidget, GridWidget.Adder adder) {
        adder.add(this.createButton(
                Text.of("Client Settings"),
                () -> new ClientSettingsScreen(null, MinecraftClient.getInstance().options)
        ));
    }

    private ButtonWidget createButton(Text message, Supplier<Screen> screenSupplier) {
        return ButtonWidget.builder(message, (button) -> {
            MinecraftClient.getInstance().setScreen(screenSupplier.get());
        }).build();
    }
}