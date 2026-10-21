package com.peershare.kademlia.api;

import java.time.Duration;

/**
 * Shared Kademlia protocol constants (default profile).
 *
 * <p>Values come from {@code docs/protocol/PARAMETERS.md}. If a value changes there,
 * it must change here in the same pull request. Test-profile values belong in
 * {@code kademlia-testkit}, not in this class.
 */
public final class KademliaParams {

    /** Size of node IDs and value keys, in bits. Fixed for protocol compatibility. */
    public static final int ID_BITS = 160;

    /** Size of node IDs and value keys, in bytes ({@code ID_BITS / 8}). */
    public static final int ID_BYTES = ID_BITS / 8;

    /** Max contacts per bucket, closest-contact response limit, and replication target. */
    public static final int DEFAULT_K = 20;

    /** Max simultaneous lookup RPCs per lookup. */
    public static final int DEFAULT_ALPHA = 3;

    /** Max replacement contacts kept per bucket. */
    public static final int DEFAULT_REPLACEMENT_CACHE_SIZE = 20;

    /** Deadline for a single RPC attempt. */
    public static final Duration DEFAULT_RPC_TIMEOUT = Duration.ofSeconds(5);

    /** Additional attempts after the initial attempt (3 attempts in total). */
    public static final int DEFAULT_RETRY_COUNT = 2;

    /** Inactivity after which a contact becomes eligible for a liveness check. */
    public static final Duration DEFAULT_STALE_CONTACT_AGE = Duration.ofMinutes(5);

    /** Lifetime of a stored value. */
    public static final Duration DEFAULT_VALUE_TTL = Duration.ofHours(24);

    /** Interval at which original publishers republish their values. */
    public static final Duration DEFAULT_REPUBLISH_INTERVAL = Duration.ofHours(1);

    /** Max size of one complete serialized message (IPv4 UDP payload ceiling). */
    public static final int MAX_PACKET_BYTES = 65_507;

    private KademliaParams() {
        throw new AssertionError("No instances");
    }
}
