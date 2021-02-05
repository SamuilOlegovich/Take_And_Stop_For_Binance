package main.model;

import com.google.gson.JsonObject;

public class TradeReport extends Thread {
    private final StrategyObject strategyObject;
    private JsonObject jsonObject;

    public TradeReport(StrategyObject strategyObject, JsonObject jsonObject) {
        this.strategyObject = strategyObject;
        this.jsonObject = jsonObject;
        start();
    }

    @Override
    public void run() {


    }
}
