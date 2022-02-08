package de.eldritch.anura.core.module.event;

import de.eldritch.anura.core.AnuraInstance;
import de.eldritch.anura.core.module.AnuraModule;
import de.eldritch.anura.core.module.OptionalModule;
import net.dv8tion.jda.api.entities.Emoji;
import org.jetbrains.annotations.NotNull;

/**
 * Allows the user to schedule events - This will allow the bot to post memes on certain times or with a delay.
 */
public class EventModule extends AnuraModule implements OptionalModule {
    public EventModule(@NotNull AnuraInstance instance) {
        super(instance);
    }

    /* ---------- UI ---------- */

    @Override
    public Emoji getEmoji() {
        return Emoji.fromUnicode("U+23F0");
    }

    /* ------------------------- */
}
