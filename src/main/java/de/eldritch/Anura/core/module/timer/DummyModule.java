package de.eldritch.Anura.core.module.timer;

import de.eldritch.Anura.core.AnuraInstance;
import de.eldritch.Anura.core.module.AnuraModule;
import de.eldritch.Anura.core.module.AnuraModuleEnableException;
import org.jetbrains.annotations.NotNull;

public class DummyModule extends AnuraModule {
    public DummyModule(@NotNull AnuraInstance instance) {
        super(instance);
    }

    @Override
    public void onEnable() throws AnuraModuleEnableException {

    }

    @Override
    public void onDisable() {

    }
}
