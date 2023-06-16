package com.milamber_brass.brass_armory.mixin;

import com.milamber_brass.brass_armory.item.KatanaItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow public abstract ItemStack getMainHandItem();

    @Shadow public abstract ItemStack getOffhandItem();;

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"), remap = true)
    private void tick(CallbackInfo ci) {
        if (this.level().isClientSide) {
            ItemStack stack = ItemStack.EMPTY;
            if (this.getMainHandItem().getItem() instanceof KatanaItem katanaItem && katanaItem.canWither()) stack = this.getMainHandItem();
            if (this.getOffhandItem().getItem() instanceof KatanaItem katanaItem && katanaItem.canWither()) stack = this.getOffhandItem();

            if (!stack.isEmpty()) {
                int wither = KatanaItem.getWither(stack);
                for (int i = 0; i < wither / 10; i++) {
                    if (this.level().random.nextInt(10) == 7)
                        this.level().addParticle(ParticleTypes.SMOKE, this.getRandomX(0.5D), this.getRandomY() - 0.25D, this.getRandomZ(0.5D), 0D, 0D, 0D);
                }
            }
        }
    }
}
