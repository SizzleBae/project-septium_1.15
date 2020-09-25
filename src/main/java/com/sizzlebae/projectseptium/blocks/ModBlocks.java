package com.sizzlebae.projectseptium.blocks;

import com.sizzlebae.projectseptium.ProjectSeptium;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import static com.sizzlebae.projectseptium.ModUtils.setup;

@Mod.EventBusSubscriber(modid = ProjectSeptium.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(ProjectSeptium.MODID)
public class ModBlocks {

    public static final BlockCrystal CRYSTAL_BLOCK = null;

    @SubscribeEvent
    public static void onBlocksRegistration(final RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                setup(new BlockCrystal(), "crystal_block")
        );
    }
}
