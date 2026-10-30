package com.peershare.kademlia.api;

import java.util.concurrent.CompletableFuture;

public interface RpcGateway {
    CompletableFuture<PingResponse> ping(Contact peer, PingRequest request);
    CompletableFuture<StoreResponse> store(Contact peer, StoreRequest request);
    CompletableFuture<FindNodeResponse> findNode(Contact peer, FindNodeRequest request);
    CompletableFuture<FindValueResponse> findValue(Contact peer, FindValueRequest request);
}
