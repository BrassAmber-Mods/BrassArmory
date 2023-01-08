package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.behaviour.GunBehaviours;
import com.milamber_brass.brass_armory.behaviour.ammo.*;
import com.milamber_brass.brass_armory.behaviour.powder.BlazePowderBehaviour;
import com.milamber_brass.brass_armory.behaviour.powder.GunpowderBehaviour;
import com.milamber_brass.brass_armory.entity.projectile.BulletEntity;
import com.milamber_brass.brass_armory.entity.projectile.SpikyBallEntity;
import com.milamber_brass.brass_armory.entity.projectile.bomb.BombEntity;
import com.milamber_brass.brass_armory.entity.projectile.bomb.BouncyBombEntity;
import com.milamber_brass.brass_armory.entity.projectile.bomb.StickyBombEntity;
import com.milamber_brass.brass_armory.entity.projectile.cannon_balls.CannonBallEntity;
import com.milamber_brass.brass_armory.entity.projectile.cannon_balls.CarcassRoundEntity;
import com.milamber_brass.brass_armory.entity.projectile.cannon_balls.SiegeRoundEntity;
import com.milamber_brass.brass_armory.item.SpikyBallItem;
import com.mojang.math.Vector3f;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public class BrassArmoryGunBehaviours {
    public static void init() {
        GunBehaviours.registerAmmoBehavior(BrassArmory.locate("empty"), new EmptyAmmoBehaviour());
        GunBehaviours.registerAmmoBehavior(BrassArmory.locate("musket_ball"), new BulletAmmoBehaviour(itemStack -> itemStack.is(BrassArmoryItems.MUSKET_BALL.get()), BulletEntity::new));
        GunBehaviours.registerAmmoBehavior(BrassArmory.locate("cannon_ball"), new BulletAmmoBehaviour(itemStack -> itemStack.is(BrassArmoryItems.CANNON_BALL.get()), CannonBallEntity::new));
        GunBehaviours.registerAmmoBehavior(BrassArmory.locate("carcass_round"), new BulletAmmoBehaviour(itemStack -> itemStack.is(BrassArmoryItems.CARCASS_ROUND.get()), CarcassRoundEntity::new));
        GunBehaviours.registerAmmoBehavior(BrassArmory.locate("siege_round"), new BulletAmmoBehaviour(itemStack -> itemStack.is(BrassArmoryItems.SIEGE_ROUND.get()), SiegeRoundEntity::new));
        GunBehaviours.registerAmmoBehavior(BrassArmory.locate("bundle_shot"), new BundleShotAmmoBehaviour());
        GunBehaviours.registerAmmoBehavior(BrassArmory.locate("arrow"), new ArrowAmmoBehaviour());
        GunBehaviours.registerAmmoBehavior(BrassArmory.locate("firework"), new FireworkAmmoBehaviour());
        GunBehaviours.registerAmmoBehavior(BrassArmory.locate("ender_pearl"), new ThrownItemAmmoBehaviour(itemStack -> itemStack.is(Items.ENDER_PEARL), ThrownEnderpearl::new));
        GunBehaviours.registerAmmoBehavior(BrassArmory.locate("snow_ball"), new ThrownItemAmmoBehaviour(itemStack -> itemStack.is(Items.SNOWBALL), Snowball::new));
        GunBehaviours.registerAmmoBehavior(BrassArmory.locate("egg"), new ThrownItemAmmoBehaviour(itemStack -> itemStack.is(Items.EGG), ThrownEgg::new));
        GunBehaviours.registerAmmoBehavior(BrassArmory.locate("spiky_ball"), new ThrownItemAmmoBehaviour(itemStack -> itemStack.getItem() instanceof SpikyBallItem, SpikyBallEntity::new));
        GunBehaviours.registerAmmoBehavior(BrassArmory.locate("bomb"), new ThrownItemAmmoBehaviour(itemStack -> itemStack.is(BrassArmoryItems.BOMB.get()), BombEntity::new));
        GunBehaviours.registerAmmoBehavior(BrassArmory.locate("bouncy_bomb"), new ThrownItemAmmoBehaviour(itemStack -> itemStack.is(BrassArmoryItems.BOUNCY_BOMB.get()), BouncyBombEntity::new));
        GunBehaviours.registerAmmoBehavior(BrassArmory.locate("sticky_bomb"), new ThrownItemAmmoBehaviour(itemStack -> itemStack.is(BrassArmoryItems.STICKY_BOMB.get()), StickyBombEntity::new));

        GunBehaviours.registerPowderBehaviour(BrassArmory.locate("gunpowder"), new GunpowderBehaviour(new DustParticleOptions(new Vector3f(new Vec3(0.98D, 0.94D, 0.9D)), 0.8F), itemStack -> itemStack.is(Items.GUNPOWDER)));
        GunBehaviours.registerPowderBehaviour(BrassArmory.locate("blaze_powder"), new BlazePowderBehaviour(ParticleTypes.SMOKE, itemStack -> itemStack.is(Items.BLAZE_POWDER)));
    }
}
