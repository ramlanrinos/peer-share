package com.peershare.kademlia.api;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public interface RoutingTable {
    boolean addOrUpdate(Contact contact, long nowMillis);
    void markFailed(NodeId id, long nowMillis);
    void remove(NodeId id);
    Optional<Contact> find(NodeId id);
    List<Contact> closestContacts(NodeId key, int maxContacts);
    List<Contact> staleContacts(Duration maxSilence, int maxContacts);
    int size();
}
