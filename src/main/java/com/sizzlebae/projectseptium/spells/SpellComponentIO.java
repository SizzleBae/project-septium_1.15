package com.sizzlebae.projectseptium.spells;

import com.sizzlebae.projectseptium.ProjectSeptium;
import com.sizzlebae.projectseptium.spells.components.Origin;
import com.sizzlebae.projectseptium.spells.components.SpawnProjectile;
import com.sizzlebae.projectseptium.spells.components.SpellComponent;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class SpellComponentIO {
    public static final SpellComponentIO INSTANCE = new SpellComponentIO();

    private final HashMap<Class<? extends SpellComponent>, ResourceLocation> componentLocations = new HashMap<>();
    private final HashMap<ResourceLocation, Class<? extends SpellComponent>> componentClasses = new HashMap<>();

    private SpellComponentIO() {
        register(new ResourceLocation(ProjectSeptium.MODID, "origin"), Origin.class);
        register(new ResourceLocation(ProjectSeptium.MODID, "spawn_projectile"), SpawnProjectile.class);
    }

    public CompoundNBT serialize(SpellComponent component) {
        CompoundNBT tag = new CompoundNBT();

        if(!componentLocations.containsKey(component.getClass())) {
            ProjectSeptium.LOGGER.error("SpellComponentIO: Failed to serialize SpellComponent because it is not registered.");
            return tag;
        }

        tag.putString("id", componentLocations.get(component.getClass()).toString());
        tag.put("content", component.memento());

        return tag;
    }

    public SpellComponent deserialize(CompoundNBT tag) {
        String id = tag.getString("id");
        if(id.equals("")) {
            ProjectSeptium.LOGGER.error("SpellComponentIO: Failed to deserialize SpellComponent because tag does not contain an id.");
            return null;
        }

        ResourceLocation location = ResourceLocation.tryCreate(tag.getString("id"));
        if(location == null) {
            ProjectSeptium.LOGGER.error("SpellComponentIO: Failed to deserialize SpellComponent because id is invalid.");
            return null;
        }

        Class<? extends SpellComponent> componentClass = componentClasses.get(location);
        if(componentClass == null) {
            ProjectSeptium.LOGGER.error("SpellComponentIO: Failed to deserialize SpellComponent because it is not registered.");
            return null;
        }

        try {
            SpellComponent component = componentClass.getDeclaredConstructor().newInstance();

            component.restore((CompoundNBT) tag.get("content"));

            return component;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            ProjectSeptium.LOGGER.error("SpellComponentIO: Failed to deserialize SpellComponent, failed to call its default constructor.");
            e.printStackTrace();
            return null;
        }
    }

    private void register(ResourceLocation location, Class<? extends SpellComponent> type) {
        componentLocations.put(type, location);
        componentClasses.put(location, type);
    }


}
