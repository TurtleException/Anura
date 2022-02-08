package de.eldritch.anura.core.listener;

import de.eldritch.anura.Anura;
import de.eldritch.anura.core.AnuraInstance;
import de.eldritch.anura.data.StaticData;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Generic listener for global command auto complete interactions.
 * @see CommandAutoCompleteInteractionEvent
 */
public class AutoCompleteListener extends AbstractInstanceListener {
    public AutoCompleteListener(@NotNull AnuraInstance instance) {
        super(instance);
    }

    @Override
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent event) {
        // possibly check guild availability
        if (event.getChannel() instanceof TextChannel guildChannel && !instance.checkGuildAvailable(guildChannel.getGuild().getIdLong()))
            return;

        // determine command
        switch (event.getName().toLowerCase()) {
            case "timezone" -> this.commandTimezone(event);
            // TODO
        }
    }

    /* ---------- COMMAND COMPLETIONS ---------- */

    /**
     * Handles the "<code>timezone</code>" command.
     * <p>
     *     <b>COMMAND</b>: Set your servers timezone.
     * </p>
     * @param event The interaction event.
     */
    private void commandTimezone(@NotNull CommandAutoCompleteInteractionEvent event) {
        // build a Set of possible values beginning with recently used values
        LinkedHashSet<String> choices = new LinkedHashSet<>(Anura.singleton.getDataService().getRecentTimeZoneCache());
        choices.addAll(StaticData.AVAILABLE_ZONE_IDS);

        // filter possible matches
        String buffer = event.getFocusedOption().getValue().toLowerCase();
        choices.removeIf(s -> !s.toLowerCase().contains(buffer));

        // reply to event
        event.replyChoiceStrings(
                // remove overflow
                choices.size() > 25
                        ? choices.stream().toList().subList(0, 25)
                        : choices
        ).queue();
    }
}
