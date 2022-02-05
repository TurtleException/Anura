package de.eldritch.anura.core.module.invite;

import de.eldritch.anura.core.module.AnuraModule;
import de.eldritch.anura.core.module.invite.query.ModuleQuery;
import de.eldritch.anura.core.module.invite.query.Query;
import de.eldritch.anura.core.module.invite.query.TimeQuery;
import de.eldritch.anura.util.DiscordUtil;
import de.eldritch.anura.util.text.TextUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class InviteForm {
    private final AnuraModule module;
    private final Guild guild;

    private final MessageChannel channel;
    private long message;

    public final MessageEmbed embedTemplate;

    private int queryPointer = 0;
    private final Query[] queries = {
            new TimeQuery(this),
            new ModuleQuery(this)
    };

    InviteForm(AnuraModule module, @NotNull Guild guild, @NotNull MessageChannel channel) {
        this.module = module;
        this.guild = guild;
        this.channel = channel;

        // template for all embeds
        embedTemplate = new EmbedBuilder()
                .setFooter(DiscordUtil.FOOTER_TEXT + TextUtil.get("module.invite.message.footerSuffix", module.getInstance().getLanguage()).toString())
                .setTitle(TextUtil.get("module.invite.message.title", module.getInstance().getLanguage()).toString())
                .setColor(DiscordUtil.COLOR_NEUTRAL)
                .build();

        // send initial message
        module.getInstance().getJDA().addEventListener(queries[0]);
        channel.sendMessage(queries[0].build()).queue(
                message1 -> message = message1.getIdLong(),
                throwable -> module.getLogger().log(Level.WARNING, "Unable to start InviteForm", throwable)
        );
    }

    /* ------------------------- */

    /**
     * Called to skip ro proceed to the next query. If the current query is the last one the message is edited to tell
     * the user all queries have been submitted.
     */
    public void next() {
        // remove old interaction listener
        module.getInstance().getJDA().removeEventListener(queries[queryPointer]);

        if (++queryPointer < queries.length) {
            // add new interaction listener
            module.getInstance().getJDA().addEventListener(queries[queryPointer]);
            // send new message
            refreshMessage();
        } else {
            // send final message
            channel.editMessageById(message, getFinalMessage()).queue();
        }
    }

    /**
     * Rebuilds and edits the existing message.
     */
    public void refreshMessage() {
        channel.editMessageById(message, queries[queryPointer].build()).queue();
    }

    private Message getFinalMessage() {
        String description = TextUtil.get("module.invite.finalMessage.description",  module.getInstance().getLanguage()).toString();
        String repeatSetup = TextUtil.get("module.invite.finalMessage.button.repeat", module.getInstance().getLanguage()).toString();
        return new MessageBuilder()
                .setEmbeds(new EmbedBuilder(embedTemplate).setDescription(description).build())
                .setActionRows(ActionRow.of(Button.secondary("repeatSetup",repeatSetup)))
                .build();
    }

    /* ------------------------- */

    public AnuraModule getModule() {
        return module;
    }

    public long getChannelId() {
        return channel.getIdLong();
    }

    public long getMessageId() {
        return message;
    }
}
