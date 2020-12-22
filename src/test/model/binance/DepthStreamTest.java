package test.model.binance;

/* ============================================================
 * java-test.resources.model.binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

import com.webcerebrium.binance.websocket.BinanceWebSocketAdapterDepthLevel;
import com.webcerebrium.binance.websocket.BinanceWebSocketAdapterDepth;
import com.webcerebrium.binance.datatype.BinanceEventDepthLevelUpdate;
import com.webcerebrium.binance.datatype.BinanceEventDepthUpdate;
import com.webcerebrium.binance.datatype.BinanceSymbol;
import main.model.binance.api.BinanceApi;
import main.model.binance.api.BinanceApiException;
import org.eclipse.jetty.websocket.api.Session;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.junit.Before;
import org.slf4j.Logger;
import org.junit.Test;




//@Slf4j
public class DepthStreamTest {

    private static final Logger log = LoggerFactory.getLogger(DepthStreamTest.class);

    private BinanceApi binanceApi = null;
    private BinanceSymbol symbol = null;

    @Before
    public void setUp() throws Exception, BinanceApiException {
        binanceApi = new BinanceApi();
        symbol = BinanceSymbol.valueOf("ETHBTC");
    }

    @Test
    public void testDepthStreamWatcher() throws Exception, BinanceApiException {
        Session session = binanceApi.websocketDepth(symbol, new BinanceWebSocketAdapterDepth() {
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
        Session session = binanceApi.websocketDepth5(symbol, new BinanceWebSocketAdapterDepthLevel() {
            @Override
            public void onMessage(BinanceEventDepthLevelUpdate message) {
                log.info(message.toString());
            }
        });
        Thread.sleep(3000);
        session.close();
    }
}
