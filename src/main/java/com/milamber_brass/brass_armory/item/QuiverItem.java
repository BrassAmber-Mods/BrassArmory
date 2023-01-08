package com.milamber_brass.brass_armory.item;

import com.milamber_brass.brass_armory.init.BrassArmoryCapabilities;
import com.milamber_brass.brass_armory.inventory.QuiverTooltip;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
public class QuiverItem extends Item {
    public static final String TAG_ITEMS = "Items";
    public static final int MAX_WEIGHT = 256;
    private static final int BUNDLE_IN_BUNDLE_WEIGHT = 4;
    private static final int BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);

    public QuiverItem(Item.Properties properties) {
        super(properties);
    }

    public static float getFullnessDisplay(ItemStack stack) {
        return (float)getContentWeight(stack) / (float)MAX_WEIGHT;
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction clickAction, Player player) {
        if (clickAction != ClickAction.SECONDARY) {
            return false;
        } else {
            ItemStack itemstack = slot.getItem();
            if (itemstack.isEmpty()) {
                this.playRemoveOneSound(player);
                removeOne(stack).ifPresent((itemStack) -> add(stack, slot.safeInsert(itemStack)));
            } else if (itemstack.is(ItemTags.ARROWS) && itemstack.getItem().canFitInsideContainerItems()) {
                int i = (MAX_WEIGHT - getContentWeight(stack)) / getWeight(itemstack);
                int j = add(stack, slot.safeTake(itemstack.getCount(), i, player));
                if (j > 0) this.playInsertSound(player);
            }

            return true;
        }
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack stack1, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        if (clickAction == ClickAction.SECONDARY && slot.allowModification(player)) {
            if (stack1.isEmpty()) {
                removeOne(stack).ifPresent((itemStack) -> {
                    this.playRemoveOneSound(player);
                    slotAccess.set(itemStack);
                });
            } else {
                int i = add(stack, stack1);
                if (i > 0) {
                    this.playInsertSound(player);
                    stack1.shrink(i);
                }
            }

            return true;
        } else return false;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        player.getCapability(BrassArmoryCapabilities.QUIVER_CAPABILITY).ifPresent(cap -> cap.getAmmoStack().shrink(1));

        ItemStack itemstack = player.getItemInHand(interactionHand);
        if (dropContents(itemstack, player)) {
            this.playDropContentsSound(player);
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        } else return InteractionResultHolder.fail(itemstack);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getContentWeight(stack) > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.min(1 + 12 * getContentWeight(stack) / MAX_WEIGHT, 13);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return BAR_COLOR;
    }

    public static int add(ItemStack stack, ItemStack stack1) {
        if (!stack1.isEmpty() && stack1.is(ItemTags.ARROWS) && stack1.getItem().canFitInsideContainerItems()) {
            CompoundTag compoundTag = stack.getOrCreateTag();
            if (!compoundTag.contains(TAG_ITEMS)) compoundTag.put(TAG_ITEMS, new ListTag());

            int i = getContentWeight(stack);
            int j = getWeight(stack1);
            int k = Math.min(stack1.getCount(), (MAX_WEIGHT - i) / j);
            if (k == 0) return 0;
            else {
                ListTag listtag = compoundTag.getList(TAG_ITEMS, 10);
                Optional<CompoundTag> optional = getMatchingItem(stack1, listtag);
                if (optional.isPresent()) {
                    CompoundTag compoundTag1 = optional.get();
                    ItemStack itemstack = ItemStack.of(compoundTag1);

                    int maxSize = itemstack.getMaxStackSize();
                    int currentSize = itemstack.getCount();

                    if (maxSize == currentSize) {
                        ItemStack stack2 = stack1.copy();
                        stack2.setCount(k);
                        CompoundTag compoundTag2 = new CompoundTag();
                        stack2.save(compoundTag2);
                        listtag.add(0, compoundTag2);
                    } else if (currentSize + k <= maxSize) {
                        itemstack.grow(k);
                        itemstack.save(compoundTag1);
                        listtag.remove(compoundTag1);
                        listtag.add(0, compoundTag1);
                    } else {
                        itemstack.setCount(maxSize);
                        itemstack.save(compoundTag1);
                        listtag.remove(compoundTag1);
                        listtag.add(0, compoundTag1);

                        ItemStack stack2 = stack1.copy();
                        stack2.setCount(k - (maxSize - currentSize));
                        CompoundTag compoundTag2 = new CompoundTag();
                        stack2.save(compoundTag2);
                        listtag.add(0, compoundTag2);
                    }
                } else {
                    ItemStack stack2 = stack1.copy();
                    stack2.setCount(k);
                    CompoundTag compoundTag1 = new CompoundTag();
                    stack2.save(compoundTag1);
                    listtag.add(0, compoundTag1);
                }

                return k;
            }
        } else return 0;
    }

    private static Optional<CompoundTag> getMatchingItem(ItemStack stack, ListTag listTag) {
        return stack.is(Items.BUNDLE) ? Optional.empty() : listTag.stream().filter(CompoundTag.class::isInstance).map(CompoundTag.class::cast).filter((compoundTag) ->
                ItemStack.isSameItemSameTags(ItemStack.of(compoundTag), stack)).findFirst();
    }

    private static int getWeight(ItemStack stack) {
        if (stack.is(Items.BUNDLE)) return BUNDLE_IN_BUNDLE_WEIGHT + getContentWeight(stack);
        else {
            if ((stack.is(Items.BEEHIVE) || stack.is(Items.BEE_NEST)) && stack.hasTag()) {
                CompoundTag compoundtag = BlockItem.getBlockEntityData(stack);
                if (compoundtag != null && !compoundtag.getList("Bees", 10).isEmpty()) {
                    return 64;
                }
            }

            return 64 / stack.getMaxStackSize();
        }
    }

    private static int getContentWeight(ItemStack stack) {
        return getContents(stack).mapToInt((itemStack) -> getWeight(itemStack) * itemStack.getCount()).sum();
    }

    public static Optional<ItemStack> removeOne(ItemStack stack) {
        CompoundTag compoundTag = stack.getOrCreateTag();
        if (!compoundTag.contains(TAG_ITEMS)) return Optional.empty();
        else {
            ListTag listtag = compoundTag.getList(TAG_ITEMS, 10);
            if (listtag.isEmpty()) return Optional.empty();
            else {
                ItemStack itemstack = ItemStack.of(listtag.getCompound(0));
                listtag.remove(0);
                if (listtag.isEmpty()) stack.removeTagKey(TAG_ITEMS);

                return Optional.of(itemstack);
            }
        }
    }

    private static boolean dropContents(ItemStack stack, Player player) {
        CompoundTag compoundTag = stack.getOrCreateTag();
        if (!compoundTag.contains(TAG_ITEMS)) return false;
        else {
            if (player instanceof ServerPlayer) {
                ListTag listtag = compoundTag.getList(TAG_ITEMS, 10);

                for(int i = 0; i < listtag.size(); ++i) {
                    CompoundTag compoundTag1 = listtag.getCompound(i);
                    ItemStack itemstack = ItemStack.of(compoundTag1);
                    player.drop(itemstack, true);
                }
            }

            stack.removeTagKey(TAG_ITEMS);
            return true;
        }
    }

    public static Stream<ItemStack> getContents(ItemStack stack) {
        CompoundTag compoundtag = stack.getTag();
        if (compoundtag == null) return Stream.empty();
        else {
            ListTag listtag = compoundtag.getList(TAG_ITEMS, 10);
            return listtag.stream().map(CompoundTag.class::cast).map(ItemStack::of);
        }
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        NonNullList<ItemStack> nonNullList = NonNullList.create();
        getContents(stack).forEach(nonNullList::add);
        return Optional.of(new QuiverTooltip(nonNullList, getContentWeight(stack)));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        list.add((new TranslatableComponent("item.minecraft.bundle.fullness", getContentWeight(stack), MAX_WEIGHT)).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity) {
        ItemUtils.onContainerDestroyed(itemEntity, getContents(itemEntity.getItem()));
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.getLevel().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.getLevel().getRandom().nextFloat() * 0.4F);
    }

    private void playDropContentsSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + entity.getLevel().getRandom().nextFloat() * 0.4F);
    }
}