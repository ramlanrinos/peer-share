package com.peershare.kademlia.testkit;

import com.peershare.kademlia.api.NodeId;
import com.peershare.kademlia.api.RoutingTable;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Contract tests that every {@link RoutingTable} implementation must pass.
 *
 * <p>To use: extend this class in the implementation module's tests and
 * override {@link #createRoutingTable(NodeId, int)}.
 */
@Disabled("Enabled in Phase 1")
public abstract class RoutingTableContractTest {

    /** Bucket size used by contract tests (test profile in PARAMETERS.md). */
    protected static final int TEST_K = 3;

    /**
     * Returns a new, empty RoutingTable owned by the given local node.
     * The local id and K are passed in so tests control XOR distances
     * and when a bucket becomes full.
     */
    protected abstract RoutingTable createRoutingTable(NodeId localId, int k);

    // --- addOrUpdate ---

    @Test
    void addOrUpdateAddsNewContact() {
        // TODO Phase 1
    }

    @Test
    void addOrUpdateRefreshesExistingContactInsteadOfDuplicating() {
        // TODO Phase 1
    }

    @Test
    void addOrUpdateIgnoresLocalNode() {
        // TODO Phase 1
    }

    @Test
    void addOrUpdateReturnsFalseWhenBucketIsFull() {
        // TODO Phase 1
    }

    // --- find / remove / markFailed ---

    @Test
    void findReturnsEmptyForUnknownNode() {
        // TODO Phase 1
    }

    @Test
    void removeDeletesContact() {
        // TODO Phase 1
    }

    @Test
    void markFailedOnUnknownNodeHasNoEffect() {
        // TODO Phase 1
    }

    // --- closestContacts ---

    @Test
    void closestContactsAreOrderedByXorDistanceToKey() {
        // TODO Phase 1
    }

    @Test
    void closestContactsReturnsAtMostMaxContacts() {
        // TODO Phase 1
    }

    @Test
    void closestContactsReturnsAllWhenFewerThanMaxExist() {
        // TODO Phase 1
    }

    @Test
    void closestContactsReturnsImmutableSnapshot() {
        // TODO Phase 1
    }

    // --- staleContacts ---

    @Test
    void staleContactsReturnsOnlyContactsSilentLongerThanMaxSilence() {
        // TODO Phase 1
    }

    @Test
    void staleContactsReturnsAtMostMaxContacts() {
        // TODO Phase 1
    }

    // --- size ---

    @Test
    void sizeReflectsNumberOfContacts() {
        // TODO Phase 1
    }
}