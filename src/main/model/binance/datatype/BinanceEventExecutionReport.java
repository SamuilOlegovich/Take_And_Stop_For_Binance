package main.model.binance.datatype;/* ============================================================
 * java-test.resources.model.binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

import com.google.gson.JsonObject;
import com.webcerebrium.binance.api.BinanceApiException;
import lombok.Data;

import java.math.BigDecimal;

/*
 {
 "e": "executionReport",		// order or trade report
 "E": 1499406026404,			// event time
 "s": "ETHBTC",					// symbol
 "c": "1hRLKJhTRsXy2ilYdSzhkk",	// newClientOrderId
 "S": "BUY",					// side: buy or sell
 "o": "LIMIT",					// order type LIMIT, MARKET
 "f": "GTC",					// time in force, GTC: Good Till Cancel, IOC: Immediate or Cancel
 "q": "22.42906458",			// original quantity
 "p": "0.10279999",				// price
 "P": "0.00000000", //? undocumented?
 "F": "0.00000000", //? undocumented?
 "g": -1,           //? undocumented?
 "C": "null",       //? undocumented?
 "x": "TRADE",					// executionType NEW, CANCELED, REPLACED, REJECTED, TRADE,EXPIRED
 "X": "NEW", 				    // orderStatus NEW, PARTIALLY_FILLED, FILLED, CANCELED，PENDING_CANCEL, REJECTED, EXPIRED
 "r": "NONE", 					// orderRejectReason，NONE, UNKNOWN_INSTRUMENT, MARKET_CLOSED, PRICE_QTY_EXCEED_HARD_LIMITS, UNKNOWN_ORDER, DUPLICATE_ORDER, UNKNOWN_ACCOUNT, INSUFFICIENT_BALANCE, ACCOUNT_INACTIVE, ACCOUNT_CANNOT_SETTLE
 "i": 4294220,					// orderid

 "l": "17.42906458",				// quantity of last filled trade
 "z": "22.42906458",				// accumulated quantity of filled trades on this order
 "L": "0.10279999",				// price of last filled trade
 "n": "0.00000001",				// commission
 "N": "BNC",						// asset on which commission is taken
 "T": 1499406026402,				// trade time
 "t": 77517,						// trade id
 "I": 8644124,					// can be ignored
 "w": false,						// can be ignored
 "m": false,						// is buyer maker
 "M": true						// can be ignored
 }
*/

@Data
public class BinanceEventExecutionReport {

    public com.webcerebrium.binance.datatype.BinanceTimeInForce timeInForce;
    public String newClientOrderId;
    public com.webcerebrium.binance.datatype.BinanceOrderSide side;
    public com.webcerebrium.binance.datatype.BinanceOrderType type;
    public com.webcerebrium.binance.datatype.BinanceSymbol symbol;
    public Long eventTime;

    public BigDecimal quantity;
    public BigDecimal price;

    public com.webcerebrium.binance.datatype.BinanceExecutionType executionType;
    public com.webcerebrium.binance.datatype.BinanceRejectReason rejectReason;
    public com.webcerebrium.binance.datatype.BinanceOrderStatus status;

    public BigDecimal accumulatedQuantityOfFilledTrades;
    public BigDecimal quantityOfLastFilledTrade;
    public BigDecimal priceOfLastFilledTrade;
    public BigDecimal commission;
    public Long orderId;

    public String assetOfCommission;
    public boolean isMaker;
    public Long tradeTime;
    public Long tradeId;

    public BinanceEventExecutionReport(JsonObject event) throws BinanceApiException {
        eventTime = event.get("E").getAsLong();
        symbol = com.webcerebrium.binance.datatype.BinanceSymbol.valueOf(event.get("s").getAsString());
        newClientOrderId = event.get("c").getAsString();

        side = com.webcerebrium.binance.datatype.BinanceOrderSide.valueOf(event.get("S").getAsString()); // was using "c" again
        type = com.webcerebrium.binance.datatype.BinanceOrderType.valueOf(event.get("o").getAsString());
        timeInForce = com.webcerebrium.binance.datatype.BinanceTimeInForce.valueOf(event.get("f").getAsString());

        price = event.get("p").getAsBigDecimal();
        quantity = event.get("q").getAsBigDecimal();

        executionType = com.webcerebrium.binance.datatype.BinanceExecutionType.valueOf(event.get("x").getAsString());
        status = com.webcerebrium.binance.datatype.BinanceOrderStatus.valueOf(event.get("X").getAsString());
        rejectReason = com.webcerebrium.binance.datatype.BinanceRejectReason.valueOf(event.get("r").getAsString());

        orderId = event.get("i").getAsLong();

        quantityOfLastFilledTrade = event.get("l").getAsBigDecimal();
        accumulatedQuantityOfFilledTrades = event.get("z").getAsBigDecimal();
        priceOfLastFilledTrade = event.get("L").getAsBigDecimal();
        commission = event.get("n").getAsBigDecimal();

        //assetOfCommission = event.get("N").getAsString();
        assetOfCommission = (event.get("N").isJsonNull()? "" : event.get("N").getAsString()); // Binance API returns null for orders, only used for trades

        tradeTime = event.get("T").getAsLong();
        tradeId = event.get("t").getAsLong();
        isMaker = event.get("m").getAsBoolean();
    }
}
