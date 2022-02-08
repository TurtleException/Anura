package de.eldritch.anura.core.module.invite.query;

import de.eldritch.anura.core.module.invite.InviteForm;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public abstract class Query extends ListenerAdapter {
    protected final InviteForm form;

    // incremented with every error that occurs
    // TODO: implement "Need any help?" message
    protected int errors = 0;

    public Query(@NotNull InviteForm form) {
        this.form = form;
    }

    public abstract Message build();
}
