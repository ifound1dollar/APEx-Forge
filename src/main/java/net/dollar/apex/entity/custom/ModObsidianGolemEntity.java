package net.dollar.apex.entity.custom;

import net.dollar.apex.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Custom boss Monster entity, naturally spawning very low in the world.
 */
public class ModObsidianGolemEntity extends Monster implements NeutralMob {
    private int attackAnimationTick;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private int remainingPersistentAngerTime;
    @Nullable
    private UUID persistentAngerTarget;

    private int ticksSinceLastAttack = 0;
    private int teleportDelayTicks = 0;

    public ModObsidianGolemEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }



    /**
     * Register mob goals (AI).
     */
    @Override
    protected void registerGoals() {
        //NOTE: smaller numbers (first argument) imply higher priority

        //speedModifier, followingTargetEvenIfNotSeen
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0d, true));
        //speedModifier
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0d));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    /**
     * Checks whether a spawn attempt is valid, specifically whether it is below a specific y-value.
     * @param entityType EntityType of ObsidianGolemEntity (this)
     * @param accessor Active LevelAccessor
     * @param spawnType Type of mob spawn (NATURAL)
     * @param blockPos Position of spawn attempt being queried
     * @param randomSource RandomSource instance
     * @return Whether the spawn attempt is valid
     */
    public static boolean checkObsidianGolemSpawnRules(EntityType<ModObsidianGolemEntity> entityType, LevelAccessor accessor,
                                                       MobSpawnType spawnType, BlockPos blockPos, RandomSource randomSource) {
        //Only valid spawn very low in the world
        if (blockPos.getY() >= 0) {
            return false;
        }

        return checkMobSpawnRules(entityType, accessor, spawnType, blockPos, randomSource);
    }



    /**
     * Set mob attributes, like MAX_HEALTH, FOLLOW_RANGE, etc.
     * @return Newly created AttributeSupplier
     */
    public static AttributeSupplier setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 120)
                .add(Attributes.ARMOR, 5)
                .add(Attributes.ATTACK_DAMAGE, 15.0)    //Normal, Easy/Hard values are auto-scaled
                .add(Attributes.ATTACK_KNOCKBACK, 1.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
                .add(Attributes.FOLLOW_RANGE, 30)
                .build();
    }

    /**
     * Decreases air supply while the mob is underwater (infinite).
     * @param value Original air supply value
     * @return New air supply value
     */
    @Override
    protected int decreaseAirSupply(int value) {
        return value;
    }

    /**
     * Performs per-tick AI operations. Updates attackAnimationTick and persistentAnger (neutral mob).
     */
    public void aiStep() {
        super.aiStep();
        if (this.attackAnimationTick > 0) {
            --this.attackAnimationTick;
        }

        if (!this.level().isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level(), true);
        }
    }

    public void addAdditionalSaveData(CompoundTag p_28867_) {
        super.addAdditionalSaveData(p_28867_);
        this.addPersistentAngerSaveData(p_28867_);
    }

    public void readAdditionalSaveData(CompoundTag p_28857_) {
        super.readAdditionalSaveData(p_28857_);
        this.readPersistentAngerSaveData(this.level(), p_28857_);
    }


    /**
     * Begins counting persistent anger, called when a LivingEntity attacks this Monster.
     */
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    /**
     * Sets current remaining persistent anger time.
     * @param value New persistent anger time
     */
    public void setRemainingPersistentAngerTime(int value) {
        this.remainingPersistentAngerTime = value;
    }

    /**
     * Gets current remaining persistent anger time.
     * @return Current remaining persistent anger time.
     */
    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    /**
     * Sets current persistent anger target via UUID.
     * @param targetUUID UUID of new persistent anger target
     */
    public void setPersistentAngerTarget(@javax.annotation.Nullable UUID targetUUID) {
        this.persistentAngerTarget = targetUUID;
    }

    /**
     * Gets current persistent target UUID.
     * @return Current persistent target UUID
     */
    @javax.annotation.Nullable
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }



    /**
     * Gets hurt sound produced by this Monster.
     * @param source DamageSource of damage being dealt
     * @return Hurt SoundEvent
     */
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.IRON_GOLEM_HURT;
    }

    /**
     * Gets death sound produced by this Monster.
     * @return Death SoundEvent
     */
    protected SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DEATH;
    }

    /**
     * Plays step sound of this Monster.
     * @param blockPos Position being stepped on
     * @param blockState Blockstate of block at position being stepped on
     */
    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 1.0F, 1.0F);
    }



    /**
     * Gets ATTACK_DAMAGE attribute assigned to this Monster.
     * @return ATTACK_DAMAGE attribute as float
     */
    private float getAttackDamage() {
        return (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    /**
     * Performs attack operations like checking for attack timer, dealing damage to target, and playing sound.
     * @param targetEntity Target Entity
     * @return Whether attack was performed successfully
     */
    @Override
    public boolean doHurtTarget(Entity targetEntity) {
        //Can only attack once every 1.5 seconds, then resets counter.
        if (ticksSinceLastAttack < 30) {
            return false;
        }
        ticksSinceLastAttack = 0;

        //Actual attack here.
        this.attackAnimationTick = 10;  //why?
        this.level().broadcastEntityEvent(this, (byte)4);
        float attackDamage = this.getAttackDamage();
        float $$2 = (int)attackDamage > 0 ?
                attackDamage / 2.0F + (float)this.random.nextInt((int)attackDamage) : attackDamage;
        DamageSource damageSource = this.damageSources().mobAttack(this);
        boolean flag = targetEntity.hurt(damageSource, $$2);

        //If damaging target was successful.
        if (flag) {
            double knockbackResistance;
            if (targetEntity instanceof LivingEntity livingEntity) {
                knockbackResistance = livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
            } else {
                knockbackResistance = 0.0;
            }

            double knockbackResistanceInverted = Math.max(0.0, 1.0 - knockbackResistance);
            targetEntity.setDeltaMovement(targetEntity.getDeltaMovement().add(
                    0.0, 0.4000000059604645 * knockbackResistanceInverted, 0.0));
            Level var11 = this.level();
            if (var11 instanceof ServerLevel serverLevel) {
                EnchantmentHelper.doPostAttackEffects(serverLevel, targetEntity, damageSource);
            }

            //After default post-attack effects, do mob-specific effects.
            if (targetEntity instanceof LivingEntity livingEntity) {
                //CHANCE TO APPLY EFFECT TO TARGET HERE, 50% chance on-hit
                if (this.random.nextInt(100) < 50) {
                    //APPLY ONE OF THESE TWO EFFECTS
                    if (this.random.nextBoolean()) {
                        //apply only level 1 slow, 15%/level (30% was a bit too much)
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 0));
                    } else {
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 0));
                    }
                }

                //ALSO CHANCE TO SET TARGET ON FIRE BASED ON % MISSING HP + 10% (LOOSELY CORRESPONDS TO CRACKINESS)
                if (this.random.nextFloat() > (this.getHealth() / this.getMaxHealth()) - 0.1f) {
                    livingEntity.setRemainingFireTicks(80); //4 seconds
                }
            }
        }

        this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        return flag;
    }

    /**
     * Performs default hurt operations like taking damage, playing hurt sound, etc. Here, reduces
     *  Sharp damage taken and updates visual crackiness.
     * @param source DamageSource of damage being dealt
     * @param value Original amount of damage
     * @return Whether hurt operation was completed successfully
     */
    @Override
    public boolean hurt(DamageSource source, float value) {
        Crackiness.Level irongolem$crackiness = this.getCrackiness();
        boolean flag = super.hurt(source, value);
        if (flag && this.getCrackiness() != irongolem$crackiness) {
            this.playSound(SoundEvents.IRON_GOLEM_DAMAGE, 1.0F, 1.0F);
        }

        return flag;
    }

    /**
     * Gets Crackiness enum value based on percent current Health.
     * @return IronGolem.Crackiness value
     */
    public Crackiness.Level getCrackiness() {
        return Crackiness.GOLEM.byFraction(this.getHealth() / this.getMaxHealth());
    }

    /**
     * Gets current attack animation tick.
     * @return Current attack animation tick
     */
    public int getAttackAnimationTick() {
        return this.attackAnimationTick;
    }



    /**
     * Performs any per-tick operations of this Entity. Here, checks if this Monster has not been
     *  able to attack for at least 3s. If it hasn't, rolls a chance each tick to blind and slow all
     *  nearby LivingEntities then teleport toward its target.
     */
    @Override
    public void tick() {
        super.tick();

        //If there is no target, ensure that ticksSinceLastAttack remains at 0 and return.
        if (this.getTarget() == null) {
            ticksSinceLastAttack = 0;
            return;
        }

        ticksSinceLastAttack++;
        teleportDelayTicks--;
        //if this hasn't attacked in >3 seconds, roll 1% chance per tick to afflict nearby players and teleport to target
        if (ticksSinceLastAttack > 60 && teleportDelayTicks <= 0 && this.random.nextInt(100) < 1) {
            blindAndSlowNearbyLivingEntities();

            //IF this hasn't been able to attack in 12s (240 ticks), teleport directly on top of the
            //  target, ELSE teleport to near the target
            teleportTowardTarget(this.getTarget(), ticksSinceLastAttack > 240);
        }
    }

    /**
     * Applies Blindness and Slowness effects to all nearby LivingEntities, then plays aggressive sound.
     */
    private void blindAndSlowNearbyLivingEntities() {
        //Blind and slow for 3s all players within configurable block radius
        double radius = 24.0;
        double x = this.position().x;
        double y = this.position().y;
        double z = this.position().z;

        List<Entity> entities = this.level().getEntities(this,
                new AABB(x - radius, y - radius, z - radius,
                        x + radius, y + radius, z + radius));

        //Play aggressive sound, then apply effects to all nearby LivingEntities.
        this.playSound(SoundEvents.RAVAGER_ROAR, 1.0F, 1.0F);   //volume, pitch???
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity) {
                //blind and slow ALL nearby LivingEntities, regardless of whether angry at
                livingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1));
            }
        }
    }

    /**
     * Teleports this Monster either toward or directly on top of its target, depending on param value.
     * @param target Target to teleport toward / on top of
     * @param onTop Whether to teleport directly on top
     */
    private void teleportTowardTarget(LivingEntity target, boolean onTop) {
        if (onTop) {
            //teleport directly on top of target, +- 0.5 blocks
            teleportTo((this.random.nextDouble() - 0.5D) + target.getX(),
                    target.getY() + 0.5D,
                    (this.random.nextDouble() - 0.5D) + target.getZ());
        } else {
            //teleport to within 5 blocks of the target
            randomTeleport((this.random.nextDouble() - 0.5D) + target.getX() + (this.random.nextInt(10) - 5),
                    target.getY() + 2.5D,
                    (this.random.nextDouble() - 0.5D) + target.getZ() + (this.random.nextInt(10) - 5),
                    false);

            //if now in attack range, should delay 0.5s before allowing attack
            //IMPORTANT: Must happen only here BECAUSE: when teleporting directly onto target, must attack
            //  much faster to prevent cheesing (ex. knock off of pillar).
            if (this.isWithinMeleeAttackRange(target)) {
                ticksSinceLastAttack = 20;
            }
        }
        teleportDelayTicks = 100;   //minimum of 5s delay between teleports
    }


    /**
     * Determines whether this Monster can be affected by a specific MobEffect.
     * @param effectInstance MobEffectInstance to check validity of
     * @return Whether the MobEffect can be applied to this Monster
     */
    @Override
    public boolean canBeAffected(MobEffectInstance effectInstance) {
        Holder<MobEffect> mobEffect = effectInstance.getEffect();
        return mobEffect != MobEffects.POISON && mobEffect != MobEffects.WITHER && mobEffect != MobEffects.HUNGER;
    }

    /**
     * Drops custom loot from this Monster when slain by a player. Also checks certain conditions to
     *  determine whether this should drop a custom collector item.
     * @param level Active ServerLevel
     * @param source DamageSource of killing blow
     * @param killedByPlayer Whether this was killed by a player
     */
    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean killedByPlayer) {
        if (!killedByPlayer) {
            //Only drop if last attacker was Player.
            return;
        }

        //Below is copied from how a Nether Star drops from WitherBoss.
        ItemEntity itementity = this.spawnAtLocation(ModItems.MOLTEN_CORE.get());
        if (itementity != null) {
            itementity.setExtendedLifetime();
        }

        //if killer player is holding Tungsten-Carbide Mace with Hardness V, drop collector item
        HolderLookup.RegistryLookup<Enchantment> registryLookup = this.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        if (source.getEntity() instanceof Player player) {
            ItemStack heldItem = player.getItemBySlot(EquipmentSlot.MAINHAND);
            if (heldItem.getItem() == ModItems.TUNGSTEN_CARBIDE_BATTLEAXE.get() &&
                    EnchantmentHelper.getItemEnchantmentLevel(registryLookup.getOrThrow(Enchantments.SHARPNESS), heldItem) >= 5)
            {
                //Drop Obsidian Dust trophy item and give it a long despawn delay.
                ItemEntity trophyItem = this.spawnAtLocation(ModItems.TROPHY_OBSIDIAN_DUST.get());
                if (trophyItem != null) {
                    trophyItem.setExtendedLifetime();
                }
            }
        }
    }

    /**
     * Gets experience drop from this Monster on death.
     * @return Amount of experience reward
     */
    @Override
    public int getBaseExperienceReward() {
        //WitherBoss drops 50xp on death
        return 50;
    }

    /**
     * Gets maximum distance that this Monster will voluntarily drop during pathfinding.
     * @return Maximum fall distance
     */
    @Override
    public int getMaxFallDistance() {
        return 10;  //can always fall 10 blocks with no concern
    }

    /**
     * Gets whether this Entity is fire immune (true).
     * @return Whether this Entity is fire immune
     */
    @Override
    public boolean fireImmune() {
        return true;
    }
}