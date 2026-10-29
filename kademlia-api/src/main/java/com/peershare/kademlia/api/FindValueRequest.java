package com.peershare.kademlia.api;

import java.util.Objects;
import java.util.UUID;

public record FindValueRequest(UUID messageId, Contact sender, NodeId target)
    implements Message {

  public FindValueRequest {
    Objects.requireNonNull(messageId, "messageId must not be null");
    Objects.requireNonNull(sender, "sender must not be null");
    Objects.requireNonNull(target, "target must not be null");
  }
}