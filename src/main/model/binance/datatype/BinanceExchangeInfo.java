package main.model.binance.datatype;
/* ============================================================
 * java-test.resources.model.binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import main.model.binance.api.BinanceApiException;


import java.util.LinkedList;
import java.util.List;

/*
{"timezone":"UTC","serverTime":1515514334979,
        "rateLimits":[
        {"rateLimitType":"REQUESTS","interval":"MINUTE","limit":1200},
        {"rateLimitType":"ORDERS","interval":"SECOND","limit":10},
        {"rateLimitType":"ORDERS","interval":"DAY","limit":100000}
        ],
        "exchangeFilters":[],"symbols":[]
}
*/
@Data
@Slf4j
public class BinanceExchangeInfo {

    List<BinanceExchangeSymbol> symbols = new LinkedList<>();
    List<BinanceRateLimit> rateLimits = new LinkedList<>();
    List<JsonObject> exchangeFilters = new LinkedList<>(); // missing proper documentation on that yet
    String timezone = null;
    Long serverTime = 0L;

    public BinanceExchangeInfo(JsonObject obj) throws BinanceApiException {
        if (obj.has("timezone")) {
            timezone = obj.get("timezone").getAsString();
        }
        if (obj.has("serverTime")) {
            serverTime = obj.get("serverTime").getAsLong();
        }
        if (obj.has("rateLimits") && obj.get("rateLimits").isJsonArray()) {
            JsonArray arrRateLimits = obj.get("rateLimits").getAsJsonArray();
            rateLimits.clear();
            for (JsonElement entry: arrRateLimits) {
                BinanceRateLimit limit = new BinanceRateLimit(entry.getAsJsonObject());
                rateLimits.add(limit);
            }
        }
        if (obj.has("exchangeFilters") && obj.get("exchangeFilters").isJsonArray()) {
            JsonArray arrExchangeFilters = obj.get("exchangeFilters").getAsJsonArray();
            exchangeFilters.clear();
            for (JsonElement entry: arrExchangeFilters) {
                exchangeFilters.add(entry.getAsJsonObject());
            }
        }
        if (obj.has("symbols") && obj.get("symbols").isJsonArray()) {
            JsonArray arrSymbols = obj.get("symbols").getAsJsonArray();
            symbols.clear();
            for (JsonElement entry: arrSymbols) {
                JsonObject jsonObject = entry.getAsJsonObject();
                if (!jsonObject.has("symbol")) continue;
                String sym = jsonObject.get("symbol").getAsString();
                if (sym.equals("123456")) continue; // some special symbol that doesn't fit

                BinanceExchangeSymbol symbol = new BinanceExchangeSymbol(jsonObject);
                symbols.add(symbol);
            }
        }
    }

    /////////////////////////////////////    I had to add == пришлось добавить   ///////////////////////////////////////

    public static List<BinanceExchangeSymbol> getSymbols() {
        return symbols;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
