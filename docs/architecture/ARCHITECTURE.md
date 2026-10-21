# Kademlia Java Implementation — Architecture Document

> **Status:** Phase 0 — Frozen on approval  
> **Owner:** Whole Group  
> **Last Updated:** 2026-10-22

---

## 1. Overview

This project is an **incremental, modular implementation of the Kademlia Distributed Hash Table (DHT)** in Java 21. The implementation is split across five Maven modules so that two sub-teams — **Team CORE** (algorithmic primitives) and **Team NET** (transport and RPC runtime) — can work **fully in parallel** without blocking each other.

The architecture enforces a strict separation between **logic** and **transport** through a shared API module (`kademlia-api`). All cross-module communication happens only through interfaces defined in this API module.

### Design Principles

1. **Dependency isolation** — `kademlia-core` and `kademlia-net` never import each other.
2. **Interface-driven design** — All cross-module contracts are expressed as Java interfaces or records in `kademlia-api`.
3. **Immutability by default** — DTOs, messages, and identifiers are immutable records or defensive-copy wrappers.
4. **Deterministic testing** — Time is injected via a `Clock` interface; transport is injected via `RpcGateway` and transport abstractions.
5. **Fail-soft, never crash** — Handler exceptions are caught and logged; they never bring down the node.
6. **Incremental delivery** — Each phase produces a runnable, testable increment.

---

## 2. Modules

The project is a Maven multi-module build. Every module has a single, well-defined responsibility.

```text
kademlia/
├── kademlia-api/        # Shared interfaces, DTOs, message records, constants
├── kademlia-core/       # Kademlia logic: routing table, k-buckets, value store, lookup
├── kademlia-net/        # Serialization, UDP/in-memory transport, RPC gateway, dispatcher
├── kademlia-testkit/    # Fake clock, fake stores, fake RPC gateway, contract tests
├── kademlia-node/       # Composition root, CLI, final wiring, demo application
└── pom.xml              # Parent POM with shared dependency management
```

### Module Responsibilities

| Module | Responsibility | Typical Contents |
|---|---|---|
| `kademlia-api` | The **contract** between all other modules. Frozen after Phase 0. | `NodeId`, `Contact`, `StoredValue`, `Message` records, `RpcGateway`, `KademliaLogic`, `RoutingTable`, `ValueStore`, `KademliaNode`, `Clock`, `KademliaParams` |
| `kademlia-core` | Algorithmic primitives. Owns state and logic. | `DefaultRoutingTable`, `KBucket`, `InMemoryValueStore`, `DefaultKademliaLogic`, iterative lookup, message handlers |
| `kademlia-net` | Wire-level concerns. Owns bytes on the network. | `JsonMessageSerializer`, `UdpTransport`, `InMemoryTransport`, `UdpRpcGateway`, `MessageDispatcher`, timeout/retry logic |
| `kademlia-testkit` | Test doubles used by both teams. | `FakeClock`, `FakeRoutingTable`, `FakeValueStore`, `FakeRpcGateway`, `EchoKademliaLogic`, contract test skeletons |
| `kademlia-node` | Composition root. The only module that wires everything together. | `Main`, CLI commands, node factory, demo scripts |

---

## 3. Dependency Rules

These rules are **mandatory and machine-enforced**. Violations break parallel development.

### Allowed Dependencies

```text
kademlia-core    → kademlia-api
kademlia-net     → kademlia-api
kademlia-testkit → kademlia-api
kademlia-node    → kademlia-core, kademlia-net, kademlia-api, kademlia-testkit (test scope)
```

### Forbidden Dependencies

```text
❌ kademlia-core  MUST NOT depend on kademlia-net
❌ kademlia-net   MUST NOT depend on kademlia-core
```

### Why This Matters

*   **Team CORE** can develop and test routing, storage, and lookup using only `FakeRpcGateway` from `kademlia-testkit`. They never need a real UDP socket.
*   **Team NET** can develop and test serialization, transport, and RPC correlation using only `EchoKademliaLogic` from `kademlia-testkit`. They never need a real routing table.
*   **Integration** happens exactly once, in `kademlia-node`, where the real implementations are wired together.

### Enforcement

The forbidden dependency rule is enforced via:
1.  **Maven Enforcer Plugin** with `bannedDependencies` rules (configured in the parent POM).
2.  **ArchUnit tests** (added in Phase 4) that fail the build on illegal imports.
3.  **Code review** — reviewers must reject any PR that introduces a forbidden import.

---

## 4. Team Ownership

| Area | Owner | Notes |
|---|---|---|
| Shared API (`kademlia-api`) | **Whole Group** | Frozen after Phase 0. Changes require group approval. |
| Routing table, k-buckets, value store, lookup | **Team CORE** | Algorithmic primitives |
| Serialization, UDP transport, RPC gateway, dispatcher | **Team NET** | Wire-level concerns |
| Protocol and message specification | **Whole Group** (NET lead drafts) | Documented in `docs/protocol/PROTOCOL.md` |
| Kademlia parameter constants | **Whole Group** (CORE lead drafts) | Documented in `docs/protocol/PARAMETERS.md` |
| Test doubles and contract tests | **Whole Group** (testkit owner drafts) | `kademlia-testkit` |
| Final node wiring, CLI, demo | **Whole Group** | `kademlia-node` |
| Build system, CI, formatting | **Build Owner** | Parent POM, GitHub Actions |
| Documentation structure | **Documentation Owner** | `docs/` folder |

---

## 5. High-Level Architecture Diagram

```text
                        ┌─────────────────────────────────┐
                        │         kademlia-node           │
                        │   (Composition root / CLI)      │
                        └──────────────┬──────────────────┘
                                       │ wires together
                       ┌───────────────┴───────────────┐
                       ▼                               ▼
          ┌────────────────────────┐       ┌────────────────────────┐
          │    kademlia-core       │       │     kademlia-net       │
          │                        │       │                        │
          │  RoutingTable          │       │  MessageSerializer     │
          │  KBucket               │       │  UdpTransport          │
          │  ValueStore            │       │  InMemoryTransport     │
          │  KademliaLogic         │       │  RpcGateway            │
          │  Lookup / Store / Get  │       │  MessageDispatcher     │
          │  Message handlers      │       │  Timeout / Retry       │
          └───────────┬────────────┘       └───────────┬────────────┘
                      │                                │
                      │ implements / uses              │ implements / uses
                      ▼                                ▼
          ┌─────────────────────────────────────────────────────────┐
          │                    kademlia-api                         │
          │  NodeId, Contact, StoredValue, Message records,         │
          │  RpcGateway, KademliaLogic, RoutingTable, ValueStore,   │
          │  KademliaNode, Clock, KademliaParams                    │
          └─────────────────────────────────────────────────────────┘
                      ▲
                      │ uses in tests
                      │
          ┌────────────────────────┐
          │    kademlia-testkit    │
          │  FakeClock             │
          │  FakeRoutingTable      │
          │  FakeValueStore        │
          │  FakeRpcGateway        │
          │  EchoKademliaLogic     │
          │  Contract test skels   │
          └────────────────────────┘
```

---

## 6. Data Flow for RPC Messages

Every RPC follows the same request/response lifecycle. This flow is the single most important contract in the system.

### Outgoing Request (Client Side)

```text
KademliaLogic  ──(1)──▶  RpcGateway.findNode(peer, request)
                              │
                              ▼
                         MessageSerializer.serialize(request) → byte[]
                              │
                              ▼
                         Transport.send(peer, bytes)
                              │
                              ▼
                         (UDP socket / in-memory channel)
```

### Incoming Request (Server Side)

```text
(UDP socket / in-memory channel)
      │
      ▼
Transport receives byte[]
      │
      ▼
MessageDispatcher.dispatch(bytes)
      │
      ├──▶ MessageSerializer.deserialize(bytes) → Message
      │
      ├──▶ Route by message type:
      │       PingRequest      → KademliaLogic.handle(PingRequest)
      │       StoreRequest     → KademliaLogic.handle(StoreRequest)
      │       FindNodeRequest  → KademliaLogic.handle(FindNodeRequest)
      │       FindValueRequest → KademliaLogic.handle(FindValueRequest)
      │
      └──▶ MessageSerializer.serialize(response) → byte[]
              │
              ▼
         Transport.send(requester, bytes)
```

### Response Correlation

*   Every request carries a **unique `UUID messageId`**.
*   The `RpcGateway` stores a `CompletableFuture` keyed by `messageId`.
*   When a response arrives, the dispatcher completes the matching future.
*   Duplicate or late responses for unknown `messageId`s are **silently dropped**.
*   Timeouts complete the future exceptionally with a `TimeoutException`.

---

## 7. Threading and Concurrency Model

### Core Assumptions

1.  **All shared state is thread-safe.** `RoutingTable`, `ValueStore`, and the `RpcGateway` pending-request map must be safe for concurrent access.
2.  **Handlers must not block.** `KademliaLogic.handle(...)` methods must return quickly. Long-running work (e.g., iterative lookup) uses `CompletableFuture` chains.
3.  **Transport is asynchronous.** The UDP receive loop runs on a dedicated daemon thread; incoming packets are handed off to a worker thread pool in the dispatcher.
4.  **No `System.currentTimeMillis()` in logic.** All time-sensitive code uses the injected `Clock` interface, making tests deterministic.
5.  **Monotonic time for timeouts.** RPC timeouts use `System.nanoTime()` (or the `Clock` abstraction) to avoid wall-clock jumps.

### Thread Layout at Runtime

| Thread | Owner | Responsibility |
|---|---|---|
| Main thread | `kademlia-node` | CLI, bootstrap, user commands |
| UDP receive thread | `kademlia-net` | Reads packets from socket, hands to dispatcher |
| Dispatcher worker pool | `kademlia-net` | Decodes messages, invokes handlers, encodes responses |
| Timeout reaper | `kademlia-net` | Periodically completes expired pending futures |
| Maintenance scheduler | `kademlia-net` | Pings stale contacts, refreshes buckets, republishes values |

### Synchronization Strategy

*   Use **`ConcurrentHashMap`** for pending RPC futures and value store.
*   Use **immutable snapshots** (e.g., `List.copyOf(...)`) when returning collections from the routing table.
*   Use **fine-grained locks** on individual k-buckets, not the whole routing table.
*   **Never hold a lock while performing I/O or calling another module.**

---

## 8. Error Handling Assumptions

### Principle: Isolate, Log, Continue

A single malformed packet, a throwing handler, or a dead peer must **never crash the node**.

| Failure Mode | Handling |
|---|---|
| Malformed packet (bad JSON, wrong version) | Log at `WARN`, drop packet, no response sent |
| Unknown message type | Log at `WARN`, drop packet |
| Handler throws an exception | Catch, log at `ERROR`, send `ErrorResponse` to requester |
| RPC timeout | Complete future with `TimeoutException`, mark contact as stale |
| Network unreachable | Complete future with `IOException`, mark contact as failed |
| Duplicate response (same `messageId`) | Silently drop; future already completed |
| Oversized packet (> `MAX_PACKET_BYTES`) | Reject at transport layer, log at `WARN` |
| Invalid `NodeId` length | Reject at deserialization, log at `WARN` |
| Node shutdown with pending futures | Cancel all pending futures with `CancellationException` |

### What Is NOT Handled in Phase 0–2

*   Authentication / message signing (Phase 4 optional)
*   NAT traversal (out of scope)
*   Sybil / node-ID spoofing protection (out of scope)

---

## 9. Testing Strategy

Testing is layered. Each layer has a distinct purpose and runs at a different speed.

### Layer 1: Unit Tests (per module)

*   **Location:** `src/test/java` in each module.
*   **Scope:** One class or one method at a time.
*   **Tools:** JUnit 5, AssertJ, Mockito.
*   **Examples:**
    *   `NodeId` XOR distance symmetry and self-distance.
    *   `KBucket` add/update/evict behavior.
    *   `JsonMessageSerializer` round-trip for every message type.
    *   `UdpTransport` localhost send/receive.

### Layer 2: Contract Tests (shared)

*   **Location:** `kademlia-testkit` and `kademlia-api`.
*   **Scope:** Verify that an implementation satisfies the interface contract.
*   **Run against multiple implementations:**
    *   `RpcGatewayContractTest` runs against `FakeRpcGateway`, `InMemoryRpcGateway`, and `UdpRpcGateway`.
    *   `RoutingTableContractTest` runs against `FakeRoutingTable` and `DefaultRoutingTable`.
*   **Phase 0:** Skeletons exist, annotated `@Disabled("Enabled in Phase 1")`.

### Layer 3: Integration Tests (localhost)

*   **Location:** `kademlia-node/src/test/java`.
*   **Scope:** Multiple real nodes talking over real UDP on localhost.
*   **Examples:**
    *   Two-node PING over UDP.
    *   Three-node FIND_NODE.
    *   Five-node DHT store/get.
    *   Node failure: stop one node, verify value still retrievable.

### Layer 4: Deterministic Simulation Tests (Phase 4)

*   Uses `InMemoryTransport` + `FakeClock` + configurable packet loss and latency.
*   Allows testing Kademlia behavior without flaky real UDP tests.

---

## 10. Future Extensions

The architecture is designed to accommodate these without breaking the Phase 0 API:

| Extension | Where It Lands | Notes |
|---|---|---|
| Binary serialization (Protobuf) | `kademlia-net` | New `MessageSerializer` implementation; `kademlia-api` unchanged |
| Bucket splitting by prefix | `kademlia-core` | Internal to `DefaultRoutingTable` |
| Value caching | `kademlia-core` | Internal to `ValueStore` / lookup logic |
| Rate limiting per IP | `kademlia-net` | Added to dispatcher |
| Message signing / auth | `kademlia-api` + `kademlia-net` | Requires API extension (group approval) |
| TCP / QUIC transport | `kademlia-net` | New `Transport` implementation |
| Metrics / observability | `kademlia-net` | Counters, histograms, optional health endpoint |
| CLI polish | `kademlia-node` | New commands, output formatting |

Any change to `kademlia-api` after Phase 0 freeze requires:
1.  A new issue.
2.  Whole-group discussion.
3.  Additive change if possible (no breaking signatures).
4.  Update to all fakes and contract tests in `kademlia-testkit`.

---

## 11. Document Change Process

This document is **frozen after Phase 0 approval**. To change it:

1.  Open a PR targeting `main` with the proposed change.
2.  Label the PR `architecture-change`.
3.  Require approval from **both** Team CORE lead and Team NET lead.
4.  Update the "Last Updated" date and version at the top of this file.