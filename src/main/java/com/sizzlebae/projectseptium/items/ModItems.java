package com.sizzlebae.projectseptium.items;

import com.sizzlebae.projectseptium.ProjectSeptium;
import com.sizzlebae.projectseptium.blocks.BlockCrystal;
import com.sizzlebae.projectseptium.blocks.ModBlocks;
import com.sizzlebae.projectseptium.capabilities.AetherType;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Objects;

import static com.sizzlebae.projectseptium.ModUtils.setup;

@Mod.EventBusSubscriber(modid = ProjectSeptium.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {
    public static ItemAetherProbe AETHER_PROBE = null;
    public static ItemAetherMap AETHER_MAP = null;

    public static final HashMap<AetherType, ItemSepith> SEPITH_ITEMS = new HashMap<>();

    @SubscribeEvent
    public static void onItemRegistration(final RegistryEvent.Register<Item> event) {
        // Register unique items
        event.getRegistry().registerAll(
                AETHER_PROBE = setup(new ItemAetherProbe(), "aether_probe"),
                AETHER_MAP = setup(new ItemAetherMap(), "aether_map")
        );

        // Register block items
        event.getRegistry().registerAll(
//                setupBlockItem(ModBlocks.SEPTIUM_CRYSTAL, new Item.Properties().group(ItemGroup.MISC))
        );
        // Register all block crystal variants
        for(BlockCrystal blockCrystal : ModBlocks.SEPTIUM_CRYSTALS.values()) {
            event.getRegistry().register(setupBlockItem(blockCrystal, new Item.Properties().group(ItemGroup.MISC)));
        }

        // Register all sepith variants
        for(AetherType type : AetherType.values()) {
            ItemSepith sepithItem = setup(new ItemSepith(type), "sepith_" + type.name().toLowerCase());
            SEPITH_ITEMS.put(type, sepithItem);
            event.getRegistry().register(sepithItem);
        }

    }

    public static BlockItem setupBlockItem(Block block, Item.Properties properties) {
        return setup(new BlockItem(block, properties), Objects.requireNonNull(block.getRegistryName()));
    }

}
