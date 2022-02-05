package de.eldritch.anura.data.entities;

import de.eldritch.anura.util.text.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a meme stored in the database.
 */
public record Meme(long id, long timestamp, @NotNull String url, @NotNull Language language, @Nullable Long authorID, int rating) {
    /**
     * Checks whether this meme has no author and is therefore considered anonymous.
     * @return true is this meme has no specified author.
     */
    public boolean isAnonymous() {
        return authorID == null;
    }
}
