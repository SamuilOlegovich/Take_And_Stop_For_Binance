package main.model.binance.datatype;
/* ============================================================
 * java-test.resources.model.binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

import com.google.gson.JsonObject;
import lombok.Data;
import main.model.binance.api.BinanceApiException;


import java.math.BigDecimal;

/**
 {
 "e": "aggTrade",		// event type
 "E": 1499405254326,	// event time
 "s": "ETHBTC",			// symbol
 "a": 70232,			// aggregated tradeid => агрегированный tradeid
 "p": "0.10281118",		// price
 "q": "8.15632997",		// quantity => количество
 "f": 77489,			// first breakdown trade id => идентификатор сделки первой разбивки
 "l": 77489,			// last breakdown trade id => идентификатор сделки последней разбивки
 "T": 1499405254324,	// trade time
 "m": false,			// whehter buyer is a maker => является ли покупатель производителем
 "M": true				// can be ignored => можно игнорировать
 }
 */
@Data
public class BinanceEventAggTrade {

    public Long firstBreakdownTradeId;
    public Long lastBreakdownTradeId;
    public Long aggregatedTradeId;
    public BinanceSymbol symbol;
    public BigDecimal quantity;
    public BigDecimal price;
    public boolean isMaker;
    public Long eventTime;
    public Long tradeTime;

    public BinanceEventAggTrade(JsonObject event) throws BinanceApiException {
        eventTime = event.get("E").getAsLong();
        symbol = BinanceSymbol.valueOf(event.get("s").getAsString());
        aggregatedTradeId = event.get("a").getAsLong();
        price = event.get("p").getAsBigDecimal();
        quantity = event.get("q").getAsBigDecimal();
        firstBreakdownTradeId = event.get("f").getAsLong();
        lastBreakdownTradeId = event.get("l").getAsLong();
        tradeTime = event.get("T").getAsLong();
        isMaker = event.get("m").getAsBoolean();
    }
}
