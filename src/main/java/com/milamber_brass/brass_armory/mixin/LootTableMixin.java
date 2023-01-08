package com.milamber_brass.brass_armory.mixin;

import com.milamber_brass.brass_armory.data.BrassArmoryTags;
import com.milamber_brass.brass_armory.init.BrassArmoryItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;

@Mixin(LootTable.class)
@ParametersAreNonnullByDefault
public abstract class LootTableMixin {
    @Inject(method = "getRandomItems(Lnet/minecraft/world/level/storage/loot/LootContext;)Ljava/util/List;", at = @At(value = "RETURN"), cancellable = true, remap = true)
    private void getRandomModifiedItems(LootContext context, CallbackInfoReturnable<List<ItemStack>> cir) {
        List<ItemStack> loot = cir.getReturnValue();
        for (int i = 0; i < loot.size(); i++) {
            if (loot.get(i).is(BrassArmoryTags.Items.WARP_CRYSTAL_GENERATORS)) {
                int roll = context.getRandom().nextInt(100);
                if (roll < 2) loot.add(BrassArmoryItems.GRAND_WARP_CRYSTAL.get().getDefaultInstance());
                else if (roll < 6) loot.add(BrassArmoryItems.GREATER_WARP_CRYSTAL.get().getDefaultInstance());
                else if (roll < 14) loot.add(BrassArmoryItems.COMMON_WARP_CRYSTAL.get().getDefaultInstance());
                else if (roll < 30) loot.add(BrassArmoryItems.LESSER_WARP_CRYSTAL.get().getDefaultInstance());
                else if (roll < 62) loot.add(BrassArmoryItems.PETTY_WARP_CRYSTAL.get().getDefaultInstance());
            }
        }
        Collections.shuffle(loot);
        cir.setReturnValue(loot);
    }
}
