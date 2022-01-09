package de.eldritch.Anura.util.config;

/**
 * A simple class for a few utils
 */
class ConfigUtil {
    /**
     * Checks whether the provided String is a valid path.
     * <p>A path is valid if and only if all the following criteria are met:
     * <li> The path is not <code>null</code>.
     * <li> The path does not contain "<code>..</code>" - This would conflict with the path structure as it implies
     * <code>null</code> key.
     * <li> The path does not contain '<code>:</code>' - This would conflict with the object assignment structure.
     *
     * @param key String to check.
     * @return true if the provided String represents a valid path.
     */
    public static boolean validatePath(String key) {
        if (key == null)
            return false;

        if (key.contains(".."))
            return false;

        if (key.contains(":"))
            return false;

        return true;
    }
}
