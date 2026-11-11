package com.milamber_brass.brass_armory.entity.projectile.arrow;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import com.milamber_brass.brass_armory.client.render.SpecialArrowRenderer;
import com.milamber_brass.brass_armory.entity.projectile.abstracts.AbstractSpecialArrowEntity;
import com.milamber_brass.brass_armory.init.BrassArmoryEntityTypes;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import com.milamber_brass.brass_armory.util.ArmoryUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class TorchArrowEntity extends AbstractSpecialArrowEntity implements ItemSupplier {
    protected static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(TorchArrowEntity.class, EntityDataSerializers.ITEM_STACK);

    public TorchArrowEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public TorchArrowEntity(Level level, LivingEntity shooter) {
        super(BrassArmoryEntityTypes.TORCH_ARROW.get(), level, shooter);
        this.setBaseDamage(0.5D);
        if (shooter instanceof Player) this.pickup = Pickup.ALLOWED;
    }

    public TorchArrowEntity(Level level, double x, double y, double z) {
        super(BrassArmoryEntityTypes.TORCH_ARROW.get(), level, x, y, z);
        this.setBaseDamage(0.5D);
        this.pickup = AbstractArrow.Pickup.ALLOWED;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ITEM_STACK, ItemStack.EMPTY);
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);

        Direction direction = blockHitResult.getDirection();
        BlockPos relative = blockHitResult.getBlockPos().relative(direction);
        boolean flag = this.level().getBlockState(relative).canBeReplaced();
        if (direction.equals(Direction.UP) && flag) {
            BlockState torchState = this.getTorch().getBlock().defaultBlockState();
            if (torchState.canSurvive(this.level(), relative)) {
                this.level().setBlock(relative, torchState, 11);
                this.level().gameEvent(this.getOwner(), GameEvent.BLOCK_PLACE, relative);
                this.discard();
            }
        } else if (!direction.equals(Direction.DOWN) && flag) {
            BlockState torchState = this.getTorch().wallBlock.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, direction);
            if (torchState.canSurvive(this.level(), relative)) {
                this.level().setBlock(relative, torchState, 11);
                this.level().gameEvent(this.getOwner(), GameEvent.BLOCK_PLACE, relative);
                this.discard();
            }
        }
    }

    @Override
    protected void spawnArrowParticles(int particleCount) {
        if (this.getTorch().getBlock() instanceof TorchBlock torchBlock) {
            torchBlock.animateTick(torchBlock.defaultBlockState(), this.level(), this.blockPosition(), this.random);
        }

    }

    @Override
    public Identifier getTextureLocation() {
        return SpecialArrowRenderer.TORCH_ARROW_TEXTURE;
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return BrassArmoryItems.DIRT_ARROW.get().getDefaultInstance();
    }

    @Override
    public void addAdditionalSaveData(ValueOutput tag) {
        tag.store("BATorch", ItemStack.CODEC, this.getItemRaw());
        super.addAdditionalSaveData(tag);
    }

    @Override
    public void readAdditionalSaveData(ValueInput tag) {
        this.setItem(tag.read("BATorch", ItemStack.CODEC).orElse(Items.TORCH.getDefaultInstance()));
        super.readAdditionalSaveData(tag);
    }

    public void setTorch(CompoundTag tag) {
        this.setItem(ArmoryUtil.loadStack(tag, "BATorch", Items.TORCH.getDefaultInstance()));
    }

    public void setItem(ItemStack stack) {
        this.entityData.set(DATA_ITEM_STACK, stack.copy());
    }

    protected ItemStack getItemRaw() {
        return this.entityData.get(DATA_ITEM_STACK);
    }

        @Override
    public ItemStack getItem() {
        ItemStack itemstack = this.getItemRaw();
        return itemstack.isEmpty() ? new ItemStack(Items.TORCH) : itemstack;
    }

        protected StandingAndWallBlockItem getTorch() {
        if (this.getItem().getItem() instanceof StandingAndWallBlockItem torch && torch.getBlock() instanceof TorchBlock) return torch;
        else return (StandingAndWallBlockItem)Items.TORCH;
    }
}
