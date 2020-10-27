package com.sizzlebae.projectseptium.spells.sockets;

import com.sizzlebae.projectseptium.spells.components.SpellComponent;
import com.sizzlebae.projectseptium.spells.properties.Property;
import com.sizzlebae.projectseptium.spells.properties.PropertyStrategy;

import java.util.HashMap;
import java.util.Map;

public class SpellSocket {
    public SpellComponent attachedComponent;

    public final HashMap<String, Property<?>> parameters = new HashMap<>();

    public boolean isAttachable(SpellComponent component) {
        // If parameters contains all the requirements of the component, the component is attachable
        for(Map.Entry<String, PropertyStrategy<?>> entry : component.getSocketRequirements().entrySet()) {
            String name = entry.getKey();
            PropertyStrategy<?> parameterStrategy = entry.getValue();

            Property<?> parameter = parameters.get(name);
            if(parameter == null) {
                return false;
            }
            if(!parameter.compare(parameterStrategy)) {
                return false;
            }
        }

        return  true;
    }
}
