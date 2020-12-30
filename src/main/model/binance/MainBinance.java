package main.model.binance;


import main.model.binance.api.BinanceAPI;
import main.model.binance.api.BinanceApiException;
import main.model.binance.datatype.BinanceSymbol;

public class MainBinance {

    public static void main(String[] args) {
        try {
            BinanceAPI binanceAPI = new BinanceAPI(API.getApiKey(), API.getSecretKey());
            BinanceSymbol binanceSymbol = new BinanceSymbol("BTC");
            System.out.println("ETH-BTC PRICE=" + binanceAPI.pricesMap().get("ETHBTC"));
            System.out.println("balans" + binanceAPI.balances());
        } catch (BinanceApiException e) {
            System.out.println( "ERROR: " + e.getMessage());
        }
    }
}
