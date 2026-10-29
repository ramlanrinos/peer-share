package com.peershare.kademlia.api;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record FindValueResponse(
    UUID messageId, Contact sender, boolean hasValue, byte[] value, List<Contact> closestContacts)
    implements Message {

  public FindValueResponse {
    Objects.requireNonNull(messageId, "messageId must not be null");
    Objects.requireNonNull(sender, "sender must not be null");

    if (hasValue && value == null) {
      throw new IllegalArgumentException("value must be present when hasValue is true");
    }
    if (!hasValue && value != null) {
      throw new IllegalArgumentException("value must be null when hasValue is false");
    }

    value = (value == null) ? null : value.clone(); // copy on input
    closestContacts = (closestContacts == null) ? List.of() : List.copyOf(closestContacts);
  }

  @Override
  public byte[] value() {
    return value == null ? null : value.clone(); // copy on output
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof FindValueResponse other
        && messageId.equals(other.messageId)
        && sender.equals(other.sender)
        && hasValue == other.hasValue
        && Arrays.equals(value, other.value)
        && closestContacts.equals(other.closestContacts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageId, sender, hasValue, Arrays.hashCode(value), closestContacts);
  }

  @Override
  public String toString() {
    return "FindValueResponse[messageId=" + messageId
        + ", sender=" + sender
        + ", hasValue=" + hasValue
        + ", valueLength=" + (value == null ? 0 : value.length)
        + ", closestContacts=" + closestContacts + "]";
  }
}