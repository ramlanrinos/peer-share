package com.peershare.kademlia.api;

import java.util.Objects;
import java.util.UUID;

public record StoreResponse(UUID messageId, Contact sender, boolean accepted)
    implements Message {

  public StoreResponse {
    Objects.requireNonNull(messageId, "messageId must not be null");
    Objects.requireNonNull(sender, "sender must not be null");
  }
}