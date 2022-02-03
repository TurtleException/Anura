package de.eldritch.anura;

import de.eldritch.anura.control.ControlInstance;
import de.eldritch.anura.core.AnuraInstance;
import de.eldritch.anura.core.AnuraInstanceBuilder;
import de.eldritch.anura.util.text.Language;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.logging.Level;

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
                this.enableInstance(value);
            }
        }
    }

    private void enableInstance(@NotNull Language language) {
        try {
            instances.put(language,
                    new AnuraInstanceBuilder(true)
                            .setInstanceManager(this)
                            .setInstanceKey(language)
                            .setToken(null)
                            .build());
        } catch (Exception e) {
            Anura.singleton.getLogger().log(Level.SEVERE, "Encountered an exception while attempting to enable Instance '" + language.code() + "'.", e);
        }
    }
}
