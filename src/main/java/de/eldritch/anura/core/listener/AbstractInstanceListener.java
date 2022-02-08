package de.eldritch.anura.core.listener;

import de.eldritch.anura.core.AnuraInstance;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

abstract class AbstractInstanceListener extends ListenerAdapter {
    protected final AnuraInstance instance;

    public AbstractInstanceListener(@NotNull AnuraInstance instance) {
        this.instance = instance;
    }
}
