package com.peershare.kademlia.api;

import java.util.Objects;
import java.util.UUID;

public record FindNodeRequest(UUID messageId, Contact sender, NodeId target)
    implements Message {

  public FindNodeRequest {
    Objects.requireNonNull(messageId, "messageId must not be null");
    Objects.requireNonNull(sender, "sender must not be null");
    Objects.requireNonNull(target, "target must not be null");
  }
}