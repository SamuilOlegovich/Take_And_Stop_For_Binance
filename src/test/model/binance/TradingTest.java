package test.model.binance;

/* ============================================================
 * java-test.resources.model.binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

// This class contains tests for trading. Take it wisely

import com.webcerebrium.binance.datatype.BinanceOrderPlacement;
import com.webcerebrium.binance.datatype.BinanceWalletAsset;
import com.webcerebrium.binance.datatype.BinanceTimeInForce;
import com.webcerebrium.binance.datatype.BinanceOrderSide;
import com.webcerebrium.binance.datatype.BinanceSymbol;
import main.model.binance.api.BinanceApi;
import main.model.binance.api.BinanceApiException;
import main.model.binance.datatype.BinanceOrder;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.junit.Before;
import org.slf4j.Logger;
import org.junit.After;
import org.junit.Test;

import java.math.BigDecimal;

import static com.webcerebrium.binance.datatype.BinanceOrderType.MARKET;
import static com.webcerebrium.binance.datatype.BinanceOrderType.LIMIT;

//@Slf4j
public class TradingTest {

    private static final Logger log = LoggerFactory.getLogger(TradingTest.class);

    private BinanceApi binanceApi = null;
    private BinanceSymbol symbol = null;
    private BinanceOrder order = null;
    private String asset = "";

    private boolean canTrade = false;
    private BinanceWalletAsset walletAsset = null;

    @Before
    public void setUp() throws Exception, BinanceApiException {
        binanceApi = new BinanceApi();
        asset = "BNB";
        symbol = BinanceSymbol.valueOf(asset + "BTC");
        order = null;

        walletAsset = binanceApi.balancesMap().get(asset);
        log.info("walletAsset={}", walletAsset.toString());
        canTrade = (walletAsset.getFree().compareTo(BigDecimal.ZERO) > 0);
    }

    @After
    public void tearDown() throws Exception {
        if (order != null) {
            try {
                JsonObject jsonObject = binanceApi.deleteOrder(order);
                log.info("Deleted order = {}", jsonObject.toString());
            } catch (BinanceApiException e) {
                log.info("Order clean up (non-critical) exception = {}", e.toString());
            }
            order = null;
        }
    }

    @Test
    public void testOrderWithoutPlacing() throws Exception, BinanceApiException {
        if (canTrade) {
            BinanceOrderPlacement placement = new BinanceOrderPlacement(symbol, BinanceOrderSide.SELL);
            placement.setTimeInForce(BinanceTimeInForce.GOOD_TILL_CANCELLED);
            placement.setPrice(BigDecimal.valueOf(1));

            BigDecimal qty = BigDecimal.valueOf(walletAsset.getFree().longValue()); // so we could tes ton BNB
            if (qty.compareTo(BigDecimal.ZERO) > 0) {
                placement.setQuantity(qty); // sell some our asset for 1 BTC each
                log.info("Order Test = {}", binanceApi.testOrder(placement));
            }
        }
    }

    @Test
    public void testMarketOrder() throws Exception, BinanceApiException {
        if (canTrade) {
            // Testing Buying BNB with BTC - using market price
            BinanceOrderPlacement placement = new BinanceOrderPlacement(symbol, BinanceOrderSide.BUY);
            placement.setType(MARKET);
            BigDecimal qty = BigDecimal.ONE; // so we want to buy exactly 1 BNB
            if (qty.compareTo(BigDecimal.ZERO) > 0) {
                placement.setQuantity(qty); // sell some our asset for 1 BTC each
                log.info("Market Order Test = {}", binanceApi.testOrder(placement));
            }
        }
    }

    @Test
    public void testPlacingCheckingLimitOrder() throws Exception, BinanceApiException {
        if (canTrade) {
            BinanceOrderPlacement placement = new BinanceOrderPlacement(symbol, BinanceOrderSide.SELL);
            placement.setTimeInForce(BinanceTimeInForce.GOOD_TILL_CANCELLED);
            placement.setType(LIMIT);
            placement.setPrice(BigDecimal.valueOf(1));

            BigDecimal qty = BigDecimal.valueOf(walletAsset.getFree().longValue());
            if (qty.compareTo(BigDecimal.ZERO) > 0) {
                placement.setQuantity(qty); // sell some of our asset for 1 BTC each
                JsonObject jsonObject = binanceApi.createOrder(placement);
                log.info("Order Placement = {}", jsonObject.toString());
                order = binanceApi.getOrderById(symbol, jsonObject.get("orderId").getAsLong());
                System.out.println(order);
            }
        }
    }
}
