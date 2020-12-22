package main.model.binance.datatype;
/* ============================================================
 * java-test.resources.model.binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

import com.webcerebrium.binance.api.BinanceApiException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import lombok.extern.slf4j.Slf4j;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.List;

/**
 {
     "symbol":"ETHBTC",
     "status":"TRADING",
     "baseAsset":"ETH",
     "baseAssetPrecision":8,
     "quoteAsset":"BTC",
     "quotePrecision":8,
     "orderTypes":["LIMIT","LIMIT_MAKER","MARKET","STOP_LOSS_LIMIT","TAKE_PROFIT_LIMIT"],
     "icebergAllowed":true,
     "filters":
     [
        {"filterType":"PRICE_FILTER","minPrice":"0.00000100","maxPrice":"100000.00000000","tickSize":"0.00000100"},
        {"filterType":"LOT_SIZE","minQty":"0.00100000","maxQty":"100000.00000000","stepSize":"0.00100000"},
        {"filterType":"MIN_NOTIONAL","minNotional":"0.00100000"}
     ]
 }
 */
@Slf4j
@Data
public class BinanceExchangeSymbol {

    private static final Logger log = LoggerFactory.getLogger(BinanceExchangeSymbol.class);

    List<com.webcerebrium.binance.datatype.BinanceOrderType> orderTypes = new LinkedList<>();
    HashMap<String, JsonObject> filters = new HashMap<>();
    Long baseAssetPrecision;
    boolean icebergAllowed;
    com.webcerebrium.binance.datatype.BinanceSymbol symbol;
    Long quotePrecision;
    String quoteAsset;
    String baseAsset;
    String status;

    public BinanceExchangeSymbol(JsonObject obj) throws BinanceApiException {
        // log.debug("Reading Symbol {}, {}", obj.get("symbol").getAsString(), obj.toString());

        symbol = com.webcerebrium.binance.datatype.BinanceSymbol.valueOf(obj.get("symbol").getAsString());
        status = obj.get("status").getAsString();

        baseAsset = obj.get("baseAsset").getAsString();
        baseAssetPrecision = obj.get("baseAssetPrecision").getAsLong();
        quoteAsset = obj.get("quoteAsset").getAsString();
        quotePrecision = obj.get("quotePrecision").getAsLong();
        icebergAllowed = obj.get("icebergAllowed").getAsBoolean();

        if (obj.has("orderTypes") && obj.get("orderTypes").isJsonArray()) {
            JsonArray arrOrderTypes = obj.get("orderTypes").getAsJsonArray();
            orderTypes.clear();
            for (JsonElement entry: arrOrderTypes) {
                orderTypes.add(com.webcerebrium.binance.datatype.BinanceOrderType.valueOf(entry.getAsString()));
            }
        }

        if (obj.has("filters") && obj.get("filters").isJsonArray()) {
            JsonArray arrFilters = obj.get("filters").getAsJsonArray();
            filters.clear();
            for (JsonElement entry: arrFilters) {
                JsonObject item = entry.getAsJsonObject();
                String key = item.get("filterType").getAsString();
                filters.put(key, item);
            }
        }
    }

    public JsonObject getPriceFilter() {
        return filters.get("PRICE_FILTER");
    }

    public JsonObject getLotSize() {
        return filters.get("LOT_SIZE");
    }

    public JsonObject getMinNotional() {
        return filters.get("MIN_NOTIONAL");
    }

    public BigDecimal getMinNotionalValue() {
        if (filters.containsKey("MIN_NOTIONAL")) {
            JsonObject obj = this.getMinNotional();
            if (obj.has("minNotional")) {
                return obj.get("minNotional").getAsBigDecimal();
            }
        }
        return BigDecimal.ZERO;
    }

    /////////////////////////////////////    I had to add == пришлось добавить   ///////////////////////////////////////

    public String getBaseAsset() {
        return baseAsset;
    }

    public String getQuoteAsset() {
        return quoteAsset;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
