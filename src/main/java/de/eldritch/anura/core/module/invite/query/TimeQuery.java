package de.eldritch.anura.core.module.invite.query;

import de.eldritch.anura.core.module.invite.InviteForm;
import de.eldritch.anura.util.text.TextUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;

/**
 * Allows the guild owner to set a specific time zone (offset from UTC) so wednesday is always on time.
 */
public class TimeQuery extends Query {
    private int currentOffsetMinutes = 0;
    private boolean daylightSaving;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final Message messageTemplate;
    private final String description, fieldTitle;


    public TimeQuery(@NotNull InviteForm form) {
        super(form);

        description = TextUtil.get("module.invite.query.time.description",      form.getModule().getInstance().getLanguage()).toString();
        fieldTitle  = TextUtil.get("module.invite.query.time.field.time.title", form.getModule().getInstance().getLanguage()).toString();

        String next   = TextUtil.get("module.invite.query.button.next", form.getModule().getInstance().getLanguage()).toString();
        String preset = TextUtil.get("module.invite.query.menu.placeholder", form.getModule().getInstance().getLanguage()).toString();

        messageTemplate = new MessageBuilder()
                .setActionRows(ActionRow.of(
                        Button.secondary("minus15", "-15"),
                        Button.secondary("minus01", "-1"),
                        Button.secondary("plus01", "+1"),
                        Button.secondary("plus15", "+15"),
                        Button.secondary("next", next)
                ), ActionRow.of(
                        SelectMenu.create("presets")
                                .setRequiredRange(1,1)
                                .setPlaceholder(preset)
                                .addOption("", "")
                        .build()))
                .build();
    }


    @Override
    public Message build() {
        // actually use the same instant for both time zones
        Instant instant = Instant.now();

        return new MessageBuilder(messageTemplate)
                .setEmbeds(new EmbedBuilder(form.embedTemplate)
                        .setDescription(description)
                        .addField("UTC", formatter.format(instant), false)
                        .addField(fieldTitle, formatter.format(instant.plus(currentOffsetMinutes, ChronoUnit.MINUTES)), false)
                        .build())
                .build();
    }

    /* ------------------------- */

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        // confirm channel & message
        if (event.getChannel().getIdLong() != form.getChannelId()) return;
        if (event.getMessage().getIdLong() != form.getMessageId()) return;

        event.deferEdit().queue();

        // check which button has been pressed
        String id = event.getButton().getId();
        if ("next".equals(id)) {
            form.next();
        } else {
            form.getModule().getLogger().log(Level.WARNING, "Illegal button ID '" + id + "' in message "
                    + event.getMessageId() + " of channel " + event.getChannel().getId() + ".");
            return;
        }
        form.refreshMessage();
    }

    @Override
    public void onSelectMenuInteraction(@NotNull SelectMenuInteractionEvent event) {

    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        // confirm channel
        if (event.getChannel().getIdLong() != form.getChannelId()) return;
    }

    @Override
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent event) {

    }
}
