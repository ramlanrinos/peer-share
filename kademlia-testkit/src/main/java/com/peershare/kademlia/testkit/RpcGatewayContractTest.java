package com.peershare.kademlia.testkit;

import com.peershare.kademlia.api.Contact;
import com.peershare.kademlia.api.RpcGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Contract tests that every {@link RpcGateway} implementation must pass.
 *
 * <p>To use: extend this class in the implementation module's tests and
 * override {@link #createGateway()}, {@link #reachablePeer()} and
 * {@link #unresponsivePeer()}.
 */
@Disabled("Enabled in Phase 1")
public abstract class RpcGatewayContractTest {

    protected RpcGateway gateway;

    /** Returns a new RpcGateway instance for each test. */
    protected abstract RpcGateway createGateway();

    /** Returns a peer that answers every request. */
    protected abstract Contact reachablePeer();

    /** Returns a peer that never answers, so requests to it time out. */
    protected abstract Contact unresponsivePeer();

    @BeforeEach
    void setUp() {
        gateway = createGateway();
    }

    // --- successful round trips ---

    @Test
    void pingCompletesWithResponseMatchingRequestMessageId() {
        // TODO Phase 1
    }

    @Test
    void storeCompletesWithResponseMatchingRequestMessageId() {
        // TODO Phase 1
    }

    @Test
    void findNodeReturnsAtMostKContacts() {
        // TODO Phase 1
    }

    @Test
    void findValueReturnsEitherValueOrContactsButNotBoth() {
        // TODO Phase 1
    }

    // --- failures ---

    @Test
    void requestToUnresponsivePeerCompletesExceptionallyWithTimeout() {
        // TODO Phase 1
    }

    @Test
    void timedOutRequestIsAttemptedAtMostRetryCountPlusOneTimes() {
        // TODO Phase 1
    }

    @Test
    void errorResponseCompletesFutureExceptionallyWithoutRetry() {
        // TODO Phase 1
    }
}