package de.eldritch.Anura.util.config;

import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.Reader;
import java.util.*;

/**
 * Represents an individual section of a configuration.
 * <p> ConfigSections are stored in-memory.
 */
@SuppressWarnings("unused")
public class ConfigSection {
    private final String key;

    // parent configuration, null if this is the root section
    private final ConfigSection parent;

    // deeper sections that are stored within this section
    private final TreeSet<ConfigSection> children = new TreeSet<>(Comparator.comparing(ConfigSection::getKey));

    // values that are stored within this section (on the same level)
    private final HashMap<String, String> values = new HashMap<>();


    ConfigSection(@Nullable ConfigSection parent, @Nullable String key) {
        this.key = key;
        this.parent = parent;
    }


    /**
     * Creates a new ConfigSection that is a child of this section.
     * @param key The key of the section.
     * @return The new ConfigSection.
     */
    public @NotNull ConfigSection createSection(@NotNull String key) throws IllegalArgumentException {
        if (key.contains("\\.") || key.contains(":"))
            throw new IllegalArgumentException("Not a valid key: " + key);

        ConfigSection section = new ConfigSection(this, key);
        this.children.add(section);
        return section;
    }


    /**
     * Get the key of this ConfigSection. This does not involve the keys of parent sections.
     * @return Key of the section.
     */
    public @Nullable String getKey() {
        return key;
    }

    /**
     * Get the path of this ConfigSection. This involves all keys of parent sections.
     * @return Path of the section.
     */
    public @Nullable String getPath() {
        if (this.getKey() == null)
            return null;

        StringBuilder key = new StringBuilder(this.getKey());
        ConfigSection section = this.getParent();
        while (section != null && !section.isRoot()) {
            key.insert(0, section.getKey() + ".");
            section = section.getParent();
        }

        return key.toString();
    }

    /**
     * Provides the parent config of this section.
     * @return The parent of this section, <code>null</code> if this is the root node.
     * @see ConfigSection#isRoot()
     */
    public @Nullable ConfigSection getParent() {
        return parent;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public void set(@NotNull String path, Object object) {
        if (!ConfigUtil.validatePath(path))
            throw new IllegalArgumentException("Not a valid path: " + path);

        if (this.isRoot() && path.equals(""))
            throw new IllegalArgumentException("Key may not be empty String on root section.");

        String[] keys = path.split("\\.");
        if (keys.length < 2) {
            // set value in this section
            if (object instanceof ConfigSection) {
                // value is config section
                children.add((ConfigSection) object);
            } else if (object == null) {
                // value is null -> remove value / section
                values.remove(keys[0]);
                children.removeIf(configSection -> keys[0].equals(configSection.getKey()));
            } else {
                // value is String
                values.put(keys[0], object.toString());
            }
        } else {
            // set value in deeper section
            for (ConfigSection child : children) {
                if (child.getKey().equals(keys[0])) {
                    child.set(path.substring(keys[0].length() + 1), object);
                    return;
                }
            }
            // set value in new deeper section
            createSection(keys[0]).set(path.substring(keys[0].length() + 1), object);
        }
    }

    /**
     * Provides a nullable {@link String} located at the specified path.
     *
     * @param path Path to the value.
     * @return Value of the path.
     *
     * @see ConfigSection#getString(String, String)
     */
    public @Nullable String getString(@NotNull String path) {
        if (!ConfigUtil.validatePath(path))
            return null;

        if (path.equals(this.key))
            return this.toString();

        if (values.containsKey(path))
            return values.get(path);

        String[] keys = path.split("\\.");
        if (keys.length < 2)
            return null;

        for (ConfigSection child : children) {
            if (keys[0].equals(child.getKey())) {
                return child.getString(path.substring(keys[0].length() + 1));
            }
        }

        return null;
    }

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
    public @NotNull String getString(@NotNull String path, @NotNull String def) {
        String str = this.getString(path);
        return str == null ? def : str;
    }

    /**
     * Provides a nullable object of the given type located at the specified path. If the path does not contain a value
     * or the value is not an instance of the given type <code>null</code> is returned.
     *
     * @param path Path to the value.
     * @param type Class that the value should be an object of.
     * @return Instance of type or <code>null</code>.
     *
     * @see ConfigSection#isType(String, Class)
     */
    public <T> @Nullable T get(@NotNull String path, @NotNull Class<T> type) {
        String raw = this.getString(path);
        if (raw == null) return null;

        try {
            return type.cast(raw);
        } catch (ClassCastException e) {
            return null;
        }
    }

    /**
     * Provides a nullable object at the specific path. If the path does not contain a value <code>null</code> is
     * returned.
     *
     * @param path Path to the value.
     * @return Object saved at the given path.
     */
    public @Nullable Object get(@NotNull String path) {
        return this.get(path, Object.class);
    }

    /**
     * Checks whether the given path contains a valid byte or the value can be parsed to a valid byte by
     * {@link Byte#parseByte(String)}.
     *
     * @param path Path to the value to check.
     * @return true if the value of the given path is a valid byte.
     *
     * @see ConfigSection#getByte(String)
     * @see ConfigSection#getByte(String, byte)
     */
    public boolean isByte(@NotNull String path) {
        return this.getByte(path, (byte) 0) == this.getByte(path, (byte) 1);
    }

    /**
     * Checks whether the given path contains a valid short or the value can be parsed to a valid short by
     * {@link Short#parseShort(String)}.
     *
     * @param path Path to the value to check.
     * @return true if the value of the given path is a valid short.
     *
     * @see ConfigSection#getShort(String)
     * @see ConfigSection#getShort(String, short)
     */
    public boolean isShort(@NotNull String path) {
        return this.getShort(path, (short) 0) == this.getShort(path, (short) 1);
    }

    /**
     * Checks whether the given path contains a valid integer or the value can be parsed to a valid integer by
     * {@link Integer#parseInt(String)}.
     *
     * @param path Path to the value to check.
     * @return true if the value of the given path is a valid integer.
     *
     * @see ConfigSection#getInt(String)
     * @see ConfigSection#getInt(String, int)
     */
    public boolean isInt(@NotNull String path) {
        return this.getInt(path, 0) == this.getInt(path, 1);
    }

    /**
     * Checks whether the given path contains a valid long or the value can be parsed to a valid long by
     * {@link Long#parseLong(String)}.
     *
     * @param path Path to the value to check.
     * @return true if the value of the given path is a valid long.
     *
     * @see ConfigSection#getLong(String)
     * @see ConfigSection#getLong(String, long)
     */
    public boolean isLong(@NotNull String path) {
        return this.getLong(path, 0L) == this.getLong(path, 1L);
    }

    /**
     * Checks whether the given path contains a valid float or the value can be parsed to a valid float by
     * {@link Float#parseFloat(String)}.
     *
     * @param path Path to the value to check.
     * @return true if the value of the given path is a valid float.
     *
     * @see ConfigSection#getFloat(String)
     * @see ConfigSection#getFloat(String, float)
     */
    public boolean isFloat(@NotNull String path) {
        return this.getFloat(path, (float) 0) == this.getFloat(path, (float) 1);
    }

    /**
     * Checks whether the given path contains a valid double or the value can be parsed to a valid double by
     * {@link Double#parseDouble(String)}.
     *
     * @param path Path to the value to check.
     * @return true if the value of the given path is a valid double.
     *
     * @see ConfigSection#getDouble(String)
     * @see ConfigSection#getDouble(String, double)
     */
    public boolean isDouble(@NotNull String path) {
        return this.getDouble(path, 0) == this.getDouble(path, 1);
    }

    /**
     * Checks whether the given path contains a valid boolean. A valid boolean is considered either <code>true</code> or
     * <code>false</code> (case-insensitive). If the value does not represent either of the two <code>false</code> is
     * returned.
     * <p>
     *     Note that the return value of {@link ConfigSection#getBoolean(String)} does necessarily represent a valid
     *     boolean as it only returns whether the given value equals "true" (case-insensitive).
     * </p>
     * @param path Path to the value to check.
     * @return true if the value of the given path is a valid boolean.
     */
    public boolean isBoolean(@NotNull String path) {
        String str = this.getString(path, "");
        return str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false");
    }

    /**
     * Checks whether the given path contains a valid object of the given type.
     *
     * @param path Path to the value to check.
     * @param type Class that the value should be an object of.
     * @return True if the value at the specified path is an instant of type.
     *
     * @see ConfigSection#get(String, Class)
     */
    public boolean isType(@NotNull String path, @NotNull Class<?> type) {
        return this.get(path, type) != null;
    }

    /**
     * Provides a byte located at the specified path. If the value at that path does not represent a byte <code>0</code>
     * is returned.
     *
     * @param path Path to the value.
     * @return Parsed byte.
     *
     * @see ConfigSection#isByte(String)
     * @see ConfigSection#getByte(String, byte)
     */
    public byte getByte(@NotNull String path) {
        return this.getByte(path, (byte) 0);
    }

    /**
     * Provides a byte located at the specified path. If the path does not contain a value or the value is not a byte
     * the default value is returned.
     *
     * @param path Path to the value.
     * @return Parsed byte or the provided default value.
     *
     * @see ConfigSection#isByte(String)
     * @see ConfigSection#getByte(String)
     */
    public byte getByte(@NotNull String path, byte def) {
        try {
            return Byte.parseByte(this.getString(path, ""));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /**
     * Provides a short located at the specified path. If the value at that path does not represent a short
     * <code>0</code> is returned.
     *
     * @param path Path to the value.
     * @return Parsed short.
     *
     * @see ConfigSection#isShort(String)
     * @see ConfigSection#getShort(String, short)
     */
    public short getShort(@NotNull String path) {
        return this.getShort(path, (short) 0);
    }

    /**
     * Provides a short located at the specified path. If the path does not contain a value or the value is not a short
     * the default value is returned.
     *
     * @param path Path to the value.
     * @return Parsed short or the provided default value.
     *
     * @see ConfigSection#isShort(String)
     * @see ConfigSection#getShort(String)
     */
    public short getShort(@NotNull String path, short def) {
        try {
            return Short.parseShort(this.getString(path, ""));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /**
     * Provides an int located at the specified path. If the value at that path does not represent an integer
     * <code>0</code> is returned.
     *
     * @param path Path to the value.
     * @return Parsed int.
     *
     * @see ConfigSection#isInt(String)
     * @see ConfigSection#getInt(String, int)
     */
    public int getInt(@NotNull String path) {
        return this.getInt(path, 0);
    }

    /**
     * Provides an int located at the specified path. If the path does not contain a value or the value is not an
     * integer the default value is returned.
     *
     * @param path Path to the value.
     * @return Parsed int or the provided default value.
     *
     * @see ConfigSection#isInt(String)
     * @see ConfigSection#getInt(String)
     */
    public int getInt(@NotNull String path, int def) {
        try {
            return Integer.parseInt(this.getString(path, ""));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /**
     * Provides a long located at the specified path. If the value at that path does not represent a long <code>0</code>
     * is returned.
     *
     * @param path Path to the value.
     * @return Parsed long.
     *
     * @see ConfigSection#isLong(String)
     * @see ConfigSection#getLong(String, long)
     */
    public long getLong(@NotNull String path) {
        return this.getLong(path, 0);
    }

    /**
     * Provides a long located at the specified path. If the path does not contain a value or the value is not a long
     * the default value is returned.
     *
     * @param path Path to the value.
     * @return Parsed long or the provided default value.
     *
     * @see ConfigSection#isLong(String)
     * @see ConfigSection#getLong(String)
     */
    public long getLong(@NotNull String path, long def) {
        try {
            return Long.parseLong(this.getString(path, ""));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /**
     * Provides a float located at the specified path. If the value at that path does not represent a float
     * <code>0</code> is returned.
     *
     * @param path Path to the value.
     * @return Parsed float.
     *
     * @see ConfigSection#isFloat(String)
     * @see ConfigSection#getFloat(String, float)
     */
    public float getFloat(@NotNull String path) {
        return this.getFloat(path, (float) 0);
    }

    /**
     * Provides a float located at the specified path. If the path does not contain a value or the value is not a float
     * the default value is returned.
     *
     * @param path Path to the value.
     * @return Parsed float or the provided default value.
     *
     * @see ConfigSection#isFloat(String)
     * @see ConfigSection#getFloat(String)
     */
    public float getFloat(@NotNull String path, float def) {
        try {
            return Float.parseFloat(this.getString(path, ""));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /**
     * Provides a double located at the specified path. If the value at that path does not represent a double
     * <code>0</code> is returned.
     *
     * @param path Path to the value.
     * @return Parsed double.
     *
     * @see ConfigSection#isDouble(String)
     * @see ConfigSection#getDouble(String, double)
     */
    public double getDouble(@NotNull String path) {
        return this.getDouble(path, 0);
    }

    /**
     * Provides a double located at the specified path. If the path does not contain a value or the value is not a
     * double the default value is returned.
     *
     * @param path Path to the value.
     * @return Parsed double or the provided default value.
     *
     * @see ConfigSection#isDouble(String)
     * @see ConfigSection#getDouble(String)
     */
    public double getDouble(@NotNull String path, double def) {
        try {
            return Double.parseDouble(this.getString(path, ""));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /**
     * Provides a boolean located at the specified path. If the value at that path represented as a {@link String}
     * equals "true" (case-insensitive) <code>true</code> is returned, otherwise <code>false</code>.
     *
     * @param path Path to the value.
     * @return Parsed boolean.
     *
     * @see ConfigSection#isBoolean(String)
     */
    public boolean getBoolean(@NotNull String path) {
        return Boolean.parseBoolean(this.getString(path));
    }

    /**
     * Provides a {@link List<String>} of all keys that are contained by this section and its children.
     * @param deep Whether to provide keys from deeper levels or just the values of this section.
     * @return List of all keys this section contains.
     */
    public List<String> getKeys(boolean deep) {
        List<String> keys = new ArrayList<>();

        String fullKey = this.getPath();

        values.forEach((key, value) -> {
            if (fullKey == null) {
                keys.add(key);
            } else {
                keys.add(fullKey + "." + key);
            }
        });

        if (deep) {
            children.forEach(configSection -> keys.addAll(configSection.getKeys(true)));
        }

        return keys;
    }

    /**
     * Loads values from a YAML provided by a reader.
     * @param reader Reader that can be used to read the YAML.
     * @param clear whether to clear existing data before loading.
     */
    public void load(Reader reader, boolean clear) {
        if (clear) {
            for (String key : this.getKeys(false)) {
                this.set(key, null);
            }
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) new Yaml().load(reader);

        ConfigUtil.clearMap(map);
        map.forEach(this::set);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName()).append("[").append(this.getKey()).append("]{");

        TreeSet<String> val = new TreeSet<>();
        values.forEach((key, value)    -> val.add(key + "='" + value + "'"));
        children.forEach(configSection -> val.add(configSection.toString()));

        for (String s : val) {
            builder.append(s).append(", ");
        }

        // remove ", " from the end
        if (builder.substring(builder.length() - 2, builder.length()).equals(", ")) {
            builder.delete(builder.length() - 2, builder.length());
        }
        builder.append("}");

        return builder.toString();
    }
}
