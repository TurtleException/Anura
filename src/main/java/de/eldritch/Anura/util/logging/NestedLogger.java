package de.eldritch.Anura.util.logging;

import org.jetbrains.annotations.NotNull;

import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

/**
 * A very simple way to have an easier implementation of nested {@link Logger Loggers}. It simply disables use of parent
 * handlers and publishes {@link LogRecord LogRecords} manually after changing their message content to embed the name.
 */
public class NestedLogger extends Logger {
    public NestedLogger(@NotNull String name, @NotNull Logger parent) {
        super(name, null);

        this.setUseParentHandlers(false);

        // manually publish records to parent
        this.addHandler(new StreamHandler() {
            @Override
            public void publish(LogRecord record) {
                record.setMessage("[" + record.getLoggerName() + "] " + record.getMessage());
                parent.log(record);
            }
        });
    }
}
