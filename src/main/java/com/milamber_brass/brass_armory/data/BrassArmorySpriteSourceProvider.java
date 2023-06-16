package com.milamber_brass.brass_armory.data;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.inventory.GunContainer;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SpriteSourceProvider;

import java.util.Optional;

public class BrassArmorySpriteSourceProvider extends SpriteSourceProvider {
    public BrassArmorySpriteSourceProvider(PackOutput output, ExistingFileHelper fileHelper) {
        super(output, fileHelper, BrassArmory.MOD_ID);
    }

    @Override
    protected void addSources() {
        atlas(SpriteSourceProvider.BLOCKS_ATLAS).addSource(new SingleFile(GunContainer.EMPTY_POWDER_SLOT, Optional.empty()));
        atlas(SpriteSourceProvider.BLOCKS_ATLAS).addSource(new SingleFile(GunContainer.EMPTY_AMMO_SLOT, Optional.empty()));
        atlas(SpriteSourceProvider.BLOCKS_ATLAS).addSource(new SingleFile(BrassArmoryBlockModelBuilder.ROPE_ARROW_TEXTURE, Optional.empty()));
    }
}
