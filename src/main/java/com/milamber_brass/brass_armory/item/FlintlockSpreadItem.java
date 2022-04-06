package com.milamber_brass.brass_armory.item;

import com.milamber_brass.brass_armory.init.BrassArmoryTags;
import com.milamber_brass.brass_armory.item.abstracts.AbstractGunItem;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

public class FlintlockSpreadItem extends AbstractGunItem {
    public FlintlockSpreadItem(Properties properties, boolean oneHanded, double baseDamage, float loadSpeed, double recoil) {
        super(properties, oneHanded, 1, baseDamage, loadSpeed, 8F, recoil);
    }

    @Nullable
    @Override
    protected TagKey<Item> getAmmoType() {
        return BrassArmoryTags.Items.BLUNDERBUSS_AMMO;
    }
}
