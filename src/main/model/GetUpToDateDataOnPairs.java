package main.model;

import main.model.binance.API;
import main.model.binance.api.BinanceAPI;
import main.model.binance.api.BinanceApiException;

import java.util.ArrayList;
import java.util.TreeSet;



public class GetUpToDateDataOnPairs implements Runnable {


    @Override
    public void run() {
        get();
        Thread thread = new Thread(new ConnectWebSocket());
        thread.start();
    }

    private void get() {
        try {
            // считываем актуальные существующие пары на данный момент на бирже
            Agent.setBinanceAPI(new BinanceAPI(API.getApiKey(), API.getSecretKey()));
            // Получаем ключь к веб сокету
            Agent.setKeyForWebSocket(Agent.getBinanceAPI().startUserDataStream());
            TreeSet<String> treeSet = new TreeSet<>(Agent.getBinanceAPI().allBookTickersMap().keySet());
            Agent.setAllCoinPairList(new ArrayList<>(treeSet));
            System.out.println(treeSet.size());//////////////////////////////////////////////////
            treeSet.clear();
        } catch (BinanceApiException e) {
            new DeleteKeysAndSettings();
            Agent.setGetUpToDateDataOnPairs(false);
        }
        Agent.setGetUpToDateDataOnPairs(true);
    }
}
