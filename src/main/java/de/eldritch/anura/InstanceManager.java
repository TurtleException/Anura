package de.eldritch.anura;

import de.eldritch.anura.control.ControlInstance;
import de.eldritch.anura.core.AnuraInstance;
import de.eldritch.anura.util.text.Language;

import java.util.HashMap;

public class InstanceManager {
    private final HashMap<InstanceKey, Instance> instances = new HashMap<>();

    private enum SpecialKey implements InstanceKey { CONTROL }

    public InstanceManager() {
        instances.put(SpecialKey.CONTROL, new ControlInstance(this));
    }

    /**
     * Determines each {@link AnuraInstance} that should be created and puts them into the instances map.
     */
    void init() {
        // implement an instance for each language
        for (Language value : Language.values()) {
            // only implement instance if the language is flagged for implementation
            if (value.shouldImplement()) {
                instances.put(value, new AnuraInstance(this, value));
            }
        }
    }
}
