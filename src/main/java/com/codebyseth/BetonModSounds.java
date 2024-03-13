package com.codebyseth;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class BetonModSounds {

    public static final Identifier SOUNDTRACK_ID = new Identifier("betonmod:soundtrack");
    public static SoundEvent SOUNDTRACK_EVENT = SoundEvent.of(SOUNDTRACK_ID);

    public static final Identifier WALK_ID = new Identifier("betonmod:walk");
    public static SoundEvent WALK_EVENT = SoundEvent.of(WALK_ID);

    public static final Identifier RUN_ID = new Identifier("betonmod:run");
    public static SoundEvent RUN_EVENT = SoundEvent.of(RUN_ID);

    public static final Identifier JUMP_ID = new Identifier("betonmod:jump");
    public static SoundEvent JUMP_EVENT = SoundEvent.of(JUMP_ID);

    public static final Identifier LAND_ID = new Identifier("betonmod:land");
    public static SoundEvent LAND_EVENT = SoundEvent.of(LAND_ID);

    public static final Identifier WIND_ID = new Identifier("betonmod:wind");
    public static SoundEvent WIND_EVENT = SoundEvent.of(WIND_ID);

    public static void init(){
        Registry.register(Registries.SOUND_EVENT, BetonModSounds.SOUNDTRACK_ID, SOUNDTRACK_EVENT);
        Registry.register(Registries.SOUND_EVENT, BetonModSounds.WALK_ID, WALK_EVENT);
        Registry.register(Registries.SOUND_EVENT, BetonModSounds.RUN_ID, RUN_EVENT);
        Registry.register(Registries.SOUND_EVENT, BetonModSounds.JUMP_ID, JUMP_EVENT);
        Registry.register(Registries.SOUND_EVENT, BetonModSounds.LAND_ID, LAND_EVENT);
        Registry.register(Registries.SOUND_EVENT, BetonModSounds.WIND_ID, WIND_EVENT);
    }

}
