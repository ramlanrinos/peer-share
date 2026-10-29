package com.peershare.kademlia.api;

import java.util.Objects;
import java.util.UUID;

public record ErrorResponse(UUID messageId, Contact sender, String reason)
    implements Message {

  public ErrorResponse {
    Objects.requireNonNull(messageId, "messageId must not be null");
    Objects.requireNonNull(sender, "sender must not be null");
    Objects.requireNonNull(reason, "reason must not be null");
  }
}