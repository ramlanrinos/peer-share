package com.peershare.kademlia.testkit;

import com.peershare.kademlia.api.Clock;

public class FakeClock implements Clock {

    private long nowMillis;

    public FakeClock(long nowMillis) {
        this.nowMillis = nowMillis;
    }

    @Override
    public long nowMillis() {
        return nowMillis;
    }

    public void advance(long millis) {
        nowMillis += millis;
    }

    public void set(long millis) {
        nowMillis = millis;
    }
}