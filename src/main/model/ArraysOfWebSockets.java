package main.model;

import java.util.HashMap;
import java.util.Map;



public class ArraysOfWebSockets {
    private final ArraysOfStrategies arraysOfStrategies;
    private final Map<String, ConnectWebSocket> map;



    public ArraysOfWebSockets() {
        this.arraysOfStrategies = Agent.getArraysOfStrategies();
        this.map = new HashMap<>();
    }



    // Запустить все розетки для рабочих пар
    public void runAllWebSocketsForWorkingCouples() {
        for (StrategyObject strategyObject : arraysOfStrategies.getTradedStrategyListObject()) {
            addToListener(strategyObject);
        }
    }



    // Остановить все розетки для рабочих пар
    public void stopAllWebSocketsForWorkingCouples() {
        String[] keys = map.keySet().toArray(new String[map.size()]);



    }



    // добавить одну стратегию на прослушку
    public void addOneStrategyToWiretap(StrategyObject in) {
        addToListener(in);
    }



    public synchronized void closeWebSocket(String key) {
        map.get(key).getThread().interrupt();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        map.remove(key, map.get(key));
    }



    private void addToListener(StrategyObject in) {
        String key = in.getTradingPair();
        if (map.containsKey(key)) map.get(key).addStrategyObject(in);
        else map.put(key, new ConnectWebSocket(in));
    }
}
