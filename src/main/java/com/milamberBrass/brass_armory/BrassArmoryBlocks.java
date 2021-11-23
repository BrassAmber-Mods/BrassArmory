package com.milamberBrass.brass_armory;

import com.milamberBrass.brass_armory.blocks.RopeBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = BrassArmory.MOD_ID, bus = Bus.MOD)
public class BrassArmoryBlocks {

    // Block Registry
    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, BrassArmory.MOD_ID);

    //------------------------------------MISC----------------------------------------------------------

    public static final RegistryObject<RopeBlock> ROPE = registerBlock("rope", new RopeBlock(AbstractBlock.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.WOOL).noOcclusion().noCollission()));

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent()
    public static void onRegisterBlocks(RegistryEvent.Register<Block> event) {
        BrassArmory.LOGGER.info("Set Block RenderTypes");
        RenderType cutoutRenderType = RenderType.cutout();
        RenderTypeLookup.setRenderLayer(ROPE.get(), cutoutRenderType);
    }

    /**
     * Helper method for inserting blocks into the registry.
     */
    private static <B extends Block> RegistryObject<B> registerBlock(String registryName, B block) {
        // Blocks are registered before Items
        BrassArmoryItems.REGISTRY.register(registryName, () -> new BlockItem(block, new Item.Properties().tab(BrassArmoryItemGroup.BRASS_ARMORY)));
        return REGISTRY.register(registryName, () -> block);
    }

    // Attach event handler for registry.
    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }

}
