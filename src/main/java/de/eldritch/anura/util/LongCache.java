package de.eldritch.anura.util;

import java.util.Arrays;

/**
 * A simple cache that stores a fixed amount of <code>long</code> values. When the capacity is reached the oldest value
 * will be overwritten.
 */
public class LongCache {
    private final long[] arr;
    private int pointer = 0;

    public LongCache(int capacity, long... entries) throws IllegalArgumentException {
        if (capacity <= 0)
            throw new IllegalArgumentException("Capacity must be higher than 0");

        arr = new long[capacity];

        for (long entry : entries) {
            put(entry);
        }
    }

    public synchronized void put(long l) {
        arr[pointer++] = l;

        if (pointer >= arr.length)
            pointer = 0;
    }

    public boolean contains(long l) {
        return Arrays.stream(arr).anyMatch(value -> value == l);
    }

    /**
     * Provides an exact copy of the underlying array. The new array is not guaranteed to be ordered from oldest to
     * newest due to the way values are stored.
     * @return A copy of the underlying array.
     */
    public long[] toArray() {
        long[] copy = new long[arr.length];
        System.arraycopy(arr, 0, copy, 0, arr.length);
        return copy;
    }
}
