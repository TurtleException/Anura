package de.eldritch.anura.core.module.submission;

import de.eldritch.anura.core.AnuraInstance;
import de.eldritch.anura.core.module.AnuraModule;
import de.eldritch.anura.core.module.AnuraModuleEnableException;
import de.eldritch.anura.core.module.OptionalModule;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.logging.Level;

/**
 * This module is used to submit new memes for the database.
 */
public class SubmissionModule extends AnuraModule implements OptionalModule {
    public SubmissionModule(@NotNull AnuraInstance instance) {
        super(instance);
    }

    @Override
    public void onEnable() throws AnuraModuleEnableException {
        List<Guild> guilds = getInstance().getJDA().getGuilds();
        getLogger().log(Level.INFO, "Registering commands for " + guilds.size() + " guilds...");
        for (Guild guild : guilds) {

        }
    }

    /* ---------- UI ---------- */

    @Override
    public Emoji getEmoji() {
        return Emoji.fromUnicode("U+1F91D");
    }

    /* ------------------------- */
}
