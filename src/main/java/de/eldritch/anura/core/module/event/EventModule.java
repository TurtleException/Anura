package de.eldritch.anura.core.module.event;

import de.eldritch.anura.core.AnuraInstance;
import de.eldritch.anura.core.module.AnuraModule;
import org.jetbrains.annotations.NotNull;

/**
 * Allows the user to schedule events - This will allow the bot to post memes on certain times or with a delay.
 */
public class EventModule extends AnuraModule {
    public EventModule(@NotNull AnuraInstance instance) {
        super(instance);
    }
}
