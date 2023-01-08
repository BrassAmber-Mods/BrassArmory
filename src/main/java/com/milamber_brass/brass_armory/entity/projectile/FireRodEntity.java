package com.milamber_brass.brass_armory.entity.projectile;

import com.milamber_brass.brass_armory.util.ArmoryUtil;
import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractThrownWeaponEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.init.BrassArmorySounds;
import com.milamber_brass.brass_armory.item.FireRodItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class FireRodEntity extends AbstractThrownWeaponEntity {
    public FireRodEntity(EntityType<FireRodEntity> entityType, Level level) {
        super(entityType, level);
    }

    public FireRodEntity(Level level, LivingEntity livingEntity, ItemStack spearStack) {
        super(BrassArmoryEntityTypes.FIRE_ROD.get(), livingEntity, level, spearStack);
        this.setRemainingFireTicks(Integer.MAX_VALUE);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide && this.getSharedFlag(0) && (this.inGround || level.getRandom().nextInt(2) == 1)) {
            Vec3 smokeVec = this.position();
            this.level.addParticle(ParticleTypes.SMOKE, smokeVec.x, smokeVec.y, smokeVec.z, 0.0D, 0.0D, 0.0D);
            this.level.addParticle(this.getItem().getItem() instanceof FireRodItem fireRod ? fireRod.flameParticle : ParticleTypes.FLAME, smokeVec.x, smokeVec.y, smokeVec.z, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        ArmoryUtil.blockHitSetOnFire(blockHitResult, this.level, this.getOwner());
    }

    @Override
    public boolean isOnFire() {
        return !this.level.isClientSide && super.isOnFire();
    }

    public boolean hasBeenExtinguished() {
        return !this.getSharedFlag(0);
    }

    @Override
    protected String onHitDamageSource() {
        return "fire_rod";
    }

    @Override
    protected SoundEvent onHitSoundEvent() {
        return BrassArmorySounds.FIRE_ROD_HIT.get();
    }

    @NotNull
    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return BrassArmorySounds.FIRE_ROD_HIT_GROUND.get();
    }

    @Override
    protected Item getDefaultItem() {
        return BrassArmoryItems.FIRE_ROD.get();
    }
}
