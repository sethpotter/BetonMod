package com.codebyseth;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class BetonModSounds {

    public static final Identifier SOUNDTRACK_ID = new Identifier("betonmod:soundtrack");
    public static SoundEvent SOUNDTRACK_EVENT = SoundEvent.of(SOUNDTRACK_ID);

    public static void init(){
        Registry.register(Registries.SOUND_EVENT, BetonModSounds.SOUNDTRACK_ID, SOUNDTRACK_EVENT);
    }

}
