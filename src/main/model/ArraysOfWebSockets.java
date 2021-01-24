package main.model;

import java.util.HashMap;
import java.util.Map;



public class ArraysOfWebSockets {
    private ArraysOfStrategies arraysOfStrategies;
    private final Map<String, WebSocket> map;



    public ArraysOfWebSockets() {
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
        String[] keys = map.keySet().toArray(new String[0]);
        for (String key : keys) { map.get(key).stopAll(); }
    }



    // добавить одну стратегию на прослушку
    public synchronized void addOneStrategyToWiretap(StrategyObject in) {
        addToListener(in);
    }



    public synchronized void closeWebSocket(String key) {
        map.get(key).setSocket();
        map.remove(key);
    }



    private void addToListener(StrategyObject in) {
        String key = in.getTradingPair();
        if (map.containsKey(key)) {
            map.get(key).addStrategyObject(in);
            map.get(key).startAll();
        }
        else {
            map.put(key, new WebSocket(in));
        }
    }



    public void deleteStrategy(StrategyObject in) {
        String key = in.getTradingPair();
        map.get(key).removeStrategyObject(in);
    }



    // добавить сокет для визуальных котироваок, без стратегии
    public void addViewSocket(String in) {
        map.put(in, new WebSocket(in));
    }



    public Double getPriceNow(String in) {
        if (map.containsKey(in)) {
            return map.get(in).getPriceNow();
        }
        return null;
    }



    public void setArraysOfStrategies(ArraysOfStrategies arraysOfStrategies) { this.arraysOfStrategies = arraysOfStrategies; }
}
