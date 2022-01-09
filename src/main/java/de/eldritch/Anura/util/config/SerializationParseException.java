package de.eldritch.Anura.util.config;

import org.jetbrains.annotations.NotNull;

public class SerializationParseException extends Exception {
    private final Class<? extends Serializable> type;
    private final String str;

    public SerializationParseException(Class<? extends Serializable> type, @NotNull String str) {
        super("Not a valid representation of " + type.getSimpleName() + ": " + str);

        this.type = type;
        this.str = str;
    }

    public Class<? extends Serializable> getType() {
        return type;
    }

    public @NotNull String getStr() {
        return str;
    }
}
