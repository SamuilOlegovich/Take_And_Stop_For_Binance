package main.model.binance;


import main.model.API;
import main.model.binance.api.BinanceAPI;
import main.model.binance.api.BinanceApiException;

public class MainBinance {
    private static String KEY_API = "VNxOm5Z4JL6qkaD3pNY9TApQKxJmxz0UJBIpIyXMYofkJTtavV8ILePM2PxHXOiY";
    private static String SICRET_KEY = "1BDo8bFIU0HBMY8nFC0nhCIvrGl4Kixz4EsiEMMMyVo3wGDskaPvvPjBs0eucR8r";

    public static void main(String[] args) {

        try {
//            BinanceAPI api = new BinanceAPI(KEY_API, SICRET_KEY);
            BinanceAPI binanceAPI = new BinanceAPI(API.getApiKey(), API.getSecretKey());
//            JsonObject account = api.account();
//            System.out.println("ETH-BTC PRICE=" + api.pricesMap().get("ETHBTC"));
//            System.out.println(api.ping().toString());
            System.out.println(binanceAPI.balances());
        } catch (BinanceApiException e) {
            System.out.println( "ERROR: " + e.getMessage());
        }
    }
}
