package com.peershare.kademlia.api;

import java.util.Objects;
import java.util.UUID;

public record PingRequest(UUID messageId, Contact sender) implements Message {

  public PingRequest {
    Objects.requireNonNull(messageId, "messageId must not be null");
    Objects.requireNonNull(sender, "sender must not be null");
  }
}