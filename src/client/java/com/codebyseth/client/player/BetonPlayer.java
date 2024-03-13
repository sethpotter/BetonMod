package com.codebyseth.client.player;

import com.codebyseth.BetonModClient;
import com.codebyseth.BetonModSounds;
import com.codebyseth.WindSoundInstance;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.StatHandler;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public class BetonPlayer extends ClientPlayerEntity {

    public float slideBoost;
    public boolean honeySliding;

    public BetonPlayer(MinecraftClient client, ClientWorld world, ClientPlayNetworkHandler networkHandler, StatHandler stats, ClientRecipeBook recipeBook, boolean lastSneaking, boolean lastSprinting) {
        super(client, world, networkHandler, stats, recipeBook, lastSneaking, lastSprinting);

        this.client.getSoundManager().play(new WindSoundInstance(this));
    }

    @Override
    public void travel(Vec3d movementInput) {
        if (!BetonModClient.config.betonMovement.getValue()) {
            super.travel(movementInput);
            return;
        }

        double d;
        if (this.isSwimming() && !this.hasVehicle()) {
            d = this.getRotationVector().y;
            double e = d < -0.2 ? 0.085 : 0.06;
            if (d <= 0.0 || this.jumping || !this.getWorld().getBlockState(BlockPos.ofFloored(this.getX(), this.getY() + 1.0 - 0.1, this.getZ())).getFluidState().isEmpty()) {
                Vec3d vec3d = this.getVelocity();
                this.setVelocity(vec3d.add(0.0, (d - vec3d.y) * e, 0.0));
            }
        }

        if (this.getAbilities().flying && !this.hasVehicle()) {
            d = this.getVelocity().y;
            //super.travel(movementInput); // TODO Error.
            livingEntityTravel(movementInput);
            Vec3d vec3d2 = this.getVelocity();
            this.setVelocity(vec3d2.x, d * 0.6, vec3d2.z);
            this.onLanding();
            this.setFlag(7, false);
        } else {
            livingEntityTravel(movementInput);
        }
    }

    private void livingEntityTravel(Vec3d movementInput) {
        if (this.isLogicalSideForUpdatingMovement()) {
            double d = BetonModClient.config.playerGravity.getValue();
            boolean bl = this.getVelocity().y <= 0.0;
            if (bl && this.hasStatusEffect(StatusEffects.SLOW_FALLING)) {
                d = 0.01;
            }

            FluidState fluidState = this.getWorld().getFluidState(this.getBlockPos());
            float f;
            double e;
            if (this.isTouchingWater() && this.shouldSwimInFluids() && !this.canWalkOnFluid(fluidState)) {
                e = this.getY();
                f = this.isSprinting() ? 0.9F : this.getBaseMovementSpeedMultiplier();
                float g = 0.02F;
                float h = (float) EnchantmentHelper.getDepthStrider(this);
                if (h > 3.0F) {
                    h = 3.0F;
                }

                if (!this.isOnGround()) {
                    h *= 0.5F;
                }

                if (h > 0.0F) {
                    f += (0.54600006F - f) * h / 3.0F;
                    g += (this.getMovementSpeed() - g) * h / 3.0F;
                }

                if (this.hasStatusEffect(StatusEffects.DOLPHINS_GRACE)) {
                    f = 0.96F;
                }

                this.updateVelocity(g, movementInput);
                this.move(MovementType.SELF, this.getVelocity());
                Vec3d vec3d = this.getVelocity();
                if (this.horizontalCollision && this.isClimbing()) {
                    vec3d = new Vec3d(vec3d.x, 0.2, vec3d.z);
                }

                this.setVelocity(vec3d.multiply(f, 0.800000011920929, f));
                Vec3d vec3d2 = this.applyFluidMovingSpeed(d, bl, this.getVelocity());
                this.setVelocity(vec3d2);
                if (this.horizontalCollision && this.doesNotCollide(vec3d2.x, vec3d2.y + 0.6000000238418579 - this.getY() + e, vec3d2.z)) {
                    this.setVelocity(vec3d2.x, 0.30000001192092896, vec3d2.z);
                }
            } else if (this.isInLava() && this.shouldSwimInFluids() && !this.canWalkOnFluid(fluidState)) {
                e = this.getY();
                this.updateVelocity(0.02F, movementInput);
                this.move(MovementType.SELF, this.getVelocity());
                Vec3d vec3d3;
                if (this.getFluidHeight(FluidTags.LAVA) <= this.getSwimHeight()) {
                    this.setVelocity(this.getVelocity().multiply(0.5, 0.800000011920929, 0.5));
                    vec3d3 = this.applyFluidMovingSpeed(d, bl, this.getVelocity());
                    this.setVelocity(vec3d3);
                } else {
                    this.setVelocity(this.getVelocity().multiply(0.5));
                }

                if (!this.hasNoGravity()) {
                    this.setVelocity(this.getVelocity().add(0.0, -d / 4.0, 0.0));
                }

                vec3d3 = this.getVelocity();
                if (this.horizontalCollision && this.doesNotCollide(vec3d3.x, vec3d3.y + 0.6000000238418579 - this.getY() + e, vec3d3.z)) {
                    this.setVelocity(vec3d3.x, 0.30000001192092896, vec3d3.z);
                }
            } else if (this.isFallFlying()) {
                this.limitFallDistance();
                Vec3d vec3d4 = this.getVelocity();
                Vec3d vec3d5 = this.getRotationVector();
                f = this.getPitch() * 0.017453292F;
                double i = Math.sqrt(vec3d5.x * vec3d5.x + vec3d5.z * vec3d5.z);
                double j = vec3d4.horizontalLength();
                double k = vec3d5.length();
                double l = Math.cos(f);
                l = l * l * Math.min(1.0, k / 0.4);
                vec3d4 = this.getVelocity().add(0.0, d * (-1.0 + l * 0.75), 0.0);
                double m;
                if (vec3d4.y < 0.0 && i > 0.0) {
                    m = vec3d4.y * -0.1 * l;
                    vec3d4 = vec3d4.add(vec3d5.x * m / i, m, vec3d5.z * m / i);
                }

                if (f < 0.0F && i > 0.0) {
                    m = j * (double)(-MathHelper.sin(f)) * 0.04;
                    vec3d4 = vec3d4.add(-vec3d5.x * m / i, m * 3.2, -vec3d5.z * m / i);
                }

                if (i > 0.0) {
                    vec3d4 = vec3d4.add((vec3d5.x / i * j - vec3d4.x) * 0.1, 0.0, (vec3d5.z / i * j - vec3d4.z) * 0.1);
                }

                this.setVelocity(vec3d4.multiply(0.9900000095367432, 0.9800000190734863, 0.9900000095367432));
                this.move(MovementType.SELF, this.getVelocity());
                if (this.horizontalCollision && !this.getWorld().isClient) {
                    m = this.getVelocity().horizontalLength();
                    double n = j - m;
                    float o = (float)(n * 10.0 - 3.0);
                    if (o > 0.0F) {
                        this.playSound(this.getFallSound((int)o), 1.0F, 1.0F);
                        this.damage(this.getDamageSources().flyIntoWall(), o);
                    }
                }

                if (this.isOnGround() && !this.getWorld().isClient) {
                    this.setFlag(7, false);
                }
            } else {
                BlockPos blockPos = this.getVelocityAffectingPos();
                Block block = this.getWorld().getBlockState(blockPos).getBlock();

                float blockFriction = (BetonModClient.config.applyBlockFriction.getValue()) ? block.getSlipperiness() : 1.0f;

                float speed;

                if (this.isOnGround()) {
                    speed = this.getMovementSpeed() + BetonModClient.config.groundSpeedBonus.getValue().floatValue();

                    // Ramping Friction
                    if (BetonModClient.config.applyBlockFriction.getValue()) {
                        speed *= 0.21600002F / (block.getSlipperiness() * block.getSlipperiness() * block.getSlipperiness());
                    }
                } else {
                    speed = this.getMovementSpeed() + BetonModClient.config.airSpeedBonus.getValue().floatValue();
                }

                if (this.isSprinting()) {
                    speed += BetonModClient.config.sprintingSpeed.getValue();
                }

                speed *= 1f + slideBoost;

                Vec3d movementInputVelocity = movementInputToVelocity(movementInput, speed, this.getYaw());

                if (this.isOnGround()) {
                    if (block.getSlipperiness() >= BetonModClient.config.lowFrictionThreshold.getValue()) {
                        this.setVelocity(lerpVector(this.getVelocity(), movementInputVelocity, BetonModClient.config.deltaTicks.getValue().floatValue() * BetonModClient.config.lowFrictionControl.getValue().floatValue()));
                    } else {
                        this.setVelocity(lerpVector(this.getVelocity(), movementInputVelocity, BetonModClient.config.deltaTicks.getValue().floatValue() * BetonModClient.config.groundControl.getValue().floatValue()));
                    }
                } else {
                    this.setVelocity(lerpVector(this.getVelocity(), movementInputVelocity, BetonModClient.config.deltaTicks.getValue().floatValue() * BetonModClient.config.airControl.getValue().floatValue()));
                }

                this.setVelocity(this.applyClimbingSpeed(this.getVelocity()));

                this.move(MovementType.SELF, this.getVelocity());

                if(this.isOnGround() && block.getSlipperiness() < BetonModClient.config.slideBoostFrictionThreshold.getValue() && block != Blocks.SLIME_BLOCK && block != Blocks.AIR) {
                    slideBoost = 0f;
                }
                slideBoost = moveTowards(slideBoost, 0f, (float) (BetonModClient.config.deltaTicks.getValue().floatValue() * 0.5 * BetonModClient.config.slideBoostDecay.getValue().floatValue()));

                // Getting stuff after we already calculated the movement.
                Vec3d vec3d = this.getVelocity();

                if ((this.horizontalCollision || this.jumping) && (this.isClimbing() || this.getBlockStateAtPos().isOf(Blocks.POWDER_SNOW) && PowderSnowBlock.canWalkOnPowderSnow(this))) {
                    vec3d = new Vec3d(vec3d.x, BetonModClient.config.climbingSpeed.getValue(), vec3d.z);
                }

                double q = vec3d.y;

                if (this.hasStatusEffect(StatusEffects.LEVITATION)) {
                    q += (0.05 * (double)(this.getStatusEffect(StatusEffects.LEVITATION).getAmplifier() + 1) - vec3d.y) * 0.2;
                } else if (this.getWorld().isClient && !this.getWorld().isChunkLoaded(blockPos)) {
                    if (this.getY() > (double)this.getWorld().getBottomY()) {
                        q = -0.1;
                    } else {
                        q = 0.0;
                    }
                } else if (!this.hasNoGravity()) {
                    q -= d;
                }

                if (this.hasNoDrag()) {
                    this.setVelocity(vec3d.x, q, vec3d.z);
                } else {
                    this.setVelocity(vec3d.x * blockFriction, q * 0.9800000190734863, vec3d.z * blockFriction);
                }
            }
        }

        this.updateLimbs(this instanceof Flutterer);
    }

    @Override
    public void jump() {
        if (!BetonModClient.config.betonMovement.getValue()) {
            super.jump();
            return;
        }

        Vec3d vec3d = this.getVelocity();
        this.setVelocity(vec3d.x, this.getJumpVelocity(), vec3d.z);

        BlockPos blockpos = this.getVelocityAffectingPos();
        Block block = this.clientWorld.getBlockState(blockpos).getBlock();

        boolean canSlideBoost = block.getSlipperiness() >= BetonModClient.config.slideBoostFrictionThreshold.getValue();
        boolean hasMovementInput = this.input.movementForward >= 0.8f || this.input.movementForward <= -0.8f;

        if (this.isSprinting() && hasMovementInput && canSlideBoost) {
            this.slideBoost = Math.min(BetonModClient.config.slideBoostCap.getValue().floatValue(), slideBoost + BetonModClient.config.slideBoostAmount.getValue().floatValue());
        }

        this.velocityDirty = true;

        this.playSound(BetonModSounds.JUMP_EVENT, 0.75f, 1.0f);

        this.incrementStat(Stats.JUMP);
        if (this.isSprinting()) {
            this.addExhaustion(0.2F);
        } else {
            this.addExhaustion(0.05F);
        }
    }

    @Override
    protected float getJumpVelocity() {
        if (!BetonModClient.config.betonMovement.getValue()) {
            return super.getJumpVelocity();
        }

        float jumpPower = BetonModClient.config.jumpPower.getValue().floatValue();

        if (BetonModClient.config.applyBlockJumpFactor.getValue())
            jumpPower *= this.getJumpVelocityMultiplier();

        return jumpPower + this.getJumpBoostVelocityModifier();
    }

    /*public Vec3d applyMovementInput(Vec3d movementInput, float slipperiness) {
        this.updateVelocity(this.getMovementSpeed(slipperiness), movementInput);
        this.setVelocity(this.applyClimbingSpeed(this.getVelocity()));
        this.move(MovementType.SELF, this.getVelocity());
        Vec3d vec3d = this.getVelocity();
        if ((this.horizontalCollision || this.jumping) && (this.isClimbing() || this.getBlockStateAtPos().isOf(Blocks.POWDER_SNOW) && PowderSnowBlock.canWalkOnPowderSnow(this))) {
            vec3d = new Vec3d(vec3d.x, 0.2, vec3d.z);
        }

        return vec3d;
    }*/

    // Exists in a super method?
    /*private float getMovementSpeed(float slipperiness) {
        return this.isOnGround() ? this.getMovementSpeed() * (0.21600002F / (slipperiness * slipperiness * slipperiness)) : this.getOffGroundSpeed();
    }*/

    @Override
    public float getMovementSpeed() {
        this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).removeModifier(UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D"));
        return (float) this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (this.isTouchingWater()) {
            this.playSwimSound();
            this.playSecondaryStepSound(state);
        } else {
            BlockPos blockPos = this.getStepSoundPos(pos);
            if (!pos.equals(blockPos)) {
                BlockState blockState = this.getWorld().getBlockState(blockPos);
                if (blockState.isIn(BlockTags.COMBINATION_STEP_SOUND_BLOCKS)) {
                    this.playCombinationStepSounds(blockState, state);
                } else {
                    entityPlayStepSound(blockPos, blockState);
                }
            } else {
                entityPlayStepSound(pos, state);
            }
        }
    }

    private void entityPlayStepSound(BlockPos pos, BlockState state) {
        BlockSoundGroup blockSoundGroup = state.getSoundGroup();

        if (blockSoundGroup == BlockSoundGroup.STONE) {
            if (this.isSprinting()) {
                this.playSound(BetonModSounds.RUN_EVENT, blockSoundGroup.getVolume() * 0.15F, blockSoundGroup.getPitch());
            } else {
                this.playSound(BetonModSounds.WALK_EVENT, blockSoundGroup.getVolume() * 0.15F, blockSoundGroup.getPitch());
            }
        } else {
            this.playSound(blockSoundGroup.getStepSound(), blockSoundGroup.getVolume() * 0.15F, blockSoundGroup.getPitch());
        }
    }

    @Override
    public void setOnGround(boolean onGround, Vec3d movement) {
        if (!this.isOnGround() && onGround) {
            this.playSound(BetonModSounds.LAND_EVENT, 1.0f, 1.0f);
        }

        super.setOnGround(onGround, movement);
    }

    /*@Override
    public void setSprinting(boolean sprinting) {
        if (!BetonModClient.config.betonMovement.getValue()) {
            super.setSprinting(sprinting);
            return;
        }

        // Remove Speed Boost.
        super.setSprinting(false);
        this.setFlag(3, sprinting);
    }*/

    // Flying speed.
    /*protected float getOffGroundSpeed() {
        return this.getControllingPassenger() instanceof PlayerEntity ? this.getMovementSpeed() * 0.1F : 0.02F;
    }*/

    private SoundEvent getFallSound(int distance) {
        return distance > 4 ? this.getFallSounds().big() : this.getFallSounds().small();
    }

    private Vec3d lerpVector(Vec3d a, Vec3d b, float amount) {
        double x = MathHelper.lerp(amount, a.x, b.x);
        double z = MathHelper.lerp(amount, a.z, b.z);
        return new Vec3d(x, a.y + b.y, z);
    }

    private float moveTowards(float current, float target, float maxDelta) {
        if (Math.abs(target - current) <= maxDelta)
        {
            return target;
        }

        return current + Math.signum(target - current) * maxDelta;
    }

    private Vec3d applyClimbingSpeed(Vec3d motion) {
        if (this.isClimbing()) {
            this.onLanding();
            float f = BetonModClient.config.climbingSpeedCap.getValue().floatValue();
            double d = MathHelper.clamp(motion.x, -f, f);
            double e = MathHelper.clamp(motion.z, -f, f);
            double g = Math.max(motion.y, -f);
            if (g < 0.0 && !this.getBlockStateAtPos().isOf(Blocks.SCAFFOLDING) && this.isHoldingOntoLadder() && this instanceof PlayerEntity) {
                g = 0.0;
            }

            motion = new Vec3d(d, g, e);
        }

        return motion;
    }

    private float getFrictionAffectedMovementSpeed(float slipperiness) {
        return this.isOnGround() ? this.getMovementSpeed() * (0.21600002F / (slipperiness * slipperiness * slipperiness)) : this.getOffGroundSpeed();
    }

    private static Vec3d movementInputToVelocity(Vec3d movementInput, float speed, float yaw) {
        double d = movementInput.lengthSquared();
        if (d < 1.0E-7) {
            return Vec3d.ZERO;
        } else {
            Vec3d vec3d = (d > 1.0 ? movementInput.normalize() : movementInput).multiply(speed);
            float f = MathHelper.sin(yaw * 0.017453292F);
            float g = MathHelper.cos(yaw * 0.017453292F);
            return new Vec3d(vec3d.x * (double)g - vec3d.z * (double)f, vec3d.y, vec3d.z * (double)g + vec3d.x * (double)f);
        }
    }
}
