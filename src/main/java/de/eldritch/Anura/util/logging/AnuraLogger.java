package de.eldritch.Anura.util.logging;

import de.eldritch.Anura.Anura;
import de.eldritch.Anura.core.AnuraInstance;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

/**
 * A simple {@link Logger} extension for {@link AnuraInstance AnuraInstances} to help manage different instances.
 */
public class AnuraLogger extends Logger {
    private final AnuraInstance instance;

    /**
     * Constructs a Logger named <code>ANURA#0</code> (0 being an example of the {@link AnuraInstance} ID).
     * @param instance {@link AnuraInstance} that will be associated with the logger.
     */
    public AnuraLogger(@NotNull AnuraInstance instance) {
        super("ANURA#" + instance.getId(), null);

        this.instance = instance;

        this.setParent(Anura.singleton.getLogger());
        this.setUseParentHandlers(true);
    }

    /**
     * Provides the {@link AnuraInstance} associated with this logger.
     * @return Instance associated with this logger.
     */
    public @NotNull AnuraInstance getInstance() {
        return instance;
    }
}
