package de.eldritch.anura.util.logging;
import de.eldritch.anura.Anura;
import de.eldritch.anura.Instance;
import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link NestedLogger} extension to help manage different {@link Instance Instances}.
 */
public class AnuraLogger extends NestedLogger {
    private final Instance instance;

    public AnuraLogger(@NotNull Instance instance) {
        super(instance.getFullName(), Anura.singleton.getLogger());
        this.instance = instance;
    }

    /**
     * Provides the {@link Instance} associated with this logger.
     * @return Instance associated with this logger.
     */
    public @NotNull Instance getInstance() {
        return instance;
    }
}