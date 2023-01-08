package com.milamber_brass.brass_armory.entity.projectile.arrow;

import com.milamber_brass.brass_armory.client.render.SpecialArrowRenderer;
import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractSpecialArrowEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryEffects;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ConfusionArrowEntity extends AbstractSpecialArrowEntity {
    public ConfusionArrowEntity(EntityType<? extends ConfusionArrowEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ConfusionArrowEntity(Level level, LivingEntity shooter) {
        super(BrassArmoryEntityTypes.CONFUSION_ARROW.get(), level, shooter);
        this.setBaseDamage(0.25D);
    }

    public ConfusionArrowEntity(Level level, double x, double y, double z) {
        super(BrassArmoryEntityTypes.CONFUSION_ARROW.get(), level, x, y, z);
        this.setBaseDamage(0.25D);
    }

    @Override
    protected void doPostHurtEffects(LivingEntity living) {
        super.doPostHurtEffects(living);
        if (!this.dealtDamage) {
            living.addEffect(new MobEffectInstance(BrassArmoryEffects.CONFUSION.get(), Mth.clamp(this.tickCount * 2 + 40, 120, 240), 0));
        }
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return BrassArmoryItems.CONFUSION_ARROW.get().getDefaultInstance();
    }

    @Override
    protected void spawnArrowParticles(int particleCount) {

    }

    @Override
    public ResourceLocation getTextureLocation() {
        return SpecialArrowRenderer.CONFUSION_ARROW_TEXTURE;
    }
}