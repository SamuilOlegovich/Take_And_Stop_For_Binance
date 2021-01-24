package main.model;

import javafx.stage.Stage;
import main.controller.MainPageController;
import main.model.binance.api.BinanceAPI;
import main.model.binance.datatype.BinanceEventDepthUpdate;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;



public class Agent {
    private static final ArrayList<String> allCoinPairList = new ArrayList<>();
    private static final ArrayList<String> viewPairSockets = new ArrayList<>();

    private static BinanceEventDepthUpdate binanceEventDepthUpdate;
    private static CreatesTemplatesAndData createsTemplatesAndData;
    private static WriteKeysAndSettings writeKeysAndSettings;
    private static ArraysOfWebSockets arraysOfWebSockets;
    private static ReadKeysAndSettings readKeysAndSettings;
    private static FilesAndPathCreator filesAndPathCreator;
    private static MainPageController mainPageController;
    private static ArraysOfStrategies arraysOfStrategies;
    private static WriterAndReadFile writerAndReadFile;
    private static String keyForWebSocket;
    private static BinanceAPI binanceAPI;
    private static API api;

    private static boolean getUpToDateDataOnPairs;
    private static boolean startAllOrStopAll;
    private static boolean connectWebSocket;
    private static boolean yesOrNotAPIKey;


    private static int DateDifference = 0;





    public static BinanceEventDepthUpdate getBinanceEventDepthUpdate() {
        return binanceEventDepthUpdate;
    }

    public static void setBinanceEventDepthUpdate(BinanceEventDepthUpdate binanceEventDepthUpdate) {
        Agent.binanceEventDepthUpdate = binanceEventDepthUpdate;
    }

    public static ArrayList<String> getAllCoinPairList() {
        return allCoinPairList;
    }

    public static void setAllCoinPairList(ArrayList<String> allCoinPairList) {
        Agent.allCoinPairList.addAll(allCoinPairList);
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

    public static ArraysOfStrategies getArraysOfStrategies() {
        return arraysOfStrategies;
    }

    public static void setArraysOfStrategies(ArraysOfStrategies arraysOfStrategies) {
        Agent.arraysOfStrategies = arraysOfStrategies;
    }

    public static FilesAndPathCreator getFilesAndPathCreator() {
        return filesAndPathCreator;
    }

    public static void setFilesAndPathCreator(FilesAndPathCreator filesAndPathCreator) {
        Agent.filesAndPathCreator = filesAndPathCreator;
    }

    public static ReadKeysAndSettings getReadKeysAndSettings() {
        return readKeysAndSettings;
    }

    public static void setReadKeysAndSettings(ReadKeysAndSettings readKeysAndSettings) {
        Agent.readKeysAndSettings = readKeysAndSettings;
    }

    public static int getDateDifference() {
        return DateDifference;
    }

    public static void setDateDifference(int dateDifference) {
        DateDifference = dateDifference;
    }

    public static WriterAndReadFile getWriterAndReadFile() {
        return writerAndReadFile;
    }

    public static void setWriterAndReadFile(WriterAndReadFile writerAndReadFile) {
        Agent.writerAndReadFile = writerAndReadFile;
    }

    public static CreatesTemplatesAndData getCreatesTemplatesAndData() {
        return createsTemplatesAndData;
    }

    public static void setCreatesTemplatesAndData(CreatesTemplatesAndData createsTemplatesAndData) {
        Agent.createsTemplatesAndData = createsTemplatesAndData;
    }

    public static WriteKeysAndSettings getWriteKeysAndSettings() {
        return writeKeysAndSettings;
    }

    public static void setWriteKeysAndSettings(WriteKeysAndSettings writeKeysAndSettings) {
        Agent.writeKeysAndSettings = writeKeysAndSettings;
    }

    public static MainPageController getMainPageController() {
        return mainPageController;
    }

    public static void setMainPageController(MainPageController mainPageController) {
        Agent.mainPageController = mainPageController;
    }

    public static API getApi() {
        return api;
    }

    public static void setApi(API api) {
        Agent.api = api;
    }

    public static ArraysOfWebSockets getArraysOfWebSockets() {
        return arraysOfWebSockets;
    }

    public static void setArraysOfWebSockets(ArraysOfWebSockets arraysOfWebSockets) {
        Agent.arraysOfWebSockets = arraysOfWebSockets;
    }

    public static ArrayList<String> getViewPairSockets() {
        return viewPairSockets;
    }

    public static void addViewPairSockets(String in) {
        Agent.viewPairSockets.add(in);
    }

    public static boolean isStartAllOrStopAll() {
        return startAllOrStopAll;
    }

    public static void setStartAllOrStopAll(boolean startAllOrStopAll) {
        Agent.startAllOrStopAll = startAllOrStopAll;
    }
}
