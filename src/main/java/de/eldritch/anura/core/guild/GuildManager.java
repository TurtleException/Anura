package de.eldritch.anura.core.guild;

import de.eldritch.anura.core.AnuraInstance;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * Controls all {@link GuildContainer} objects for a single {@link AnuraInstance}.
 */
public class GuildManager {
    private final AnuraInstance instance;

    private final HashMap<Long, GuildContainer> guilds = new HashMap<>();


    public GuildManager(@NotNull AnuraInstance instance) {
        this.instance = instance;
    }


    public GuildContainer getContainer(long snowflake) {
        GuildContainer guild = guilds.get(snowflake);
        if (guild == null) {
            guild = new GuildContainer(this, snowflake);
        }
        return guild;
    }

    /**
     * Provides all known recently used IDs per guild. If no IDs have been cached for that guild yet an empty array will
     * be returned.
     * @param snowflake The snowflake ID of the guild.
     * @return Array of recently used IDs.
     */
    public long[] getRecentIDs(long snowflake) {
        return getContainer(snowflake).getIDCache().toArray();
    }

    /**
     * Caches an ID for a specific guild, marking it as recently used.
     * @param snowflake Snowflake ID of the guild.
     * @param id ID to cache.
     */
    public void markRecent(long snowflake, long id) {
        getContainer(snowflake).addID(id);
    }

    public GuildContainer.Status getStatus(long snowflake) {
        return getContainer(snowflake).getStatus();
    }

    public void setStatus(long snowflake, GuildContainer.Status status) {
        getContainer(snowflake).setStatus(status);
    }
}
