package de.eldritch.anura.core.module.invite;

import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class InviteListener extends ListenerAdapter {
    private final InviteModule module;

    InviteListener(InviteModule module) {
        this.module = module;
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        // TODO
    }
}
