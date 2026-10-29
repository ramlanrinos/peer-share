package com.peershare.kademlia.api;

import java.util.Objects;
import java.util.UUID;

public record PingResponse(UUID messageId, Contact sender) implements Message {

  public PingResponse {
    Objects.requireNonNull(messageId, "messageId must not be null");
    Objects.requireNonNull(sender, "sender must not be null");
  }
}