package com.peershare.kademlia.api;

import java.util.Optional;

public interface ValueStore {
    void put(NodeId key, byte[] value, long expiresAtMillis);
    Optional<byte[]> get(NodeId key, long nowMillis);
    void remove(NodeId key);
    int purgeExpired(long nowMillis);
    int size();
}
