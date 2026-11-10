package com.milamber_brass.brass_armory.data;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.inventory.GunContainer;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SpriteSourceProvider;

public class BrassArmorySpriteSourceProvider extends SpriteSourceProvider {
    public BrassArmorySpriteSourceProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper fileHelper) {
        super(output, registries, BrassArmory.MOD_ID, fileHelper);
    }

    @Override
    protected void gather() {
        atlas(SpriteSourceProvider.BLOCKS_ATLAS).addSource(new SingleFile(GunContainer.EMPTY_POWDER_SLOT, Optional.empty()));
        atlas(SpriteSourceProvider.BLOCKS_ATLAS).addSource(new SingleFile(GunContainer.EMPTY_AMMO_SLOT, Optional.empty()));
        atlas(SpriteSourceProvider.BLOCKS_ATLAS).addSource(new SingleFile(BrassArmoryBlockModelBuilder.ROPE_ARROW_TEXTURE, Optional.empty()));
    }
}
