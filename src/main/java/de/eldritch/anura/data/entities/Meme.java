package de.eldritch.anura.data.entities;

import de.eldritch.anura.util.text.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record Meme(long id, long timestamp, @NotNull String url, @NotNull Language language, @Nullable Long authorID, int rating) {
    public boolean isAnonymous() {
        return authorID == null;
    }
}
