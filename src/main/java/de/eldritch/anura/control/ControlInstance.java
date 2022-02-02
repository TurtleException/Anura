package de.eldritch.anura.control;

import de.eldritch.anura.Instance;
import de.eldritch.anura.InstanceManager;
import org.jetbrains.annotations.NotNull;

public class ControlInstance extends Instance {
    public ControlInstance(@NotNull InstanceManager instanceManager) {
        super(instanceManager);
    }

    @Override
    public @NotNull String getFullName() {
        return "CONTROL";
    }
}
