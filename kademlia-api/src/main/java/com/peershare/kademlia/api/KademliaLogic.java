package com.peershare.kademlia.api;

public interface KademliaLogic {
    PingResponse handle(PingRequest request);
    StoreResponse handle(StoreRequest request);
    FindNodeResponse handle(FindNodeRequest request);
    FindValueResponse handle(FindValueRequest request);
}
