package de.eldritch.anura.core.module.invite.query;

import de.eldritch.anura.core.module.invite.InviteForm;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

public class ModuleQuery extends Query {
    public ModuleQuery(@NotNull InviteForm form) {
        super(form);
    }

    @Override
    public Message build() {
        return null;
    }
}
