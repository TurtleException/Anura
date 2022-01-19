package de.eldritch.Anura.util.time;

@SuppressWarnings("unused")
public class Duration {
    private final long millis;
    private final Time start;

    Duration(long millis) throws IllegalArgumentException {
        this(null, millis);
    }

    Duration(Time start, long millis) throws IllegalArgumentException {
        if (millis < 0)
            throw new IllegalArgumentException("Duration may not be negative");

        this.start = start;
        this.millis = millis;
    }

    /**
     * Provides the duration represented by this object in milliseconds.
     * @return The millisecond value of this duration.
     */
    public long toMillis() {
        return millis;
    }

    public Time getStart() throws IllegalStateException {
        if (start == null)
            throw new IllegalStateException("Relative duration does not have set start");

        return start;
    }

    public Time getEnd() throws IllegalStateException {
        if (start == null)
            throw new IllegalStateException("Relative duration does not have set end");

        return start.plus(millis);
    }

    /* -------------------- */

    public static Duration of(long millis) throws IllegalArgumentException {
        return new Duration(millis);
    }

    public static Duration of(Time start, long millis) {
        if (millis < 0) {
            return new Duration(start.plus(millis), millis);
        } else {
            return new Duration(start, millis);
        }
    }
}
