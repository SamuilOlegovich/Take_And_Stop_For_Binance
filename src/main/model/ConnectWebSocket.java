package main.model;

import main.model.binance.api.BinanceApiException;
import main.model.binance.datatype.BinanceEventDepthUpdate;
import main.model.binance.datatype.BinanceSymbol;
import main.model.binance.websocket.BinanceWebSocketAdapterDepth;
import org.eclipse.jetty.websocket.api.Session;



public class ConnectWebSocket implements Runnable {

//    @SneakyThrows
    @Override
    public void run() {
        started();
    }


    private void started() {
        try {
            new WriteDownKeysAndSettings();
            BinanceSymbol binanceSymbol = new BinanceSymbol("ETHBTC");
            Session session = Agent.getBinanceAPI().webSocketDepth(binanceSymbol,
                    new BinanceWebSocketAdapterDepth() {
                        @Override
                        public void onMessage(BinanceEventDepthUpdate message) {
                            System.out.println(message.toString());
                        }
                    });
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            session.close();
        } catch (BinanceApiException e) {
//            throw new BinanceApiException("Сокет не подключился => " + e);
            System.out.println("Сокет не подключился => " + e);
        }
    }
}
