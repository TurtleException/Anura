package de.eldritch.Anura.util.time;

@SuppressWarnings("unused")
public enum Offset {
    /**
     * Unix epoch - starts at <code>1970-01-01T00:00:00Z</code>. (no offset)
     */
    UNIX(0L),

    /**
     * Discord epoch - starts at <code>2015-01-01T00:00:00Z</code>.
     */
    DISCORD(1420070400000L),

    /**
     * Eldritch epoch - starts at <code>2021-01-01T00:00:00Z</code>.
     */
    ELDRITCH(1609459200000L),

    /**
     * Anura epoch - starts at <code>2022-01-01T00:00:00Z</code>.
     */
    ANURA(1640995200000L);

    final long offset;

    Offset(long millis) {
        this.offset = millis;
    }
}