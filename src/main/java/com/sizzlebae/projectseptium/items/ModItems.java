package com.sizzlebae.projectseptium.items;

import com.sizzlebae.projectseptium.ProjectSeptium;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import static com.sizzlebae.projectseptium.ModUtils.*;

@Mod.EventBusSubscriber(modid = ProjectSeptium.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(ProjectSeptium.MODID)
public class ModItems {
    public static final Item ITEM_AETHER_PROBE = null;

    @SubscribeEvent
    public static void onItemRegistration(final RegistryEvent.Register<Item> event) {

        event.getRegistry().registerAll(
                setup(new ItemAetherProbe(), "item_aether_probe")
        );
    }

}
