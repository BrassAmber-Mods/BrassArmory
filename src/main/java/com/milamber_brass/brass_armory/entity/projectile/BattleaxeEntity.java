package com.milamber_brass.brass_armory.entity.projectile;

import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractThrownWeaponEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.TierSortingRegistry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class BattleaxeEntity extends AbstractThrownWeaponEntity {
    public BattleaxeEntity(EntityType<BattleaxeEntity> entityType, Level level) {
        super(entityType, level);
    }

    public BattleaxeEntity(LivingEntity livingEntity, Level level, ItemStack battleaxeStack) {
        super(BrassArmoryEntityTypes.BATTLEAXE.get(), livingEntity, level, battleaxeStack);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void onHitEntity(EntityHitResult entityHitResult) {
        if (entityHitResult.getEntity() instanceof Player player) {
            player.disableShield(true);
        }
        super.onHitEntity(entityHitResult);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void onHitBlock(BlockHitResult result) {
        ItemStack battleaxeStack = this.getItem();
        if (battleaxeStack.getItem() instanceof TieredItem tieredItem) {
            BlockPos pos = result.getBlockPos();//TODO, better sound
            BlockState hitBlockState = this.level.getBlockState(pos);
            if (hitBlockState.getBlock() instanceof DoorBlock || hitBlockState.getBlock() instanceof TrapDoorBlock) {
                boolean flag = !TierSortingRegistry.getTiersLowerThan(Tiers.DIAMOND).contains(tieredItem.getTier());
                if (hitBlockState.getMaterial() == Material.WOOD || hitBlockState.getMaterial() == Material.NETHER_WOOD || flag) {
                    Vec3 deltaMovement = this.onHitDeltaMovement();
                    this.level.destroyBlock(pos, true, this.getOwner());
                    this.setDeltaMovement(deltaMovement);
                    return;
                }
            }
        }
        super.onHitBlock(result);
    }

    @Override
    protected String onHitDamageSource() {
        return "BABattleaxe";
    }

    @Override
    protected SoundEvent onHitSoundEvent() {
        return SoundEvents.TRIDENT_HIT;//TODO:SOUNDS
    }

    @NotNull
    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    @Override
    protected Item getDefaultItem() {
        return BrassArmoryItems.WOODEN_DAGGER.get();
    }
}