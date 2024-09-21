package com.assignment2.client;

public class LamportClock {

    public int clock;

    public LamportClock(int clock) {
        this.clock = clock;
    }

    public int getClock() {
        return clock;
    }

    public void setClock(int clock) {
        this.clock = clock;
    }
    public void increment() {
        clock++;
    }
}
