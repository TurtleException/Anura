package de.eldritch.Anura.util.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * A config is a tree like data structure that is based on YAML.
 * <p>This implementation mainly uses snakeyaml to parse between implementations and actual files.
 */
@SuppressWarnings("unused")
public interface Config {
    /* ---------- MAIN GET & SET ---------- */

    /**
     * Returns a nullable {@link Object} located at the given path.
     * @param path Path where the object should be located.
     * @return Object at the given path.
     * @throws NullPointerException if the provided path is invalid.
     * @throws IllegalArgumentException if the provided path is invalid. Specifically providing an empty String on root
     *                                  level will cause this exception to be thrown.
     */
    @Nullable Object get(@NotNull String path) throws NullPointerException, IOException;

    /**
     * Sets an {@link Object} to a given path or removes all objects (including child configs) from that path if the
     * provided value is <code>null</code>.
     * @param path Path where the object should be located.
     * @param value New object at the given path. Use <code>null</code> to remove the path.
     * @throws NullPointerException if the path is invalid.
     * @throws IllegalArgumentException if the provided path is invalid. Specifically providing an empty String on root
     *                                  level will cause this exception to be thrown.
     */
    void set(@NotNull String path, @Nullable Object value) throws NullPointerException, IllegalArgumentException;


    /* ---------- ADDITIONAL GET ---------- */

    /**
     * Provides a nullable object of the given type located at the specified path. If the path does not contain a value
     * <code>null</code> is returned.
     * @param path Path to the value.
     * @param type Class that the value should be an object of.
     * @return Instance of type or <code>null</code>.
     * @throws NullPointerException if the path is invalid.
     * @throws IllegalArgumentException if the provided path is invalid. Specifically providing an empty String on root
     *                                  level will cause this exception to be thrown.
     * @throws ClassCastException if the object could not be cast to the provided type.
     * @see ConfigSection#get(String)
     * @see ConfigSection#isType(String, Class)
     */
    <T> @Nullable T get(@NotNull String path, @NotNull Class<T> type) throws NullPointerException, IllegalArgumentException, ClassCastException;

    /**
     * Provides a nullable {@link String} located at the specified path.
     * @param path Path to the value.
     * @return Value of the path.
     * @see ConfigSection#getString(String, String)
     */
    @Nullable String getString(@NotNull String path);

    /**
     * Provides a non-null {@link String} located at the specified path. If the path does not contain a value or the
     * value equals <code>null</code> the default value provided by the second parameter will be returned.
     *
     * @param path Path to the value.
     * @param def Default value.
     * @return Value of the path, def if <code>null</code>
     *
     * @see ConfigSection#getString(String)
     */
    @NotNull String getString(@NotNull String path, @NotNull String def);

    /* ------------------------- */

    /**
     * Checks whether the given path contains a valid byte or the value can be parsed to a valid byte by
     * {@link Byte#parseByte(String)}.
     *
     * @param path Path to the value to check.
     * @return true if the value of the given path is a valid byte.
     *
     * @see Config#getByte(String)
     * @see Config#getByte(String, byte)
     */
    boolean isByte(@NotNull String path);

    /**
     * Checks whether the given path contains a valid short or the value can be parsed to a valid short by
     * {@link Short#parseShort(String)}.
     *
     * @param path Path to the value to check.
     * @return true if the value of the given path is a valid short.
     *
     * @see Config#getShort(String)
     * @see Config#getShort(String, short)
     */
    boolean isShort(@NotNull String path);

    /**
     * Checks whether the given path contains a valid integer or the value can be parsed to a valid integer by
     * {@link Integer#parseInt(String)}.
     *
     * @param path Path to the value to check.
     * @return true if the value of the given path is a valid integer.
     *
     * @see Config#getInt(String)
     * @see Config#getInt(String, int)
     */
    boolean isInt(@NotNull String path);

    /**
     * Checks whether the given path contains a valid long or the value can be parsed to a valid long by
     * {@link Long#parseLong(String)}.
     *
     * @param path Path to the value to check.
     * @return true if the value of the given path is a valid long.
     *
     * @see Config#getLong(String)
     * @see Config#getLong(String, long)
     */
    boolean isLong(@NotNull String path);

    /**
     * Checks whether the given path contains a valid float or the value can be parsed to a valid float by
     * {@link Float#parseFloat(String)}.
     *
     * @param path Path to the value to check.
     * @return true if the value of the given path is a valid float.
     *
     * @see Config#getFloat(String)
     * @see Config#getFloat(String, float)
     */
    boolean isFloat(@NotNull String path);

    /**
     * Checks whether the given path contains a valid double or the value can be parsed to a valid double by
     * {@link Double#parseDouble(String)}.
     *
     * @param path Path to the value to check.
     * @return true if the value of the given path is a valid double.
     *
     * @see Config#getDouble(String)
     * @see Config#getDouble(String, double)
     */
    boolean isDouble(@NotNull String path);

    /**
     * Checks whether the given path contains a valid boolean. A valid boolean is considered either <code>true</code> or
     * <code>false</code> (case-insensitive). If the value does not represent either of the two <code>false</code> is
     * returned.
     * <p>
     *     Note that the return value of {@link Config#getBoolean(String)} does necessarily represent a valid
     *     boolean as it only returns whether the given value equals "true" (case-insensitive).
     * </p>
     * @param path Path to the value to check.
     * @return true if the value of the given path is a valid boolean.
     */
    boolean isBoolean(@NotNull String path);

    /**
     * Checks whether the given path contains a valid object of the given type.
     *
     * @param path Path to the value to check.
     * @param type Class that the value should be an object of.
     * @return True if the value at the specified path is an instant of type.
     *
     * @see Config#get(String, Class)
     */
    boolean isType(@NotNull String path, @NotNull Class<?> type);

    /**
     * Provides a byte located at the specified path. If the value at that path does not represent a byte <code>0</code>
     * is returned.
     *
     * @param path Path to the value.
     * @return Parsed byte.
     *
     * @see Config#isByte(String)
     * @see Config#getByte(String, byte)
     */
    byte getByte(@NotNull String path);

    /**
     * Provides a byte located at the specified path. If the path does not contain a value or the value is not a byte
     * the default value is returned.
     *
     * @param path Path to the value.
     * @return Parsed byte or the provided default value.
     *
     * @see Config#isByte(String)
     * @see Config#getByte(String)
     */
    byte getByte(@NotNull String path, byte def);

    /**
     * Provides a short located at the specified path. If the value at that path does not represent a short
     * <code>0</code> is returned.
     *
     * @param path Path to the value.
     * @return Parsed short.
     *
     * @see Config#isShort(String)
     * @see Config#getShort(String, short)
     */
    short getShort(@NotNull String path);

    /**
     * Provides a short located at the specified path. If the path does not contain a value or the value is not a short
     * the default value is returned.
     *
     * @param path Path to the value.
     * @return Parsed short or the provided default value.
     *
     * @see Config#isShort(String)
     * @see Config#getShort(String)
     */
    short getShort(@NotNull String path, short def);

    /**
     * Provides an int located at the specified path. If the value at that path does not represent an integer
     * <code>0</code> is returned.
     *
     * @param path Path to the value.
     * @return Parsed int.
     *
     * @see Config#isInt(String)
     * @see Config#getInt(String, int)
     */
    int getInt(@NotNull String path);

    /**
     * Provides an int located at the specified path. If the path does not contain a value or the value is not an
     * integer the default value is returned.
     *
     * @param path Path to the value.
     * @return Parsed int or the provided default value.
     *
     * @see Config#isInt(String)
     * @see Config#getInt(String)
     */
    int getInt(@NotNull String path, int def);

    /**
     * Provides a long located at the specified path. If the value at that path does not represent a long <code>0</code>
     * is returned.
     *
     * @param path Path to the value.
     * @return Parsed long.
     *
     * @see Config#isLong(String)
     * @see Config#getLong(String, long)
     */
    long getLong(@NotNull String path);

    /**
     * Provides a long located at the specified path. If the path does not contain a value or the value is not a long
     * the default value is returned.
     *
     * @param path Path to the value.
     * @return Parsed long or the provided default value.
     *
     * @see Config#isLong(String)
     * @see Config#getLong(String)
     */
    long getLong(@NotNull String path, long def);

    /**
     * Provides a float located at the specified path. If the value at that path does not represent a float
     * <code>0</code> is returned.
     *
     * @param path Path to the value.
     * @return Parsed float.
     *
     * @see Config#isFloat(String)
     * @see Config#getFloat(String, float)
     */
    float getFloat(@NotNull String path);

    /**
     * Provides a float located at the specified path. If the path does not contain a value or the value is not a float
     * the default value is returned.
     *
     * @param path Path to the value.
     * @return Parsed float or the provided default value.
     *
     * @see Config#isFloat(String)
     * @see Config#getFloat(String)
     */
    float getFloat(@NotNull String path, float def);

    /**
     * Provides a double located at the specified path. If the value at that path does not represent a double
     * <code>0</code> is returned.
     *
     * @param path Path to the value.
     * @return Parsed double.
     *
     * @see Config#isDouble(String)
     * @see Config#getDouble(String, double)
     */
    double getDouble(@NotNull String path);

    /**
     * Provides a double located at the specified path. If the path does not contain a value or the value is not a
     * double the default value is returned.
     *
     * @param path Path to the value.
     * @return Parsed double or the provided default value.
     *
     * @see Config#isDouble(String)
     * @see Config#getDouble(String)
     */
    double getDouble(@NotNull String path, double def);

    /**
     * Provides a boolean located at the specified path. If the value at that path represented as a {@link String}
     * equals "true" (case-insensitive) <code>true</code> is returned, otherwise <code>false</code>.
     *
     * @param path Path to the value.
     * @return Parsed boolean.
     *
     * @see Config#isBoolean(String)
     */
    boolean getBoolean(@NotNull String path);

    /* ------------------------- */

    /**
     * Provides all values this config holds as a {@link Map} with their path as key.
     * @param deep Whether to provide values from deeper levels. Depending on each implementation this might not be
     *             possible and the parameter will be ignored.
     * @return Map of all values this config contains.
     */
    @NotNull Map<String, Object> getMap(boolean deep);

    /**
     * Provides a {@link List<String>} of all keys that are contained by this config.
     * @param deep Whether to provide keys from deeper levels. Depending on each implementation this might not be
     *             possible and the parameter will be ignored.
     * @return List of all keys this config contains.
     */
    List<String> getKeys(boolean deep);
}
