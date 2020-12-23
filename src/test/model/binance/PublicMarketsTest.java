package test.model.binance;
/* ============================================================
 * java-test.resources.model.binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

import main.model.binance.api.BinanceApi;
import main.model.binance.api.BinanceApiException;
import main.model.binance.datatype.BinanceExchangeInfo;
import main.model.binance.datatype.BinanceExchangeStats;
import main.model.binance.datatype.BinanceExchangeSymbol;
import org.slf4j.LoggerFactory;
import org.junit.Before;
import org.slf4j.Logger;
import org.junit.Test;

import java.util.List;




//@Slf4j
public class PublicMarketsTest {

    private static final Logger log = LoggerFactory.getLogger(PublicMarketsTest.class);

    private BinanceApi binanceApi = null;

    @Before
    public void setUp() throws Exception, BinanceApiException {
        binanceApi = new BinanceApi();
    }

    @Test
    public void testPublicMarkets() throws Exception, BinanceApiException {
        BinanceExchangeStats binanceExchangeStats = binanceApi.publicStats();
        log.info("Public Exchange Stats (not documented): {}", binanceExchangeStats.toString());
    }

    @Test
    public void testExchangeInfo() throws Exception, BinanceApiException {
        BinanceExchangeInfo binanceExchangeInfo = binanceApi.exchangeInfo();
        List<BinanceExchangeSymbol> symbols = BinanceExchangeInfo.getSymbols();
        // BinanceExchangeSymbol BNB = symbols.stream().filter(a -> a.getQuoteAsset().equals("BNB")).findFirst().get();
        // log.info("BNB Lot Size: {}", BNB.getLotSize().toString());
        symbols
        .stream()
        .filter(b -> (b.getBaseAsset().equals("BNB") || b.getQuoteAsset().equals("BNB")))
        .forEach(a -> {
//            log.info("Base: {} Quote: {} Lot Size: {} Min Notional: {}", a.getBaseAsset(), a.getQuoteAsset(), a.getLotSize().toString(), a.getMinNotionalValue() );
        });
    }
}
