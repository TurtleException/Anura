package de.eldritch.Anura.util.config;

/**
 * Indicates that a {@link java.io.File} does not represent a valid {@link FileConfig}.
 */
public class IllegalConfigException extends Exception {
    public IllegalConfigException(String message) {
        super(message);
    }
}
