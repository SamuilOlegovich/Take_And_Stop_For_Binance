package main.model;

import main.controller.MainStartController;
import main.model.binance.api.BinanceAPI;
import main.model.binance.datatype.BinanceEventDepthUpdate;

import java.util.ArrayList;

public class Agent {
    private static BinanceEventDepthUpdate binanceEventDepthUpdate;
    private static MainStartController mainStartController;
    private static BinanceAPI binanceAPI;

    private static ArrayList<String> listAllCoinPair;

    private static boolean getUpToDateDataOnPairs;
    private static boolean connectWebSocket;
    private static boolean yesOrNotAPIKey;

    private static String keyForWebSocket;


    public static BinanceEventDepthUpdate getBinanceEventDepthUpdate() {
        return binanceEventDepthUpdate;
    }

    public static void setBinanceEventDepthUpdate(BinanceEventDepthUpdate binanceEventDepthUpdate) {
        Agent.binanceEventDepthUpdate = binanceEventDepthUpdate;
    }

    public static ArrayList<String> getListAllCoinPair() {
        return listAllCoinPair;
    }

    public static void setListAllCoinPair(ArrayList<String> listAllCoinPair) {
        if (Agent.listAllCoinPair == null) {
            Agent.listAllCoinPair = new ArrayList<>(listAllCoinPair);
        }
    }

    public static boolean isYesOrNotAPIKey() {
        return yesOrNotAPIKey;
    }

    public static void setYesOrNotAPIKey(boolean yesOrNotAPIKey) {
        Agent.yesOrNotAPIKey = yesOrNotAPIKey;
    }

    public static String getKeyForWebSocket() {
        return keyForWebSocket;
    }

    public static void setKeyForWebSocket(String keyForWebSocket) {
        Agent.keyForWebSocket = keyForWebSocket;
    }

    public static BinanceAPI getBinanceAPI() {
        return binanceAPI;
    }

    public static void setBinanceAPI(BinanceAPI binanceAPI) {
        Agent.binanceAPI = binanceAPI;
    }

    public static MainStartController getMainStartController() {
        return Agent.mainStartController;
    }

    public static void setMainStartController(MainStartController mainStartController) {
        Agent.mainStartController = mainStartController;
    }

    public static boolean isGetUpToDateDataOnPairs() {
        return getUpToDateDataOnPairs;
    }

    public static void setGetUpToDateDataOnPairs(boolean getUpToDateDataOnPairs) {
        Agent.getUpToDateDataOnPairs = getUpToDateDataOnPairs;
    }

    public static boolean isConnectWebSocket() {
        return connectWebSocket;
    }

    public static void setConnectWebSocket(boolean connectWebSocket) {
        Agent.connectWebSocket = connectWebSocket;
    }
}
