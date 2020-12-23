package main.model.binance.datatype;/* ============================================================
 * java-test.resources.model.binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

import com.google.gson.JsonArray;

import java.math.BigDecimal;

@Data
public class BinanceBidOrAsk {

    public BigDecimal quantity = null;
    public BigDecimal price = null;
    public BinanceBidType type;

    public BinanceBidOrAsk() {}

    public BinanceBidOrAsk(BinanceBidType type, JsonArray arr) {
        this.type = type;
        this.price = arr.get(0).getAsBigDecimal();
        this.quantity = arr.get(1).getAsBigDecimal();
    }
}
