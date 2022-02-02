package de.eldritch.anura.core;

import de.eldritch.anura.Instance;
import de.eldritch.anura.InstanceKey;
import de.eldritch.anura.InstanceManager;
import de.eldritch.anura.util.text.Language;
import org.jetbrains.annotations.NotNull;

public class AnuraInstance extends Instance {
    private final InstanceKey instanceKey;

    public AnuraInstance(@NotNull InstanceManager instanceManager, @NotNull InstanceKey instanceKey) {
        super(instanceManager);
        this.instanceKey = instanceKey;
    }

    @Override
    public @NotNull String getFullName() {
        if (instanceKey instanceof Language language)
            return "ANURA - " + language.code();
        else
            return instanceKey.toString();
    }
}
