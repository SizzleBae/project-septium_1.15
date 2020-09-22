package com.sizzlebae.projectseptium.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class ModCapabilities {

    @CapabilityInject(Aether.class)
    public static Capability<Aether> CAPABILITY_AETHER = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(
            Aether.class,
            new Aether.Storage(),
            Aether::defaultInstance
        );
    }
}
