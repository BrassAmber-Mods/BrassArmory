package com.milamber_brass.brass_armory.capabilities;

import com.milamber_brass.brass_armory.BrassArmory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

public interface IQuiverCapability extends INBTSerializable<CompoundTag> {

    ResourceLocation ID = BrassArmory.locate("quiver_capability");

    void setEntity(Player player);

    void tick();

    @NotNull
    ItemStack getAmmoStack();

    void setAmmoStack(ItemStack stack);

    @NotNull
    ItemStack getQuiverStack();

    void setQuiverStack(ItemStack stack);

}
