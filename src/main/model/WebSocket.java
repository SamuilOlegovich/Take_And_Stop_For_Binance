package main.model;

import main.model.binance.api.BinanceApiException;
import main.model.binance.datatype.BinanceEventDepthUpdate;
import main.model.binance.datatype.BinanceSymbol;
import main.model.binance.websocket.BinanceWebSocketAdapterDepth;
import org.eclipse.jetty.websocket.api.Session;

import java.math.BigDecimal;
import java.util.ArrayList;




public class WebSocket implements Runnable {
    private volatile BinanceEventDepthUpdate binanceEventDepthUpdate;
    private final ArraysOfStrategies arraysOfStrategies;
    private final ArrayList<StrategyObject> arrayList;
    private final Thread thread;
    private final String symbol;

    private SocketThread socketThread;
    private Double previousPriceValue;
    private Session session;
    private String id;

    private boolean isBinanceSymbol;
    private boolean stopStartThread;
    private boolean stopAndStartAll;



    public WebSocket(StrategyObject strategyObject) {
        this.arraysOfStrategies = Agent.getArraysOfStrategies();
        this.symbol = strategyObject.getTradingPair();
        this.arrayList = new ArrayList<>();
        this.previousPriceValue = 0.0;
        arrayList.add(strategyObject);
        this.isBinanceSymbol = true;
        this.stopStartThread = true;
        this.socketThread = null;
        stopAndStartAll = true;
        this.thread = new Thread(this);
        thread.start();
    }


    public WebSocket(String in) {
        this.arraysOfStrategies = Agent.getArraysOfStrategies();
        this.arrayList = new ArrayList<>();
        this.previousPriceValue = 0.0;
        this.stopStartThread = true;
        this.socketThread = null;
        stopAndStartAll = false;
        this.symbol = in;
        this.thread = new Thread(this);
        thread.start();
    }



    @Override
    public void run() {
        socketThread = new SocketThread();
        socketThread.start();
        readsAndTransmits();
    }

    private class SocketThread extends Thread {
        BinanceSymbol binanceSymbol;

        @Override
        public synchronized void start() {
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
                            }
                        });
                try {
                    Thread.sleep(5000);
                    System.out.println("2");
                } catch (InterruptedException e) {
                    System.out.println("3");////////////////////////////////////////
                    restartSocket();
                }
            } catch (BinanceApiException e) {
                System.out.println("4");
                restartSocket();
//            throw new BinanceApiException("Сокет не подключился => " + e);
            }
        }
    }



    private void readsAndTransmits() {
        int countExceptions = 0;
        while (stopStartThread) {
            try {
                if (stopAndStartAll) {
//                   System.out.println(symbol + "===" + binanceEventDepthUpdate.asks.get(0).price.toString());
//                    BigDecimal priceAsk = binanceEventDepthUpdate.asks.get(0).price;
//                    BigDecimal priceBid = binanceEventDepthUpdate.bids.get(0).price;
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

                    if (index >= 0 && string.equals(id) ) {
                        arraysOfStrategies.replaceStrategy(arrayList.get(index));
                        arrayList.remove(index);
                    }
//                            if (arrayList.size() == 0) arraysOfWebSockets.closeWebSocket(symbol);
                }
            }
            catch (NullPointerException e) {
                countExceptions++;
            }
            catch (IndexOutOfBoundsException e) {
                countExceptions++;
                System.out.println("310");////////////////////////////////////////////
            }
            try { Thread.sleep(1000);
            } catch (InterruptedException e) { stopStartThread = false; }

            if (countExceptions > 120) {
                restartSocket();
                System.out.println("Перезапустил сокет");////////////////////////////////////////////
                countExceptions = 0;
            }
        }
        closed();
        thread.interrupt();
    }


    private synchronized void restartSocket() {
        closed();
        socketThread = new SocketThread();
        socketThread.start();
        System.out.println("Перезапустил сокет");////////////////////////////////////////////
    }



    private synchronized void closed() {
        session.close();
        socketThread.interrupt();
        socketThread = null;
    }




    public Double getPriceNow() {
        double priceBid = 0.0;

        try {
            priceBid = binanceEventDepthUpdate.bids.get(0).price.doubleValue();
            previousPriceValue = priceBid;
        }
        catch (NullPointerException e) { return 0.0; }
        catch (IndexOutOfBoundsException e) { return previousPriceValue; }
        return priceBid;
    }



    public void addStrategyObject(StrategyObject strategyObject) {
        if (!arrayList.contains(strategyObject)) { arrayList.add(strategyObject); }
    }


    public void removeStrategyObject(StrategyObject strategyObject) { this.id = strategyObject.getClassID(); }
    public void setSocket() { this.stopStartThread = false; }
    public void stopAll() { this.stopAndStartAll = false; }
    public void startAll() { this.stopAndStartAll = true; }

}
