package com.sizzlebae.projectseptium;

import com.sizzlebae.projectseptium.blocks.AetherBlockColor;
import com.sizzlebae.projectseptium.blocks.BlockCrystal;
import com.sizzlebae.projectseptium.blocks.ModBlocks;
import com.sizzlebae.projectseptium.items.AetherBlockItemColor;
import com.sizzlebae.projectseptium.items.AetherItemColor;
import com.sizzlebae.projectseptium.items.ItemSepith;
import com.sizzlebae.projectseptium.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ProjectSeptium.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModStartupClientOnly {

    @SubscribeEvent
    public static void onClientStartup(FMLClientSetupEvent event) {
        
    }

    @SubscribeEvent
    public static void onItemColorHandlerEvent(ColorHandlerEvent.Item event) {
        event.getItemColors().register(new AetherItemColor(), ModItems.SEPITH_ITEMS.values().toArray(new ItemSepith[0]));

        event.getItemColors().register(new AetherBlockItemColor(), ModBlocks.SEPTIUM_CRYSTALS.values().stream()
                .map(Block::asItem).toArray(Item[]::new));
    }

    @SubscribeEvent
    public static void onBlockColorHandlerEvent(ColorHandlerEvent.Block event) {
        event.getBlockColors().register(new AetherBlockColor(), ModBlocks.SEPTIUM_CRYSTALS.values().toArray(new BlockCrystal[0]));
    }
}
