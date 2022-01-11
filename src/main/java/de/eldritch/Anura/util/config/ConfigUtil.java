package de.eldritch.Anura.util.config;

import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.Reader;
import java.util.Map;
import java.util.Set;

/**
 * A simple class for a few utils
 */
public class ConfigUtil {
    /**
     * Checks whether the provided {@link String} is a valid path.
     * <p>A path is valid if and only if all the following criteria are met:
     * <li> The path is not <code>null</code> or an empty String.
     * <li> The path does not contain "<code>..</code>" - This would conflict with the path structure as it implies a
     * <code>null</code> key.
     * <li> The path does not contain '<code>:</code>' - This would conflict with the object assignment structure.
     * <li> The path does not contain '<code>#</code>' - This would conflict with the comment structure of YAML.
     * <li> The path does not start or end with '<code>.</code>' - For the same reason why '<code>..</code>' is not
     * allowed.
     * <li> All of its keys are valid.
     * @param path String to check.
     * @throws NullPointerException if the provided path or one of its keys is null.
     * @throws IllegalArgumentException if one of the other criteria is not met or a key does not meet the criteria for
     *                                  a valid key.
     */
    static void validatePath(String path) throws NullPointerException, IllegalArgumentException {
        if (path == null)
            throw new NullPointerException("Path may not be null");

        if (path.equals(""))
            throw new IllegalArgumentException("Path may not be empty string");

        if (path.contains(".."))
            throw new IllegalArgumentException("Path may not contain '..'");

        if (path.startsWith("."))
            throw new IllegalArgumentException("Path may not start with '.'");

        if (path.endsWith("."))
            throw new IllegalArgumentException("Path may not end with '.'");


        String[] keys = path.split("\\.");
        for (String key : keys) {
            try {
                validateKey(key);
            } catch (NullPointerException | IllegalArgumentException e) {
                throw new IllegalArgumentException("Path contains an invalid key: '" + key + "'");
            }
        }
    }

    /**
     * Checks whether the provided {@link String} is a valid key.
     * <p>A key is valid if and only if all the following criteria are met:
     * <li> The key is not <code>null</code> or an empty String.
     * <li> The key does not contain "<code>.</code>" - This would conflict with the path structure.
     * <li> The key does not contain '<code>:</code>' - This would conflict with the object assignment structure.
     * <li> The key does not contain '<code>#</code>' - This would conflict with the comment structure of YAML.
     * @param key String to check.
     * @throws NullPointerException if the provided key is null.
     * @throws IllegalArgumentException if one of the other criteria is not met.
     */
    static void validateKey(String key) throws NullPointerException, IllegalArgumentException {
        if (key == null)
            throw new NullPointerException("Key may not be null");

        if (key.equals(""))
            throw new IllegalArgumentException("Key may not be empty string");

        if (key.contains("\\."))
            throw new IllegalArgumentException("Key may not contain ':'");

        if (key.contains(":"))
            throw new IllegalArgumentException("Key may not contain '.'");

        if (key.contains("#"))
            throw new IllegalArgumentException("Key may not contain '#'");
    }

    /**
     * Flattens the provided map. All other instances of {@link Map Map<?, ?>} will be moved into the upper layer with
     * their keys extended to match their entire path.
     * @param map Map to clear.
     */
    static void clearMap(@NotNull Map<String, Object> map) {
        boolean loop = true;
        while (loop) {
            loop = false;
            for (Map.Entry<String, Object> entry : Set.copyOf(map.entrySet())) {
                if (entry.getValue() instanceof Map<?, ?> mapObj) {
                    map.remove(entry.getKey());
                    mapObj.forEach((o, o2) -> map.put(entry.getKey() + "." + o, o2));
                    loop = true;
                }
            }
        }
    }

    /**
     * Loads values from a YAML provided by a reader.
     * @param reader Reader that can be used to read the YAML.
     */
    public static Map<String, Object> parseYaml(@NotNull Reader reader) {
        @SuppressWarnings({"unchecked", "RedundantCast"})
        Map<String, Object> map = (Map<String, Object>) ((Map<String, ?>) new Yaml().load(reader));
        ConfigUtil.clearMap(map);
        return map;
    }
}
