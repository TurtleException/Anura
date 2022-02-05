package de.eldritch.anura;

import de.eldritch.anura.control.ControlInstance;
import de.eldritch.anura.core.AnuraInstance;
import de.eldritch.anura.core.AnuraInstanceBuilder;
import de.eldritch.anura.util.text.Language;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.logging.Level;

/**
 * Responsible for controlling all {@link Instance} objects (mainly {@link AnuraInstance}). This manager is part of the
 * main thread.
 * @see Instance
 * @see AnuraInstance
 */
public class InstanceManager {
    private final HashMap<InstanceKey, Instance> instances = new HashMap<>();

    private enum SpecialKey implements InstanceKey { CONTROL }

    /**
     * Package-private constructor - Only called by the {@link Anura} constructor.
     */
    InstanceManager() {
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

    /**
     * Enabled a single {@link AnuraInstance} based on a provided {@link Language}. If the instance fails to initialize
     * the exception is logged as a <code>SEVERE</code> record on the root logger.
     * @param language Language as key.
     */
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
