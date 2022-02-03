package de.eldritch.anura.core;

import de.eldritch.anura.util.LongCache;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class GuildManager {
    private final AnuraInstance instance;

    private final static int CACHE_CAPACITY_RECENT_IDS = 10;

    private final HashMap<Long, LongCache> recentIDsPerGuild = new HashMap<>();

    GuildManager(@NotNull AnuraInstance instance) {
        this.instance = instance;
    }

    /**
     * Provides all known recently used IDs per guild. If no IDs have been cached for that guild yet an empty array will
     * be returned.
     * @param guildID The snowflake ID of the guild.
     * @return Array of recently used IDs.
     */
    public long[] getRecentIDs(long guildID) {
        LongCache cache = recentIDsPerGuild.get(guildID);
        return cache == null ? new long[]{} : cache.toArray();
    }

    /**
     * Caches an ID for a specific guild, marking it as recently used.
     * @param guildID Snowflake ID of the guild.
     * @param id ID to cache.
     */
    public void markRecent(long guildID, long id) {
        LongCache cache = recentIDsPerGuild.putIfAbsent(guildID, new LongCache(CACHE_CAPACITY_RECENT_IDS, id));
        if (cache != null)
            cache.put(id);
    }
}
