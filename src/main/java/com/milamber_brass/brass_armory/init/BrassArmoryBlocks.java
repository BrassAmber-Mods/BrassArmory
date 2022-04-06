package com.milamber_brass.brass_armory.init;

import com.milamber_brass.brass_armory.BrassArmory;
import com.milamber_brass.brass_armory.block.RopeBlock;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


@Mod.EventBusSubscriber(modid = BrassArmory.MOD_ID, bus = Bus.MOD)
public class BrassArmoryBlocks {

    // Block Registry
    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, BrassArmory.MOD_ID);

    //------------------------------------MISC----------------------------------------------------------

    public static final RegistryObject<RopeBlock> ROPE = REGISTRY.register("rope", () -> new RopeBlock(Block.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.WOOL).noOcclusion().noCollission()));

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent()
    public static void onRegisterBlocks(RegistryEvent.Register<Block> event) {
        BrassArmory.LOGGER.info("Set Block RenderTypes");
        RenderType cutoutRenderType = RenderType.cutout();
        ItemBlockRenderTypes.setRenderLayer(ROPE.get(), cutoutRenderType);
    }

    // Attach event handler for registry.
    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }

}
