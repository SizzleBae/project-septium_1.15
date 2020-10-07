package com.sizzlebae.projectseptium.tileentity;

import com.sizzlebae.projectseptium.ProjectSeptium;
import com.sizzlebae.projectseptium.blocks.BlockCrystal;
import com.sizzlebae.projectseptium.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.util.function.Supplier;

import static com.sizzlebae.projectseptium.ModUtils.setup;

@Mod.EventBusSubscriber(modid = ProjectSeptium.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(ProjectSeptium.MODID)
public class ModTileEntities {

    public static final TileEntityType<TileEntityAetherContainer> AETHER_CONTAINER = null;

    @SubscribeEvent
    public static void onRegisterTileEntities(final RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().registerAll(
                setupTE(TileEntityAetherContainer::new, "aether_container",
                        ModBlocks.SEPTIUM_CRYSTALS.values().toArray(new BlockCrystal[0]))
        );
    }


    public static TileEntityType setupTE(Supplier<TileEntity> supplier, String name, Block ...validBlocks) {
        return setup(TileEntityType.Builder.create(supplier, validBlocks).build(null), name);
    }

}
