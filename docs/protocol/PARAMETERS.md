# Kademlia Parameters

Issue: P0-10 — Write Kademlia parameter document (#10)
Intended repository path: `docs/protocol/PARAMETERS.md`
Status: Draft for CORE and NET review; not yet an approved protocol contract.

## 1. Purpose and scope

Define shared constants and configuration profiles so CORE routing/storage and NET transport/RPC implementations use consistent values. Phase 0 defines these decisions; implementation belongs to later tasks. Message schemas belong in `PROTOCOL.md`.

## 2. Parameter profiles

"Source" means a value given in the supplied project planning documents. "Proposal" means a choice or clarification introduced in this draft that requires CORE and NET agreement. All profiles require final team review.

| Parameter | Default profile | Test profile | Meaning / origin |
| --- | --- | --- | --- |
| `ID_BITS` | 160 | 160 | Node IDs and keys share a 160-bit XOR space. Source. Fixed for this protocol. |
| `ID_BYTES` | 20 | 20 | Derived: `ID_BITS / 8`; decoded binary length, not text length. |
| `K` | 20 | 3 | Maximum contacts per bucket, closest-contact response limit, and replication target. Source; source permits test K of 3 or 5; draft selects 3. |
| `ALPHA` | 3 | 2 | Maximum simultaneous lookup RPCs per lookup. Source. |
| `REPLACEMENT_CACHE_SIZE` | 20 | 3 | Maximum replacement contacts per bucket, if implemented. Default source; test value proposed to match K. |
| `RPC_TIMEOUT` | 5 seconds | 200 milliseconds | Deadline for one RPC attempt. Values from source; per-attempt interpretation is proposed. |
| `RETRY_COUNT` | 2 | 0 | Additional attempts after initial attempt. Source permits 2 or 3; draft selects 2. Test retries disabled for deterministic timeout tests; proposal. |
| `STALE_CONTACT_AGE` | 5 minutes | 1 second | Contact becomes eligible for a liveness check after this inactivity. Default source; test value proposed. Not an immediate eviction deadline. |
| `VALUE_TTL` | 24 hours | 5 seconds | Lifetime of a stored value. Source. |
| `REPUBLISH_INTERVAL` | 1 hour | 2 seconds | Interval for original publishers to republish values. Source. |
| `MAX_PACKET_BYTES` | 65,507 bytes | 65,507 bytes | Project's documented UDP payload ceiling; counts the complete serialized message. Source. See packet policy below. |

Test settings are explicit overrides; never choose them implicitly because the node runs on localhost. IDs remain 160 bits in all profiles so the same wire format and NodeId validation are exercised.

## 3. Configuration and validation

Proposed configuration policy:

- Keep `ID_BITS` and derived `ID_BYTES` fixed for protocol compatibility.
- Make operational settings configurable through the shared parameter/configuration object, with the defaults above.
- Use explicit duration units (`Duration` in Java); do not mix seconds and milliseconds in integer fields.
- Reject invalid configurations before starting the node.
- Require K >= 1, 1 <= ALPHA <= K, replacement cache size >= 0, and retry count >= 0.
- Require positive RPC timeout, stale-contact age, value TTL, and republish interval.
- Require republish interval < value TTL.
- Require 1 <= MAX_PACKET_BYTES <= 65,507 under this project's initial IPv4 UDP policy.
- Limit contacts in FIND_NODE and value-not-found responses to K, and additionally enforce the serialized byte limit.

Runtime adaptation to network conditions, bucket refresh intervals, expiry sweep intervals, rate limits, and lookup-wide deadlines are not specified by the supplied P0-10 values. Define them explicitly before implementing those features; do not invent hidden defaults in either team.

## 4. Timeout and retry semantics — proposed

An RPC attempt times out when its matching response does not arrive within RPC_TIMEOUT. Use monotonic elapsed time for this deadline. Clean up the pending request and complete its future exceptionally when the operation fails; do not leave futures pending forever.

RETRY_COUNT counts retries, excluding the initial send. With default values, the maximum is 3 attempts. Without backoff, their configured waiting budget totals 15 seconds; scheduling and processing can add overhead. This is not a complete iterative lookup deadline.

Only timeout failures are retried by this draft policy. Invalid messages and explicit error responses are not automatically retried. Duplicate, unknown, and late responses must not complete an RPC more than once.

The exact message-ID reuse policy and STORE duplicate handling must be agreed in PROTOCOL.md before retries are implemented. ALPHA limits outstanding lookup RPCs including retries; it is not a node-wide traffic limit.

## 5. Value expiry and republishing — proposed semantics

For the initial implementation, a receiver assigns `expiresAt = receiptTime + VALUE_TTL`. A value is expired when `now >= expiresAt`. Reads treat expired entries as absent even if physical cleanup has not run.

Use the injected clock for value timestamps and tests. Follow the agreed StoredValue API; do not change shared API fields in this documentation task. Wire representation of TTL/expiry must be reconciled with PROTOCOL.md before implementation.

Original publishers republish every REPUBLISH_INTERVAL. A successful STORE refreshes the receiving copy's lifetime under this proposed receipt-based policy. Republish intervals being shorter than TTL provide opportunities to renew a value, but do not guarantee availability during failures.

## 6. Packet-size policy

The planning document specifies 65,507 bytes. Preserve that value as the draft initial IPv4 UDP payload ceiling; it is not a guarantee of fragmentation-free delivery and is not the application value-size limit.

Measure the complete encoded byte sequence, including envelope, contacts, and encoded value. JSON metadata and byte-array encoding consume space. Reject oversize outbound messages before sending; discard oversize inbound datagrams without processing a truncated prefix. Receive handling must distinguish an oversize datagram from a valid complete message.

No application-level fragmentation/reassembly is specified. If a message cannot fit, fail it explicitly. NET should review whether to adopt a smaller operational cap before deployment. No separate MAX_VALUE_BYTES is approved by this draft.

## 7. Deterministic test guidance

Use the test profile for isolated tests, with fake clocks or controllable schedulers rather than real sleeps. The 200 ms timeout is a logical test setting; real UDP integration tests may need an explicit larger override to avoid CI timing failures.

Suggested acceptance checks for later implementation:

1. Reject a decoded ID of 19 or 21 bytes; accept 20 bytes.
2. Keep each bucket and closest-contact result within K.
3. Keep concurrent lookup RPCs within ALPHA.
4. Trigger timeout at the configured deadline; terminate the pending future.
5. With RETRY_COUNT=2, perform at most 3 attempts; with 0, perform one.
6. Return a value immediately before expiry and treat it as absent at expiry.
7. Trigger republishing using the injected clock/scheduler.
8. Accept a valid complete message at the configured byte limit and reject one byte above it; never treat a truncated message as complete.

These are implementation acceptance examples, not claims that code or tests already exist.

## 8. Review and completion checklist

- [ ] Compare this file with the current repository's KademliaParams and PROTOCOL.md, if present; resolve mismatches.
- [ ] CORE agrees on K, ALPHA, cache size, TTL, expiry, and republishing semantics.
- [ ] NET agrees on timeout/retry behavior, packet ceiling, and encoding overhead.
- [ ] CORE and NET resolve retry message-ID and duplicate STORE behavior in PROTOCOL.md.
- [ ] Both leads review all proposed choices and approve the final parameter contract.
- [ ] Record approval in the pull request and update this status to Approved after actual approval.
- [ ] Merge the approved file at docs/protocol/PARAMETERS.md; notify the P0-11 owner through the normal team process.

## 9. Source basis and unresolved choices

Source basis: screenshot of issue #10; Phase 0 Implementation Doc(1).pdf, P0-10 section (pages 8–9); 1. Recommended initial scope.pdf, recommended parameters (pages 42–44); Phase0_Member_Tasks.pdf, execution waves (page 2).

Open review choices: retry count 2 versus 3; test K 3 versus 5; practical operational packet cap; per-attempt retry policy; expiry/refresh semantics; test-only cache and stale-contact settings. This draft makes explicit proposals so reviewers can accept or change them.
