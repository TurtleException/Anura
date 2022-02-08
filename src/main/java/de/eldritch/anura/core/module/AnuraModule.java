package de.eldritch.anura.core.module;

import de.eldritch.anura.core.AnuraInstance;
import de.eldritch.anura.util.logging.NestedLogger;
import de.eldritch.anura.util.text.TextUtil;
import net.dv8tion.jda.api.entities.Emoji;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public abstract class AnuraModule {
    private final String moduleName = getName(this.getClass());

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

    /* ---------- UI ---------- */

    public static String getDescription(AnuraModule module) throws IllegalArgumentException {
        if (!(module instanceof OptionalModule))
            throw new IllegalArgumentException("Non-optional modules do not have a description");

        return TextUtil.get("module." + module.getName() + ".description", module.getInstance().getLanguage()).toString();
    }

    /* ------------------------- */


    public final void setEnabled(boolean b) {
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
    public final boolean isEnabled() {
        return enabled;
    }

    /**
     * Provides the module's simple name.
     * @see AnuraModule#getName(Class)
     */
    public final String getName() {
        return moduleName;
    }

    /**
     * Provides the simple name of an {@link AnuraModule} class.
     * <p> The name of "ExampleModule.java" would be "example".
     * <p> The name of "NewExampleModule.java" would be "newExample".
     * @see AnuraModule#getName()
     */
    public synchronized static String getName(Class<? extends AnuraModule> module) {
        return module.getSimpleName().substring(0, module.getSimpleName().length() - "Module".length()).toLowerCase();
    }

    public final @NotNull NestedLogger getLogger() {
        return logger;
    }

    public final @NotNull AnuraInstance getInstance() {
        return instance;
    }

    /**
     * Provides the offset of this module in the chain of bits that each represent a module being activated or not per
     * server. If the offset is negative the module will always be active.
     * @return Offset of bit representation.
     */
    public int offset() {
        return -1;
    }
}