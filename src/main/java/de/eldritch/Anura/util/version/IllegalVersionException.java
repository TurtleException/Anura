package de.eldritch.Anura.util.version;

public class IllegalVersionException extends RuntimeException {
    public IllegalVersionException(String raw, Throwable cause) {
        super("'" + raw + "' could not be converted to Version", cause);
    }

    public IllegalVersionException(String msg) {
        super(msg);
    }
}