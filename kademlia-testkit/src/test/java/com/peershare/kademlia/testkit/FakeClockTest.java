package com.peershare.kademlia.testkit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class FakeClockTest {

    @Test
    void shouldAdvanceTime() {
        FakeClock clock = new FakeClock(1000);

        assertEquals(1000, clock.nowMillis());

        clock.advance(5000);

        assertEquals(6000, clock.nowMillis());
    }

    @Test
    void shouldSetTime() {
        FakeClock clock = new FakeClock(1000);

        clock.set(10000);

        assertEquals(10000, clock.nowMillis());
    }
}