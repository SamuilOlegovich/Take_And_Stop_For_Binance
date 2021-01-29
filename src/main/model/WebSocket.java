package main.model;

import main.controller.MainPageController;
import main.model.binance.api.BinanceApiException;
import main.model.binance.datatype.BinanceEventDepthUpdate;
import main.model.binance.datatype.BinanceSymbol;
import main.model.binance.websocket.BinanceWebSocketAdapterDepth;
import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;




public class WebSocket implements Runnable {
    private BinanceEventDepthUpdate binanceEventDepthUpdate;
//    private final ArraysOfWebSockets arraysOfWebSockets;
    private final ArraysOfStrategies arraysOfStrategies;
    private final ReadsAndTransmits readsAndTransmits;
    private final Thread thread;

//    private MainPageController mainPageController;

    private final ArrayList<StrategyObject> arrayList;
    private final String symbol;

    private boolean isBinanceSymbol;
    private boolean stopStartThread;
    private boolean stopAndStartAll;

    private Session session;

    private String id;

    private Double previousPriceValue;




    public WebSocket(StrategyObject strategyObject) {
        this.arraysOfStrategies = Agent.getArraysOfStrategies();
//        this.arraysOfWebSockets = Agent.getArraysOfWebSockets();
        this.readsAndTransmits = new ReadsAndTransmits();
        this.symbol = strategyObject.getTradingPair();
        this.thread = new Thread(this);
        this.arrayList = new ArrayList<>();
        this.previousPriceValue = 0.0;
        arrayList.add(strategyObject);
        this.isBinanceSymbol = true;
        this.stopStartThread = true;
        stopAndStartAll = true;
        thread.start();
        readsAndTransmits.start();
    }

    public WebSocket(String in) {
        this.arraysOfStrategies = Agent.getArraysOfStrategies();
//        this.arraysOfWebSockets = Agent.getArraysOfWebSockets();
        this.readsAndTransmits = new ReadsAndTransmits();
        this.thread = new Thread(this);
        this.arrayList = new ArrayList<>();
        this.previousPriceValue = 0.0;
        this.stopStartThread = true;
        stopAndStartAll = false;
        this.symbol = in;
        thread.start();
        readsAndTransmits.start();
    }



    @Override
    public void run() {
        started();
    }

    private void started() {
        BinanceSymbol binanceSymbol;
        try {
            if (isBinanceSymbol) {
                binanceSymbol = new BinanceSymbol(arrayList.get(0).getTradingPair());
            } else {
                binanceSymbol = new BinanceSymbol(symbol);
            }
            session = Agent.getBinanceAPI().webSocketDepth(binanceSymbol,
                    new BinanceWebSocketAdapterDepth() {
                        @Override
                        public void onMessage(BinanceEventDepthUpdate message) {
                            binanceEventDepthUpdate = message;
//                            System.out.println(symbol + "===" + message.asks.get(0).price.toString());
                        }
                    });
            try { Thread.sleep(5000);
                System.out.println("2"); }
            catch (InterruptedException e) {
                session.close();
                System.out.println("3");////////////////////////////////////////
                stopStartThread = true;
            }
        } catch (BinanceApiException e) {
            System.out.println("3");
            session.close();
            stopStartThread = false;
//            throw new BinanceApiException("Сокет не подключился => " + e);
        }
    }



    private class ReadsAndTransmits extends Thread {

        @Override
        public void run() {
            while (stopStartThread) {
                try {
                    if (stopAndStartAll) {
//                    System.out.println(symbol + "===" + binanceEventDepthUpdate.asks.get(0).price.toString());
//                            BigDecimal priceAsk = binanceEventDepthUpdate.asks.get(0).price;
//                            BigDecimal priceBid = binanceEventDepthUpdate.bids.get(0).price;
                        Double priceAsk = binanceEventDepthUpdate.asks.get(0).price.doubleValue();
                        Double priceBid = binanceEventDepthUpdate.bids.get(0).price.doubleValue();
                        previousPriceValue = priceBid;
                        String string = "";
                        int index = -1;

                        for (StrategyObject strategyObject : arrayList) {
                            if (strategyObject.getWorks() && !strategyObject.getClassID().equals(id)) {
                                strategyObject.setPriceAskAndBidNow(priceAsk, priceBid);
                            } else {
                                index = arrayList.indexOf(strategyObject);
                                string = strategyObject.getClassID();
                            }
                        }

                        if (index >= 0 && !string.equals(id) ) {
                            arraysOfStrategies.replaceStrategy(arrayList.get(index));
                            arrayList.remove(index);
                        } else if (index >= 0) {
                            arrayList.remove(index);
                        }
//                            if (arrayList.size() == 0) arraysOfWebSockets.closeWebSocket(symbol);
                    }
                }
                catch (NullPointerException e) {}
                catch (IndexOutOfBoundsException e) {
                    System.out.println("310");////////////////////////////////////////////
                }
                try { Thread.sleep(1000);
                } catch (InterruptedException e) { stopStartThread = false; }
            }
            session.close();
            thread.interrupt();
            readsAndTransmits.interrupt();
        }
    }


    public Double getPriceNow() {
        Double priceBid = 0.0;
        try {
            priceBid = binanceEventDepthUpdate.bids.get(0).price.doubleValue();
            previousPriceValue = priceBid;
        }
        catch (NullPointerException e) { return 0.0; }
        catch (IndexOutOfBoundsException e) { return previousPriceValue; }
        return priceBid;
    }



    public void addStrategyObject(StrategyObject strategyObject) {
        if (!arrayList.contains(strategyObject)) {
            arrayList.add(strategyObject);
        }
    }

    public void removeStrategyObject(StrategyObject strategyObject) { this.id = strategyObject.getClassID(); }
    public void setSocket() { this.stopStartThread = false; }
    public void stopAll() { this.stopAndStartAll = false; }
    public void startAll() { this.stopAndStartAll = true; }

//    public void mainInfoPrice(boolean) {

//    }
}
