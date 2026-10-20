# Phase 0 Implementation Plan

## 1. Purpose
The purpose of Phase 0 is to prepare the project foundation so that all team members can work independently on features in later phases without blocking each other.

Phase 0 does **not** implement full Kademlia behavior. Instead, it creates:
*   Repository structure & Maven multi-module build system
*   CI pipeline & Code formatting
*   Documentation structure
*   Shared API skeleton (`kademlia-api`)
*   Protocol & Parameter documents
*   Testkit skeleton (`kademlia-testkit`)
*   Commit point plan

---

## 2. Phase 0 Goals
1.  Create a multi-module Java project using **Maven**.
2.  Define shared interfaces and message types in `kademlia-api`.
3.  Define the Kademlia protocol constants (ID size, K, ALPHA, Timeouts).
4.  Define the message schema (JSON envelope, request/response types).
5.  Create fake/test utilities in `kademlia-testkit` for parallel development.
6.  Freeze the Phase 0 API contract so no breaking changes occur in Phase 1.

---

## 3. Phase 0 Non-Goals
The following are **not** part of Phase 0:
1.  Full routing table implementation.
2.  Full iterative lookup algorithm.
3.  Real UDP transport implementation.
4.  Full JSON/Protobuf serialization logic.
5.  Complete Kademlia message handlers (logic).
6.  Performance optimization or security hardening.

---

## 4. Module Structure
The project contains the following modules with strict dependency rules:

| Module | Responsibility | Dependencies |
| :--- | :--- | :--- |
| `kademlia-api` | Shared interfaces, DTOs, message records, constants | None |
| `kademlia-core` | Kademlia logic, routing table, k-buckets, value store | `kademlia-api` |
| `kademlia-net` | Serialization, UDP transport, RPC gateway, dispatcher | `kademlia-api`, `kademlia-testkit` (for tests) |
| `kademlia-node` | Final node wiring, CLI, demo application | `kademlia-core`, `kademlia-net`, `kademlia-api` |
| `kademlia-testkit` | Fake transport, fake clock, fake stores, test utilities | `kademlia-api` |

### Dependency Rules (Mandatory)
*   `kademlia-core` **must not** depend on `kademlia-net`.
*   `kademlia-net` **must not** depend on `kademlia-core`.
*   All implementation work depends **only** on `kademlia-api` and `kademlia-testkit`.

---

## 5. Phase 0 Commit Points

Each commit point represents a small, independently claimable deliverable. 
**Status Legend:** ✅ Done | 🔄 In Progress | ⬜ Todo

| ID | Commit Point | Status | Done When |
| :--- | :--- | :--- | :--- |
| **P0-01** | Create repository and base project folder | ✅ | Repo exists, main branch protected |
| **P0-02** | Create multi-module Maven build skeleton | ✅ | `mvn clean compile` succeeds |
| **P0-03** | Add Java toolchain configuration | ✅ | Java version enforced in `pom.xml` |
| **P0-04** | Add JUnit 5 and test dependencies | ⬜ | Sample test runs successfully |
| **P0-05** | Add code formatting/linting (Checkstyle/Spotless) | ⬜ | `mvn checkstyle:check` passes |
| **P0-06** | Add CI pipeline (GitHub Actions) | ⬜ | CI builds and tests pass on PR |
| **P0-07** | Create docs folder structure | ⬜ | `docs/architecture`, `protocol`, etc. exist |
| **P0-08** | Write Phase 0 implementation document | ⬜ | This document is approved |
| **P0-09** | Write Architecture document (`ARCHITECTURE.md`) | ⬜ | Modules and rules documented |
| **P0-10** | Write Kademlia parameter document (`PARAMETERS.md`) | ⬜ | Constants (K, ALPHA, TTL) agreed |
| **P0-11** | Write Protocol/message document (`PROTOCOL.md`) | ⬜ | Message schema and envelope agreed |
| **P0-12** | Create `kademlia-api` common types skeleton | ⬜ | `NodeId`, `Contact`, `StoredValue` compile |
| **P0-13** | Create `kademlia-api` message records skeleton | ⬜ | All `Message` records compile |
| **P0-14** | Create `kademlia-api` service interfaces skeleton | ⬜ | `RpcGateway`, `RoutingTable`, etc. compile |
| **P0-15** | Create `kademlia-testkit` skeleton | ⬜ | Fakes (`FakeClock`, etc.) compile |
| **P0-16** | Create contract test skeletons | ⬜ | Test classes compile (can be disabled) |
| **P0-17** | Add README with build instructions | ⬜ | New dev can build using README |
| **P0-18** | Whole-group API review | ⬜ | No unresolved interface questions |
| **P0-19** | Tag Phase 0 release | ⬜ | Tag `v0.0.1-phase0` exists |

---

## 6. Required Shared API Skeleton

The following types must exist in `kademlia-api` by the end of Phase 0. They can throw `UnsupportedOperationException` but must compile.

### Common Types
*   `NodeId`: Immutable 160-bit identifier.
*   `Contact`: Record containing `NodeId`, `host`, and `port`.
*   `StoredValue`: Record containing `key`, `value` (byte[]), and expiry times.
*   `Clock`: Interface for time abstraction (`nowMillis()`).
*   `KademliaParams`: Constants for ID bits, K, ALPHA, and timeouts.

### Message Types
*   `Message`: Sealed interface permitting all request/response types.
*   Requests: `PingRequest`, `StoreRequest`, `FindNodeRequest`, `FindValueRequest`.
*   Responses: `PingResponse`, `StoreResponse`, `FindNodeResponse`, `FindValueResponse`, `ErrorResponse`.

### Service Interfaces
*   `RpcGateway`: Asynchronous client for sending messages (`CompletableFuture`).
*   `KademliaLogic`: Server-side handler for incoming messages.
*   `RoutingTable`: Management of k-buckets and closest contacts.
*   `ValueStore`: In-memory key-value storage with expiry.
*   `KademliaNode`: High-level facade for CLI and integration tests.

---

## 7. Required Documents

By the end of Phase 0, the following documents must be present in the `docs/` folder:

1.  `docs/architecture/ARCHITECTURE.md`: Module structure, dependency rules, and high-level data flow.
2.  `docs/protocol/PARAMETERS.md`: All protocol constants (ID size, K, ALPHA, Timeouts, TTL).
3.  `docs/protocol/PROTOCOL.md`: Wire format, JSON envelope structure, and message field definitions.
4.  `docs/protocol/INTERFACES.md`: Detailed contract for each shared interface (threading, null-safety).
5.  `docs/phase0/PHASE0_IMPLEMENTATION.md`: This file.

---

## 8. Definition of Done (Phase 0)

Phase 0 is considered complete when:
1.  [x] Multi-module project builds locally (`mvn clean install`).
2.  [ ] CI pipeline passes on the main branch.
3.  [ ] Code formatting/checkstyle check passes.
4.  [ ] All `kademlia-api` interfaces and records compile.
5.  [ ] All `kademlia-testkit` stubs compile.
6.  [ ] All required documentation files exist and are reviewed.
7.  [ ] The whole group explicitly approves the API.
8.  [ ] Git tag `v0.0.1-phase0` is created.

---

## 10. Next Steps (Phase 1 Kickoff)

Once Phase 0 is tagged, the team will move to Phase 1. 
All members will begin implementing specific features across both the Core (Logic/Routing) and Net (Transport/Serialization) domains. They will use the `FakeRpcGateway` and `EchoKademliaLogic` from the testkit to decouple their work and ensure continuous integration without waiting for the other domains to be finished.
