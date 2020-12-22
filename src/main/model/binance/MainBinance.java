package main.model.binance;


public class MainBinance {
    private static String KEY_API = "VNxOm5Z4JL6qkaD3pNY9TApQKxJmxz0UJBIpIyXMYofkJTtavV8ILePM2PxHXOiY";
    private static String SICRET_KEY = "1BDo8bFIU0HBMY8nFC0nhCIvrGl4Kixz4EsiEMMMyVo3wGDskaPvvPjBs0eucR8r";

    public static void main(String[] args) {

        try {
            com.webcerebrium.binance.api.BinanceApi api = new com.webcerebrium.binance.api.BinanceApi(KEY_API, SICRET_KEY);
//            JsonObject account = api.account();
//            System.out.println("ETH-BTC PRICE=" + api.pricesMap().get("ETHBTC"));
//            System.out.println(api.ping().toString());
            System.out.println(api.balances());
        } catch (com.webcerebrium.binance.api.BinanceApiException e) {
            System.out.println( "ERROR: " + e.getMessage());
        }
    }
}
