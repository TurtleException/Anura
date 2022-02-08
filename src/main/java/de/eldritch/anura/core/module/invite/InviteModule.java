package de.eldritch.anura.core.module.invite;

import de.eldritch.anura.core.AnuraInstance;
import de.eldritch.anura.core.module.AnuraModule;
import de.eldritch.anura.core.module.AnuraModuleEnableException;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

/**
 * Manages new guild invites and the setup dialogue with the guild owner.
 */
public class InviteModule extends AnuraModule {
    private final HashSet<InviteForm> forms = new HashSet<>();

    public InviteModule(@NotNull AnuraInstance instance) {
        super(instance);
    }

    @Override
    public void onEnable() throws AnuraModuleEnableException {

    }
}
