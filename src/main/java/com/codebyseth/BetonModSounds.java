package com.codebyseth;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class BetonModSounds {

    public static final Identifier SOUNDTRACK_ID = new Identifier("betonmod:music.soundtrack");
    public static SoundEvent SOUNDTRACK_EVENT = SoundEvent.of(SOUNDTRACK_ID);

    public static final Identifier WALK_ID = new Identifier("betonmod:player.walk");
    public static SoundEvent WALK_EVENT = SoundEvent.of(WALK_ID);

    public static final Identifier RUN_ID = new Identifier("betonmod:player.run");
    public static SoundEvent RUN_EVENT = SoundEvent.of(RUN_ID);

    public static final Identifier JUMP_ID = new Identifier("betonmod:player.jump");
    public static SoundEvent JUMP_EVENT = SoundEvent.of(JUMP_ID);

    public static final Identifier LAND_ID = new Identifier("betonmod:player.land");
    public static SoundEvent LAND_EVENT = SoundEvent.of(LAND_ID);

    public static final Identifier WIND_ID = new Identifier("betonmod:player.wind");
    public static SoundEvent WIND_EVENT = SoundEvent.of(WIND_ID);

    public static final Identifier CONCRETE_AMBIENCE_ID = new Identifier("betonmod:ambient.concrete_ambience");
    public static SoundEvent CONCRETE_AMBIENCE_EVENT = SoundEvent.of(CONCRETE_AMBIENCE_ID);

    public static final Identifier CRUMBLING_ID = new Identifier("betonmod:ambient.crumbling");
    public static SoundEvent CRUMBLING_EVENT = SoundEvent.of(CRUMBLING_ID);

    public static final Identifier DISTANT_NATURE_ID = new Identifier("betonmod:ambient.distance_nature");
    public static SoundEvent DISTANT_NATURE_EVENT = SoundEvent.of(DISTANT_NATURE_ID);

    public static final Identifier NATURE_ID = new Identifier("betonmod:ambient.nature");
    public static SoundEvent NATURE_EVENT = SoundEvent.of(NATURE_ID);

    public static final Identifier FEAR_ID = new Identifier("betonmod:player.fear");
    public static SoundEvent FEAR_EVENT = SoundEvent.of(FEAR_ID);

    public static final Identifier ICESLIDE_ID = new Identifier("betonmod:player.iceslide");
    public static SoundEvent ICESLIDE_EVENT = SoundEvent.of(ICESLIDE_ID);

    public static void init(){
        Registry.register(Registries.SOUND_EVENT, BetonModSounds.SOUNDTRACK_ID, SOUNDTRACK_EVENT);
        Registry.register(Registries.SOUND_EVENT, BetonModSounds.WALK_ID, WALK_EVENT);
        Registry.register(Registries.SOUND_EVENT, BetonModSounds.RUN_ID, RUN_EVENT);
        Registry.register(Registries.SOUND_EVENT, BetonModSounds.JUMP_ID, JUMP_EVENT);
        Registry.register(Registries.SOUND_EVENT, BetonModSounds.LAND_ID, LAND_EVENT);
        Registry.register(Registries.SOUND_EVENT, BetonModSounds.WIND_ID, WIND_EVENT);

        Registry.register(Registries.SOUND_EVENT, BetonModSounds.CONCRETE_AMBIENCE_ID, CONCRETE_AMBIENCE_EVENT);
        Registry.register(Registries.SOUND_EVENT, BetonModSounds.CRUMBLING_ID, CRUMBLING_EVENT);
        Registry.register(Registries.SOUND_EVENT, BetonModSounds.DISTANT_NATURE_ID, DISTANT_NATURE_EVENT);
        Registry.register(Registries.SOUND_EVENT, BetonModSounds.NATURE_ID, NATURE_EVENT);

        Registry.register(Registries.SOUND_EVENT, BetonModSounds.FEAR_ID, FEAR_EVENT);
        Registry.register(Registries.SOUND_EVENT, BetonModSounds.ICESLIDE_ID, ICESLIDE_EVENT);

    }

}
