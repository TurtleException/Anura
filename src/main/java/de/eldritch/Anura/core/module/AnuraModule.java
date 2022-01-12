package de.eldritch.Anura.core.module;

import de.eldritch.Anura.core.AnuraInstance;
import de.eldritch.Anura.util.config.ConfigSection;
import de.eldritch.Anura.util.config.FileConfig;
import de.eldritch.Anura.util.logging.NestedLogger;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public abstract class AnuraModule {
    private final String moduleName = this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length() - "Module".length()).toLowerCase();
    private ConfigSection config;

    private final AnuraInstance instance;
    private final NestedLogger logger;

    private boolean enabled;

    public AnuraModule(@NotNull AnuraInstance instance) {
        this.instance = instance;

        logger = new NestedLogger("MODULE | " + moduleName.toUpperCase(), instance.getLogger());
    }


    /* ------------------------- */

    public void onEnable() throws AnuraModuleEnableException { }

    public void onDisable() { }

    /* ------------------------- */


    public void setEnabled(boolean b) {
        if (enabled && !b) {
            enabled = false;
            this.onDisable();
        } else if (!enabled && b) {
            try {
                enabled = true;
                this.onEnable();
            } catch (AnuraModuleEnableException e) {
                getLogger().log(Level.WARNING, "Exception while attempting to enable module '" + getName() + "'.", e);
                enabled = false;
            }
        }
    }

    /**
     * Returns whether this module is currently enabled.
     * @return true if module is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Provides the {@link ConfigSection} of the module, which is stored in
     * the plugins main {@link FileConfig}.
     */
    public ConfigSection getConfig() {
        return config;
    }

    /**
     * Provides the module's simple name.
     * <p> The name of "ExampleModule.java" would be "example".
     * <p> The name of "NewExampleModule.java" would be "newExample".
     */
    public String getName() {
        return moduleName;
    }

    public NestedLogger getLogger() {
        return logger;
    }
}