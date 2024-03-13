package com.codebyseth;

import com.codebyseth.client.player.BetonPlayer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class IceSlideSoundInstance extends MovingSoundInstance {

    private final BetonPlayer player;

    public IceSlideSoundInstance(BetonPlayer player) {
        super(BetonModSounds.ICESLIDE_EVENT, SoundCategory.PLAYERS, SoundInstance.createRandom());
        this.player = player;
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = 1.0F;
        //this.relative = true;
    }

    @Override
    public void tick() {
        boolean onIce = player.isOnGround() && player.getSteppingBlockState().getBlock() == Blocks.ICE;
        if (!this.player.isRemoved() && onIce) {
            this.x = (float) this.player.getX();
            this.y = (float) this.player.getY();
            this.z = (float) this.player.getZ();
            float f = (float) this.player.getVelocity().lengthSquared();
            if ((double) f >= 1.0E-7) {
                this.volume = MathHelper.clamp(f * 2.0F, 0.0F, 1.0F);
            } else {
                this.volume = 0.0F;
            }

            if (this.volume > 0.8F) {
                this.pitch = 1.0F + (this.volume - 0.8F);
            } else {
                this.pitch = 1.0F;
            }
        } else {
            this.volume = 0.0F;
            //this.setDone();
        }
    }
}
