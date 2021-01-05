package test.model.binance;

/* ============================================================
 * java-test.resources.model.binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

import main.model.binance.api.BinanceAPI;
import main.model.binance.api.BinanceApiException;
import main.model.binance.datatype.BinanceEventExecutionReport;
import main.model.binance.datatype.BinanceEventOutboundAccountInfo;
import main.model.binance.websocket.BinanceWebSocketAdapterUserData;
import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.LoggerFactory;
import org.junit.Before;
import org.slf4j.Logger;
import org.junit.Test;




//@Slf4j
// Тест потока пользовательских данных
public class UserDataStreamTest {

    private static final Logger log = LoggerFactory.getLogger(UserDataStreamTest.class);

    private BinanceAPI binanceApi = null;

    @Before
    public void setUp() throws Exception, BinanceApiException {
        binanceApi = new BinanceAPI();
    }

    @Test
    public void testUserDataStreamIsCreatedAndClosed() throws Exception, BinanceApiException {
        String listenKey = binanceApi.startUserDataStream();
        log.info("LISTEN KEY=" + listenKey);
        Session session = binanceApi.webSocket(listenKey, new BinanceWebSocketAdapterUserData() {
            @Override
            public void onOutboundAccountInfo(BinanceEventOutboundAccountInfo event) throws BinanceApiException {
                log.info(event.toString());
            }
            @Override
            public void onExecutionReport(BinanceEventExecutionReport event) throws BinanceApiException {
                log.info(event.toString());
            }
        });
        Thread.sleep(2000);
        log.info("KEEPING ALIVE=" + binanceApi.keepUserDataStream(listenKey));
        Thread.sleep(2000);
        session.close();
        log.info("DELETED=" + binanceApi.deleteUserDataStream(listenKey));
    }
}

