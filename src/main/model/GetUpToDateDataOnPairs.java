package main.model;

import main.model.binance.api.BinanceAPI;
import main.model.binance.api.BinanceApiException;

import java.util.ArrayList;
import java.util.TreeSet;



public class GetUpToDateDataOnPairs implements Runnable {


    @Override
    public void run() {
        get();
    }



    private void get() {
        boolean b = false;
        try {
            // считываем актуальные существующие пары на данный момент на бирже
            Agent.setBinanceAPI(new BinanceAPI(Agent.getApi().getAPI_KEY(), Agent.getApi().getSECRET_KEY()));
            // Получаем ключь к веб сокету
            Agent.setKeyForWebSocket(Agent.getBinanceAPI().startUserDataStream());
            TreeSet<String> treeSet = new TreeSet<>(Agent.getBinanceAPI().allBookTickersMap().keySet());
            Agent.setAllCoinPairList(new ArrayList<>(treeSet));
            treeSet.clear();
            b = true;
        } catch (BinanceApiException e) {
            Agent.getWriteKeysAndSettings().writePatternForKeys();
            Agent.setGetUpToDateDataOnPairs(false);
        }

        if (b) { Agent.setGetUpToDateDataOnPairs(true); }
    }
}
