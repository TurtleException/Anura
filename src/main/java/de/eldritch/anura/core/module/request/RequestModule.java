package de.eldritch.anura.core.module.request;

import de.eldritch.anura.core.AnuraInstance;
import de.eldritch.anura.core.module.AnuraModule;
import de.eldritch.anura.core.module.OptionalModule;
import net.dv8tion.jda.api.entities.Emoji;
import org.jetbrains.annotations.NotNull;

/**
 * A plain old way to request memes manually.
 */
public class RequestModule extends AnuraModule implements OptionalModule {
    public RequestModule(@NotNull AnuraInstance instance) {
        super(instance);
    }

    /* ---------- UI ---------- */

    @Override
    public Emoji getEmoji() {
        return Emoji.fromUnicode("U+1F438");
    }

    /* ------------------------- */
}
