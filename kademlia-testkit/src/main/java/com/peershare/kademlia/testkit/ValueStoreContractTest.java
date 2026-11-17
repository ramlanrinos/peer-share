package com.peershare.kademlia.testkit;

import com.peershare.kademlia.api.ValueStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Contract tests that every {@link ValueStore} implementation must pass.
 *
 * <p>To use: extend this class in the implementation module's tests and
 * override {@link #createValueStore()}.
 */
@Disabled("Enabled in Phase 1")
public abstract class ValueStoreContractTest {

    protected ValueStore store;

    /** Returns a new, empty ValueStore instance for each test. */
    protected abstract ValueStore createValueStore();

    @BeforeEach
    void setUp() {
        store = createValueStore();
    }

    @Test
    void getReturnsValuePreviouslyPut() {
        // TODO Phase 1
    }

    @Test
    void getReturnsEmptyForUnknownKey() {
        // TODO Phase 1
    }

    @Test
    void putOverwritesExistingValueForSameKey() {
        // TODO Phase 1
    }

    @Test
    void getReturnsValueJustBeforeExpiry() {
        // TODO Phase 1
    }

    @Test
    void getReturnsEmptyAtExpiryTime() {
        // TODO Phase 1
    }

    @Test
    void removeDeletesValue() {
        // TODO Phase 1
    }

    @Test
    void purgeExpiredRemovesOnlyExpiredEntriesAndReturnsCount() {
        // TODO Phase 1
    }

    @Test
    void sizeReflectsNumberOfStoredEntries() {
        // TODO Phase 1
    }
}