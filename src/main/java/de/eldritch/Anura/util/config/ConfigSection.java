package de.eldritch.Anura.util.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Represents an individual section of a configuration.
 * <p> ConfigSections are stored in-memory.
 */
@SuppressWarnings("unused")
public class ConfigSection implements Config {
    // parent configuration, null if this is the root section
    private ConfigSection parent;

    private String key;
    private Object value;

    private final HashSet<ConfigSection> children = new HashSet<>();

    /**
     * Constructs a new root Config without any values.
     */
    public ConfigSection() {
        this(null, null);
    }

    ConfigSection(@Nullable ConfigSection parent, @Nullable String key) {
        this.key = key;
        this.parent = parent;
    }

    /* ---------- UTILITIES ---------- */

    /**
     * Provides an exact copy of this config. (keys & values)
     * @param config Config to create a copy of.
     * @return Newly created config.
     */
    public static ConfigSection copyOf(@NotNull ConfigSection config) {
        ConfigSection conf = new ConfigSection(null, null);
        conf.setAll(config.getMap(true));
        return conf;
    }

    /* ---------- MAIN GET & SET ---------- */

    @Override
    public @Nullable Object get(@NotNull String path) throws NullPointerException, IllegalArgumentException {
        if (!path.equals(""))
            ConfigUtil.validatePath(path);

        if (parent == null && path.equals(""))
            throw new IllegalArgumentException("Path may not be null on root");


        String[] keys = path.split("\\.");

        if (keys.length < 1) {
            // return value (if not null) or this config
            return Objects.requireNonNullElse(value, this);
        } else {
            // pass on to deeper section
            for (ConfigSection child : children) {
                if (child.getKey().equals(keys[0])) {
                    return child.get(path.substring(keys[0].length() + 1));
                }
            }
            return null;
        }
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) throws NullPointerException, IllegalArgumentException {
        if (!path.equals(""))
            ConfigUtil.validatePath(path);

        if (parent == null && path.equals(""))
            throw new IllegalArgumentException("Path may not be null on root");


        String[] keys = path.split("\\.");

        // determine whether this belongs here or should be passed to a deeper level
        if (keys.length < 2) {
            // set value in this section
            if (value instanceof ConfigSection conf) {
                // reassign internal values
                conf.reassign(this, keys[0]);

                // remove old config at that location
                children.removeIf(config -> config.getKey().equals(keys[0]));

                // add new config
                children.add(conf);
            } else if (value == null) {
                // delete value
                this.value = null;

                // detach children
                children.clear();
            } else {
                // remove possible config with that same key
                children.removeIf(config -> config.getKey().equals(keys[0]));

                // assign value
                this.value = value;
            }
        } else {
            // pass on to deeper section or create new one
            getSection(keys[0]).set(path.substring(keys[0].length() + 1), value);
        }
    }

    /* ------------------------- */

    @Override
    public void loadDefaults(String resource) {
        try {
            InputStream stream = ClassLoader.getSystemResourceAsStream(resource);
            if (stream == null)
                return;

            Map<String, Object> values = ConfigUtil.parseYaml(new InputStreamReader(stream));

            values.forEach((s, o) -> {
                if (this.get(s) != null)
                    this.set(s, o);
            });
        } catch (Exception ignored) { }
    }

    /* ---------- ADDITIONAL GET ---------- */

    @Override
    public <T> @Nullable T get(@NotNull String path, @NotNull Class<T> type) throws NullPointerException, IllegalArgumentException, ClassCastException {
        Object raw = this.get(path);
        if (raw == null) return null;

        return type.cast(raw);
    }

    @Override
    public @Nullable String getString(@NotNull String path) {
        try {
            return (String) this.get(path);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public @NotNull String getString(@NotNull String path, @NotNull String def) {
        String str = this.getString(path);
        return str == null ? def : str;
    }

    /* ------------------------- */

    @Override
    public boolean isByte(@NotNull String path) {
        return this.getByte(path, (byte) 0) == this.getByte(path, (byte) 1);
    }

    @Override
    public boolean isShort(@NotNull String path) {
        return this.getShort(path, (short) 0) == this.getShort(path, (short) 1);
    }

    @Override
    public boolean isInt(@NotNull String path) {
        return this.getInt(path, 0) == this.getInt(path, 1);
    }

    @Override
    public boolean isLong(@NotNull String path) {
        return this.getLong(path, 0L) == this.getLong(path, 1L);
    }

    @Override
    public boolean isFloat(@NotNull String path) {
        return this.getFloat(path, (float) 0) == this.getFloat(path, (float) 1);
    }

    @Override
    public boolean isDouble(@NotNull String path) {
        return this.getDouble(path, 0) == this.getDouble(path, 1);
    }

    @Override
    public boolean isBoolean(@NotNull String path) {
        String str = this.getString(path, "");
        return str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false");
    }

    @Override
    public boolean isType(@NotNull String path, @NotNull Class<?> type) {
        return this.get(path, type) != null;
    }

    @Override
    public byte getByte(@NotNull String path) {
        return this.getByte(path, (byte) 0);
    }

    @Override
    public byte getByte(@NotNull String path, byte def) {
        try {
            return Byte.parseByte(this.getString(path, ""));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    @Override
    public short getShort(@NotNull String path) {
        return this.getShort(path, (short) 0);
    }

    @Override
    public short getShort(@NotNull String path, short def) {
        try {
            return Short.parseShort(this.getString(path, ""));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    @Override
    public int getInt(@NotNull String path) {
        return this.getInt(path, 0);
    }

    @Override
    public int getInt(@NotNull String path, int def) {
        try {
            return Integer.parseInt(this.getString(path, ""));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    @Override
    public long getLong(@NotNull String path) {
        return this.getLong(path, 0);
    }

    @Override
    public long getLong(@NotNull String path, long def) {
        try {
            return Long.parseLong(this.getString(path, ""));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    @Override
    public float getFloat(@NotNull String path) {
        return this.getFloat(path, (float) 0);
    }

    @Override
    public float getFloat(@NotNull String path, float def) {
        try {
            return Float.parseFloat(this.getString(path, ""));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    @Override
    public double getDouble(@NotNull String path) {
        return this.getDouble(path, 0);
    }

    @Override
    public double getDouble(@NotNull String path, double def) {
        try {
            return Double.parseDouble(this.getString(path, ""));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    @Override
    public boolean getBoolean(@NotNull String path) {
        return Boolean.parseBoolean(this.getString(path));
    }


    /* ------------------------- */

    /**
     * Sets all provided values to their given paths. If a value cannot be set it will be ignored.
     * @param map Map of all values with their desired path as key.
     */
    public void setAll(@NotNull Map<String, ?> map) {
        for (String mapKey : map.keySet()) {
            try {
                this.set(mapKey, map.get(mapKey));
            } catch (NullPointerException | IllegalArgumentException ignored) { }
        }
    }

    /**
     * Parses all values this config and its children hold into a {@link Map} with their path as key.
     * @param deep Whether to provide values from deeper levels or just the values of this section.
     * @return Map of all values this config contains.
     */
    @Override
    public @NotNull Map<String, Object> getMap(boolean deep) {
        HashMap<String, Object> map = new HashMap<>();

        if (value != null) {
            map.put(this.getPath(), value);
        } else {
            for (ConfigSection child : children) {
                if (deep) {
                    map.putAll(child.getMap(true));
                } else {
                    if (child.hasValue()) {
                        map.put(child.getPath(), child.getValue());
                    }
                }
            }
        }

        return map;
    }

    /**
     * Provides a {@link List<String>} of all keys that are contained by this section and its children.
     * @param deep Whether to provide keys from deeper levels or just the values of this section.
     * @return List of all keys this config contains.
     */
    public List<String> getKeys(boolean deep) {
        List<String> keys = new ArrayList<>();

        if (value != null) {
            keys.add(this.getPath());
        } else {
            for (ConfigSection child : children) {
                if (deep) {
                    keys.addAll(child.getKeys(true));
                } else {
                    if (child.hasValue()) {
                        keys.add(child.getPath());
                    }
                }
            }
        }

        return keys;
    }

    /**
     * Checks whether this config holds a value. This is practically equivalent to checking whether
     * {@link ConfigSection#getValue()} returns <code>null</code>.
     * @return true if this config holds a value.
     */
    boolean hasValue() {
        return value != null;
    }

    /**
     * Provides the nullable value of this config. If this config only is a node with children this will return
     * <code>null</code> as a config can only either hold a value or children.
     * @return Nullable value of this config.
     */
    @Nullable Object getValue() {
        return value;
    }

    /**
     * Provides a child config located at the specified key (not recursive) or creates a new config if necessary.
     * @param key The key of the config.
     * @return (possibly new) config at that location.
     */
    ConfigSection getSection(@NotNull String key) {
        if (key.contains(".") || key.contains(":"))
            throw new IllegalArgumentException("Illegal key: '" + key + "'");

        ConfigSection section = null;
        for (ConfigSection child : children) {
            if (child.getKey().equals(key)) {
                section = child;
                break;
            }
        }

        if (section == null) {
            // section does not yet exist
            section = new ConfigSection(this, key);
            children.add(section);
        }

        return section;
    }

    /**
     * Provides a {@link ConfigSection} located at the specified path. If no section exists at this location a new one
     * will be created and returned.
     * @param path Path of the desired {@link ConfigSection}.
     * @return (possibly new) config at that location.
     * @see ConfigSection#getSection(String)
     */
    public @NotNull ConfigSection createSection(@NotNull String path) {
        if (path.equals(""))
            return this;

        ConfigUtil.validatePath(path);

        String[] keys = path.split("\\.");
        String deeperPath = path.substring(keys[0].length() + 1);

        return this.getSection(keys[0]).createSection(deeperPath);
    }

    /**
     * Used to reassign the parent and key. This is used when providing a Config as the second parameter when calling
     * {@link ConfigSection#set(String, Object)}.
     * @param parent New parent config.
     * @param key New key of this config.
     */
    void reassign(@NotNull ConfigSection parent, @NotNull String key) {
        this.parent = parent;
        this.key = key;
    }

    /* ------------------------- */

    /**
     * Returns the parent {@link ConfigSection}.
     * @return Tha parent config.
     */
    public @Nullable ConfigSection getParent() {
        return parent;
    }

    /**
     * Returns the key of this config.
     * @return the key of this config.
     * @see ConfigSection#getPath()
     */
    public @NotNull String getKey() {
        return key;
    }

    /**
     * Returns the entire path where this config is located. The path is constructed by this configs key preceded by the
     * keys of all parent configs recursively (A path looks like this: <code>root.child.thisConfig</code>).
     * @return This configs path.
     * @see ConfigSection#getKey()
     * @see ConfigSection#getParent()
     */
    public @NotNull String getPath() {
        return (parent != null ? parent.getPath() + "." : "") + getKey();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName()).append("[").append(this.getKey()).append("]{");

        if (value != null) {
            builder.append(value);
        } else {
            for (ConfigSection child : children)
                builder.append(child.toString()).append(", ");

            // remove ", " from the end
            if (!children.isEmpty())
                builder.delete(builder.length() - 2, builder.length());
        }

        return builder.append("}").toString();
    }
}
