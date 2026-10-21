package com.peershare.kademlia.api;

/**
 * Source of time for all Kademlia logic.
 *
 * <p>Code must read time through this interface instead of calling
 * {@code System.currentTimeMillis()} directly, so tests can use a fake clock
 * and never need real sleeps.
 */
@FunctionalInterface
public interface Clock {

    /**
     * Returns the current time in milliseconds.
     *
     * @return current time in milliseconds
     */
    long nowMillis();

    /**
     * Returns a clock backed by the system wall clock.
     *
     * @return the system clock
     */
    static Clock system() {
        return System::currentTimeMillis;
    }
}
