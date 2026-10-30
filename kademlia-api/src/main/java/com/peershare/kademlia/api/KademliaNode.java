package com.peershare.kademlia.api;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface KademliaNode extends AutoCloseable {
    NodeId localNodeId();
    Contact localContact();
    void start();
    CompletableFuture<Void> join(Collection<Contact> bootstrapContacts);
    CompletableFuture<List<Contact>> lookupNode(NodeId target);
    CompletableFuture<Integer> store(NodeId key, byte[] value);
    CompletableFuture<Optional<byte[]>> get(NodeId key);

    @Override
    void close();
}
