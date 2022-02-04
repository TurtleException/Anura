package de.eldritch.anura.core.module.invite;

import de.eldritch.anura.core.AnuraInstance;
import de.eldritch.anura.core.module.AnuraModule;
import de.eldritch.anura.core.module.AnuraModuleEnableException;
import org.jetbrains.annotations.NotNull;

/**
 * Manages new guild invites and the setup dialogue with the guild owner.
 */
public class InviteModule extends AnuraModule {
    public InviteModule(@NotNull AnuraInstance instance) {
        super(instance);
    }

    @Override
    public void onEnable() throws AnuraModuleEnableException {
        // TODO: Index guilds and restart / continue query
    }
}
