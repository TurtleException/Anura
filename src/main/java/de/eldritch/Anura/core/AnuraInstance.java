package de.eldritch.Anura.core;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

/**
 * Represents a single instance of the bot. Each instance is assigned to its specific Discord Bot account and may differ
 * from other instances in language, functionality and data access.
 *
 * @see de.eldritch.Anura.Anura
 */
public class AnuraInstance {
    private boolean enabled = false;

    private final int id;

    private JDA jda;

    public AnuraInstance(int id) {
        this.id = id;
    }

    private void init() throws LoginException, IllegalArgumentException {
        JDABuilder builder = JDABuilder.createDefault("");

        jda = builder.build();
    }

    /**
     * Enables the instance and initiates JDA.
     *
     * @throws IllegalStateException if the instance is already enabled.
     * @throws IllegalArgumentException if the bot token is empty or null.
     * @throws LoginException if JDA fails to log in.
     *
     * @see AnuraInstance#onDisable()
     * @see AnuraInstance#init()
     */
    public void onEnable() throws IllegalStateException, IllegalArgumentException, LoginException {
        if (enabled)
            throw new IllegalStateException("Instance is already enabled.");
        enabled = true;

        this.init();
    }

    /**
     * Shuts down JDA and disables the instance.
     * @throws IllegalStateException if the instance is already disabled.
     * @see AnuraInstance#onEnable()
     */
    public void onDisable() throws IllegalStateException {
        if (!enabled)
            throw new IllegalStateException("Instance is already disabled.");
        enabled = false;

        jda.shutdown();
    }

    /* ------------------------- */

    public int getId() {
        return id;
    }
}
