   package com.peershare.kademlia.api;

   import java.util.UUID;

   public sealed interface Message
       permits PingRequest, PingResponse,
               StoreRequest, StoreResponse,
               FindNodeRequest, FindNodeResponse,
               FindValueRequest, FindValueResponse,
               ErrorResponse {

     UUID messageId();

     Contact sender();
   }