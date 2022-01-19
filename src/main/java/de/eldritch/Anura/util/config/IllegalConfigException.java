package de.eldritch.Anura.util.config;

/**
 * Indicates that a {@link java.io.File} does not represent a valid {@link FileConfig}.
 */
public class IllegalConfigException extends RuntimeException {
    public IllegalConfigException(String message) {
        super(message);
    }

    public IllegalConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}