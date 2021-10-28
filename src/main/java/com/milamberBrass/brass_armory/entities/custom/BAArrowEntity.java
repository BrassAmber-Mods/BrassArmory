package com.milamberBrass.brass_armory.entities.custom;

import com.milamberBrass.brass_armory.items.ModItems;
import com.milamberBrass.brass_armory.util.ArrowType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BAArrowEntity extends ArrowEntity {
    private static final DataParameter<? super Integer> COLOR = EntityDataManager.createKey(ArrowEntity.class, DataSerializers.VARINT);;
    public ArrowType arrowType;
    private BlockState inBlockState;

    public BAArrowEntity(World worldIn, LivingEntity shooter, ArrowType typeIn) {
        super(worldIn, shooter);
        this.arrowType = typeIn;
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(arrowType.getModItemFor(arrowType));
    }

    @Override
    protected void arrowHit(LivingEntity living) {
        super.arrowHit(living);
        if (this.arrowType == ArrowType.FROST) {
            living.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 2, 3));
        }

    }

    protected void func_230299_a_(BlockRayTraceResult result) {
        super.func_230299_a_(result);
        this.inBlockState = this.world.getBlockState(result.getPos());
        if (this.arrowType == ArrowType.DIRT) {
            this.world.setBlockState(new BlockPos(this.getPosX(), this.getPosY(), this.getPosZ()), Blocks.DIRT.getDefaultState());
        }
    }


    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        super.tick();
        if (this.world.isRemote) {
            if (this.inGround) {
                if (this.timeInGround % 5 == 0) {
                    this.spawnArrowParticles(1);
                }
            } else {
                this.spawnArrowParticles(2);
            }
        } else if (this.inGround && this.timeInGround != 0 && this.timeInGround >= 600) {
            this.world.setEntityState(this, (byte)0);
            this.dataManager.set(COLOR, -1);
        }

    }

    private void spawnArrowParticles(int particleCount) {
        if(particleCount > 0) {
            double d0 =  100.0;
            double d1 =  200.0;
            double d2 =  100.0;

            for(int j = 0; j < particleCount; ++j) {
                this.world.addParticle(ParticleTypes.FLAME, this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), d0, d1, d2);
            }

        }
    }

}
