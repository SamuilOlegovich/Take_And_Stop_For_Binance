package main.model;

import java.util.ArrayList;
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
        ArrayList<StrategyObject> list = new ArrayList<>(arraysOfStrategies.getTradedStrategyListObject());
        for (StrategyObject strategyObject : list) {
            addToListener(strategyObject);
        }
        list.clear();
    }



    // добавить одну стратегию на прослушку
    public synchronized void addOneStrategyToWiretap(StrategyObject in) {
        addToListener(in);
    }



    // останавливает нить сокета и убирает его из списка сокетов
    public synchronized void closeWebSocket(String key) {
        map.get(key).setSocket();
        map.remove(key);
    }



    private void addToListener(StrategyObject in) {
        String key = in.getTradingPair();
        if (map.containsKey(key)) { map.get(key).addStrategyObject(in);
        } else { map.put(key, new WebSocket(in)); }
    }



    public void deleteStrategy(StrategyObject in) {
        String key = in.getTradingPair();
    }



    // добавить сокет для визуальных котироваок, без стратегии
    public void addViewSocket(String in) {
        map.put(in, new WebSocket(in));
    }



    public Double getPriceNow(String in) {
        if (map.containsKey(in)) { return map.get(in).getPriceNow(); }
        return null;
    }


    public void setArraysOfStrategies(ArraysOfStrategies arraysOfStrategies) { this.arraysOfStrategies = arraysOfStrategies; }
}
