package com.sizzlebae.projectseptium;

import com.google.common.base.Preconditions;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;

public class ModUtils {
    /**
     * Performs setup on a registry entry
     *
     * @param name The path of the entry's name. Used to make a name who's domain is our mod's modid
     */
    @Nonnull
    public static <T extends IForgeRegistryEntry<T>> T setup(@Nonnull final T entry, @Nonnull final String name) {
        return setup(entry, new ResourceLocation(ProjectSeptium.MODID, name));
    }

    /**
     * Performs setup on a registry entry
     *
     * @param registryName The full registry name of the entry
     */
    @Nonnull
    public static <T extends IForgeRegistryEntry<T>> T setup(@Nonnull final T entry, @Nonnull final ResourceLocation registryName) {
        entry.setRegistryName(registryName);
        return entry;
    }
}
