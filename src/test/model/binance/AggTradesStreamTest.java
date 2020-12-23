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
import main.model.binance.datatype.BinanceEventAggTrade;
import main.model.binance.datatype.BinanceSymbol;
import main.model.binance.websocket.BinanceWebSocketAdapterAggTrades;
import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.LoggerFactory;
import org.junit.Before;
import org.slf4j.Logger;
import org.junit.Test;




//@Slf4j
public class AggTradesStreamTest {

    private static final Logger log = LoggerFactory.getLogger(AggTradesStreamTest.class);

    private BinanceApi binanceApi = null;
    private BinanceSymbol symbol = null;

    @Before
    public void setUp() throws Exception, BinanceApiException {
        binanceApi = new BinanceApi();
        symbol = BinanceSymbol.valueOf("ETHBTC");
    }

    @Test
    public void testTradesStreamWatcher() throws Exception, BinanceApiException {
        Session session = binanceApi.websocketTrades(symbol, new BinanceWebSocketAdapterAggTrades() {
            @Override
            public void onMessage(BinanceEventAggTrade message) {
                log.info(message.toString());
            }
        });
        Thread.sleep(5000);
        session.close();
    }
}
