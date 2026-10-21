package com.peershare.kademlia.api;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import org.junit.jupiter.api.Test;

/** Smoke tests for the P0-12 common types. Full contract tests come in P0-16. */
class CommonTypesTest {

    private static NodeId idOf(int fill) {
        byte[] bytes = new byte[NodeId.LENGTH_BYTES];
        bytes[0] = (byte) fill;
        return NodeId.fromBytes(bytes);
    }

    @Test
    void paramsMatchParametersDoc() {
        assertEquals(160, KademliaParams.ID_BITS);
        assertEquals(20, KademliaParams.ID_BYTES);
        assertEquals(20, KademliaParams.DEFAULT_K);
        assertEquals(3, KademliaParams.DEFAULT_ALPHA);
        assertEquals(Duration.ofSeconds(5), KademliaParams.DEFAULT_RPC_TIMEOUT);
        assertTrue(KademliaParams.DEFAULT_REPUBLISH_INTERVAL.compareTo(KademliaParams.DEFAULT_VALUE_TTL) < 0);
        assertEquals(65_507, KademliaParams.MAX_PACKET_BYTES);
    }

    @Test
    void nodeIdRejectsWrongLength() {
        assertThrows(NullPointerException.class, () -> NodeId.fromBytes(null));
        assertThrows(IllegalArgumentException.class, () -> NodeId.fromBytes(new byte[19]));
        assertThrows(IllegalArgumentException.class, () -> NodeId.fromBytes(new byte[21]));
    }

    @Test
    void nodeIdIsImmutableAndComparesByValue() {
        byte[] raw = new byte[NodeId.LENGTH_BYTES];
        NodeId id = NodeId.fromBytes(raw);
        raw[0] = 1;
        assertEquals(0, id.bytes()[0]);
        id.bytes()[0] = 1;
        assertEquals(0, id.bytes()[0]);

        assertEquals(idOf(7), idOf(7));
        assertEquals(idOf(7).hashCode(), idOf(7).hashCode());
        assertNotEquals(idOf(7), idOf(8));
    }

    @Test
    void contactValidatesFields() {
        assertThrows(NullPointerException.class, () -> new Contact(null, "127.0.0.1", 4000));
        assertThrows(IllegalArgumentException.class, () -> new Contact(idOf(1), " ", 4000));
        assertThrows(IllegalArgumentException.class, () -> new Contact(idOf(1), "127.0.0.1", 0));
        assertThrows(IllegalArgumentException.class, () -> new Contact(idOf(1), "127.0.0.1", 65_536));
        assertEquals(new Contact(idOf(1), "127.0.0.1", 4000), new Contact(idOf(1), "127.0.0.1", 4000));
    }

    @Test
    void storedValueCopiesBytesAndTracksExpiry() {
        byte[] raw = {1, 2, 3};
        StoredValue sv = new StoredValue(idOf(1), raw, 1_000, 2_000);
        raw[0] = 9;
        assertArrayEquals(new byte[] {1, 2, 3}, sv.value());

        assertFalse(sv.isExpired(1_999));
        assertTrue(sv.isExpired(2_000));

        assertEquals(sv, new StoredValue(idOf(1), new byte[] {1, 2, 3}, 1_000, 2_000));
        assertThrows(IllegalArgumentException.class, () -> new StoredValue(idOf(1), new byte[0], 0, 1));
        assertThrows(IllegalArgumentException.class, () -> new StoredValue(idOf(1), raw, 2_000, 1_000));
    }

    @Test
    void systemClockReturnsCurrentTime() {
        long before = System.currentTimeMillis();
        long now = Clock.system().nowMillis();
        assertTrue(now >= before);
    }
}
