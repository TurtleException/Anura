package de.eldritch.anura.core.guild;

import de.eldritch.anura.util.LongCache;
import org.jetbrains.annotations.NotNull;

import java.util.SimpleTimeZone;

public class GuildContainer {
    private static final int LONG_CACHE_CAPACITY = 10;

    private final GuildManager manager;
    private final long snowflake;

    private Status status;

    private LongCache recentIDs;
    private SimpleTimeZone timeZone;

    public GuildContainer(@NotNull GuildManager manager, long snowflake) {
        this.manager = manager;
        this.snowflake = snowflake;

        this.recentIDs = new LongCache(LONG_CACHE_CAPACITY);
    }

    /**
     * Provides the snowflake ID of the guild.
     * @return Guild ID.
     */
    public long getSnowflake() {
        return snowflake;
    }

    /* ------------------------- */

    public enum Status {
        /**
         * The status is unknown. No interaction should be performed or unexpected error could occur.
         */
        UNKNOWN,
        /**
         * The JDA instance does not know the guild because the bot is not a member of it.
         */
        FOREIGN,
        /**
         * The invitation is still pending. The bot has been invited but the setup dialogue is not yet finished.
         * @see de.eldritch.anura.core.module.invite.InviteModule InviteModule
         */
        PENDING,
        /**
         * Everything has been set up and the bot is active.
         */
        READY,
        /**
         * The guild has been blocked by moderation or the bot does not have sufficient permissions.
         */
        BLOCKED;
    }

    public @NotNull Status getStatus() {
        return status;
    }

    public void setStatus(@NotNull Status status) {
        this.status = status;
    }

    /* ------------------------- */

    /**
     * Provides the cache of recently used IDs for this guild.
     * @return ID cache.
     */
    public @NotNull LongCache getIDCache() {
        return recentIDs;
    }

    public SimpleTimeZone getTimeZone() {
        return timeZone;
    }

    /**
     * Caches an ID for this guild, marking it as recently used.
     * @param id ID to cache.
     */
    public void addID(long id) {
        recentIDs.put(id);
    }
}
