package com.sizzlebae.projectseptium;

import com.sizzlebae.projectseptium.items.AetherColor;
import com.sizzlebae.projectseptium.items.ItemSepith;
import com.sizzlebae.projectseptium.items.ModItems;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ProjectSeptium.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModStartupClientOnly {

    @SubscribeEvent
    public static void onColorHandlerEvent(ColorHandlerEvent.Item event)
    {
        // the LiquidColour lambda function is used to change the rendering colour of the liquid in the bottle
        // i.e.: when vanilla wants to know what colour to render our itemVariants instance, it calls the LiquidColour lambda function
        event.getItemColors().register(new AetherColor(), ModItems.SEPITH_ITEMS.values().toArray(new ItemSepith[0]));
    }

}
