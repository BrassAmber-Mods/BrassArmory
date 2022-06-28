package com.milamber_brass.brass_armory.item;

import com.milamber_brass.brass_armory.data.BrassArmoryTags;
import com.milamber_brass.brass_armory.item.abstracts.AbstractGunItem;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class FlintlockItem extends AbstractGunItem {
    public FlintlockItem(Properties builderIn, boolean oneHanded, double baseDamage, float loadSpeed, double recoil) {
        super(builderIn, oneHanded, 1, baseDamage, loadSpeed, 1F, recoil);
    }

    @Override
    protected TagKey<Item> getAmmoType() {
        return BrassArmoryTags.Items.FLINTLOCK_AMMO;
    }
}
