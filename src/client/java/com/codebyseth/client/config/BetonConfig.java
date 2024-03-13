package com.codebyseth.client.config;

import com.mojang.serialization.Codec;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

import java.util.Locale;

@Config(name = "betonmod")
public class BetonConfig implements ConfigData {

    public final SimpleOption<Boolean> betonMovement = SimpleOption.ofBoolean("Beton Movement", true);
    public final SimpleOption<Boolean> applyBlockFriction = SimpleOption.ofBoolean("Block Friction", false);
    public final SimpleOption<Boolean> applyBlockJumpFactor = SimpleOption.ofBoolean("Block Jump Factor", true);
    public final SimpleOption<Boolean> scrollJump = SimpleOption.ofBoolean("Scroll Jump", false);
    public final SimpleOption<Double> airControl = intRangeSlider("Air Control", 6.5, 0.0, 20.0, 0, 2000, 100.0);
    public final SimpleOption<Double> groundControl = intRangeSlider("Ground Control", 7.5, 0.0, 20.0, 0, 2000, 100.0);
    public final SimpleOption<Double> lowFrictionControl = intRangeSlider("Low Friction Control", 0.7, 0.0, 20.0, 0, 2000, 100.0);
    public final SimpleOption<Double> airSpeedBonus = intRangeSlider("Air Speed Bonus", 0.26, -10.0, 10.0, -500, 500, 500.0);
    public final SimpleOption<Double> groundSpeedBonus = intRangeSlider("Ground Speed Bonus", 0.20, -10.0, 10.0, -500, 500, 500.0);
    public final SimpleOption<Double> slideBoostCap = intRangeSlider("Slide Boost Cap", 3.0, 0.0, 20.0, 0, 2000, 100.0);
    public final SimpleOption<Double> slideBoostDecay = intRangeSlider("Slide Boost Decay", 0.8, 0.0, 10.0, 0, 1000, 100.0);
    public final SimpleOption<Double> slideBoostAmount = intRangeSlider("Slide Boost Amount", 0.35, 0.0, 1.0, 0, 100, 100.0);
    public final SimpleOption<Double> slideBoostFrictionThreshold = intRangeSlider("Slide Boost Friction Threshold", 0.9, 0.0, 1.0, 0, 100, 100.0);
    public final SimpleOption<Double> lowFrictionThreshold = intRangeSlider("Low Friction Threshold", 0.9, 0.0, 1.0, 0, 100, 100.0);
    public final SimpleOption<Double> deltaTicks = intRangeSlider("Delta Ticks", 0.05, 0.0, 1.0, 0, 20, 20.0);
    public final SimpleOption<Double> climbingSpeedCap = intRangeSlider("Climbing Speed Cap", 0.3, 0.0, 2.0, 0, 200, 100.0);
    public final SimpleOption<Double> climbingSpeed = intRangeSlider("Climbing Speed", 0.35, 0.0, 2.0, 0, 200, 100.0);
    public final SimpleOption<Double> sprintingSpeed = intRangeSlider("Sprinting Speed", 0.06, 0.0, 2.0, 0, 200, 100.0);
    public final SimpleOption<Double> playerGravity = intRangeSlider("Player Gravity", 0.08, 0.0, 0.3, 0, 300, 1000.0);
    public final SimpleOption<Double> jumpPower = intRangeSlider("Jump Power", 0.42, 0.0, 2.0, 0, 200, 100.0);
    public final SimpleOption<Double> honeyJumpPower = intRangeSlider("Honey Jump Power", 0.0, 0.0, 1.0, 0, 100, 100.0);
    public final SimpleOption<Double> honeyJumpSpeed = intRangeSlider("Honey Jump Speed", 0.3, 0.0, 2.0, 0, 200, 100.0);


    private SimpleOption<Double> intRangeSlider(String text, double defaultValue, double min, double max, int minStep, int maxStep, double step) {
        return new SimpleOption<>(text, SimpleOption.emptyTooltip(), (optionText, value) -> {
            return GameOptions.getGenericValueText(optionText, Text.of(String.format(Locale.ROOT, "%.2f", value)));
        }, (new SimpleOption.ValidatingIntSliderCallbacks(minStep, maxStep)).withModifier((sliderProgressValue) -> {
            return (double) sliderProgressValue / step;
        }, (sliderProgressValue) -> {
            return (int) (sliderProgressValue * step);
        }), Codec.doubleRange(min, max), defaultValue, (out) -> {
        });
    }
}
