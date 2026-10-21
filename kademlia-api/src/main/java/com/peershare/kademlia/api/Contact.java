package com.peershare.kademlia.api;

import java.util.Objects;

/**
 * Network address of a node: its ID plus host and UDP port.
 *
 * @param id   the node ID
 * @param host hostname or IP address; must not be blank
 * @param port UDP port, 1 to 65535
 */
public record Contact(NodeId id, String host, int port) {

    /** Lowest valid port number. */
    public static final int MIN_PORT = 1;

    /** Highest valid port number. */
    public static final int MAX_PORT = 65_535;

    /**
     * Validates the contact fields.
     */
    public Contact {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(host, "host must not be null");
        if (host.isBlank()) {
            throw new IllegalArgumentException("host must not be blank");
        }
        if (port < MIN_PORT || port > MAX_PORT) {
            throw new IllegalArgumentException(
                    "port must be between " + MIN_PORT + " and " + MAX_PORT + ", got " + port);
        }
    }
}
