package com.peershare.kademlia.api;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record FindNodeResponse(UUID messageId, Contact sender, List<Contact> closestContacts)
    implements Message {

  public FindNodeResponse {
    Objects.requireNonNull(messageId, "messageId must not be null");
    Objects.requireNonNull(sender, "sender must not be null");
    Objects.requireNonNull(closestContacts, "closestContacts must not be null");
    closestContacts = List.copyOf(closestContacts); // unmodifiable copy
  }
}