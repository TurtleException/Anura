package de.eldritch.anura.core.guild;

import de.eldritch.anura.util.LongCache;
import org.jetbrains.annotations.NotNull;

import java.time.ZoneId;

public class GuildContainer {
    private static final int LONG_CACHE_CAPACITY = 10;

    private final GuildManager manager;
    private final long snowflake;

    private Status status = Status.UNKNOWN;

    private final LongCache recentIDs;
    private ZoneId timeZone;

    GuildContainer(@NotNull GuildManager manager, long snowflake) {
        this.manager = manager;
        this.snowflake = snowflake;

        this.recentIDs = new LongCache(LONG_CACHE_CAPACITY);

        this.retrievePermanentData();
        this.checkStatus();
    }

    private void retrievePermanentData() {
        // TODO
    }

    private void checkStatus() {
        // TODO
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
     * Provides the cache of recently used IDs for this guild. If no IDs have been cached for that guild yet an empty
     * array will be returned.
     * <p>This is used to prevent the same memes from being posted not long enough apart.
     * @return ID cache.
     * @see GuildContainer#addID(long)
     */
    public @NotNull LongCache getIDCache() {
        return recentIDs;
    }

    /**
     * Caches an ID for this guild, marking it as recently used.
     * <p>This is used to prevent the same memes from being posted not long enough apart.
     * @param id ID to cache.
     * @see GuildContainer#getIDCache()
     */
    public void addID(long id) {
        recentIDs.put(id);
    }

    public ZoneId getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(ZoneId timeZone) {
        this.timeZone = timeZone;
    }
}
