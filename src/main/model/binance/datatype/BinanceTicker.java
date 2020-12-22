package main.model.binance.datatype;
/* ============================================================
 * java-test.resources.model.binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

/*
"symbol": "LTCBTC",
"bidPrice": "4.00000000",
"bidQty": "431.00000000",
"askPrice": "4.00000200",
"askQty": "9.00000000"
*/

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BinanceTicker {
    public BigDecimal bidPrice = null;
    public BigDecimal askPrice = null;
    public BigDecimal bidQty = null;
    public BigDecimal askQty = null;
    public String symbol = null;

    /////////////////////////////////////    I had to add == пришлось добавить   ///////////////////////////////////////

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getBidQty() {
        return bidQty;
    }

    public BigDecimal getAskQty() {
        return askQty;
    }

    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    public BigDecimal getAskPrice() {
        return askPrice;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
