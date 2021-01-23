package main.model;

import lombok.SneakyThrows;
import main.model.binance.api.BinanceApiException;
import main.model.binance.datatype.BinanceEventDepthUpdate;
import main.model.binance.datatype.BinanceSymbol;
import main.model.binance.websocket.BinanceWebSocketAdapterDepth;
import org.eclipse.jetty.websocket.api.Session;

import java.math.BigDecimal;
import java.util.ArrayList;




public class WebSocket implements Runnable {
    private final ArraysOfWebSockets arraysOfWebSockets;
    private final ArraysOfStrategies arraysOfStrategies;
    private final ArrayList<StrategyObject> arrayList;
    private final String symbol;
    private final Thread thread;
    private String id;

    private BinanceEventDepthUpdate binanceEventDepthUpdate = null;
    private Session session;
    private Read read;
    private boolean aBoolean;



    public WebSocket(StrategyObject strategyObject) {
        this.arraysOfStrategies = Agent.getArraysOfStrategies();
        this.arraysOfWebSockets = Agent.getArraysOfWebSockets();
        this.symbol = strategyObject.getTradingPair();
        this.thread = new Thread(this);
        this.arrayList = new ArrayList<>();
        arrayList.add(strategyObject);
        this.read = new Read();
        this.aBoolean = true;
        thread.start();
    }



//    @SneakyThrows
    @Override
    public void run() {
        started();
    }



    private void started() {
        try {
            BinanceSymbol binanceSymbol = new BinanceSymbol(arrayList.get(0).getTradingPair());
            session = Agent.getBinanceAPI().webSocketDepth(binanceSymbol,
                    new BinanceWebSocketAdapterDepth() {
                        @Override
                        public void onMessage(BinanceEventDepthUpdate message) {
                            if (aBoolean) {
                                read.start();
                            }
                            binanceEventDepthUpdate = message;
//                            System.out.println(symbol + "===" + message.asks.get(0).price.toString());
//                            Thread thread = new Thread();
//                            thread.start();
//                            sendMessagesToAll(message);

                        }
                    });
            try { Thread.sleep(5000);
                System.out.println("2"); }
            catch (InterruptedException e) {
                read.interrupt();
                System.out.println("3");
                session.close(); }
        } catch (BinanceApiException e) {
            System.out.println("3");
//            session.close();
//            throw new BinanceApiException("Сокет не подключился => " + e);
        }
    }

    private class Read extends Thread {

        @Override
        public void run() {
            aBoolean = false;
            while (true) {
                try {
                    System.out.println(symbol + "===" + binanceEventDepthUpdate.asks.get(0).price.toString());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("310");

                }
            }
        }
    }



    private synchronized void sendMessagesToAll(BinanceEventDepthUpdate message) {
        BigDecimal priceAsk = message.asks.get(0).price;
        BigDecimal priceBid = message.bids.get(0).price;
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
        if (arrayList.size() == 0) arraysOfWebSockets.closeWebSocket(symbol);
    }


    public void removeStrategyObject(StrategyObject strategyObject) { this.id = strategyObject.getClassID(); }
    public void addStrategyObject(StrategyObject strategyObject) { arrayList.add(strategyObject); }
    public Thread getThread() { return thread; }
}
