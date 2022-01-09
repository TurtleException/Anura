package de.eldritch.Anura.util.config;

import org.jetbrains.annotations.NotNull;

/**
 * An object that can be represented in a {@link String}.
 */
public interface Serializable<T> {
    /**
     * @return Representation of the object.
     */
    @NotNull String serialize();

    /**
     * Parse an object from a String.
     * @param s String representation of the object.
     */
    T deserialize(@NotNull String s) throws SerializationParseException;
}
