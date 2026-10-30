
package com.peershare.kademlia.testkit;

import com.peershare.kademlia.api.Contact;
import com.peershare.kademlia.api.NodeId;
import com.peershare.kademlia.api.RoutingTable;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public class FakeRoutingTable implements RoutingTable {

    @Override
    public boolean addOrUpdate(Contact contact, long nowMillis) {
        throw new UnsupportedOperationException("Phase 1");
    }

    @Override
    public void markFailed(NodeId id, long nowMillis) {
        throw new UnsupportedOperationException("Phase 1");
    }

    @Override
    public void remove(NodeId id) {
        throw new UnsupportedOperationException("Phase 1");
    }

    @Override
    public Optional<Contact> find(NodeId id) {
        throw new UnsupportedOperationException("Phase 1");
    }

    @Override
    public List<Contact> closestContacts(
            NodeId key, int maxContacts) {
        throw new UnsupportedOperationException("Phase 1");
    }

    @Override
    public List<Contact> staleContacts(
            Duration maxSilence, int maxContacts) {
        throw new UnsupportedOperationException("Phase 1");
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("Phase 1");
    }
}
