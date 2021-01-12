package main.model.binance.websocket;

import main.model.binance.api.BinanceApiException;
import main.model.binance.datatype.BinanceEventDepthUpdate;



public class WebSocketAdapterDepth extends BinanceWebSocketAdapterDepth {

    @Override
    public void onMessage(BinanceEventDepthUpdate event) throws BinanceApiException {

        System.out.println("DDD");
//        System.out.println(event.eventTime);
//        System.out.println(event.updateId);
//        System.out.println(event.asks);
//        System.out.println(event.bids);
//        System.out.println(event.symbol);
    }
}
