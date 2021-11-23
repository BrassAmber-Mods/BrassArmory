package com.milamberBrass.brass_armory.blocks;

import com.milamberBrass.brass_armory.BrassArmory;
import com.milamberBrass.brass_armory.blocks.custom.RopeBlock;
import com.milamberBrass.brass_armory.items.ModItemGroup;
import com.milamberBrass.brass_armory.items.ModItems;

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
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = BrassArmory.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BrassArmory.MOD_ID);

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
	 * Helper method for registering all Blocks and Items
	 */
	private static <B extends Block> RegistryObject<B> registerBlock(String registryName, B block) {
		// Blocks are registered before Items
		ModItems.registerItem(registryName, new BlockItem(block, new Item.Properties().tab(ModItemGroup.BRASS_ARMORY)));
		return BLOCKS.register(registryName, () -> block);
	}

	/**
	 * Helper method for registering all blocks.
	 */
	public static void register(IEventBus eventBus) {
		BLOCKS.register(eventBus);
	}
}
