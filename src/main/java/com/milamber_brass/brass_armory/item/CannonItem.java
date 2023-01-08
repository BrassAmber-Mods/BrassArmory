package com.milamber_brass.brass_armory.item;

import com.milamber_brass.brass_armory.entity.CannonEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CannonItem extends Item {
    public CannonItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        BlockPos blockpos = useOnContext.getClickedPos();
        Direction face = useOnContext.getClickedFace();

        if (level.getBlockState(blockpos).getMaterial().isReplaceable()) {
            face = Direction.UP;
            blockpos = blockpos.relative(face.getOpposite());
        }

        if (face != Direction.UP || !CannonEntity.canSurvive(level, blockpos)) {
            return InteractionResult.FAIL;
        } else {
            BlockPos relative = blockpos.relative(face);
            if (level instanceof ServerLevel) {
                CannonEntity cannon = new CannonEntity(level, relative.getX() + 0.5D, relative.getY(), relative.getZ() + 0.5D);

                float yRot = (float) Mth.floor((Mth.wrapDegrees(useOnContext.getRotation()) + 22.5F) / 90.0F) * 90.0F;
                cannon.moveTo(cannon.getX(), cannon.getY(), cannon.getZ(), yRot, 0.0F);

                level.addFreshEntity(cannon);
                level.gameEvent(useOnContext.getPlayer(), GameEvent.ENTITY_PLACE, relative);
                level.removeBlock(relative, false);

            } else {
                SoundType soundtype = level.getBlockState(relative).getSoundType(level, blockpos, useOnContext.getPlayer());
                level.playSound(useOnContext.getPlayer(), blockpos, this.getPlaceSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
            }

            useOnContext.getItemInHand().shrink(1);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    protected SoundEvent getPlaceSound() {
        return SoundEvents.ANVIL_PLACE;
    }
}
