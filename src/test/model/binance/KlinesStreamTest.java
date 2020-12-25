package test.model.binance;

import main.model.binance.api.BinanceAPI;
import main.model.binance.api.BinanceApiException;
import main.model.binance.datatype.BinanceEventKline;
import main.model.binance.datatype.BinanceInterval;
import main.model.binance.datatype.BinanceSymbol;
import main.model.binance.websocket.BinanceWebSocketAdapterKline;
import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.LoggerFactory;
import org.junit.Before;
import org.slf4j.Logger;
import org.junit.Test;

//@Slf4j
public class KlinesStreamTest {

    private static final Logger log = LoggerFactory.getLogger(KlinesStreamTest.class);

    private BinanceAPI binanceApi = null;
    private BinanceSymbol symbol = null;

    @Before
    public void setUp() throws Exception, BinanceApiException {
        binanceApi = new BinanceAPI();
        symbol = BinanceSymbol.valueOf("ETHBTC");
    }

    @Test
    public void testKlinesStreamWatcher() throws Exception, BinanceApiException {
        Session session = binanceApi.websocketKlines(symbol, BinanceInterval.ONE_MIN, new BinanceWebSocketAdapterKline() {
            @Override
            public void onMessage(BinanceEventKline message) {
                log.info(message.toString());
            }
        });
        Thread.sleep(3000);
        session.close();
    }

}
