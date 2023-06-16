package com.milamber_brass.brass_armory.behaviour.ammo;

import com.milamber_brass.brass_armory.behaviour.iGun;
import com.milamber_brass.brass_armory.behaviour.powder.AbstractPowderBehaviour;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public class FireworkAmmoBehaviour extends AbstractAmmoBehaviour {
    public FireworkAmmoBehaviour() {
        super();
    }

    @Override
    public boolean onShoot(Level level, LivingEntity owner, Entity shooter, ItemStack ammoStack, @Nullable AbstractPowderBehaviour powderBehaviour, iGun gun) {
        super.onShoot(level, owner, shooter, ammoStack, powderBehaviour, gun);
        FireworkRocketEntity rocket = new FireworkRocketEntity(level, ammoStack, owner, shooter.getX(), shooter.getEyeY(), shooter.getZ(), true);
        if (powderBehaviour != null) powderBehaviour.applyEffect(rocket);
        if (!level.isClientSide) {
            float speed = gun.speed();
            Vec3 vec31 = shooter.getUpVector(1.0F);
            Quaternionf quaternion = (new Quaternionf()).setAngleAxis(0.0D, vec31.x, vec31.y, vec31.z);
            Vec3 vec3 = shooter.getViewVector(1.0F);
            Vector3f vector3f = vec3.toVector3f().rotate(quaternion);
            rocket.shoot(vector3f.x(), vector3f.y(), vector3f.z(), speed * 0.25F, gun.accuracy());

            gun.onShoot(level, owner, shooter, ammoStack, rocket);
            return level.addFreshEntity(rocket);
        }
        return true;
    }

    @NotNull
    @Override
    public Predicate<ItemStack> getAmmoItems() {
        return itemStack -> itemStack.getItem() instanceof FireworkRocketItem;
    }
}
