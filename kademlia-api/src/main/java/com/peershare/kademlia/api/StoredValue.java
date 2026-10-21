package com.peershare.kademlia.api;

import java.util.Arrays;
import java.util.Objects;

/**
 * A value held in a node's value store.
 *
 * <p>Times are absolute milliseconds from the injected {@link Clock}. Per
 * {@code PARAMETERS.md}, a value is expired when {@code now >= expiresAtMillis}.
 *
 * @param key             the value key
 * @param value           the value bytes; non-empty, copied defensively
 * @param createdAtMillis time the value was received
 * @param expiresAtMillis time the value expires; not before {@code createdAtMillis}
 */
public record StoredValue(NodeId key, byte[] value, long createdAtMillis, long expiresAtMillis) {

    /**
     * Validates fields and copies the value bytes.
     */
    public StoredValue {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(value, "value must not be null");
        if (value.length == 0) {
            throw new IllegalArgumentException("value must not be empty");
        }
        if (expiresAtMillis < createdAtMillis) {
            throw new IllegalArgumentException("expiresAtMillis must not be before createdAtMillis");
        }
        value = value.clone();
    }

    /**
     * Returns a copy of the value bytes.
     *
     * @return a defensive copy of the value
     */
    @Override
    public byte[] value() {
        return value.clone();
    }

    /**
     * Returns whether this value has expired at the given time.
     *
     * @param nowMillis current time from the injected clock
     * @return true if {@code nowMillis >= expiresAtMillis}
     */
    public boolean isExpired(long nowMillis) {
        return nowMillis >= expiresAtMillis;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StoredValue other)) {
            return false;
        }
        return createdAtMillis == other.createdAtMillis
                && expiresAtMillis == other.expiresAtMillis
                && key.equals(other.key)
                && Arrays.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(key, createdAtMillis, expiresAtMillis);
        return 31 * result + Arrays.hashCode(value);
    }

    @Override
    public String toString() {
        return "StoredValue[key=" + key + ", valueBytes=" + value.length
                + ", createdAtMillis=" + createdAtMillis + ", expiresAtMillis=" + expiresAtMillis + "]";
    }
}
