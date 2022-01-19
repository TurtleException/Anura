package de.eldritch.Anura.util.text;

import org.jetbrains.annotations.NotNull;

public enum Language {
    /**
     * The english language. (<code>ISO 639-1</code> compliant)
     */
    ENGLISH("en"),

    /**
     * The german language. (<code>ISO 639-1</code> compliant)
     */
    GERMAN("de"),

    /**
     * The german language with literal (word-by-word) english translations. See https://reddit.com/r/ich_iel for more
     * info on how this works and why it exists. (Good luck finding a useful answer)
     */
    GERMAN_LITERAL("de2"),

    /**
     * For all languages that could not be parsed to a valid enum.
     */
    UNKNOWN("?");

    /**
     * This represents a unique code that can be used to determine the language from filenames, databases, ... It is
     * based on the <code>ISO 639-1</code> code of the language but may vary due to different modes in one language or
     * other application-specific details.
     */
    private final String code;

    Language(@NotNull String code) {
        this.code = code;
    }

    public @NotNull String getCode() {
        return code;
    }
}
