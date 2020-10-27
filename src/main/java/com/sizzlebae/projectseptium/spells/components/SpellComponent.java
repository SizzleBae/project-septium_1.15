package com.sizzlebae.projectseptium.spells.components;

import com.sizzlebae.projectseptium.spells.SpellComponentIO;
import com.sizzlebae.projectseptium.spells.properties.Property;
import com.sizzlebae.projectseptium.spells.properties.PropertyStrategy;
import com.sizzlebae.projectseptium.spells.sockets.SpellSocket;
import net.minecraft.nbt.CompoundNBT;

import java.util.HashMap;

public abstract class SpellComponent {
    public SpellSocket parentSocket;

    public final HashMap<String, SpellSocket> childSockets = new HashMap<>();
    public final HashMap<String, Property<?>> properties = new HashMap<>();

    protected <T extends Property<?>> T addProperty(String name, T property) {
        properties.put(name, property);
        return property;
    }

    protected <T extends SpellSocket> T addChildSocket(String name, T childSocket) {
        childSockets.put(name, childSocket);
        return childSocket;
    }

    public CompoundNBT memento() {
        CompoundNBT memento = new CompoundNBT();

        // Serialize this components properties
        properties.forEach((name, property)->memento.put(name, property.serialize()));

        // Serialize child sockets recursively
        childSockets.forEach((name, socket)->{
            if(socket.attachedComponent != null) {
                memento.put(name, SpellComponentIO.INSTANCE.serialize(socket.attachedComponent));
            }
        });

        return memento;
    }

    public void restore(CompoundNBT memento) {
        // Deserialize this components properties
        properties.forEach((name, property)->property.deserialize(memento.get(name)));

        // Deserialize child sockets recursively
        childSockets.forEach((name, socket)->{
            CompoundNBT childContent = (CompoundNBT) memento.get(name);
            if(childContent != null) {
                socket.attachedComponent = SpellComponentIO.INSTANCE.deserialize(childContent);
            }
        });

    }

    public abstract HashMap<String, PropertyStrategy<?>> getSocketRequirements();
}
