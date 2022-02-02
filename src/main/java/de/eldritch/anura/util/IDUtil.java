package de.eldritch.anura.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A simple utility class to provide unique IDs for data entities or possibly other objects in the future.
 * <p>To prevent over-engineering the implementation of IDs for this project, each time the application starts, the
 * current unix system time is stored as an {@link AtomicLong} - each time a new ID is requested this number gets
 * incremented and returned.
 * <p>This is why it is <b>EXTREMELY IMPORTANT</b> to rework this implementation once more than one instance of the
 * application is running at the same time or another service is permitted to generate IDs.
 */
public class IDUtil {
    private static final AtomicLong lastId = new AtomicLong(System.currentTimeMillis());

    /**
     * Provides a new unique ID.
     * @return Newly generated unique ID
     * @see IDUtil
     */
    public synchronized static long getNewID() {
        return lastId.incrementAndGet();
    }
}
