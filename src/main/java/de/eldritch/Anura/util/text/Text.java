package de.eldritch.Anura.util.text;

import org.jetbrains.annotations.NotNull;

public record Text(Language language, String content) {
    public Text(@NotNull Language language, @NotNull String content) {
        this.language = language;
        this.content = content;
    }

    @Override
    public String toString() {
        return content;
    }
}
