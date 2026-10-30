package com.peershare.kademlia.testkit;

import com.peershare.kademlia.api.Contact;
import com.peershare.kademlia.api.FindNodeRequest;
import com.peershare.kademlia.api.FindNodeResponse;
import com.peershare.kademlia.api.FindValueRequest;
import com.peershare.kademlia.api.FindValueResponse;
import com.peershare.kademlia.api.PingRequest;
import com.peershare.kademlia.api.PingResponse;
import com.peershare.kademlia.api.RpcGateway;
import com.peershare.kademlia.api.StoreRequest;
import com.peershare.kademlia.api.StoreResponse;

import java.util.concurrent.CompletableFuture;

public class FakeRpcGateway implements RpcGateway {

    @Override
    public CompletableFuture<PingResponse> ping(
            Contact peer, PingRequest request) {
        throw new UnsupportedOperationException("Phase 1");
    }

    @Override
    public CompletableFuture<StoreResponse> store(
            Contact peer, StoreRequest request) {
        throw new UnsupportedOperationException("Phase 1");
    }

    @Override
    public CompletableFuture<FindNodeResponse> findNode(
            Contact peer, FindNodeRequest request) {
        throw new UnsupportedOperationException("Phase 1");
    }

    @Override
    public CompletableFuture<FindValueResponse> findValue(
            Contact peer, FindValueRequest request) {
        throw new UnsupportedOperationException("Phase 1");
    }
}
