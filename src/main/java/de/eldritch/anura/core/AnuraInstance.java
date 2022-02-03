package de.eldritch.anura.core;

import de.eldritch.anura.Instance;
import de.eldritch.anura.InstanceKey;
import de.eldritch.anura.InstanceManager;
import de.eldritch.anura.util.text.Language;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;

public class AnuraInstance extends Instance {
    private final InstanceKey instanceKey;

    private final GuildManager guildManager;

    private final JDA jda;

    AnuraInstance(@NotNull InstanceManager instanceManager, @NotNull InstanceKey instanceKey, @NotNull JDABuilder builder) throws LoginException, IllegalArgumentException {
        super(instanceManager);
        this.instanceKey = instanceKey;

        this.guildManager = new GuildManager(this);

        this.jda = builder.build();
    }

    /* ------------------------- */

    @Override
    public @NotNull String getFullName() {
        if (instanceKey instanceof Language language)
            return "ANURA - " + language.code();
        else
            return instanceKey.toString();
    }

    public JDA getJDA() {
        return jda;
    }
}
