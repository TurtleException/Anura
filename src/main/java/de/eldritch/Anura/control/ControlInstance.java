package de.eldritch.Anura.control;

import de.eldritch.Anura.Anura;
import de.eldritch.Anura.util.config.ConfigSection;
import de.eldritch.Anura.util.config.IllegalConfigException;
import de.eldritch.Anura.util.logging.NestedLogger;
import de.eldritch.Anura.util.time.Time;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControlInstance extends Thread {
    private final Anura anura;

    private final NestedLogger logger;
    private final Logger discordLog;

    private JDA jda;


    /* ----- STATS ----- */

    private AtomicInteger guilds = new AtomicInteger();
    private final Time startTime = Time.now();

    /* ----- ----- ----- */


    public ControlInstance(@NotNull Anura anura) throws IllegalConfigException, LoginException {
        super("CONTROL");
        this.anura = anura;

        this.logger = new NestedLogger("CONTROL", anura.getLogger());
        this.discordLog = Logger.getLogger("DISCORD", null);
        this.discordLog.setUseParentHandlers(false);

        this.verifyConfig();

        this.buildJDA();
    }

    /* ---------- THREAD ---------- */

    @Override
    public void run() {
        getLogger().log(Level.FINE, "Thread has been started.");

        jda.getPresence().setPresence(OnlineStatus.ONLINE, Activity.of(Activity.ActivityType.WATCHING, "? guilds"));


        /* --------------- --------------- --------------- */

        //noinspection StatementWithEmptyBody
        while (!isInterrupted() && isAlive()) { /* WAIT FOR INTERRUPTION */ }

        /* --------------- --------------- --------------- */


        jda.getPresence().setPresence(OnlineStatus.IDLE, null);

        getLogger().log(Level.FINE, "Thread has been interrupted.");
    }

    /**
     * This method is called to build the {@link JDA} instance.
     * @see ControlInstance#run()
     */
    private void buildJDA() throws IllegalArgumentException, LoginException {
        getLogger().log(Level.FINE, "Building JDA...");
        JDABuilder builder = JDABuilder.createDefault(anura.getConfig().getString("discord.token"));

        builder.enableIntents(
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_EMOJIS,
                GatewayIntent.GUILD_WEBHOOKS,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.DIRECT_MESSAGES
        );
        builder.setActivity(Activity.of(Activity.ActivityType.PLAYING, "Startup"));
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.setStatus(OnlineStatus.IDLE);

        jda = builder.build();
        getLogger().log(Level.FINE, "Successfully built JDA.");
    }

    /* ---------- INIT ---------- */

    private void verifyConfig() throws IllegalConfigException {
        ConfigSection section = anura.getConfig().createSection("discord");

        if (section.get("token") == null)
            throw new IllegalConfigException("Token may not be null.");
        if (section.get("guild") == null || !section.isLong("guild"))
            throw new IllegalConfigException("Guild may not be null and has to be of type long");
        if (section.get("admins", List.class) == null)
            section.set("admins", List.of(""));

        getLogger().log(Level.FINE, "Config verified.");
    }

    /* ------------------------- */

    public @NotNull NestedLogger getLogger() {
        return logger;
    }
}
