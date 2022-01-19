package de.eldritch.Anura.core.module;

public class AnuraModuleEnableException extends RuntimeException {
    public AnuraModuleEnableException(String message) {
        super(message);
    }

    public AnuraModuleEnableException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnuraModuleEnableException(Throwable cause) {
        super(cause);
    }
}