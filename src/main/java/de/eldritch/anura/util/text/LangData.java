package de.eldritch.anura.util.text;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

/**
 * Represents the content of a lang file - or more precisely a map of all keys and {@link String Strings} of a single
 * language. Each key holds a single {@link String} that can be accessed via {@link TextUtil}.
 */
class LangData {
    private final Reader reader;
    private final Properties data;

    public LangData(@NotNull Reader reader) {
        this.reader = reader;
        this.data   = new Properties();

        this.load();
    }

    public void load() {
        try {
            data.load(reader);
        } catch (IOException ignored) { }
    }

    /**
     * Searches the lang data for a {@link String} with this key and returns it. If no {@link String} could be found
     * <code>null</code> will be returned.
     * @param key The key to the requested {@link String}.
     * @return String associated with the key.
     */
    @Nullable String get(@NotNull String key) {
        return data.getProperty(key);
    }
}