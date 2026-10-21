package com.peershare.kademlia.api;

import java.util.Arrays;
import java.util.Objects;

/**
 * Immutable 160-bit identifier for nodes and value keys.
 *
 * <p>Phase 0 skeleton: the public API is fixed here; methods marked
 * "Phase 1" are implemented by the CORE tasks (CORE-01 to CORE-05).
 * Construction, validation, {@code equals} and {@code hashCode} work now so
 * that records such as {@link Contact} behave correctly.
 */
public final class NodeId implements Comparable<NodeId> {

    /** Length of a node ID in bytes. */
    public static final int LENGTH_BYTES = KademliaParams.ID_BYTES;

    private final byte[] bytes;

    private NodeId(byte[] bytes) {
        Objects.requireNonNull(bytes, "bytes must not be null");
        if (bytes.length != LENGTH_BYTES) {
            throw new IllegalArgumentException(
                    "NodeId must be " + LENGTH_BYTES + " bytes, got " + bytes.length);
        }
        this.bytes = bytes.clone();
    }

    /**
     * Creates a NodeId from exactly {@link #LENGTH_BYTES} bytes (big-endian).
     *
     * @param bytes the raw ID bytes; copied defensively
     * @return the NodeId
     */
    public static NodeId fromBytes(byte[] bytes) {
        return new NodeId(bytes);
    }

    /**
     * Parses a 40-character lowercase hex string.
     *
     * @param hex the hex string
     * @return the NodeId
     */
    public static NodeId fromHex(String hex) {
        // Phase 1: CORE-02
        throw new UnsupportedOperationException("Phase 1: CORE-02");
    }

    /**
     * Generates a random NodeId using a secure random source.
     *
     * @return a random NodeId
     */
    public static NodeId random() {
        // Phase 1: CORE-02
        throw new UnsupportedOperationException("Phase 1: CORE-02");
    }

    /**
     * Returns a copy of the raw ID bytes.
     *
     * @return a defensive copy of the bytes
     */
    public byte[] bytes() {
        return bytes.clone();
    }

    /**
     * Returns the ID as a 40-character lowercase hex string.
     *
     * @return the hex form
     */
    public String hex() {
        // Phase 1: CORE-01
        throw new UnsupportedOperationException("Phase 1: CORE-01");
    }

    /**
     * Returns the XOR distance between this ID and another.
     *
     * @param other the other ID
     * @return the XOR distance as a NodeId
     */
    public NodeId xor(NodeId other) {
        // Phase 1: CORE-03
        throw new UnsupportedOperationException("Phase 1: CORE-03");
    }

    /**
     * Returns the number of leading bits shared with another ID.
     *
     * @param other the other ID
     * @return common prefix length, 0 to {@link KademliaParams#ID_BITS}
     */
    public int xorPrefixLength(NodeId other) {
        // Phase 1: CORE-04
        throw new UnsupportedOperationException("Phase 1: CORE-04");
    }

    /**
     * Compares IDs as unsigned 160-bit integers.
     *
     * @param other the other ID
     * @return negative, zero or positive
     */
    @Override
    public int compareTo(NodeId other) {
        // Phase 1: CORE-01
        throw new UnsupportedOperationException("Phase 1: CORE-01");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NodeId other)) {
            return false;
        }
        return Arrays.equals(bytes, other.bytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }

    @Override
    public String toString() {
        return "NodeId[" + LENGTH_BYTES + " bytes]";
    }
}
