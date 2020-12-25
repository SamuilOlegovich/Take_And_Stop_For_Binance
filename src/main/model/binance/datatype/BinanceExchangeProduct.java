package main.model.binance.datatype;

import com.google.gson.JsonObject;
import main.model.binance.api.BinanceApiException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
//@Slf4j
public class BinanceExchangeProduct {

    private static final Logger log = LoggerFactory.getLogger(BinanceExchangeProduct.class);

    String matchingUnitType;
    BigDecimal tradedMoney;
    BigDecimal withdrawFee;
    BigDecimal activeSell;
    String quoteAssetUnit;
    String baseAssetUnit;
    BigDecimal activeBuy;
    BigDecimal prevClose;
    BinanceSymbol symbol;
    Long lastAggTradeId;
    BigDecimal tickSize;
    BigDecimal minTrade;
    Long decimalPlaces;
    BigDecimal minQty;
    BigDecimal volume;
    String quoteAsset;
    String baseAsset;
    BigDecimal close;
    BigDecimal high;
    BigDecimal open;
    boolean active;
    BigDecimal low;
    String status;

    public BinanceExchangeProduct() {
    }

    private void jsonExpect(JsonObject obj, Set<String> fields) throws BinanceApiException {
        Set<String> missing = new HashSet<>();
        for (String f: fields) { if (!obj.has(f) || obj.get(f).isJsonNull()) missing.add(f); }
        if (missing.size() > 0) {
            log.warn("Missing fields {} in {}", missing.toString(), obj.toString());
            throw new BinanceApiException("Missing fields " + missing.toString());
        }
    }

    private BigDecimal safeDecimal(JsonObject obj, String field) {
        if (obj.has(field) && obj.get(field).isJsonPrimitive() && obj.get(field) != null) {
            try {
                return obj.get(field).getAsBigDecimal();
            } catch (NumberFormatException nfe) {
//                log.info("Number format exception in field={%s} value={%d} trade={%s}", field, obj.get(field), obj.toString());
            }
        }
        return null;
    }


    public BinanceExchangeProduct(JsonObject obj) throws BinanceApiException {

        symbol = BinanceSymbol.valueOf(obj.get("symbol").getAsString());
        active = obj.get("active").getAsBoolean();

        quoteAsset = obj.get("quoteAsset").getAsString();
        quoteAssetUnit = obj.get("quoteAssetUnit").getAsString();
        status = obj.get("status").getAsString();
        baseAsset = obj.get("baseAsset").getAsString();
        baseAssetUnit = obj.get("baseAssetUnit").getAsString();
        matchingUnitType = obj.get("matchingUnitType").getAsString();

        decimalPlaces  = obj.get("decimalPlaces").getAsLong();
        lastAggTradeId  = obj.get("lastAggTradeId").getAsLong();

        activeBuy = safeDecimal(obj, "activeBuy");
        activeSell = safeDecimal(obj, "activeSell");
        close = safeDecimal(obj, "close");
        high = safeDecimal(obj, "high");
        low = safeDecimal(obj, "low");
        minQty = safeDecimal(obj, "minQty");
        minTrade = safeDecimal(obj, "minTrade");
        open = safeDecimal(obj, "open");
        prevClose = safeDecimal(obj, "prevClose");

        tickSize = safeDecimal(obj, "tickSize");
        tradedMoney = safeDecimal(obj, "tradedMoney");
        volume = safeDecimal(obj, "volume");
        withdrawFee = safeDecimal(obj, "withdrawFee");
    }

    /////////////////////////////////////    I had to add == пришлось добавить   ///////////////////////////////////////

    public boolean isActive() {
        return active;
    }

    public BinanceSymbol getSymbol() {
        return symbol;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
