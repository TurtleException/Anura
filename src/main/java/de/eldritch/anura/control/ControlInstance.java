package de.eldritch.anura.control;

import de.eldritch.anura.Instance;
import de.eldritch.anura.InstanceManager;
import org.jetbrains.annotations.NotNull;

/**
 * The controlling instance of Anura. This class should only be initialized once.
 * <p>This {@link Instance} is responsible for moderation and administrating all other instances or rather to provide a
 * gateway to Discord. It can provide statistics, shut down instances and restart them or report issues that interrupt
 * the bot from functioning properly.
 */
public class ControlInstance extends Instance {
    public ControlInstance(@NotNull InstanceManager instanceManager) {
        super(instanceManager);
    }

    @Override
    public @NotNull String getFullName() {
        return "CONTROL";
    }
}
