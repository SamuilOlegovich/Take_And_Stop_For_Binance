package main.model.binance;


import com.google.gson.JsonObject;
import main.model.binance.api.BinanceAPI;
import main.model.binance.api.BinanceApiException;
import main.model.binance.datatype.BinanceSymbol;
import main.model.binance.datatype.BinanceTicker;

import javax.websocket.Session;
import java.util.Map;


public class MainBinance {

    public static void main(String[] args) {

        try {
            BinanceAPI binanceAPI = new BinanceAPI(API.getApiKey(), API.getSecretKey());
            BinanceSymbol binanceSymbol = new BinanceSymbol("ETHBTC");
            String keyForWebSocket = binanceAPI.startUserDataStream();
            System.out.println(keyForWebSocket);
            JsonObject jsonObject = binanceAPI.keepUserDataStream(keyForWebSocket);
            System.out.println(jsonObject.toString());
            Map<String, BinanceTicker> map = binanceAPI.allBookTickersMap();
            System.out.println(map.get("BTCUSDT").symbol
                    + " => " + map.get("BTCUSDT").askPrice);

//            JsonObject jsonObject = binanceAPI.keepUserDataStream(keyForWebSocket);
//            System.out.println();
//            webSocketAdapterDepth.onMessage(Agent.getBinanceEventDepthUpdate());

//            System.out.println("ETH-BTC PRICE=" + binanceAPI.pricesMap().get("ETHBTC"));
//            System.out.println("balans" + binanceAPI.balances());
        } catch (BinanceApiException e) {
            System.out.println( "ERROR: --- " + e.getMessage());
        }
    }
}
