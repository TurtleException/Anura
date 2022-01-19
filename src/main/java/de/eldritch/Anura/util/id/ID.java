package de.eldritch.Anura.util.id;

import de.eldritch.Anura.util.time.Offset;
import de.eldritch.Anura.util.time.Time;
import org.jetbrains.annotations.NotNull;

public class ID {
    private final Time timestamp;
    private final long increment;

    ID(@NotNull Time timestamp, long increment) {
        this.timestamp = timestamp;
        this.increment = increment;
    }

    public @NotNull Time getTimestamp() {
        return timestamp;
    }

    public long getIncrement() {
        return increment;
    }

    @Override
    public String toString() {
        return timestamp.toMillis(Offset.ANURA) + "-" + increment;
    }

    public synchronized static ID of(String str) throws IllegalArgumentException {
        if (str == null || str.equals(""))
            throw new IllegalArgumentException("Not a valid ID");

        String[] tokens = str.split("-");

        if (tokens.length != 2)
            throw new IllegalArgumentException("Not a valid ID");

        try {
            return new ID(Time.of(Long.parseLong(tokens[0])), Integer.parseInt(tokens[1]));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Not a valid ID", e);
        }
    }
}
