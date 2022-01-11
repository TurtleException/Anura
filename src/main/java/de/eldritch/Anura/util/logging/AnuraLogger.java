package de.eldritch.Anura.util.logging;

import de.eldritch.Anura.Anura;
import de.eldritch.Anura.core.AnuraInstance;
import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link NestedLogger} extension for {@link AnuraInstance AnuraInstances} to help manage different instances.
 */
public class AnuraLogger extends NestedLogger {
    private final AnuraInstance instance;

    /**
     * Constructs a Logger named <code>ANURA#0</code> (0 being an example of the {@link AnuraInstance} ID).
     * @param instance {@link AnuraInstance} that will be associated with the logger.
     */
    public AnuraLogger(@NotNull AnuraInstance instance) {
        super("ANURA#" + instance.getId(), Anura.singleton.getLogger());
        this.instance = instance;
    }

    /**
     * Provides the {@link AnuraInstance} associated with this logger.
     * @return Instance associated with this logger.
     */
    public @NotNull AnuraInstance getInstance() {
        return instance;
    }
}
