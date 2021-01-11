package main.model.binance;


import com.google.gson.JsonObject;
import main.model.Agent;
import main.model.binance.api.BinanceAPI;
import main.model.binance.api.BinanceApiException;
import main.model.binance.datatype.BinanceEventDepthLevelUpdate;
import main.model.binance.datatype.BinanceEventDepthUpdate;
import main.model.binance.datatype.BinanceSymbol;
import main.model.binance.websocket.BinanceWebSocketAdapterDepth;
import main.model.binance.websocket.BinanceWebSocketAdapterDepthLevel;
import main.model.binance.websocket.WebSocketAdapterDepth;

import javax.websocket.Session;


public class MainBinance {

    public static void main(String[] args) {

        try {
            BinanceAPI binanceAPI = new BinanceAPI(API.getApiKey(), API.getSecretKey());
            BinanceSymbol binanceSymbol = new BinanceSymbol("BTC");
            String keyForWebSocket = binanceAPI.startUserDataStream();
            System.out.println(keyForWebSocket);
//            JsonObject jsonObject = binanceAPI.keepUserDataStream(keyForWebSocket);
//            System.out.println();
            WebSocketAdapterDepth webSocketAdapterDepth = new WebSocketAdapterDepth();

            Session session = (Session) binanceAPI.webSocketDepth(binanceSymbol, webSocketAdapterDepth);
            webSocketAdapterDepth.onMessage(Agent.getBinanceEventDepthUpdate());

//            System.out.println("ETH-BTC PRICE=" + binanceAPI.pricesMap().get("ETHBTC"));
//            System.out.println("balans" + binanceAPI.balances());
        } catch (BinanceApiException e) {
            System.out.println( "ERROR: " + e.getMessage());
        }
    }
}
