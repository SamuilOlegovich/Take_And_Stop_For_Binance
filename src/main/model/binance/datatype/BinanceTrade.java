package main.model.binance.datatype;
/* ============================================================
 * java-test.resources.model.binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

import java.math.BigDecimal;

/**
 {
 "id": 28457,
 "price": "4.00000100",
 "qty": "12.00000000",
 "commission": "10.10000000",
 "commissionAsset": "BNB",
 "time": 1499865549590,
 "isBuyer": true,
 "isMaker": false,
 "isBestMatch": true
 }
 */

public class BinanceTrade {
    public String commissionAsset;
    public BigDecimal commission;
    public boolean isBestMatch;
    public BigDecimal price;
    public boolean isBuyer;
    public boolean isMaker;
    public BigDecimal qty;
    public Long time;
    public Long id;

    public BinanceTrade() {
    }

    public Long getId() {
        return this.id;
    }

    public String getCommissionAsset() {
        return this.commissionAsset;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public BigDecimal getQty() {
        return this.qty;
    }

    public BigDecimal getCommission() {
        return this.commission;
    }

    public Long getTime() {
        return this.time;
    }

    public boolean isBuyer() {
        return this.isBuyer;
    }

    public boolean isMaker() {
        return this.isMaker;
    }

    public boolean isBestMatch() {
        return this.isBestMatch;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCommissionAsset(String commissionAsset) {
        this.commissionAsset = commissionAsset;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public void setBuyer(boolean isBuyer) {
        this.isBuyer = isBuyer;
    }

    public void setMaker(boolean isMaker) {
        this.isMaker = isMaker;
    }

    public void setBestMatch(boolean isBestMatch) {
        this.isBestMatch = isBestMatch;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof BinanceTrade)) return false;
        final BinanceTrade other = (BinanceTrade) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$commissionAsset = this.getCommissionAsset();
        final Object other$commissionAsset = other.getCommissionAsset();
        if (this$commissionAsset == null ? other$commissionAsset != null : !this$commissionAsset.equals(other$commissionAsset))
            return false;
        final Object this$price = this.getPrice();
        final Object other$price = other.getPrice();
        if (this$price == null ? other$price != null : !this$price.equals(other$price)) return false;
        final Object this$qty = this.getQty();
        final Object other$qty = other.getQty();
        if (this$qty == null ? other$qty != null : !this$qty.equals(other$qty)) return false;
        final Object this$commission = this.getCommission();
        final Object other$commission = other.getCommission();
        if (this$commission == null ? other$commission != null : !this$commission.equals(other$commission))
            return false;
        final Object this$time = this.getTime();
        final Object other$time = other.getTime();
        if (this$time == null ? other$time != null : !this$time.equals(other$time)) return false;
        if (this.isBuyer() != other.isBuyer()) return false;
        if (this.isMaker() != other.isMaker()) return false;
        if (this.isBestMatch() != other.isBestMatch()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof BinanceTrade;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $commissionAsset = this.getCommissionAsset();
        result = result * PRIME + ($commissionAsset == null ? 43 : $commissionAsset.hashCode());
        final Object $price = this.getPrice();
        result = result * PRIME + ($price == null ? 43 : $price.hashCode());
        final Object $qty = this.getQty();
        result = result * PRIME + ($qty == null ? 43 : $qty.hashCode());
        final Object $commission = this.getCommission();
        result = result * PRIME + ($commission == null ? 43 : $commission.hashCode());
        final Object $time = this.getTime();
        result = result * PRIME + ($time == null ? 43 : $time.hashCode());
        result = result * PRIME + (this.isBuyer() ? 79 : 97);
        result = result * PRIME + (this.isMaker() ? 79 : 97);
        result = result * PRIME + (this.isBestMatch() ? 79 : 97);
        return result;
    }

    public String toString() {
        return "BinanceTrade(id=" + this.getId() + ", commissionAsset=" + this.getCommissionAsset() + ", price=" + this.getPrice() + ", qty=" + this.getQty() + ", commission=" + this.getCommission() + ", time=" + this.getTime() + ", isBuyer=" + this.isBuyer() + ", isMaker=" + this.isMaker() + ", isBestMatch=" + this.isBestMatch() + ")";
    }
}
