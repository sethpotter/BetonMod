package com.codebyseth.client.gui.screens;

import com.codebyseth.BetonModClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
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

        SimpleOption<?>[] options = new SimpleOption[] {
                BetonModClient.config.betonMovement,
                BetonModClient.config.applyBlockFriction,
                BetonModClient.config.applyBlockJumpFactor,
                BetonModClient.config.scrollJump,
                BetonModClient.config.airControl,
                BetonModClient.config.groundControl,
                BetonModClient.config.lowFrictionControl,
                BetonModClient.config.airSpeedBonus,
                BetonModClient.config.groundSpeedBonus,
                BetonModClient.config.slideBoostCap,
                BetonModClient.config.slideBoostDecay,
                BetonModClient.config.slideBoostAmount,
                BetonModClient.config.slideBoostFrictionThreshold,
                BetonModClient.config.lowFrictionThreshold,
                BetonModClient.config.deltaTicks,
                BetonModClient.config.climbingSpeedCap,
                BetonModClient.config.climbingSpeed,
                BetonModClient.config.sprintingSpeed,
                BetonModClient.config.playerGravity,
                BetonModClient.config.jumpPower,
                BetonModClient.config.honeyJumpPower,
                BetonModClient.config.honeyJumpSpeed,
                BetonModClient.config.honeySlideSpeed,
                BetonModClient.config.honeySlideDecay,
                BetonModClient.config.sprintAirSpeedBonus
        };

        this.list.addAll(options);
    }
}
