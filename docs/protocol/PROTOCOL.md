# PeerShare Kademlia Wire Protocol

**Status:** Phase 0 proposed contract — requires CORE and NET team approval  
**Protocol version:** `1`  
**Related:** [PARAMETERS.md](PARAMETERS.md), P0-10 (#10), P0-11 (#11)

## 1. Purpose and scope

This document specifies the initial message protocol used between PeerShare Kademlia nodes. Both the CORE and NET implementations must agree on the JSON envelope, RPC types, request/response correlation, validation and error behavior before implementation begins.

This is a **wire-format specification**, not a Java implementation. Initial scope: UDP transport and UTF-8 JSON serialization. Authentication, encryption, NAT traversal, reliable streaming and fragmentation are out of scope for Phase 0.

## 2. Transport and limits

- **Transport:** one JSON message per UDP datagram. Each message is independent; UDP provides no guaranteed delivery, ordering or duplicate suppression.
- **Encoding:** UTF-8 bytes representing a JSON object. No trailing length prefix or newline is required.
- **Maximum datagram payload:** do not exceed the configured `MAX_PACKET_BYTES` from `PARAMETERS.md`. The maximum possible IPv4 UDP payload is 65,507 bytes, but implementers should use a *smaller operational cap* to avoid IP fragmentation; this cap must be agreed before implementation.
- **Timeouts and retries:** use values from `PARAMETERS.md` (proposed default RPC timeout 5 s and 2–3 retries). Requests may be retried with the *same* `messageId`; receivers should avoid repeating side effects when practicable.
- **Node IDs and keys:** 160-bit unsigned values represented by **exactly 40 lowercase hexadecimal characters**, with no `0x` prefix.
- **Ports:** integers in the range 1–65535; `host` is a non-empty DNS name or IP address.
- **K and ALPHA:** use `PARAMETERS.md` (initial proposal `K=20`, `ALPHA=3`); `FIND_NODE_RESPONSE` and `FIND_VALUE_RESPONSE` carry at most `K` contacts.
- **Value time-to-live (TTL):** expressed in milliseconds. The proposed default is 24 hours; allowable bounds must be confirmed in `PARAMETERS.md`.

## 3. JSON envelope

All messages use the following top-level fields:

| Field | JSON type | Required | Meaning |
| --- | --- | --- | --- |
| `version` | integer | Yes | Protocol version; currently `1` |
| `type` | string | Yes | One of the message types in Section 4 |
| `messageId` | string | Yes | UUID identifying an RPC exchange |
| `payload` | object | Yes | Type-specific fields, including sender |

Example:

```json
{
  "version": 1,
  "type": "PING_REQUEST",
  "messageId": "8f2c1a4e-6b7d-4e9f-9c3a-2f6b8c1d2e3f",
  "payload": {
    "sender": {
      "nodeId": "0000000000000000000000000000000000000001",
      "host": "127.0.0.1",
      "port": 7001
    }
  }
}
```

### 3.1 Contact representation

Each `sender` and item in `closestContacts` uses:

```json
{
  "nodeId": "0000000000000000000000000000000000000001",
  "host": "127.0.0.1",
  "port": 7001
}
```

`nodeId` identifies the node; `host` and `port` describe the endpoint used to contact it. The receiver validates these fields and must not blindly trust the claimed identity or endpoint.

### 3.2 Correlation

A request has a freshly generated UUID `messageId`. Its successful response or error response **copies the same `messageId`**. The caller matches a received response to its outstanding request using the ID and the expected peer and response type. Responses with unknown IDs, unexpected types or mismatched peers are ignored/rejected. A timeout completes the caller's pending request exceptionally; it is not a network `ERROR` response.

## 4. RPC message types

| Operation | Request `type` | Success response `type` | Purpose |
| --- | --- | --- | --- |
| PING | `PING_REQUEST` | `PING_RESPONSE` | Check whether a node is reachable |
| STORE | `STORE_REQUEST` | `STORE_RESPONSE` | Request storage of a value under a key |
| FIND_NODE | `FIND_NODE_REQUEST` | `FIND_NODE_RESPONSE` | Request closest known contacts to a target ID |
| FIND_VALUE | `FIND_VALUE_REQUEST` | `FIND_VALUE_RESPONSE` | Retrieve a value or receive closer contacts |
| Error | — | `ERROR` | Report a validly decoded but rejected request when safe |

All RPC payloads contain `sender` (Contact). The tables below describe the **additional** payload properties.

### 4.1 PING

- `PING_REQUEST`: no additional payload properties.
- `PING_RESPONSE`: no additional payload properties.
- The receiver can record a validated contact and replies with its own contact as `sender`.

```json
{
  "version": 1,
  "type": "PING_RESPONSE",
  "messageId": "8f2c1a4e-6b7d-4e9f-9c3a-2f6b8c1d2e3f",
  "payload": {
    "sender": {
      "nodeId": "0000000000000000000000000000000000000002",
      "host": "127.0.0.1",
      "port": 7002
    }
  }
}
```

### 4.2 STORE

**Request fields:**

| Field | JSON type | Meaning |
| --- | --- | --- |
| `key` | string | 40-character hex Kademlia key |
| `value` | string | Base64 encoding of binary value bytes |
| `ttlMillis` | integer | Positive lifetime in milliseconds |

**Response field:** `accepted` (boolean) indicates whether the receiver accepted the store request.

Example request payload:

```json
{
  "sender": {
    "nodeId": "0000000000000000000000000000000000000001",
    "host": "127.0.0.1",
    "port": 7001
  },
  "key": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
  "value": "SGVsbG8=",
  "ttlMillis": 86400000
}
```

Example response payload:

```json
{
  "sender": {
    "nodeId": "0000000000000000000000000000000000000002",
    "host": "127.0.0.1",
    "port": 7002
  },
  "accepted": true
}
```

`SGVsbG8=` decodes to the UTF-8 text `Hello`. The wire value is **always Base64**, even when the underlying value contains text. The responder should reject invalid Base64, invalid keys, invalid TTLs and oversized values. A normal refusal can use `accepted: false`; malformed requests may receive `ERROR`.

### 4.3 FIND_NODE

**Request field:** `target` (40-character lowercase hex node ID).  
**Response field:** `closestContacts` (array of at most `K` distinct Contact objects, ordered by ascending XOR distance to `target`).

Request payload:

```json
{
  "sender": {
    "nodeId": "0000000000000000000000000000000000000001",
    "host": "127.0.0.1",
    "port": 7001
  },
  "target": "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"
}
```

Response payload example (shortened to one contact):

```json
{
  "sender": {
    "nodeId": "0000000000000000000000000000000000000002",
    "host": "127.0.0.1",
    "port": 7002
  },
  "closestContacts": [
    {
      "nodeId": "0000000000000000000000000000000000000003",
      "host": "127.0.0.1",
      "port": 7003
    }
  ]
}
```

Do not include duplicate contacts or the responding node itself in `closestContacts`.

### 4.4 FIND_VALUE

**Request field:** `target` (40-character lowercase hex key).

**Success response fields:**

| Field | JSON type | Meaning |
| --- | --- | --- |
| `hasValue` | boolean | `true` when an unexpired value is available |
| `value` | string or null | Base64 binary content if found; otherwise `null` |
| `closestContacts` | array | Empty when found; otherwise up to `K` closest contacts |

When the value is found:

```json
{
  "sender": {
    "nodeId": "0000000000000000000000000000000000000002",
    "host": "127.0.0.1",
    "port": 7002
  },
  "hasValue": true,
  "value": "SGVsbG8=",
  "closestContacts": []
}
```

When the value is missing or expired:

```json
{
  "sender": {
    "nodeId": "0000000000000000000000000000000000000002",
    "host": "127.0.0.1",
    "port": 7002
  },
  "hasValue": false,
  "value": null,
  "closestContacts": [
    {
      "nodeId": "0000000000000000000000000000000000000003",
      "host": "127.0.0.1",
      "port": 7003
    }
  ]
}
```

The response must not return both a found value and routing contacts in this initial version.

## 5. Serialization and deserialization

1. Construct the Java message DTO from the shared `kademlia-api` contract.
2. Serialize to a UTF-8 JSON object using the field names and types in this document.
3. Encode binary `byte[]` values as standard Base64 strings.
4. Send one serialized message in a UDP datagram within configured size limits.
5. The receiver decodes UTF-8, parses JSON and validates the envelope, payload, node IDs, port, UUID, and required fields **before** dispatching.
6. Dispatch the request by `type` to the corresponding Kademlia handler.
7. Serialize the response with its corresponding response type and the original `messageId`.

Field names and enum names are case-sensitive. Integer fields must be JSON numbers (not quoted strings). Reject duplicate JSON member names to avoid ambiguous interpretation. Unknown fields should be ignored only when harmless; missing required fields and invalid known fields are rejected. Deterministic property order is **not** required. Authentication and cryptographic integrity are not provided by this format.

## 6. Error handling

### 6.1 Error envelope

When a request can be safely decoded and its message ID recovered, a receiver may reply with an `ERROR` message. Use the request's `messageId` and the receiver's Contact as `sender`.

```json
{
  "version": 1,
  "type": "ERROR",
  "messageId": "8f2c1a4e-6b7d-4e9f-9c3a-2f6b8c1d2e3f",
  "payload": {
    "sender": {
      "nodeId": "0000000000000000000000000000000000000002",
      "host": "127.0.0.1",
      "port": 7002
    },
    "reason": "INVALID_REQUEST: missing target"
  }
}
```

For Phase 0, `reason` is a non-empty string. The following prefixes are **proposed conventions**, not separate required DTO fields:

| Reason prefix | Meaning |
| --- | --- |
| `INVALID_REQUEST` | Missing or incorrect field, invalid value or malformed payload |
| `UNSUPPORTED_VERSION` | Unknown `version` |
| `UNKNOWN_MESSAGE_TYPE` | Unrecognized message `type` |
| `PAYLOAD_TOO_LARGE` | Encoded content exceeds allowed limit |
| `INTERNAL_ERROR` | Unexpected failure while processing |

### 6.2 Handling failures safely

- Malformed JSON, invalid UTF-8 or oversized datagrams: **drop safely** (logging may be rate-limited); do not crash the node. A response is not required.
- Unknown version or message type: reject; send an `ERROR` only when a trusted response destination and valid ID are available; otherwise drop safely.
- Unsupported/malformed payload: respond with `ERROR` when safe, without leaking stack traces.
- Timeout, packet loss or unreachable node: the **caller** times out/retries according to `PARAMETERS.md`; no server response may arrive.
- Unsolicited or mismatched responses: ignore and log at an appropriate level.
- Duplicate UDP requests: handlers should remain safe for retries. `STORE` for the same key/value should not create conflicting repeated side effects.

## 7. End-to-end RPC example

1. Node A (`127.0.0.1:7001`) creates `PING_REQUEST` with a UUID.
2. Node A serializes the envelope to UTF-8 JSON and sends it to Node B (`127.0.0.1:7002`).
3. Node B validates the datagram and parses the JSON.
4. Node B dispatches the request to its PING handler and creates `PING_RESPONSE`.
5. Node B includes **the same `messageId`** and its own contact in the response.
6. Node A matches the UUID to its outstanding request and completes it successfully.
7. If no valid response arrives within the configured timeout, Node A's request fails or retries.

## 8. Validation checklist for implementers

- [ ] Each supported type can serialize and deserialize without losing fields.
- [ ] `messageId` is preserved from request to response.
- [ ] Sender Contact is validated and preserved.
- [ ] Node IDs and keys are exactly 40 hexadecimal characters.
- [ ] Binary values round-trip through Base64 without corruption.
- [ ] FIND_NODE returns at most K unique contacts in XOR distance order.
- [ ] FIND_VALUE returns either a value or closest contacts, not both.
- [ ] Unknown versions, unknown types and malformed JSON are safely handled.
- [ ] Oversized datagrams are rejected safely.
- [ ] UDP timeouts and duplicate packets are handled without crashing.

## 9. Decisions to confirm before API freeze

This document proposes an initial interoperable encoding. Before the team approves the contract, confirm these details with the P0-10 owner and CORE/NET leads:

1. Exact `MAX_PACKET_BYTES` operational cap, not merely the theoretical UDP maximum.
2. Exact retry policy, RPC timeouts and TTL limits in `PARAMETERS.md`.
3. Whether to encode `NodeId` as lower-case 40-character hex with no `0x` prefix.
4. Whether `FIND_VALUE_RESPONSE` always contains both `value` and `closestContacts` keys (with null/empty values as above).
5. The error type name `ERROR` on the wire versus Java DTO `ErrorResponse`.
6. Whether reason-prefix conventions should become explicit machine-readable error codes in a future version.

Any incompatible wire-format changes after approval must increment the protocol version or be approved as part of a coordinated migration.