package main.model.binance.datatype;/* ============================================================
 * java-test.resources.model.binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Data;
import main.model.Agent;
import main.model.binance.api.BinanceApiException;

import java.util.LinkedList;
import java.util.List;

/*
	{
        "e": "depthUpdate",						// event type
        "E": 1499404630606, 					// event time
        "s": "ETHBTC", 							// symbol
        "u": 7913455, 							// updateId to sync up with updateid in /api/v1/depth
        "b": [									// bid depth delta
            [
                "0.10376590", 					// price (need to upate the quantity on this price)
                "59.15767010", 					// quantity
                []								// can be ignored
            ],
        ],
        "a": [									// ask depth delta
            [
                "0.10376586", 					// price (need to upate the quantity on this price)
                "159.15767010", 				// quantity
                []								// can be ignored
            ],
            [
                "0.10383109",
                "345.86845230",
                []
            ],
            [
                "0.10490700",
                "0.00000000", 					//quantitiy=0 means remove this level
                []
            ]
        ]
    }
 */
@Data
public class BinanceEventDepthUpdate {
    public List<BinanceBidOrAsk> bids = null;
    public List<BinanceBidOrAsk> asks = null;
    public BinanceSymbol symbol;
    public Long eventTime;
    public Long updateId;

    public BinanceEventDepthUpdate(JsonObject event) throws BinanceApiException {
        eventTime = event.get("E").getAsLong();
        symbol = BinanceSymbol.valueOf(event.get("s").getAsString());
        updateId = event.get("u").getAsLong();

        bids = new LinkedList<>();
        JsonArray b = event.get("b").getAsJsonArray();
        for (JsonElement bidElement : b) {
            bids.add( new BinanceBidOrAsk(BinanceBidType.BID, bidElement.getAsJsonArray()));
        }
        asks = new LinkedList<>();
        JsonArray a = event.get("a").getAsJsonArray();
        for (JsonElement askElement : a) {
            asks.add( new BinanceBidOrAsk(BinanceBidType.ASK, askElement.getAsJsonArray()));
        }

        Agent.setBinanceEventDepthUpdate(this);
    }
}
