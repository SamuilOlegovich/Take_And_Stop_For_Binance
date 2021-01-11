package main.model;

import main.model.binance.datatype.BinanceEventDepthUpdate;

public class Agent {
    private static BinanceEventDepthUpdate binanceEventDepthUpdate;


    public static BinanceEventDepthUpdate getBinanceEventDepthUpdate() {
        return binanceEventDepthUpdate;
    }

    public static void setBinanceEventDepthUpdate(BinanceEventDepthUpdate binanceEventDepthUpdate) {
        Agent.binanceEventDepthUpdate = binanceEventDepthUpdate;
    }
}
