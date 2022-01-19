package de.eldritch.Anura.util.time;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;

@SuppressWarnings("unused")
public class Time {
    // millis are stored in UNIX time
    private final long millis;

    private Time(long millis, @NotNull Offset type) {
        this.millis = millis - type.offset;
    }

    /**
     * Provides the timestamp represented by this object in milliseconds.
     * @param offset The offset of the timestamp (e.g. {@link Offset#UNIX} for a plain unix timestamp).
     * @return The millisecond value of this timestamp.
     */
    public long toMillis(@NotNull Offset offset) {
        return millis + offset.offset;
    }

    /**
     * Provides the timestamp represented by this object in milliseconds with no offset.
     * @return The unix millisecond value of this timestamp.
     */
    public long toMillis() {
        return this.toMillis(Offset.UNIX);
    }

    public Time plus(long millis) {
        return new Time(this.millis + millis, Offset.UNIX);
    }

    /**
     * Provides a {@link Time} object representing the current timestamp.
     * <p>
     *     This method retrieves the current time in millis first and passes them
     *     on manually to prevent delay caused by instantiating the {@link Time}
     *     object. This way the timestamp is as accurate as possible.
     * </p>
     */
    public static Time now() {
        long l = System.currentTimeMillis();
        return new Time(l, Offset.UNIX);
    }

    public static Time of(long millis) {
        return of(millis, Offset.UNIX);
    }

    public static Time of(long millis, @NotNull Offset offset) {
        return new Time(millis, offset);
    }

    public static Time of(@NotNull Instant instant) {
        return of(instant.toEpochMilli(), Offset.UNIX);
    }
}
