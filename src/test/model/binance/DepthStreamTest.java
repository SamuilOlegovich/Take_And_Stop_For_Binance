package test.model.binance;

/* ============================================================
 * java-test.resources.model.binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

import main.model.API;
import main.model.Agent;
import main.model.binance.api.BinanceAPI;
import main.model.binance.api.BinanceApiException;
import main.model.binance.datatype.BinanceEventDepthLevelUpdate;
import main.model.binance.datatype.BinanceEventDepthUpdate;
import main.model.binance.datatype.BinanceSymbol;
import main.model.binance.websocket.BinanceWebSocketAdapterDepth;
import main.model.binance.websocket.BinanceWebSocketAdapterDepthLevel;
import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.LoggerFactory;
import org.junit.Before;
import org.slf4j.Logger;
import org.junit.Test;




//@Slf4j
// Тест глубины потока
public class DepthStreamTest {

    private static final Logger log = LoggerFactory.getLogger(DepthStreamTest.class);

    private BinanceAPI binanceApi = null;
    private BinanceSymbol symbol = null;

    @Before
    public void setUp() throws Exception, BinanceApiException {
        Agent.setApi(new API());
        binanceApi = new BinanceAPI();
        symbol = BinanceSymbol.valueOf("ETHBTC");
    }

    @Test
    public void testDepthStreamWatcher() throws Exception, BinanceApiException {

        Session session = binanceApi.webSocketDepth(symbol, new BinanceWebSocketAdapterDepth() {
            @Override
            public void onMessage(BinanceEventDepthUpdate message) {
                log.info(message.toString());
            }
        });
        Thread.sleep(3000);
        session.close();
    }

    @Test
    public void testDepth5StreamWatcher() throws Exception, BinanceApiException {
        Session session = binanceApi.webSocketDepth5(symbol, new BinanceWebSocketAdapterDepthLevel() {
            @Override
            public void onMessage(BinanceEventDepthLevelUpdate message) {
                log.info(message.toString());
            }
        });
        Thread.sleep(3000);
        session.close();
    }
}
