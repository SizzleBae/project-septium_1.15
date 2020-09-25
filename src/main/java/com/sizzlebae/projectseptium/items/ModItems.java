package com.sizzlebae.projectseptium.items;

import com.sizzlebae.projectseptium.ProjectSeptium;
import com.sizzlebae.projectseptium.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import static com.sizzlebae.projectseptium.ModUtils.setup;

@Mod.EventBusSubscriber(modid = ProjectSeptium.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(ProjectSeptium.MODID)
public class ModItems {
    public static final ItemAetherProbe AETHER_PROBE = null;
    public static final ItemAetherMap AETHER_MAP = null;

    @SubscribeEvent
    public static void onItemRegistration(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                setup(new ItemAetherProbe(), "aether_probe"),
                setup(new ItemAetherMap(), "aether_map"),

                setupBlockItem(ModBlocks.CRYSTAL_BLOCK, new Item.Properties().group(ItemGroup.MISC))
        );
    }

    public static Item setupBlockItem(Block block, Item.Properties properties) {
        return setup(new BlockItem(block, properties), block.getRegistryName());
    }

}
