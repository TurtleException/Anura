package de.eldritch.anura.core.module.invite;

import de.eldritch.anura.core.AnuraInstance;
import de.eldritch.anura.core.module.AnuraModule;
import de.eldritch.anura.core.module.AnuraModuleEnableException;
import de.eldritch.anura.core.module.ShadowModule;
import net.dv8tion.jda.api.entities.Emoji;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

/**
 * Manages new guild invites and the setup dialogue with the guild owner.
 */
public class InviteModule extends AnuraModule implements ShadowModule {
    private final HashSet<InviteForm> forms = new HashSet<>();

    public InviteModule(@NotNull AnuraInstance instance) {
        super(instance);
    }

    @Override
    public void onEnable() throws AnuraModuleEnableException {

    }

    /* ---------- UI ---------- */

    @Override
    public Emoji getEmoji() {
        return Emoji.fromUnicode("");
    }

    /* ------------------------- */
}
