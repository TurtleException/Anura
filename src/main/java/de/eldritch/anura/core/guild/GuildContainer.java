package de.eldritch.anura.core.guild;

import de.eldritch.anura.core.AnuraInstance;
import de.eldritch.anura.data.DataService;
import de.eldritch.anura.util.LongCache;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

import java.time.ZoneId;

/**
 * Stores information on a single {@link Guild} for an {@link AnuraInstance}.
 * The guild is only stored as its snowflake ID to prevent issues with caching.
 */
public class GuildContainer {
    // specifies the size of the ID cache
    private static final int LONG_CACHE_CAPACITY = 10;

    // responsible manager
    private final GuildManager manager;
    // snowflake ID of the guild
    private final long snowflake;

    private Status status = Status.UNKNOWN;

    private final LongCache recentIDs;
    // TZDB timezone ID of the guild
    private ZoneId timeZone;

    /**
     * Package-private constructor - called by {@link GuildManager#getContainer(long)}.
     * @param manager Responsible {@link GuildManager}.
     * @param snowflake Snowflake ID of the guild.
     */
    GuildContainer(@NotNull GuildManager manager, long snowflake) {
        this.manager = manager;
        this.snowflake = snowflake;

        this.recentIDs = new LongCache(LONG_CACHE_CAPACITY);

        this.retrievePermanentData();
        this.checkStatus();
    }

    /**
     * Query the {@link DataService} for data on the guild that has already been stored (e.g. from before a restart).
     * @see GuildContainer#GuildContainer(GuildManager, long)
     */
    private void retrievePermanentData() {
        // TODO
    }

    /**
     * Checks all currently available data for this guild and determines the {@link Status}.
     * @see GuildContainer#GuildContainer(GuildManager, long)
     */
    private void checkStatus() {
        // TODO
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
     * Provides the snowflake ID of the guild.
     * @return Guild ID.
     */
    public long getSnowflake() {
        return snowflake;
    }

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

    /**
     * Provides the {@link ZoneId} associated with the UTC-offset of this guild.
     * <p>This may be <code>null</code> if the {@link Status} is not <code>READY</code>.
     * @return ZoneId of this guild.
     */
    public ZoneId getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(@NotNull ZoneId timeZone) {
        this.timeZone = timeZone;
    }
}
