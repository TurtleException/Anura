package de.eldritch.anura;

import de.eldritch.anura.control.ControlInstance;
import de.eldritch.anura.core.AnuraInstance;
import de.eldritch.anura.util.logging.AnuraLogger;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a bot instance. This may be an {@link AnuraInstance} or a {@link ControlInstance}.
 */
public abstract class Instance extends Thread {
    private final InstanceManager instanceManager;
    private final AnuraLogger logger;

    public Instance(@NotNull InstanceManager instanceManager) {
        this.instanceManager = instanceManager;
        logger = new AnuraLogger(this);
    }

    /**
     * Provides the name of the instance. The formatting may vary per implementation.
     * @return Instance name.
     */
    public abstract @NotNull String getFullName();

    /**
     * Provides the {@link InstanceManager} controlling this instance.
     * @return Controlling InstanceManager.
     */
    public @NotNull InstanceManager getInstanceManager() {
        return instanceManager;
    }

    /**
     * Provides the {@link AnuraLogger} of this instance.
     * @return Instance logger.
     */
    public @NotNull AnuraLogger getLogger() {
        return logger;
    }
}
