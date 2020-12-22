package main.model.binance.datatype;

/* ============================================================
 * java-test.resources.model.binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

 /*
 {
     "symbol": "LTCBTC",
     "orderId": 1,
     "clientOrderId": "myOrder1",
     "price": "0.1",
     "origQty": "1.0",
     "executedQty": "0.0",
     "status": "NEW",
     "timeInForce": "GTC",
     "type": "LIMIT",
     "side": "BUY",
     "stopPrice": "0.0",
     "icebergQty": "0.0",
     "time": 1499827319559
 }
 */

import java.math.BigDecimal;

public class BinanceOrder {

    public BinanceTimeInForce timeInForce;
    public BinanceOrderStatus status;
    public BigDecimal executedQty;
    public BinanceOrderSide side;
    public BinanceOrderType type;
    public BigDecimal icebergQty;
    public BigDecimal stopPrice;
    public String clientOrderId;
    public BigDecimal origQty;
    public BigDecimal price;
    public String symbol;
    public Long orderId;
    public Long time;

    public BinanceOrder() {
    }

    public String getSymbol() {
        return this.symbol;
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public String getClientOrderId() {
        return this.clientOrderId;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public BigDecimal getOrigQty() {
        return this.origQty;
    }

    public BigDecimal getExecutedQty() {
        return this.executedQty;
    }

    public BinanceOrderStatus getStatus() {
        return this.status;
    }

    public BinanceTimeInForce getTimeInForce() {
        return this.timeInForce;
    }

    public BinanceOrderType getType() {
        return this.type;
    }

    public BinanceOrderSide getSide() {
        return this.side;
    }

    public BigDecimal getStopPrice() {
        return this.stopPrice;
    }

    public BigDecimal getIcebergQty() {
        return this.icebergQty;
    }

    public Long getTime() {
        return this.time;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setClientOrderId(String clientOrderId) {
        this.clientOrderId = clientOrderId;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setOrigQty(BigDecimal origQty) {
        this.origQty = origQty;
    }

    public void setExecutedQty(BigDecimal executedQty) {
        this.executedQty = executedQty;
    }

    public void setStatus(BinanceOrderStatus status) {
        this.status = status;
    }

    public void setTimeInForce(BinanceTimeInForce timeInForce) {
        this.timeInForce = timeInForce;
    }

    public void setType(BinanceOrderType type) {
        this.type = type;
    }

    public void setSide(BinanceOrderSide side) {
        this.side = side;
    }

    public void setStopPrice(BigDecimal stopPrice) {
        this.stopPrice = stopPrice;
    }

    public void setIcebergQty(BigDecimal icebergQty) {
        this.icebergQty = icebergQty;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof BinanceOrder)) return false;
        final BinanceOrder other = (BinanceOrder) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$symbol = this.getSymbol();
        final Object other$symbol = other.getSymbol();
        if (this$symbol == null ? other$symbol != null : !this$symbol.equals(other$symbol)) return false;
        final Object this$orderId = this.getOrderId();
        final Object other$orderId = other.getOrderId();
        if (this$orderId == null ? other$orderId != null : !this$orderId.equals(other$orderId)) return false;
        final Object this$clientOrderId = this.getClientOrderId();
        final Object other$clientOrderId = other.getClientOrderId();
        if (this$clientOrderId == null ? other$clientOrderId != null : !this$clientOrderId.equals(other$clientOrderId))
            return false;
        final Object this$price = this.getPrice();
        final Object other$price = other.getPrice();
        if (this$price == null ? other$price != null : !this$price.equals(other$price)) return false;
        final Object this$origQty = this.getOrigQty();
        final Object other$origQty = other.getOrigQty();
        if (this$origQty == null ? other$origQty != null : !this$origQty.equals(other$origQty)) return false;
        final Object this$executedQty = this.getExecutedQty();
        final Object other$executedQty = other.getExecutedQty();
        if (this$executedQty == null ? other$executedQty != null : !this$executedQty.equals(other$executedQty))
            return false;
        final Object this$status = this.getStatus();
        final Object other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) return false;
        final Object this$timeInForce = this.getTimeInForce();
        final Object other$timeInForce = other.getTimeInForce();
        if (this$timeInForce == null ? other$timeInForce != null : !this$timeInForce.equals(other$timeInForce))
            return false;
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        if (this$type == null ? other$type != null : !this$type.equals(other$type)) return false;
        final Object this$side = this.getSide();
        final Object other$side = other.getSide();
        if (this$side == null ? other$side != null : !this$side.equals(other$side)) return false;
        final Object this$stopPrice = this.getStopPrice();
        final Object other$stopPrice = other.getStopPrice();
        if (this$stopPrice == null ? other$stopPrice != null : !this$stopPrice.equals(other$stopPrice)) return false;
        final Object this$icebergQty = this.getIcebergQty();
        final Object other$icebergQty = other.getIcebergQty();
        if (this$icebergQty == null ? other$icebergQty != null : !this$icebergQty.equals(other$icebergQty))
            return false;
        final Object this$time = this.getTime();
        final Object other$time = other.getTime();
        if (this$time == null ? other$time != null : !this$time.equals(other$time)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof BinanceOrder;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $symbol = this.getSymbol();
        result = result * PRIME + ($symbol == null ? 43 : $symbol.hashCode());
        final Object $orderId = this.getOrderId();
        result = result * PRIME + ($orderId == null ? 43 : $orderId.hashCode());
        final Object $clientOrderId = this.getClientOrderId();
        result = result * PRIME + ($clientOrderId == null ? 43 : $clientOrderId.hashCode());
        final Object $price = this.getPrice();
        result = result * PRIME + ($price == null ? 43 : $price.hashCode());
        final Object $origQty = this.getOrigQty();
        result = result * PRIME + ($origQty == null ? 43 : $origQty.hashCode());
        final Object $executedQty = this.getExecutedQty();
        result = result * PRIME + ($executedQty == null ? 43 : $executedQty.hashCode());
        final Object $status = this.getStatus();
        result = result * PRIME + ($status == null ? 43 : $status.hashCode());
        final Object $timeInForce = this.getTimeInForce();
        result = result * PRIME + ($timeInForce == null ? 43 : $timeInForce.hashCode());
        final Object $type = this.getType();
        result = result * PRIME + ($type == null ? 43 : $type.hashCode());
        final Object $side = this.getSide();
        result = result * PRIME + ($side == null ? 43 : $side.hashCode());
        final Object $stopPrice = this.getStopPrice();
        result = result * PRIME + ($stopPrice == null ? 43 : $stopPrice.hashCode());
        final Object $icebergQty = this.getIcebergQty();
        result = result * PRIME + ($icebergQty == null ? 43 : $icebergQty.hashCode());
        final Object $time = this.getTime();
        result = result * PRIME + ($time == null ? 43 : $time.hashCode());
        return result;
    }

    public String toString() {
        return "BinanceOrder(symbol=" + this.getSymbol() + ", orderId=" + this.getOrderId() + ", clientOrderId=" + this.getClientOrderId() + ", price=" + this.getPrice() + ", origQty=" + this.getOrigQty() + ", executedQty=" + this.getExecutedQty() + ", status=" + this.getStatus() + ", timeInForce=" + this.getTimeInForce() + ", type=" + this.getType() + ", side=" + this.getSide() + ", stopPrice=" + this.getStopPrice() + ", icebergQty=" + this.getIcebergQty() + ", time=" + this.getTime() + ")";
    }
}
