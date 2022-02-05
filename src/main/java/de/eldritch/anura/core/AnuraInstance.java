package de.eldritch.anura.core;

import de.eldritch.anura.Instance;
import de.eldritch.anura.InstanceKey;
import de.eldritch.anura.InstanceManager;
import de.eldritch.anura.core.guild.GuildContainer;
import de.eldritch.anura.core.guild.GuildManager;
import de.eldritch.anura.core.listener.CommandListener;
import de.eldritch.anura.core.module.ModuleManager;
import de.eldritch.anura.util.text.Language;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.logging.Level;

public class AnuraInstance extends Instance {
    private final InstanceKey instanceKey;
    private final Language language;

    private GuildManager guildManager;
    private ModuleManager moduleManager;

    private final JDABuilder builder;
    private JDA jda;

    AnuraInstance(@NotNull InstanceManager instanceManager, @NotNull InstanceKey instanceKey, @NotNull JDABuilder builder) throws LoginException, IllegalArgumentException {
        super(instanceManager);
        this.instanceKey = instanceKey;

        if (instanceKey instanceof Language language) {
            this.language = language;
        } else {
            this.language = Language.ENGLISH;
        }

        this.builder = builder;
    }

    /* ---------- THREAD ---------- */

    @Override
    public void run() {
        getLogger().log(Level.INFO, "Thread has been started");

        // build jda
        try {
            this.jda = builder.build();
            this.jda.awaitReady();
        } catch (LoginException | IllegalArgumentException | InterruptedException e) {
            getLogger().log(Level.SEVERE, "Unable to build JDA!", e);
        }

        getLogger().log(Level.INFO, "Checking global commands...");
        this.checkGlobalCommands();

        getLogger().log(Level.INFO, "Checking global listeners...");
        this.checkGlobalListeners();

        this.guildManager  = new GuildManager(this);
        this.moduleManager = new ModuleManager(this);


        /* --------------- --------------- --------------- */

        // noinspection StatementWithEmptyBody
        while (!isInterrupted() && isAlive()) { /* WAIT FOR INTERRUPTION */ }

        /* --------------- --------------- --------------- */


        // disable modules
        getLogger().log(Level.INFO, "Disabling modules... (" + moduleManager.countEnabled() + ")");
        moduleManager.getRegisteredModules().forEach(anuraModule -> anuraModule.setEnabled(false));
        getLogger().log(Level.INFO, "All modules disabled.");

        // shutdown JDA
        jda.shutdown();
        getLogger().log(Level.INFO, "JDA has been shut down");

        this.guildManager  = null;
        this.moduleManager = null;

        getLogger().log(Level.INFO, "Thread has been interrupted.");
    }

    /* ---------- START CHECKS ---------- */

    private void checkGlobalCommands() {
        List<Command> commands    = getJDA().retrieveCommands().complete();
        List<String> commandNames = commands.stream().map(command -> command.getName().toLowerCase()).toList();

        if (!commandNames.contains("timezone")) {
            getLogger().log(Level.INFO, "Command 'timezone' is missing. Creating...");
            getJDA().upsertCommand("timezone", "Set your servers timezone.")
                    .addOption(OptionType.STRING, "TZDB-code", "TZDB-Format (\"Europe/Berlin\", \"America/New_York\")", true, true)
                    .queue(command -> {
                        getLogger().log(Level.INFO, "Command 'timezone' created with id " + command.getId());
                    }, throwable -> {
                        getLogger().log(Level.WARNING, "Command 'timezone' could not be created", throwable);
                    });
        }

        if (!commandNames.contains("timezones")) {
            getLogger().log(Level.INFO, "Command 'timezones' is missing. Creating...");
            getJDA().upsertCommand("timezones", "Get a list of all available timezones")
                    .queue(command -> {
                        getLogger().log(Level.INFO, "Command 'timezones' created with id " + command.getId());
                    }, throwable -> {
                        getLogger().log(Level.WARNING, "Command 'timezones' could not be created", throwable);
                    });
        }
    }

    private void checkGlobalListeners() {
        getJDA().addEventListener(new CommandListener(this));
    }

    /* ---------- RUNTIME CHECKS ---------- */

    public boolean checkGuildAvailable(long snowflake) {
        return getGuildManager().getContainer(snowflake).getStatus() == GuildContainer.Status.READY;
    }

    /* ------------------------- */

    @Override
    public @NotNull String getFullName() {
        if (instanceKey instanceof Language language)
            return "ANURA - " + language.code();
        else
            return instanceKey.toString();
    }

    public Language getLanguage() {
        return language;
    }

    public JDA getJDA() {
        return jda;
    }

    public GuildManager getGuildManager() {
        return guildManager;
    }
}
