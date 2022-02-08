package de.eldritch.anura.core.module.invite.query;

import de.eldritch.anura.Anura;
import de.eldritch.anura.core.module.invite.InviteForm;
import de.eldritch.anura.util.text.Language;
import de.eldritch.anura.util.text.TextUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;

/**
 * Allows the guild owner to set a specific time zone (offset from UTC) so wednesday is always on time.
 */
public class TimeQuery extends Query {
    private ZoneId currentZone = ZoneId.of("UTC");

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final Message messageTemplate;
    private final String description, fieldTitle;

    private final Language language;


    public TimeQuery(@NotNull InviteForm form) {
        super(form);

        this.language = form.getModule().getInstance().getLanguage();

        description = TextUtil.get("module.invite.query.time.description",      language).toString();
        fieldTitle  = TextUtil.get("module.invite.query.time.field.time.title", language).toString();

        String help   = TextUtil.get("generic.help", language).toString();
        String next   = TextUtil.get("module.invite.query.button.next", language).toString();

        messageTemplate = new MessageBuilder()
                .setActionRows(ActionRow.of(
                        Button.primary("next", next),
                        Button.link("https://github.com/TurtleException/Anura/wiki/Setting-up", help)
                ), ActionRow.of(
                        frequentZones()
                ))
                .build();
    }

    /**
     * Provides a prepared {@link SelectMenu} with frequently used time zones and their current time as description.
     * @return Prepared SelectMenu
     */
    private SelectMenu frequentZones() {
        String placeholder = TextUtil.get("module.invite.query.menu.placeholder", language).toString();

        // basic component
        SelectMenu.Builder builder = SelectMenu.create("presets")
                .setRequiredRange(1,1)
                .setPlaceholder(placeholder);

        // retrieve frequent time zones
        List<String> presets = Anura.singleton.getDataService().getRecentTimeZones();

        Instant instant = Instant.now();

        // add options (with current time as description)
        for (int i = 0; i < presets.size() && i < 25; i++) {
            String time = null;
            try {
                ZoneId zone = ZoneId.of(presets.get(i));
                time = formatter.format(instant.atZone(zone));
            } catch (Exception ignored) {
            }

            builder.addOption(presets.get(i), presets.get(i), time != null ? time : "???");
        }

        return builder.build();
    }


    @Override
    public Message build() {
        // actually use the same instant for both time zones
        Instant instant = Instant.now();

        return new MessageBuilder(messageTemplate)
                .setEmbeds(new EmbedBuilder(form.embedTemplate)
                        .setDescription(description)
                        .addField("UTC", formatter.format(instant), false)
                        .addField(fieldTitle, formatter.format(instant.atZone(currentZone)), false)
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
        // confirm channel & message
        if (event.getChannel().getIdLong() != form.getChannelId()) return;
        if (event.getMessage().getIdLong() != form.getMessageId()) return;

        event.deferEdit().queue();

        List<String> values = event.getValues();
        if (values.isEmpty()) return;

        String zone = values.get(0);

        try {
            this.currentZone = ZoneId.of(zone);
            form.refreshMessage();
        } catch (Exception e) {
            form.getModule().getLogger().log(Level.WARNING, "Illegal zone ID '" + zone + "' in SelectMenuInteraction.", e);
            // TODO: send report to ControlInstance

            String error = TextUtil.get("error.unknown", language).toString();
            event.getChannel().sendMessage(error).queue();
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        // confirm channel
        if (event.getChannel().getIdLong() != form.getChannelId()) return;
        if (event.getChannel() instanceof TextChannel) return; // not allowed on guild channels

        String zone = event.getMessage().getContentRaw();

        try {
            this.currentZone = ZoneId.of(zone);
            form.refreshMessage();
        } catch (Exception ignored) {
            String reply = TextUtil.get("module.invite.query.time.reply.notFound", language, zone).toString();
            event.getMessage().reply(reply).queue();
        }
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        // confirm channel
        if (event.getChannel().getIdLong() != form.getChannelId()) return;

        event.deferReply(true).queue();

        String zone   = event.getOptions().get(0).getAsString();
        String reply0 = TextUtil.get("module.invite.query.time.slashCommandReply.success", language, zone).toString();
        String reply1 = TextUtil.get("module.invite.query.time.reply.notFound", language, zone).toString();

        try {
            this.currentZone = ZoneId.of(zone);
            event.getHook().sendMessage(reply0).queue();
        } catch (Exception ignored) {
            event.getHook().sendMessage(reply1).queue();
        }
    }
}
