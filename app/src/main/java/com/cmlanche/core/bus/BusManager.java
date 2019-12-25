package com.cmlanche.core.bus;

import com.squareup.otto.Bus;

public class BusManager {

    private Bus bus;

    private static class BusHolder {
        public static BusManager manager = new BusManager();
    }

    private BusManager() {
        bus = new Bus();
    }

    public static Bus getBus() {
        return BusHolder.manager.bus;
    }
}
