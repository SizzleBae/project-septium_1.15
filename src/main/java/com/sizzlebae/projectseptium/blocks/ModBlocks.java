package com.sizzlebae.projectseptium.blocks;

import com.sizzlebae.projectseptium.ProjectSeptium;
import com.sizzlebae.projectseptium.capabilities.AetherType;
import com.sizzlebae.projectseptium.items.ItemSepith;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.util.HashMap;

import static com.sizzlebae.projectseptium.ModUtils.setup;

@Mod.EventBusSubscriber(modid = ProjectSeptium.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {

//    public static BlockCrystal SEPTIUM_CRYSTAL = null;

    public static final HashMap<AetherType, BlockCrystal> SEPTIUM_CRYSTALS = new HashMap<>();

    @SubscribeEvent
    public static void onBlocksRegistration(final RegistryEvent.Register<Block> event) {
//        event.getRegistry().registerAll(
//                SEPTIUM_CRYSTAL = setup(new BlockCrystal(AetherType.FIRE), "septium_crystal")
//        );

        // Register all sepith variants
        for(AetherType type : AetherType.values()) {
            BlockCrystal crystalBlock = setup(new BlockCrystal(type), "septium_crystal_" + type.name().toLowerCase());
            SEPTIUM_CRYSTALS.put(type, crystalBlock);
            event.getRegistry().register(crystalBlock);
        }
    }
}
