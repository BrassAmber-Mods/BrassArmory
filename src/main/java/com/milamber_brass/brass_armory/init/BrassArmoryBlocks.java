package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.block.RopeBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BrassArmoryBlocks {

    // Block Registry
    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(BuiltInRegistries.BLOCK, BrassArmory.MOD_ID);

    //------------------------------------MISC----------------------------------------------------------

    public static final DeferredHolder<Block, RopeBlock> EXPLORERS_ROPE_BLOCK = REGISTRY.register("explorers_rope", () -> new RopeBlock(Block.Properties.of().strength(0.4F).sound(SoundType.WOOL).noOcclusion().noCollission()));

    // Attach event handler for registry.
    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }

}
