package com.peershare.kademlia.testkit;

import com.peershare.kademlia.api.NodeId;
import com.peershare.kademlia.api.ValueStore;

import java.util.Optional;

public class FakeValueStore implements ValueStore {

    @Override
    public void put(
            NodeId key,
            byte[] value,
            long expiresAtMillis) {
        throw new UnsupportedOperationException("Phase 1");
    }

    @Override
    public Optional<byte[]> get(
            NodeId key, long nowMillis) {
        throw new UnsupportedOperationException("Phase 1");
    }

    @Override
    public void remove(NodeId key) {
        throw new UnsupportedOperationException("Phase 1");
    }

    @Override
    public int purgeExpired(long nowMillis) {
        throw new UnsupportedOperationException("Phase 1");
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("Phase 1");
    }
}
