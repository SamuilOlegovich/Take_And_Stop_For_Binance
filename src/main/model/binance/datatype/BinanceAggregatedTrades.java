package main.model.binance.datatype;
/* ============================================================
 * java-test.resources.model.binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

// Internal variables are not human readable. So this class contains better readable getters

//  {
// "a": 26129,         // Aggregate tradeId
// "p": "0.01633102",  // Price
// "q": "4.70443515",  // Quantity
// "f": 27781,         // First tradeId
// "l": 27781,         // Last tradeId
// "T": 1498793709153, // Timestamp
// "m": true,          // Was the buyer the maker?
// "M": true           // Was the trade the best price match?
// }


import java.math.BigDecimal;






@Data
public class BinanceAggregatedTrades {

    //
    public BigDecimal q;
    public BigDecimal p;
    public boolean m;
    public boolean M;
    public long a;
    public long f;
    public long l;
    public long T;

    public BigDecimal getQuantity() { return q; }
    public boolean wasBestPrice() { return M; }
    public long getFirstTradeId() { return f; }
    public long getLastTradeId() { return l; }
    public BigDecimal getPrice() { return p; }
    public long getTimestamp() { return T; }
    public boolean wasMaker() { return m; }
    public long getTradeId() { return a; }

    @Override
    public String toString() {
        return "BinanceAggregatedTrades{" +
            "tradeId=" + getTradeId() +
            ", price=" + getPrice() +
            ", quantity=" + getQuantity() +
            ", firstTradeId=" + getFirstTradeId() +
            ", lastTradeId=" + getLastTradeId() +
            ", timestamp=" + getTimestamp() +
            ", maker=" + wasMaker() +
            ", bestPrice=" + wasBestPrice() +
            '}';
    }
}
