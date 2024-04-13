package com.codebyseth.client.gui.screens;

import com.codebyseth.BetonModClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class ClientSettingsScreen extends GameOptionsScreen {

    private OptionListWidget list;

    public ClientSettingsScreen(Screen parent, GameOptions gameOptions) {
        super(parent, gameOptions, Text.of("Client Settings"));
    }

    @Override
    protected void init() {
        this.list = this.addDrawableChild(new OptionListWidget(this.client, this.width, this.height - 64, 32, 25));
        this.list.addAll(BetonModClient.config.options());
    }
}
