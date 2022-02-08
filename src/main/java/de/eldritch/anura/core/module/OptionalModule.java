package de.eldritch.anura.core.module;

import de.eldritch.anura.core.module.invite.query.ModuleQuery;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;

/**
 * Represents an {@link AnuraModule} that can be enabled or disabled per {@link Guild} on setup.
 */
public interface OptionalModule {
    /**
     * Provides the {@link Emoji} that should be displayed for this module in the {@link SelectMenu}.
     * @return Module Emoji.
     * @see ModuleQuery
     */
    Emoji getEmoji();
}
