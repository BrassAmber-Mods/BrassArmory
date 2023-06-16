package com.milamber_brass.brass_armory.mixin;

import com.milamber_brass.brass_armory.capabilities.IPowderCapability;
import com.milamber_brass.brass_armory.init.BrassArmoryCapabilities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Projectile.class)
public abstract class ProjectileMixin extends Entity {
    public ProjectileMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"), remap = true)
    private void tick(CallbackInfo ci) {
        if (!this.level().isClientSide && this.isAlive()) this.getCapability(BrassArmoryCapabilities.POWDER_CAPABILITY).ifPresent(IPowderCapability::tick);
    }
}
