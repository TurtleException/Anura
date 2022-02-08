package de.eldritch.anura.core.module.invite.query;

import de.eldritch.anura.core.module.AnuraModule;
import de.eldritch.anura.core.module.ModuleManager;
import de.eldritch.anura.core.module.OptionalModule;
import de.eldritch.anura.core.module.invite.InviteForm;
import de.eldritch.anura.util.text.Language;
import de.eldritch.anura.util.text.TextUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class ModuleQuery extends Query {
    private final HashMap<String, Boolean> moduleMap = new HashMap<>();

    private final Message messageTemplate;

    private final Language language;
    private final String description;

    public ModuleQuery(@NotNull InviteForm form) {
        super(form);

        this.buildModuleMap();

        this.language = form.getModule().getInstance().getLanguage();

        description = TextUtil.get("module.invite.query.module.description",      language).toString();

        String help   = TextUtil.get("generic.help", language).toString();
        String next   = TextUtil.get("module.invite.query.button.next", language).toString();

        messageTemplate = new MessageBuilder()
                .setActionRows(ActionRow.of(
                        Button.primary("next", next),
                        Button.link("https://github.com/TurtleException/Anura/wiki/Setting-up", help)
                ), ActionRow.of(
                        moduleMenu()
                ))
                .build();
    }

    private void buildModuleMap() {
        moduleMap.clear();
        for (AnuraModule module : form.getModule().getInstance().getModuleManager().getRegisteredModules()) {
            moduleMap.put(module.getName(), true);
        }
    }

    /* ------------------------- */

    private SelectMenu moduleMenu() {
        String placeholder = TextUtil.get("module.invite.query.menu.placeholder", language).toString();

        // retrieve OptionalModules
        List<? extends AnuraModule> modules = form
                .getModule()
                .getInstance()
                .getModuleManager()
                .getRegisteredModules()
                .stream()
                .filter(anuraModule -> anuraModule instanceof OptionalModule)
                .toList();

        // basic component
        SelectMenu.Builder builder = SelectMenu.create("presets")
                .setRequiredRange(0, modules.size())
                .setPlaceholder(placeholder);

        // add options
        for (AnuraModule module : modules) {
            builder.addOption(module.getName(), module.getName(), AnuraModule.getDescription(module), ((OptionalModule) module).getEmoji());
        }

        // select defaults
        builder.setDefaultOptions(builder
                .getOptions()
                .stream()
                .filter(selectOption -> moduleMap.getOrDefault(selectOption.getLabel(), false))
                .toList()
        );

        return builder.build();
    }

    @Override
    public Message build() {
        return new MessageBuilder(messageTemplate)
                .setEmbeds(new EmbedBuilder()
                        .setDescription(description)
                        .build())
                .build();
    }

    /* ------------------------- */

    @Override
    public void onSelectMenuInteraction(@NotNull SelectMenuInteractionEvent event) {
        // confirm channel & message
        if (event.getChannel().getIdLong() != form.getChannelId()) return;
        if (event.getMessage().getIdLong() != form.getMessageId()) return;

        //acknowledge event
        event.deferEdit().queue();

        // update map
        List<SelectOption> selected = event.getSelectedOptions();
        for (SelectOption option : event.getComponent().getOptions()) {
            moduleMap.put(option.getLabel(), selected.contains(option));
        }
    }
}
