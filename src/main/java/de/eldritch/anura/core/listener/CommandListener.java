package de.eldritch.anura.core.listener;

import de.eldritch.anura.core.AnuraInstance;
import de.eldritch.anura.core.module.AnuraModule;
import de.eldritch.anura.util.DiscordUtil;
import de.eldritch.anura.util.text.TextUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import org.jetbrains.annotations.NotNull;

import java.time.ZoneId;
import java.util.logging.Level;

/**
 * Generic listener for global {@link Command Commands} or commands that are common on all guilds and not specific to
 * one {@link AnuraModule}.
 */
public class CommandListener extends AbstractInstanceListener {
    public CommandListener(@NotNull AnuraInstance instance) {
        super(instance);
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        // possibly check guild availability
        if (event.getChannel() instanceof TextChannel guildChannel && !instance.checkGuildAvailable(guildChannel.getGuild().getIdLong()))
            return;

        // determine command
        switch (event.getName().toLowerCase()) {
            case "timezones" -> { this.commandTimezones(event); return; }
            // TODO
        }

        // don't do anything, maybe this is intended for another listener
    }

    /* ---------- COMMANDS ---------- */

    /**
     * Handles the "<code>timezones</code>" command.
     * <p>
     *     <b>COMMAND</b>: Provides a list of all currently available tzdb timezone codes.
     * </p>
     * @param event The interaction event.
     */
    private void commandTimezones(@NotNull SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(false).queue();

        String title       = TextUtil.get("command.timezones.message.title", instance.getLanguage()).toString();
        String description = TextUtil.get("command.timezones.message.description", instance.getLanguage(), "[iana TZDB](https://www.iana.org/time-zones)").toString();

        event.getHook().sendMessageEmbeds(new EmbedBuilder()
                        .setColor(DiscordUtil.COLOR_NEUTRAL)
                        .setFooter(DiscordUtil.FOOTER_TEXT)
                        .setDescription(description)
                        .setTitle(title)
                        .build())
                .addFile(getZoneIdFile(), "available-tzdb-timezones.txt")
                .queue(null,
                        throwable -> instance.getLogger().log(Level.WARNING, "Encountered an exception when attempting to send timezone list.", throwable)
                );
    }

    /* ------------------------- */

    /**
     * Provides a list of all available {@link ZoneId ZoneIds} as a byte array.
     * @return String of available ZoneIds as byte array.
     * @see CommandListener#commandTimezones(SlashCommandInteractionEvent)
     */
    private byte[] getZoneIdFile() {
        StringBuilder builder = new StringBuilder();
        for (String availableZoneId : ZoneId.getAvailableZoneIds())
            builder.append(availableZoneId).append("\n");
        return builder.toString().getBytes();
    }
}
