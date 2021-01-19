package main.model;

import lombok.SneakyThrows;
import main.model.binance.api.BinanceApiException;
import main.model.binance.datatype.BinanceEventDepthUpdate;
import main.model.binance.datatype.BinanceSymbol;
import main.model.binance.websocket.BinanceWebSocketAdapterDepth;
import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;


public class ConnectWebSocket implements Runnable {
    private final ArraysOfWebSockets arraysOfWebSockets;
    private final ArrayList<StrategyObject> arrayList;
    private final String symbol;
    private final Thread thread;



    public ConnectWebSocket(StrategyObject strategyObject) {
        this.arraysOfWebSockets = Agent.getArraysOfWebSockets();
        this.symbol = strategyObject.getTradingPair();
        this.thread = new Thread(this);
        this.arrayList = new ArrayList<>();
        arrayList.add(strategyObject);
        thread.start();
    }



    @SneakyThrows
    @Override
    public void run() {
        started();
    }



    private void started() throws BinanceApiException {
        try {
            BinanceSymbol binanceSymbol = new BinanceSymbol(arrayList.get(0).getTradingPair());
            Session session = Agent.getBinanceAPI().webSocketDepth(binanceSymbol,
                    new BinanceWebSocketAdapterDepth() {
                        @Override
                        public void onMessage(BinanceEventDepthUpdate message) {
//                            System.out.println(message.toString());
                            sendMessagesToAll(message);
                        }
                    });
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) { }
            session.close();
        } catch (BinanceApiException e) {
            throw new BinanceApiException("Сокет не подключился => " + e);
        }
    }


    private void sendMessagesToAll(BinanceEventDepthUpdate message) {
        int index = -1;
        for (StrategyObject strategyObject : arrayList) {
            if (strategyObject.getWorks()) {



            } else {
                index = arrayList.indexOf(strategyObject);
            }
        }
        if (index >= 0) arrayList.remove(index);
        if (arrayList.size() == 0) arraysOfWebSockets.closeWebSocket(symbol);

    }


    public void addStrategyObject(StrategyObject strategyObject) {
        arrayList.add(strategyObject);
    }


    public Thread getThread() {
        return thread;
    }
}
