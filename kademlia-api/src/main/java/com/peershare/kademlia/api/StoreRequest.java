package com.peershare.kademlia.api;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public record StoreRequest(
    UUID messageId, Contact sender, NodeId key, byte[] value, long ttlMillis)
    implements Message {

  public StoreRequest {
    Objects.requireNonNull(messageId, "messageId must not be null");
    Objects.requireNonNull(sender, "sender must not be null");
    Objects.requireNonNull(key, "key must not be null");
    Objects.requireNonNull(value, "value must not be null");
    value = value.clone(); // copy on input
  }

  @Override
  public byte[] value() {
    return value.clone(); // copy on output
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof StoreRequest other
        && messageId.equals(other.messageId)
        && sender.equals(other.sender)
        && key.equals(other.key)
        && Arrays.equals(value, other.value)
        && ttlMillis == other.ttlMillis;
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageId, sender, key, Arrays.hashCode(value), ttlMillis);
  }

  @Override
  public String toString() {
    return "StoreRequest[messageId=" + messageId
        + ", sender=" + sender
        + ", key=" + key
        + ", valueLength=" + value.length
        + ", ttlMillis=" + ttlMillis + "]";
  }
}