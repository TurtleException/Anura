package de.eldritch.Anura.core;

import de.eldritch.Anura.Anura;
import de.eldritch.Anura.core.module.AnuraModule;
import de.eldritch.Anura.core.module.ModuleManager;
import de.eldritch.Anura.core.module.timer.DummyModule;
import de.eldritch.Anura.util.config.ConfigSection;
import de.eldritch.Anura.util.config.FileConfig;
import de.eldritch.Anura.util.config.IllegalConfigException;
import de.eldritch.Anura.util.logging.AnuraLogger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.logging.Level;

/**
 * Represents a single instance of the bot. Each instance is assigned to its specific Discord Bot account and may differ
 * from other instances in language, functionality and data access.
 *
 * @see Anura
 */
public class AnuraInstance extends Thread {
    private final ConfigSection config;

    private final AnuraLogger logger;

    private final ModuleManager moduleManager;
    private JDA jda;

    public AnuraInstance(@NotNull ConfigSection config) throws IllegalConfigException {
        super(config.getKey());

        this.config = config;

        this.logger = new AnuraLogger(this);
        this.moduleManager = new ModuleManager(this);

        this.verifyConfig();
        this.registerModules();
    }

    /* ---------- THREAD ---------- */

    @Override
    public void run() {
        getLogger().log(Level.FINE, "Thread has been started.");

        // build JDA
        try {
            buildJDA();
        } catch (IllegalArgumentException | LoginException e) {
            getLogger().log(Level.SEVERE, "Unable to build JDA!", e);
            this.interrupt();
        }

        // enable modules
        getLogger().log(Level.INFO, "Enabling modules...");
        moduleManager.getRegisteredModules().forEach(anuraModule -> anuraModule.setEnabled(true));
        getLogger().log(Level.INFO, moduleManager.getEnabled() + " modules enabled.");

        getLogger().log(Level.INFO, "Instance running.");


        /* --------------- --------------- --------------- */

        //noinspection StatementWithEmptyBody
        while (!isInterrupted() && isAlive()) { /* WAIT FOR INTERRUPTION */ }

        /* --------------- --------------- --------------- */


        // disable modules
        getLogger().log(Level.INFO, "Disabling modules... (" + moduleManager.getEnabled() + ")");
        moduleManager.getRegisteredModules().forEach(anuraModule -> anuraModule.setEnabled(false));
        getLogger().log(Level.INFO, "All modules disabled.");

        // shutdown JDA
        jda.shutdown();
        getLogger().log(Level.FINE, "JDA has been shut down");

        getLogger().log(Level.FINE, "Thread has been interrupted.");
    }

    /**
     * This method is called to build the {@link JDA} instance.
     * @see AnuraInstance#run()
     */
    private void buildJDA() throws IllegalArgumentException, LoginException {
        getLogger().log(Level.FINE, "Building JDA...");
        JDABuilder builder = JDABuilder.createDefault(config.getString("discord.token"));

        // TODO: include gateway intents, presence and member cache policy

        jda = builder.build();
        getLogger().log(Level.FINE, "Successfully built JDA.");
    }

    /* ---------- INIT ---------- */

    /**
     * Verifies the {@link ConfigSection} this instance holds. Missing keys are filled with data from the
     * <code>instance-template.yml</code> resource. If the template fails to load an exception is thrown.
     * @throws IllegalConfigException if the template resource fails to load or the {@link FileConfig} could not be
     *                                instantiated properly.
     */
    private void verifyConfig() throws IllegalConfigException {
        FileConfig template;
        try {
            File templateFile = new File(ClassLoader.getSystemResource("instance-template.yml").toURI());
            template = new FileConfig(templateFile);
        } catch (URISyntaxException | IOException e) {
            throw new IllegalConfigException("Unable to verify config due to an unexpected exception while loading templates.", e);
        }

        // fill missing keys in config with template values
        HashSet<String> keys = new HashSet<>(template.getKeys(true));
        config.getKeys(true).forEach(keys::remove);
        keys.forEach(key -> config.set(key, template.get(key)));

        getLogger().log(Level.FINE, "Config verified.");
    }

    /**
     * Retrieves instructions from the <code>instances.yml</code> config to determine which Modules should be registered
     * and passes them to the {@link ModuleManager}.
     */
    private void registerModules() {
        getLogger().log(Level.FINE, "Registering modules...");

        // collect all modules
        HashSet<Class<? extends AnuraModule>> modules = new HashSet<>();
        modules.add(DummyModule.class);

        for (Class<? extends AnuraModule> module : modules) {
            String name = AnuraModule.getName(module);
            getLogger().log(Level.FINE, "Checking module '" + name + "'...");

            ConfigSection moduleSection;
            try {
                moduleSection = config.get("module." + name, ConfigSection.class);
            } catch (NullPointerException | IllegalArgumentException e) {
                getLogger().log(Level.WARNING, "Encountered an unexpected exception while checking module '" + name + "'", e);
                continue;
            } catch (ClassCastException e) {
                getLogger().log(Level.FINE, "Could not find valid config section for module '" + name + "', ignoring it.", e);
                continue;
            }

            if (moduleSection == null) {
                getLogger().log(Level.FINE, "Could not find a valid config section for module '" + name + "', ignoring it.");
                continue;
            }

            moduleManager.registerModule(module, moduleSection);
            getLogger().log(Level.FINE, "Registered module '" + name + "'");
        }

        getLogger().log(Level.INFO, moduleManager.getRegisteredModules().size() + " modules registered.");
    }

    /* ------------------------- */

    public @NotNull AnuraLogger getLogger() {
        return logger;
    }

    public @NotNull String getFullName() {
        return "ANURA#" + this.getId() + " - " + this.getName();
    }

    public JDA getJDA() {
        return jda;
    }
}
